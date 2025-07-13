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
import org.dominokit.domino.ui.elements.SpanElement;
import org.dominokit.domino.ui.forms.suggest.SuggestOption;
import org.dominokit.domino.ui.forms.suggest.SuggestionsStore;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;
import java.util.function.Consumer;

public final class SpreadsheetSuggestBoxComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetCellReference, SpreadsheetSuggestBoxComponent<SpreadsheetCellReference>> {

    private final static SuggestionsStore<String, SpanElement, SuggestOption<String>> SUGGESTIONS_STORE = new SuggestionsStore<>() {

        @Override
        public void filter(final String value,
                           final SuggestionsHandler<String, SpanElement, SuggestOption<String>> handler) {

        }

        @Override
        public void find(final String searchValue,
                         final Consumer<SuggestOption<String>> handler) {

        }
    };

    @Test
    public void testOptionalValidationPass() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabel("Label123")
                .optional(),
            "SpreadsheetSuggestBoxComponent\n" +
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
            "SpreadsheetSuggestBoxComponent\n" +
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
            "SpreadsheetSuggestBoxComponent\n" +
                "  Label123 [A1] REQUIRED\n"
        );
    }

    @Test
    public void testClearValueRequiredValidationFailure() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabel("Label123")
                .clearValue(),
            "SpreadsheetSuggestBoxComponent\n" +
                "  Label123 [] REQUIRED\n" +
                "  Errors\n" +
                "    Required\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<SpreadsheetCellReference> createComponent() {
        return SpreadsheetSuggestBoxComponent.with(
            SpreadsheetSelection::parseCell,
            SUGGESTIONS_STORE
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetSuggestBoxComponent<SpreadsheetCellReference>> type() {
        return Cast.to(SpreadsheetSuggestBoxComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
