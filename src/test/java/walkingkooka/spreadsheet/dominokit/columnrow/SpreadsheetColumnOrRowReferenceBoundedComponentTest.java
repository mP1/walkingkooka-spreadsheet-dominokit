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

package walkingkooka.spreadsheet.dominokit.columnrow;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReferenceOrRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetColumnOrRowReferenceBoundedComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetColumnOrRowReference, SpreadsheetColumnOrRowReferenceBoundedComponent> {

    // TreePrint........................................................................................................

    @Test
    public void testTreePrintWithoutValue() {
        this.treePrintAndCheck(
            this.createComponent(
                SpreadsheetSelection.parseColumnOrColumnRange("A:C")
            ),
            "SpreadsheetColumnOrRowReferenceBoundedComponent\n" +
                "  SuggestBoxComponent\n" +
                "    [] REQUIRED\n" +
                "    Errors\n" +
                "      Required\n"
        );
    }

    @Test
    public void testTreePrintWithColumnWithin() {
        this.treePrintAndCheck(
            this.createComponent(
                    SpreadsheetSelection.parseColumnOrColumnRange("B:D")
                )
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.parseColumn("C")
                    )
                ),
            "SpreadsheetColumnOrRowReferenceBoundedComponent\n" +
                "  SuggestBoxComponent\n" +
                "    [C] REQUIRED\n"
        );
    }

    @Test
    public void testTreePrintWithRowWithin() {
        this.treePrintAndCheck(
            this.createComponent(
                    SpreadsheetSelection.parseRowOrRowRange("2:4")
                )
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.parseRow("3")
                    )
                ),
            "SpreadsheetColumnOrRowReferenceBoundedComponent\n" +
                "  SuggestBoxComponent\n" +
                "    [3] REQUIRED\n"
        );
    }

    @Override
    public void testAllMethodsVisibility() {
        throw new UnsupportedOperationException();
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetColumnOrRowReferenceBoundedComponent createComponent() {
        return this.createComponent(
            SpreadsheetSelection.parseColumnOrColumnRange("A:C")
        );
    }

    private SpreadsheetColumnOrRowReferenceBoundedComponent createComponent(final SpreadsheetColumnOrRowReferenceOrRange columnOrRowReferenceOrRange) {
        return SpreadsheetColumnOrRowReferenceBoundedComponent.empty(
            new FakeSpreadsheetColumnOrRowReferenceBoundedComponentContext() {
                @Override
                public SpreadsheetColumnOrRowReferenceOrRange columnOrRowRange() {
                    return columnOrRowReferenceOrRange;
                }

                @Override
                public String toString() {
                    return columnOrRowReferenceOrRange.toString();
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetColumnOrRowReferenceBoundedComponent> type() {
        return SpreadsheetColumnOrRowReferenceBoundedComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
