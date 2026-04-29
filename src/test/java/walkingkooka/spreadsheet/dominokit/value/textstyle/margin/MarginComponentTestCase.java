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

package walkingkooka.spreadsheet.dominokit.value.textstyle.margin;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.textstyle.length.TextStylePropertyLengthComponentLikeTesting;
import walkingkooka.tree.text.Length;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class MarginComponentTestCase<C extends MarginSharedComponent<C>> implements TextStylePropertyLengthComponentLikeTesting<C> {

    final static String ID_PREFIX = "TestIdPrefix123-";

    final static Length<?> LENGTH = Length.pixel(12.5);

    MarginComponentTestCase() {
        super();
    }

    @Test
    public final void testWithNullIdPrefix() {
        assertThrows(
            NullPointerException.class,
            () -> this.createComponent(null)
        );
    }

    @Test
    public final void testWithEmptyIdPrefix() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createComponent("")
        );
    }

    @Override
    public C createComponent() {
        return this.createComponent(ID_PREFIX);
    }

    abstract C createComponent(final String idPrefix);

    // class............................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
