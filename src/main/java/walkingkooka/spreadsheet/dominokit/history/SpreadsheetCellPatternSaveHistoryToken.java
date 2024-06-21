/*
 * Copyright 2023 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.format.pattern.HasSpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Optional;

/**
 * Saves or replaces the formatting or parse pattern for the selected cell(s).
 * <pre>
 * /123/SpreadsheetName456/cell/A1/formatter/date/save/save/dd-mm-yyy
 * /123/SpreadsheetName456/cell/A1/formatter/date-time/save/dd-mm-yyyy hh:mm
 * /123/SpreadsheetName456/cell/A1/formatter/number/save/$0.00
 * /123/SpreadsheetName456/cell/A1/formatter/text/save/@
 * /123/SpreadsheetName456/cell/A1/formatter/time/save/hh:mm
 *
 * /123/SpreadsheetName456/cell/A1/parser/date/save/date-parser-token%20yyyy-mm-dd;yy-mm-dd
 * /123/SpreadsheetName456/cell/A1/parser/date-time/save/date-time-parser-token%20yyyy-mm-dd hh:mm:ssyy-mm-dd hh:mm
 * /123/SpreadsheetName456/cell/A1/parser/number/save/number-parser-token%20+0.00;-0.00;"ZERO"
 * /123/SpreadsheetName456/cell/A1/parser/time/save/time-parser-token%20hh:mm:ss;hh:mm
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/formatter/SpreadsheetPatternKind/save/SpreadsheetFormatSelector
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/parser/SpreadsheetPatternKind/save/SpreadsheetParserSelector
 * </pre>
 */
public final class SpreadsheetCellPatternSaveHistoryToken extends SpreadsheetCellPatternHistoryToken
        implements HasSpreadsheetPattern {

    static SpreadsheetCellPatternSaveHistoryToken with(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                                       final SpreadsheetPatternKind patternKind,
                                                       final Optional<SpreadsheetPattern> pattern) {
        return new SpreadsheetCellPatternSaveHistoryToken(
                id,
                name,
                anchoredSelection,
                patternKind,
                pattern
        );
    }

    private SpreadsheetCellPatternSaveHistoryToken(final SpreadsheetId id,
                                                   final SpreadsheetName name,
                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                   final SpreadsheetPatternKind patternKind,
                                                   final Optional<SpreadsheetPattern> pattern) {
        super(
                id,
                name,
                anchoredSelection,
                Optional.of(patternKind)
        );

        if (pattern.isPresent()) {
            if (patternKind.isParsePattern()) {
                patternKind.checkSameOrFail(pattern.get());
            }
        }

        this.pattern = pattern;
    }

    Optional<SpreadsheetPattern> pattern0() {
        return this.pattern;
    }

    private final Optional<SpreadsheetPattern> pattern;

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.cellPattern(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                this.patternKind().get()
        );
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellPatternSaveHistoryToken(
                id,
                name,
                anchoredSelection,
                this.patternKind()
                        .get(),
                this.pattern()
        );
    }

    // /cell/A1/formatter/text/save/text-format @@
    //
    // /cell/A1/parser/date/save
    @Override
    UrlFragment cellUrlFragment() {
        return this.patternKind()
                .get()
                .urlFragment()
                .appendSlashThen(
                        this.saveUrlFragment(
                                this.pattern()
                        )
                );
    }

    @Override
    HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        return cellPattern(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                patternKind.get()
        );
    }

    @Override
    HistoryToken setSave0(final String value) {
        final SpreadsheetPatternKind kind = this.patternKind()
                .get();
        return new SpreadsheetCellPatternSaveHistoryToken(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                kind,
                Optional.ofNullable(
                        value.isEmpty() ?
                                null :
                                kind.parse(value)
                )
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        context.spreadsheetDeltaFetcher()
                .savePattern(
                        this.id(),
                        this.anchoredSelection().selection(),
                        this.patternKind().get(),
                        this.pattern()
                );
    }
}
