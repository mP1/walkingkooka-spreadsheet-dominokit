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

package walkingkooka.spreadsheet.dominokit.suggestbox;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class SuggestBoxComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetCellReference, SuggestBoxComponent<SpreadsheetCellReference>> {

    private final static SuggestBoxComponentSuggestionsProvider<SpreadsheetCellReference> SUGGESTIONS_PROVIDER = new SuggestBoxComponentSuggestionsProvider<>() {

        @Override
        public void filter(final String value,
                           final SuggestBoxComponent<SpreadsheetCellReference> suggestBox) {
            // NOP
        }

        @Override
        public void verifyOption(final SpreadsheetCellReference searchValue,
                                 final SuggestBoxComponent<SpreadsheetCellReference> suggestBox) {
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
            "SuggestBoxComponent\n" +
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
            "SuggestBoxComponent\n" +
                "  Label123 []\n"
        );
    }

    @Test
    public void testValidationPass() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabel("Label123")
                .setValue(
                    Optional.of(SpreadsheetSelection.A1)
                ),
            "SuggestBoxComponent\n" +
                "  Label123 [A1] REQUIRED\n"
        );
    }

    @Test
    public void testClearValueRequiredValidationFailure() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabel("Label123")
                .clearValue(),
            "SuggestBoxComponent\n" +
                "  Label123 [] REQUIRED\n" +
                "  Errors\n" +
                "    Required\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SuggestBoxComponent<SpreadsheetCellReference> createComponent() {
        return SuggestBoxComponent.with(
            SUGGESTIONS_PROVIDER,
            (SpreadsheetCellReference c) -> {
                throw new UnsupportedOperationException();
            }
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SuggestBoxComponent<SpreadsheetCellReference>> type() {
        return Cast.to(SuggestBoxComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
