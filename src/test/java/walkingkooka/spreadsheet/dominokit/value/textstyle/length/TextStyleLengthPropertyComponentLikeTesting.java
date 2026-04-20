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

package walkingkooka.spreadsheet.dominokit.value.textstyle.length;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.naming.HasNameTesting;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStyleLengthPropertyComponentLike;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public interface TextStyleLengthPropertyComponentLikeTesting<C extends TextStyleLengthPropertyComponentLike<C>> extends ValueComponentTesting<HTMLFieldSetElement, Length<?>, C>,
    HasNameTesting<TextStylePropertyName<Length<?>>> {

    @Test
    default void testAddValueWatcher() {
        final AtomicReference<Length<?>> fired = new AtomicReference<>();

        final C component = this.createComponent();
        component.addValueWatcher(
            new ValueWatcher<Length<?>>() {
                @Override
                public void onValue(Optional<Length<?>> value) {
                    fired.set(
                        value.orElse(null)
                    );
                }
            }
        );

        final Length<?> value = Length.pixel(123.5);

        component.setValue(
            Optional.of(value)
        );

        this.checkEquals(
            value,
            fired.get(),
            "fired value"
        );
    }
}
