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

package walkingkooka.spreadsheet.dominokit.label;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;
import java.util.Set;

public final class SpreadsheetLabelLinksComponentTest implements HtmlComponentTesting<SpreadsheetLabelLinksComponent, HTMLDivElement> {

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            SpreadsheetLabelLinksComponent.empty(
                "labels-",
                new FakeSpreadsheetLabelLinksComponentContext() {

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
            "SpreadsheetLabelLinksComponent\n" +
                "  SpreadsheetLinkListComponent\n" +
                "    SpreadsheetFlexLayout\n" +
                "      ROW\n" +
                "        \"References\" DISABLED id=labels-references-Link\n" +
                "        \"Delete\" DISABLED id=labels-delete-Link\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            SpreadsheetLabelLinksComponent.empty(
                "labels-",
                new FakeSpreadsheetLabelLinksComponentContext() {

                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.cellFormula(
                            SpreadsheetId.with(1),
                            SpreadsheetName.with("SpreadsheetName222"),
                            SpreadsheetSelection.A1.setDefaultAnchor()
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
                    SpreadsheetSelection.labelName("LABELZZZ")
                )
            ),
            "SpreadsheetLabelLinksComponent\n" +
                "  SpreadsheetLinkListComponent\n" +
                "    SpreadsheetFlexLayout\n" +
                "      ROW\n" +
                "        \"References\" [#/1/SpreadsheetName222/cell/LABELZZZ/references] (2) id=labels-references-Link\n" +
                "        \"Delete\" [#/1/SpreadsheetName222/label/LABELZZZ/delete] id=labels-delete-Link\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetLabelLinksComponent> type() {
        return SpreadsheetLabelLinksComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
