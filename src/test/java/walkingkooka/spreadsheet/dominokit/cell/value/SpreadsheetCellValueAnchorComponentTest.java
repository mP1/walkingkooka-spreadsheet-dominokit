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

package walkingkooka.spreadsheet.dominokit.cell.value;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentTesting;
import walkingkooka.spreadsheet.dominokit.cell.FakeSpreadsheetCellLinksComponentContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.validation.ValueTypeName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellValueAnchorComponentTest implements AnchorComponentTesting<SpreadsheetCellValueAnchorComponent, SpreadsheetExpressionReference> {

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellValueAnchorComponent.with(
                "id123",
                null
            )
        );
    }

    // value............................................................................................................

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            SpreadsheetCellValueAnchorComponent.with(
                "cell-value-anchor-id",
                SpreadsheetCellValueAnchorComponentContexts.fake()
            ).clearValue(),
            "\"Value\" DISABLED id=cell-value-anchor-id"
        );
    }

    @Test
    public void testSetValueWithCellMissingSpreadsheetIdSpreadsheetNameGivesDisabledLink() {
        this.treePrintAndCheck(
            this.createComponent(
                "/",
                null // NO_CELL
            ).setValue(
                Optional.of(
                    SpreadsheetSelection.A1
                )
            ),
            "\"A1\" DISABLED id=cell-value-anchor-id"
        );
    }

    @Test
    public void testSetValueWithCellWithFormulaTextAndValue() {
        this.treePrintAndCheck(
            this.createComponent(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText("=1")
                            .setValue(
                                Optional.of("text123")
                            )
                    )
                )
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.A1
                    )
                ),
            "\"A1\" DISABLED id=cell-value-anchor-id"
        );
    }

    @Test
    public void testSetValueWithCellWithNoFormulaTextAndValue() {
        this.treePrintAndCheck(
            this.createComponent(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setValue(
                            Optional.of("text123")
                        )
                    )
                )
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.A1
                    )
                ),
            "\"A1\" [#/1/SpreadsheetName222/cell/A1/value/text] id=cell-value-anchor-id"
        );
    }

    @Test
    public void testSetValueWithCellWithValueIgnoresValueType() {
        this.treePrintAndCheck(
            this.createComponent(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText("=1")
                            .setValueType(
                                Optional.of(ValueTypeName.DATE)
                            ).setValue(
                                Optional.of("text123")
                            )
                    )
                )
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.A1
                    )
                ),
            "\"A1\" [#/1/SpreadsheetName222/cell/A1/value/text] id=cell-value-anchor-id"
        );
    }

    @Test
    public void testSetValueWithCellWithValueType() {
        this.treePrintAndCheck(
            this.createComponent(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText("=1")
                            .setValueType(
                                Optional.of(
                                    ValueTypeName.TEXT
                                )
                            )
                    )
                )
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.A1
                    )
                ),
            "\"A1\" [#/1/SpreadsheetName222/cell/A1/value/text] id=cell-value-anchor-id"
        );
    }

    @Test
    public void testSetValueWithCellWhenValueTypeUnavailable() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.parseCell("Z99")
                    )
                ),
            "\"Z99\" DISABLED id=cell-value-anchor-id"
        );
    }

    @Test
    public void testSetValueWithCellRange() {
        this.treePrintAndCheck(
            this.createComponent(
                SpreadsheetSelection.A1.setFormula(
                    SpreadsheetFormula.EMPTY.setValueType(
                        Optional.of(
                            ValueTypeName.TEXT
                        )
                    )
                )
            ).setValue(
                Optional.of(
                    SpreadsheetSelection.parseCellRange("B2:C3")
                )
            ),
            "\"B2:C3\" [#/1/SpreadsheetName222/cell/B2:C3/bottom-right/value/text] id=cell-value-anchor-id"
        );
    }

    @Test
    public void testSetValueWithLabel() {
        this.treePrintAndCheck(
            this.createComponent(
                SpreadsheetSelection.A1.setFormula(
                    SpreadsheetFormula.EMPTY.setValueType(
                        Optional.of(
                            ValueTypeName.TEXT
                        )
                    )
                )
            ).setValue(
                Optional.of(
                    SpreadsheetSelection.labelName("Label9999")
                )
            ),
            "\"Label9999\" [#/1/SpreadsheetName222/cell/Label9999/value/text] id=cell-value-anchor-id"
        );
    }

    @Override
    public SpreadsheetCellValueAnchorComponent createComponent() {
        return this.createComponent(
            null
        );
    }

    private SpreadsheetCellValueAnchorComponent createComponent(final SpreadsheetCell cell) {
        return this.createComponent(
            "/1/SpreadsheetName222",
            cell
        );
    }

    private SpreadsheetCellValueAnchorComponent createComponent(final String historyToken,
                                                                final SpreadsheetCell cell) {
        return SpreadsheetCellValueAnchorComponent.with(
            "cell-value-anchor-id",
            new FakeSpreadsheetCellLinksComponentContext() {
                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.parseString(historyToken);
                }

                @Override
                public Optional<SpreadsheetCell> cell(final SpreadsheetSelection selection) {
                    return Optional.ofNullable(
                        selection.toString().equals("Z99") ?
                            null :
                            cell
                    );
                }

                @Override
                public String toString() {
                    return historyToken;
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetCellValueAnchorComponent> type() {
        return SpreadsheetCellValueAnchorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
