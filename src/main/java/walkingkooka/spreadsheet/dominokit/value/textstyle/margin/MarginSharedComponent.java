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

import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStyleLengthPropertyComponentLike;
import walkingkooka.spreadsheet.dominokit.value.textstyle.length.LengthComponent;
import walkingkooka.tree.text.Margin;

/**
 * A text box that accepts text entry and validates it as a {@link Margin}.
 */
abstract class MarginSharedComponent<C extends MarginSharedComponent<C>> implements TextStyleLengthPropertyComponentLike<C> {

    MarginSharedComponent(final String idPrefix) {
        super();

        this.lengthComponent = LengthComponent.with(
            this.name()
        );

        this.setIdPrefix(idPrefix);
    }

    // LengthComponentDelegator.........................................................................................

    @Override
    public final LengthComponent lengthComponent() {
        return this.lengthComponent;
    }

    private final LengthComponent lengthComponent;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.lengthComponent.toString();
    }
}