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

import walkingkooka.color.Color;
import walkingkooka.spreadsheet.dominokit.value.textstyle.color.TextStylePropertyColorComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.color.TextStylePropertyColorComponentDelegator;
import walkingkooka.tree.text.TextStylePropertyName;

abstract class BorderColorSharedComponent<C extends BorderColorSharedComponent<C>> implements TextStylePropertyColorComponentDelegator<C> {

    BorderColorSharedComponent(final String idPrefix,
                               final BorderColorSharedComponentContext context) {
        super();

        this.component = TextStylePropertyColorComponent.with(
            idPrefix,
            this.name(),
            context // TextStylePropertyColorComponentContext
        );
    }

    @Override
    public abstract TextStylePropertyName<Color> name();

    @Override
    public final TextStylePropertyColorComponent textStylePropertyColorComponent() {
        return this.component;
    }

    private final TextStylePropertyColorComponent component;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.component.toString();
    }
}
