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

package walkingkooka.spreadsheet.dominokit.value.textstyle.color;

import walkingkooka.tree.text.TextStylePropertyName;

public final class TextStyleColorComponent implements TextStylePropertyColorComponentDelegator<TextStyleColorComponent> {

    public static TextStyleColorComponent with(final String idPrefix,
                                               final TextStyleColorComponentContext context) {
        return new TextStyleColorComponent(
            idPrefix,
            context
        );
    }

    private TextStyleColorComponent(final String idPrefix,
                                    final TextStyleColorComponentContext context) {
        super();

        this.component = TextStylePropertyColorComponent.with(
            idPrefix,
            TextStylePropertyName.COLOR,
            context // TextStyleColorComponentContext
        );
    }

    @Override
    public TextStylePropertyColorComponent textStylePropertyColorComponent() {
        return this.component;
    }

    private final TextStylePropertyColorComponent component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
