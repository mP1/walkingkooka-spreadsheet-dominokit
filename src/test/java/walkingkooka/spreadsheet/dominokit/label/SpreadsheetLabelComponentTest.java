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

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.Context;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;

import java.util.Optional;

public final class SpreadsheetLabelComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetLabelName, SpreadsheetLabelComponent> {

    private final static Context CONTEXT = new Context() {
    };

    @Test
    public void testSetValueMissingValue() {
        this.treePrintAndCheck(
            SpreadsheetLabelComponent.with(CONTEXT)
                .setLabel("Label123")
                .setValue(
                    Optional.empty()
                ),
            "SpreadsheetLabelComponent\n" +
                "  SpreadsheetSuggestBoxComponent\n" +
                "    Label123 [] REQUIRED\n" +
                "    Errors\n" +
                "      Empty \"Label\"\n"
        );
    }

    @Test
    public void testSetStringValueInvalidStringValue() {
        this.treePrintAndCheck(
            SpreadsheetLabelComponent.with(CONTEXT)
                .setLabel("Label123")
                .setStringValue(
                    Optional.of("X!")
                ),
            "SpreadsheetLabelComponent\n" +
                "  SpreadsheetSuggestBoxComponent\n" +
                "    Label123 [] REQUIRED\n" +
                "    Errors\n" +
                "      Invalid character '!' at 1\n"
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            SpreadsheetLabelComponent.with(CONTEXT)
                .setLabel("Label123")
                .setStringValue(
                    Optional.of("SpreadsheetLabel456")
                ),
            "SpreadsheetLabelComponent\n" +
                "  SpreadsheetSuggestBoxComponent\n" +
                "    Label123 [SpreadsheetLabel456] REQUIRED\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetLabelComponent createComponent() {
        return SpreadsheetLabelComponent.with(CONTEXT);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetLabelComponent> type() {
        return SpreadsheetLabelComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
