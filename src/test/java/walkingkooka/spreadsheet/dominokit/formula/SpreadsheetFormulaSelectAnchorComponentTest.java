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

package walkingkooka.spreadsheet.dominokit.formula;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetFormulaSelectAnchorComponentTest implements AnchorComponentTesting<SpreadsheetFormulaSelectAnchorComponent, SpreadsheetExpressionReference> {

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetFormulaSelectAnchorComponent.with(
                "id123",
                null
            )
        );
    }

    // value............................................................................................................

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            SpreadsheetFormulaSelectAnchorComponent.with(
                "formula-anchor-id",
                SpreadsheetFormulaSelectAnchorComponentContexts.fake()
            ).clearValue(),
            "\"Formula\" DISABLED id=formula-anchor-id"
        );
    }

    @Test
    public void testSetValueWithCellMissingSpreadsheetIdGivesDisabledLink() {
        this.treePrintAndCheck(
            this.createComponent(
                "/"
            ).setValue(
                Optional.of(
                    SpreadsheetSelection.A1
                )
            ),
            "\"A1\" DISABLED id=formula-anchor-id"
        );
    }

    @Test
    public void testSetValueWithCell() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.A1
                    )
                ),
            "\"A1\" [#/1/SpreadsheetName22/cell/A1/formula] id=formula-anchor-id"
        );
    }

    @Test
    public void testSetValueWithCellRange() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.parseCellRange("B2:C3")
                    )
                ),
            "\"B2:C3\" [#/1/SpreadsheetName22/cell/B2:C3/bottom-right/formula] id=formula-anchor-id"
        );
    }

    @Test
    public void testSetValueWithLabel() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.labelName("Label9999")
                    )
                ),
            "\"Label9999\" [#/1/SpreadsheetName22/cell/Label9999/formula] id=formula-anchor-id"
        );
    }

    @Test
    public void testSetShowFormulaTextSetValueWithCell() {
        this.treePrintAndCheck(
            this.createComponent()
                .setShowFormulaText(true)
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.A1
                    )
                ),
            "\"=1+2+3000\" [#/1/SpreadsheetName22/cell/A1/formula] id=formula-anchor-id"
        );
    }

    @Test
    public void testSetShowFormulaTextSetValueWithCellMissingFormula() {
        this.treePrintAndCheck(
            this.createComponent()
                .setShowFormulaText(true)
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.parseCell("B2")
                    )
                ),
            "\"B2\" [#/1/SpreadsheetName22/cell/B2/formula] id=formula-anchor-id"
        );
    }

    @Test
    public void testShowFormulaTextSetValueWithCell() {
        this.treePrintAndCheck(
            this.createComponent()
                .showFormulaText()
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.A1
                    )
                ),
            "\"=1+2+3000\" [#/1/SpreadsheetName22/cell/A1/formula] id=formula-anchor-id"
        );
    }


    @Override
    public SpreadsheetFormulaSelectAnchorComponent createComponent() {
        return this.createComponent("/1/SpreadsheetName22");
    }

    private SpreadsheetFormulaSelectAnchorComponent createComponent(final String currentHistoryToken) {
        return SpreadsheetFormulaSelectAnchorComponent.with(
            "formula-anchor-id",
            new FakeSpreadsheetFormulaSelectAnchorComponentContext() {
                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.parseString(currentHistoryToken);
                }

                @Override
                public String toString() {
                    return currentHistoryToken;
                }

                @Override
                public Optional<String> formulaText(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
                    return Optional.ofNullable(
                        SpreadsheetSelection.A1.equals(spreadsheetExpressionReference) ?
                            "=1+2+3000" :
                            null
                    );
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFormulaSelectAnchorComponent> type() {
        return SpreadsheetFormulaSelectAnchorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
