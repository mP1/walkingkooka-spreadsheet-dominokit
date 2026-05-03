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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;

import java.util.List;

public interface TextStylePropertyEnumComponentTesting<V extends Enum<V>, C extends TextStylePropertyEnumComponentLike<V, C>>
    extends TextStylePropertyComponentTesting<HTMLFieldSetElement, V, C> {

    // filterTest.......................................................................................................

    @Test
    default void testFilterTestWithClear() {
        this.filterTestAndCheck(
            this.createComponent(),
            "CLear",
            true
        );
    }

    @Test
    default void testFilterTestWithEachEnumValue() {
        final C component = this.createComponent();

        for(final V enumValue : this.enumValues()) {
            final String name = enumValue.name();

            this.filterTestAndCheck(
                component,
                name.substring(0, 2)
                    .toUpperCase() +
                    name.substring(2)
                        .toLowerCase(),
                true
            );
        }
    }

    List<V> enumValues();
}
