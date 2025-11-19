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

import elemental2.dom.HTMLElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;

import java.util.Optional;

public final class LabelComponentTest implements ValueComponentTesting<HTMLElement, String, LabelComponent> {

    @Test
    public void testSetValueWithEmptyOptional() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.empty()
                ),
            "LabelComponent\n"
        );
    }

    @Test
    public void testSetValueWithEmptyString() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of("")
                ),
            "LabelComponent\n"
        );
    }

    @Test
    public void testSetValueWithNotEmptyString() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of("Hello")
                ),
            "LabelComponent\n" +
                "  \"Hello\"\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public LabelComponent createComponent() {
        return LabelComponent.empty();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<LabelComponent> type() {
        return LabelComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
