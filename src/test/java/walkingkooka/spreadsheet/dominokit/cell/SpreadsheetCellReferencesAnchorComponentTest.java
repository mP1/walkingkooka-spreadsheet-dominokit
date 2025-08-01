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
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellReferencesAnchorComponentTest implements AnchorComponentTesting<SpreadsheetCellReferencesAnchorComponent, SpreadsheetExpressionReference> {

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellReferencesAnchorComponent.with(
                "id123",
                null
            )
        );
    }

    // value............................................................................................................

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            SpreadsheetCellReferencesAnchorComponent.with(
                "cell-references-anchor-id",
                SpreadsheetCellReferencesAnchorComponentContexts.fake()
            ).clearValue(),
            "\"References\" DISABLED id=cell-references-anchor-id"
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
            "\"A1\" DISABLED (0) id=cell-references-anchor-id"
        );
    }

    @Test
    public void testSetValueWithCellWithZeroSpreadsheetExpressionReferences() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.A1
                    )
                ),
            "\"A1\" [#/1/SpreadsheetName22/cell/A1/references] (0) id=cell-references-anchor-id"
        );
    }

    @Test
    public void testSetValueWithCellWithNonZeroSpreadsheetExpresionReferences() {
        this.treePrintAndCheck(
            this.createComponent(
                SpreadsheetSelection.parseCell("B2"),
                SpreadsheetSelection.parseCell("C3")
            ).setValue(
                Optional.of(
                    SpreadsheetSelection.A1
                )
            ),
            "\"A1\" [#/1/SpreadsheetName22/cell/A1/references] (2) id=cell-references-anchor-id"
        );
    }

    @Test
    public void testSetValueWithCellRange() {
        this.treePrintAndCheck(
            this.createComponent(
                SpreadsheetSelection.parseCell("B2")
            ).setValue(
                Optional.of(
                    SpreadsheetSelection.parseCellRange("B2:C3")
                )
            ),
            "\"B2:C3\" [#/1/SpreadsheetName22/cell/B2:C3/bottom-right/references] (1) id=cell-references-anchor-id"
        );
    }

    @Test
    public void testSetValueWithLabel() {
        this.treePrintAndCheck(
            this.createComponent(
                SpreadsheetSelection.parseCell("B2"),
                SpreadsheetSelection.parseCellRange("C3:D4")
            ).setValue(
                Optional.of(
                    SpreadsheetSelection.labelName("Label9999")
                )
            ),
            "\"Label9999\" [#/1/SpreadsheetName22/cell/Label9999/references] (2) id=cell-references-anchor-id"
        );
    }

    @Override
    public SpreadsheetCellReferencesAnchorComponent createComponent() {
        return this.createComponent(
            new SpreadsheetExpressionReference[0]
        );
    }

    private SpreadsheetCellReferencesAnchorComponent createComponent(final SpreadsheetExpressionReference... spreadsheetExpressionReference) {
        return this.createComponent(
            "/1/SpreadsheetName22",
            spreadsheetExpressionReference
        );
    }

    private SpreadsheetCellReferencesAnchorComponent createComponent(final String currentHistoryToken,
                                                                     final SpreadsheetExpressionReference... spreadsheetExpressionReference) {
        return SpreadsheetCellReferencesAnchorComponent.with(
            "cell-references-anchor-id",
            new FakeSpreadsheetCellReferencesAnchorComponentContext() {


                @Override
                public Set<SpreadsheetExpressionReference> cellReferences(final SpreadsheetExpressionReference r) {
                    return Sets.of(spreadsheetExpressionReference);
                }

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
    public Class<SpreadsheetCellReferencesAnchorComponent> type() {
        return SpreadsheetCellReferencesAnchorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
