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

package walkingkooka.spreadsheet.dominokit.cell;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.validation.ValueType;

import java.util.Optional;
import java.util.Set;

public final class SpreadsheetCellLinksComponentTest implements HtmlComponentTesting<SpreadsheetCellLinksComponent, HTMLDivElement> {

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            SpreadsheetCellLinksComponent.empty(
                "cells-",
                new FakeSpreadsheetCellLinksComponentContext() {

                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.cellFormula(
                            SpreadsheetId.with(1),
                            SpreadsheetName.with("SpreadsheetName222"),
                            SpreadsheetSelection.A1.setDefaultAnchor()
                        );
                    }
                }
            ),
            "SpreadsheetCellLinksComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Value\" DISABLED id=cells-value-Link\n" +
                "        \"Create Label\" DISABLED id=cells-createLabel-Link\n" +
                "        \"Labels\" DISABLED id=cells-label-Link\n" +
                "        \"References\" DISABLED id=cells-references-Link\n" +
                "        \"Delete\" DISABLED id=cells-delete-Link\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            SpreadsheetCellLinksComponent.empty(
                "cells-",
                new FakeSpreadsheetCellLinksComponentContext() {

                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.cellFormula(
                            SpreadsheetId.with(1),
                            SpreadsheetName.with("SpreadsheetName222"),
                            SpreadsheetSelection.A1.setDefaultAnchor()
                        );
                    }

                    @Override
                    public Optional<SpreadsheetCell> cell(final SpreadsheetSelection selection) {
                        return Optional.of(
                            selection.toCell().setFormula(
                                SpreadsheetFormula.EMPTY.setValueType(
                                    Optional.of(ValueType.TEXT)
                                )
                            )
                        );
                    }

                    @Override
                    public Set<SpreadsheetLabelName> cellLabels(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
                        return Sets.of(
                            SpreadsheetSelection.labelName("A1LABEL")
                        );
                    }

                    @Override
                    public Set<SpreadsheetExpressionReference> cellReferences(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
                        return Sets.of(
                            SpreadsheetSelection.parseCell("B2"),
                            SpreadsheetSelection.parseCell("C3")
                        );
                    }
                }
            ).setValue(
                Optional.of(
                    SpreadsheetSelection.A1
                )
            ),
            "SpreadsheetCellLinksComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Value\" [#/1/SpreadsheetName222/cell/A1/value/text] id=cells-value-Link\n" +
                "        \"Create Label\" [#/1/SpreadsheetName222/cell/A1/label] id=cells-createLabel-Link\n" +
                "        \"Labels\" [#/1/SpreadsheetName222/cell/A1/labels] (1) id=cells-label-Link\n" +
                "        \"References\" [#/1/SpreadsheetName222/cell/A1/references] (2) id=cells-references-Link\n" +
                "        \"Delete\" [#/1/SpreadsheetName222/cell/A1/delete] id=cells-delete-Link\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetCellLinksComponent> type() {
        return SpreadsheetCellLinksComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
