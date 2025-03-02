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

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellLabelsAnchorComponentTest implements AnchorComponentTesting<SpreadsheetCellLabelsAnchorComponent, SpreadsheetExpressionReference> {

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellLabelsAnchorComponent.with(
                "id123",
                null
            )
        );
    }

    // value............................................................................................................

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            SpreadsheetCellLabelsAnchorComponent.with(
                "cell-labels-anchor-id",
                SpreadsheetCellLabelsAnchorComponentContexts.fake()
            ).clearValue(),
            "\"Labels\" DISABLED id=cell-labels-anchor-id"
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
            "\"A1\" DISABLED id=cell-labels-anchor-id"
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
            "\"A1\" [#/1/SpreadsheetName22/cell/A1/labels] id=cell-labels-anchor-id"
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
            "\"B2:C3\" [#/1/SpreadsheetName22/cell/B2:C3/bottom-right/labels] id=cell-labels-anchor-id"
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
            "\"Label9999\" [#/1/SpreadsheetName22/cell/Label9999/labels] id=cell-labels-anchor-id"
        );
    }

    // setCount.........................................................................................................

    @Test
    public void testSetCountZero() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.labelName("Label9999")
                    )
                ).setCount(
                    OptionalInt.of(0)
                ),
            "\"Label9999\" [#/1/SpreadsheetName22/cell/Label9999/labels] (0) id=cell-labels-anchor-id"
        );
    }

    @Test
    public void testSetCountNonZero() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.labelName("Label9999")
                    )
                ).setCount(
                    OptionalInt.of(111)
                ),
            "\"Label9999\" [#/1/SpreadsheetName22/cell/Label9999/labels] (111) id=cell-labels-anchor-id"
        );
    }

    // helpers..........................................................................................................

    @Override
    public SpreadsheetCellLabelsAnchorComponent createComponent() {
        return this.createComponent("/1/SpreadsheetName22");
    }

    private SpreadsheetCellLabelsAnchorComponent createComponent(final String currentHistoryToken) {
        return SpreadsheetCellLabelsAnchorComponent.with(
            "cell-labels-anchor-id",
            new FakeSpreadsheetCellLabelsAnchorComponentContext() {

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.parseString(currentHistoryToken);
                }

                @Override
                public String toString() {
                    return currentHistoryToken;
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetCellLabelsAnchorComponent> type() {
        return SpreadsheetCellLabelsAnchorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
