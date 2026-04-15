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

package walkingkooka.spreadsheet.dominokit.value;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.text.TextStylePropertyName;

import static org.junit.Assert.assertThrows;

public final class FormElementDelegatorTest implements ClassTesting<FormElementDelegator<?, ?>> {

    // propertyNameToLabel..............................................................................................

    @Test
    public void testPropertyNameToLabelWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> FormElementDelegator.propertyNameToLabel(null)
        );
    }

    @Test
    public void testPropertyNameToLabelColor() {
        this.propertyNameToLabelAndCheck(
            TextStylePropertyName.COLOR,
            "Color"
        );
    }

    @Test
    public void testPropertyNameToLabelTextAlign() {
        this.propertyNameToLabelAndCheck(
            TextStylePropertyName.TEXT_ALIGN,
            "Text Align"
        );
    }

    private void propertyNameToLabelAndCheck(final TextStylePropertyName<?> propertyName,
                                     final String expected) {
        this.checkEquals(
            FormElementDelegator.propertyNameToLabel(propertyName),
            expected,
            propertyName::toString
        );
    }

    // class............................................................................................................

    @Override
    public Class<FormElementDelegator<?, ?>> type() {
        return Cast.to(FormElementDelegator.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
