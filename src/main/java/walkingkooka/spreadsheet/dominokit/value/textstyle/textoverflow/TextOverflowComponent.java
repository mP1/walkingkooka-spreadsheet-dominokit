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

package walkingkooka.spreadsheet.dominokit.value.textstyle.textoverflow;

import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyValueTextBoxComponentLike;
import walkingkooka.tree.text.TextOverflow;
import walkingkooka.tree.text.TextStylePropertyName;

/**
 * A text box that accepts text entry and validates it as a {@link TextOverflow}.
 */
public final class TextOverflowComponent implements TextStylePropertyValueTextBoxComponentLike<TextOverflowComponent, TextOverflow> {

    public static TextOverflowComponent with(final String idPrefix) {
        return new TextOverflowComponent(idPrefix);
    }

    private TextOverflowComponent(final String idPrefix) {
        this.textBox = ValueTextBoxComponent.with(
            TextOverflow::parse,
            TextOverflow::text
        );
        this.setIdPrefix(
            idPrefix,
            SpreadsheetElementIds.TEXT_BOX
        );
    }

    // HasName..........................................................................................................

    @Override
    public TextStylePropertyName<TextOverflow> name() {
        return TextStylePropertyName.TEXT_OVERFLOW;
    }

    // TextStylePropertyValueTextBoxComponentLikeDelegator..............................................................

    @Override
    public ValueTextBoxComponent<TextOverflow> textStylePropertyValueTextBoxComponentLike() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<TextOverflow> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}