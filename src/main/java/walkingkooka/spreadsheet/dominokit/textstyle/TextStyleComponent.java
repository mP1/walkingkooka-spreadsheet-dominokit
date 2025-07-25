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

package walkingkooka.spreadsheet.dominokit.textstyle;

import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;
import walkingkooka.tree.text.TextStyle;

/**
 * A text box that accepts entry as text parsing it as a {@link TextStyle}.
 */
public final class TextStyleComponent implements ValueSpreadsheetTextBoxWrapper<TextStyleComponent, TextStyle> {

    public static TextStyleComponent empty() {
        return new TextStyleComponent();
    }

    private TextStyleComponent() {
        this.textBox = ValueSpreadsheetTextBox.with(
            TextStyle::parse,
            TextStyle::text
        );
    }

    // ValueSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ValueSpreadsheetTextBox<TextStyle> valueSpreadsheetTextBox() {
        return this.textBox;
    }

    private final ValueSpreadsheetTextBox<TextStyle> textBox;

    // Object...........................................................................................................
    @Override
    public String toString() {
        return this.textBox.toString();
    }
}
