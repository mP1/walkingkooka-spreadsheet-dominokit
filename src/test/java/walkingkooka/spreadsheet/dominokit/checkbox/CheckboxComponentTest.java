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

package walkingkooka.spreadsheet.dominokit.checkbox;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Optional;

public final class CheckboxComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, Boolean, CheckboxComponent> {

    @Test
    public void testTreePrintDisabledTrue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabel("Label123")
                .setValue(Optional.of(true))
                .setId("id987")
                .setDisabled(true),
            "CheckboxComponent\n" +
                "  Label123 [true] id=id987 DISABLED\n"
        );
    }

    @Test
    public void testTreePrintEnabledFalse() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabel("Label123")
                .setValue(Optional.of(false))
                .setId("id987")
                .setDisabled(false),
            "CheckboxComponent\n" +
                "  Label123 [false] id=id987 DISABLED\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public CheckboxComponent createComponent() {
        return CheckboxComponent.empty();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<CheckboxComponent> type() {
        return CheckboxComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
