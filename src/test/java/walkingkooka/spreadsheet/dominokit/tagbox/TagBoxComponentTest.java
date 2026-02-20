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

package walkingkooka.spreadsheet.dominokit.tagbox;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.List;
import java.util.Optional;

public final class TagBoxComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, List<SpreadsheetCellReference>, TagBoxComponent<SpreadsheetCellReference>> {

    private final static TagBoxComponentSuggestionsProvider<SpreadsheetCellReference> SUGGESTIONS_PROVIDER = new TagBoxComponentSuggestionsProvider<>() {

        @Override
        public void filter(final String value,
                           final TagBoxComponent<SpreadsheetCellReference> TagBox) {
            // NOP
        }

        @Override
        public void verifyOption(final SpreadsheetCellReference searchValue,
                                 final TagBoxComponent<SpreadsheetCellReference> TagBox) {
            // NOP
        }

        @Override
        public String menuItemKey(final SpreadsheetCellReference value) {
            return value.text();
        }
    };

    @Test
    public void testOptionalValidationPass() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabel("Label123")
                .optional(),
            "TagBoxComponent\n" +
                "  Label123 []\n"
        );
    }

    @Test
    public void testOptionalEmptyValueValidationPass() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabel("Label123")
                .optional()
                .setValue(Optional.empty()),
            "TagBoxComponent\n" +
                "  Label123 []\n"
        );
    }

    @Test
    public void testValidationPass() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabel("Label123")
                .setValue(
                    Optional.of(
                        Lists.of(SpreadsheetSelection.A1)
                    )
                ),
            "TagBoxComponent\n" +
                "  Label123 [A1] REQUIRED\n"
        );
    }

    @Test
    public void testClearValueRequiredValidationFailure() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabel("Label123")
                .clearValue(),
            "TagBoxComponent\n" +
                "  Label123 [] REQUIRED\n" +
                "  Errors\n" +
                "    Required\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public TagBoxComponent<SpreadsheetCellReference> createComponent() {
        return TagBoxComponent.with(
            SUGGESTIONS_PROVIDER,
            (SpreadsheetCellReference c) -> {
                throw new UnsupportedOperationException();
            }
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            this.createComponent(),
            "TagBoxComponent\n" +
                "  [] REQUIRED\n" +
                "  Errors\n" +
                "    Required\n"
        );
    }

    @Test
    public void testTreePrintWithValues() {
        final TagBoxComponent<SpreadsheetCellReference> component = this.createComponent();
        component.setValue(
            Optional.of(
                Lists.of(
                    SpreadsheetSelection.A1,
                    SpreadsheetSelection.parseCell("B2"),
                    SpreadsheetSelection.parseCell("C3")
                )
            )
        );

        this.treePrintAndCheck(
            component,
            "TagBoxComponent\n" +
                "  [A1,B2,C3] REQUIRED\n"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<TagBoxComponent<SpreadsheetCellReference>> type() {
        return Cast.to(TagBoxComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
