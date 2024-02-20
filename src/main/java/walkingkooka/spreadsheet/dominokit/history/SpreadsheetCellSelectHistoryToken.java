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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.text.TextStyle;

import java.util.Optional;

public final class SpreadsheetCellSelectHistoryToken extends SpreadsheetCellHistoryToken {

    static SpreadsheetCellSelectHistoryToken with(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellSelectHistoryToken(
                id,
                name,
                anchoredSelection
        );
    }

    private SpreadsheetCellSelectHistoryToken(final SpreadsheetId id,
                                              final SpreadsheetName name,
                                              final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
                id,
                name,
                anchoredSelection
        );
    }

    @Override
    UrlFragment cellUrlFragment() {
        return SELECT;
    }

    @Override
    public HistoryToken clearAction() {
        return this.selectionSelect();
    }

    @Override //
    HistoryToken setDifferentAnchoredSelection(final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
                this.id(),
                this.name(),
                anchoredSelection
        );
    }

    @Override
    public HistoryToken setFormatPattern() {
        return cellFormatPattern(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override
    public HistoryToken setFormula() {
        return setFormula0();
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.anchoredSelection()
        );
    }

    @Override
    public HistoryToken setParsePattern() {
        return cellParsePattern(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override
    HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        final SpreadsheetId id = this.id();
        final SpreadsheetName name = this.name();
        final AnchoredSpreadsheetSelection anchoredSelection = this.anchoredSelection();

        return patternKind.isPresent() ?
                cellPattern(
                        id,
                        name,
                        anchoredSelection,
                        patternKind.get()
                ) :
                this;
    }

    @Override
    HistoryToken setSave0(final String value) {
        return this;
    }

    @Override
    public void onHistoryTokenChange0(final HistoryToken previous,
                                      final AppContext context) {
        // SpreadsheetViewportComponent will give focus to cell
    }

    /**
     * Handles parsing /cell/save tokens.
     */
    HistoryToken parseCellSave(final TextCursor cursor) {
        HistoryToken result = this;

        final Optional<String> maybeComponent = parseComponent(cursor);
        if (maybeComponent.isPresent()) {
            final String component = maybeComponent.get();

            // there will be more such as cell/pattern-format/pattern-parse/style
            switch (component) {
                case "formula":
                    result = cellSaveFormula(
                            this.id(),
                            this.name(),
                            this.anchoredSelection(),
                            SpreadsheetCellSaveHistoryToken.parseMap(
                                    cursor,
                                    String.class
                            )
                    );
                    break;
                case "style":
                    result = cellSaveStyle(
                            this.id(),
                            this.name(),
                            this.anchoredSelection(),
                            SpreadsheetCellSaveHistoryToken.parseMap(
                                    cursor,
                                    TextStyle.class
                            )
                    );
                    break;
                default:
                    cursor.end();
                    result = this; // ignore
                    break;
            }
        }

        return result;
    }
}
