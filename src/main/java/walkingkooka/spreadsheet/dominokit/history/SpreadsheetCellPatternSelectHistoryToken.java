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
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Optional;

/**
 * Opens a dialog with a form and other components supporting edit a formatting or parse pattern.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/formatter/date
 * /123/SpreadsheetName456/cell/A1/formatter/date-time
 * /123/SpreadsheetName456/cell/A1/formatter/number
 * /123/SpreadsheetName456/cell/A1/formatter/text
 * /123/SpreadsheetName456/cell/A1/formatter/time
 *
 * /123/SpreadsheetName456/cell/A1/parse-pattern/date-time
 * /123/SpreadsheetName456/cell/A1/parse-pattern/date
 * /123/SpreadsheetName456/cell/A1/parse-pattern/date-time
 * /123/SpreadsheetName456/cell/A1/parse-pattern/number
 * /123/SpreadsheetName456/cell/A1/parse-pattern/time
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/formatter/SpreadsheetPatternKind
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/parse-pattern/SpreadsheetPatternKind
 * </pre>
 */
public final class SpreadsheetCellPatternSelectHistoryToken extends SpreadsheetCellPatternHistoryToken {

    static SpreadsheetCellPatternSelectHistoryToken with(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                         final SpreadsheetPatternKind patternKind) {
        return new SpreadsheetCellPatternSelectHistoryToken(
                id,
                name,
                anchoredSelection,
                patternKind
        );
    }

    private SpreadsheetCellPatternSelectHistoryToken(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final SpreadsheetPatternKind patternKind) {
        super(
                id,
                name,
                anchoredSelection,
                Optional.of(patternKind)
        );
    }

    @Override
    UrlFragment cellUrlFragment() {
        return this.patternKind()
                .get()
                .urlFragment();
    }

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
                id,
                name,
                anchoredSelection
        ).setPatternKind(
                this.patternKind()
        );
    }

    @Override
    HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        final SpreadsheetId id = this.id();
        final SpreadsheetName name = this.name();
        final AnchoredSpreadsheetSelection anchoredSelection = this.anchoredSelection();

        return patternKind.isPresent() ?
                new SpreadsheetCellPatternSelectHistoryToken(
                        id,
                        name,
                        anchoredSelection,
                        patternKind.get()
                ) :
                this.patternKind()
                        .get()
                        .isFormatPattern() ?
                        cellFormatPattern(
                                id,
                                name,
                                anchoredSelection
                        ) :
                        cellParsePattern(
                                id,
                                name,
                                anchoredSelection
                        );
    }

    @Override
    HistoryToken setSave0(final String pattern) {
        final SpreadsheetPatternKind patternKind = this.patternKind()
                .get();

        return cellPatternSave(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                patternKind,
                Optional.ofNullable(
                        pattern.isEmpty() ?
                                null :
                                patternKind.isFormatPattern() ?
                                        SpreadsheetFormatterSelector.parse(pattern)
                                                .spreadsheetFormatPattern()
                                                .get() :
                                        patternKind.parse(pattern)
                )
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // NOP because SpreadsheetPatternDialogComponent implements ComponentLifecycle
    }
}
