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

package walkingkooka.spreadsheet.dominokit.value.textstyle.textindent;

import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStyleLengthPropertyComponentLike;
import walkingkooka.spreadsheet.dominokit.value.textstyle.length.LengthComponent;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStylePropertyName;

public final class TextIndentComponent implements TextStyleLengthPropertyComponentLike<TextIndentComponent> {

    public static TextIndentComponent empty(final String idPrefix) {
        return new TextIndentComponent(idPrefix);
    }

    private TextIndentComponent(final String idPrefix) {
        this.lengthComponent = LengthComponent.empty();

        this.setIdPrefix(idPrefix);
        this.setLabelFromPropertyName();
    }

    @Override
    public TextStylePropertyName<Length<?>> name() {
        return TextStylePropertyName.TEXT_INDENT;
    }

    // LengthComponentDelegator.........................................................................................

    @Override
    public LengthComponent lengthComponent() {
        return this.lengthComponent;
    }

    private final LengthComponent lengthComponent;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.lengthComponent.toString();
    }
}
