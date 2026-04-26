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

import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStylePropertyName;

/**
 * A text box that accepts text entry and validates it as a {@link TextStylePropertyName#MARGIN_LEFT}.
 */
public final class MarginLeftComponent extends MarginSharedComponent<MarginLeftComponent> {

    public static MarginLeftComponent with(final String idPrefix) {
        return new MarginLeftComponent(idPrefix);
    }

    private MarginLeftComponent(final String idPrefix) {
        super(idPrefix);
    }

    @Override
    public TextStylePropertyName<Length<?>> name() {
        return TextStylePropertyName.MARGIN_LEFT;
    }
}