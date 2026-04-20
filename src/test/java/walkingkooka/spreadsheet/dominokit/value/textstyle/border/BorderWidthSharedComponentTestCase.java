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

package walkingkooka.spreadsheet.dominokit.value.textstyle.border;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.length.TextStyleLengthPropertyComponentLikeTesting;
import walkingkooka.tree.text.Length;

import java.util.Optional;

public abstract class BorderWidthSharedComponentTestCase<C extends BorderWidthSharedComponent<C>> implements TextStyleLengthPropertyComponentLikeTesting<C> {

    final static Length<?> LENGTH = Length.parse("1px");

    public BorderWidthSharedComponentTestCase() {
        super();
    }

    @Test
    public final void testSetValueWithNoneLength() {
        this.setValueAndCheck(
            this.createComponent(),
            Length.none()
        );
    }

    @Test
    public final void testSetValueWithNormalLength() {
        final C component = this.createComponent();
        component.setValue(
            Optional.of(Length.normal())
        );
        this.checkNotEquals(
            Lists.empty(),
            component.errors()
        );
    }

    @Test
    public final void testSetValueWithPixelLength() {
        this.setValueAndCheck(
            this.createComponent(),
            Length.pixel(123.5)
        );
    }

    @Test
    public final void testAddValueWatcher() {
        this.fired = null;

        final C component = this.createComponent();
        component.addValueWatcher(
            new ValueWatcher<Length<?>>() {
                @Override
                public void onValue(Optional<Length<?>> value) {
                    BorderWidthSharedComponentTestCase.this.fired = value.orElse(null);
                }
            }
        );

        component.setValue(
            Optional.of(LENGTH)
        );

        this.checkEquals(
            LENGTH,
            this.fired,
            "fired value"
        );
    }

    private Length<?> fired;

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
