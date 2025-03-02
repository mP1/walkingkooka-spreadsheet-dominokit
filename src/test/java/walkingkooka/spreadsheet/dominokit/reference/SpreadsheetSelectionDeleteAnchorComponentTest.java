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

package walkingkooka.spreadsheet.dominokit.reference;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetSelectionDeleteAnchorComponentTest implements AnchorComponentTesting<SpreadsheetSelectionDeleteAnchorComponent, SpreadsheetSelection> {

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetSelectionDeleteAnchorComponent.with(
                "id123",
                null
            )
        );
    }

    // value............................................................................................................

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            SpreadsheetSelectionDeleteAnchorComponent.with(
                "selection-anchor-id",
                HistoryContexts.fake()
            ).clearValue(),
            "\"Delete\" DISABLED id=selection-anchor-id"
        );
    }

    @Test
    public void testSetValueWithColumn() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.parseColumn("A")
                    )
                ),
            "\"A\" [#/1/SpreadsheetName222/column/A/delete] id=selection-anchor-id"
        );
    }

    @Test
    public void testSetValueWithColumnRange() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.parseColumnRange("B:C")
                    )
                ),
            "\"B:C\" [#/1/SpreadsheetName222/column/B:C/right/delete] id=selection-anchor-id"
        );
    }

    @Test
    public void testSetValueWithColumnRange2() {
        this.treePrintAndCheck(
            this.createComponent("/1/SpreadsheetName222/column/B:C/left")
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.parseColumnRange("B:C")
                    )
                ),
            "\"B:C\" [#/1/SpreadsheetName222/column/B:C/left/delete] id=selection-anchor-id"
        );
    }

    @Test
    public void testSetValueWithRow() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.parseRow("4")
                    )
                ),
            "\"4\" [#/1/SpreadsheetName222/row/4/delete] id=selection-anchor-id"
        );
    }

    @Test
    public void testSetValueWithRowRange() {
        this.treePrintAndCheck(
            this.createComponent("/1/SpreadsheetName222/row/5:6")
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.parseRowRange("5:6")
                    )
                ),
            "\"5:6\" [#/1/SpreadsheetName222/row/5:6/bottom/delete] id=selection-anchor-id"
        );
    }

    @Test
    public void testSetValueWithRowRange2() {
        this.treePrintAndCheck(
            this.createComponent("/1/SpreadsheetName222/row/5:6/top")
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.parseRowRange("5:6")
                    )
                ),
            "\"5:6\" [#/1/SpreadsheetName222/row/5:6/top/delete] id=selection-anchor-id"
        );
    }

    @Test
    public void testSetValueWithCell() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(SpreadsheetSelection.A1)
                ),
            "\"A1\" [#/1/SpreadsheetName222/cell/A1/delete] id=selection-anchor-id"
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
            "\"B2:C3\" [#/1/SpreadsheetName222/cell/B2:C3/bottom-right/delete] id=selection-anchor-id"
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
            "\"Label9999\" [#/1/SpreadsheetName222/cell/Label9999/delete] id=selection-anchor-id"
        );
    }

    @Test
    public void testSetValueWithLabelWhenNonSpreadsheetNameHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent("/create")
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.labelName("Label9999")
                    )
                ),
            "\"Label9999\" DISABLED id=selection-anchor-id"
        );
    }

    // helpers..........................................................................................................

    @Override
    public SpreadsheetSelectionDeleteAnchorComponent createComponent() {
        return this.createComponent(
            "/1/SpreadsheetName222/column/A"
        );
    }

    private SpreadsheetSelectionDeleteAnchorComponent createComponent(final String currentHistoryToken) {
        return SpreadsheetSelectionDeleteAnchorComponent.with(
            "selection-anchor-id",
            new FakeHistoryContext() {

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
    public Class<SpreadsheetSelectionDeleteAnchorComponent> type() {
        return SpreadsheetSelectionDeleteAnchorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
