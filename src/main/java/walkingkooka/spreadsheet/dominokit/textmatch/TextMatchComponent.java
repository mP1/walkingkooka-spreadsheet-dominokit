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

package walkingkooka.spreadsheet.dominokit.textmatch;


import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;
import walkingkooka.spreadsheet.expression.function.TextMatch;

/**
 * A text box that allows the entry of a glob pattern which may be used to match the text form of a {@link walkingkooka.spreadsheet.SpreadsheetCell}
 * component within a FIND component.
 * <pre>
 * text contains [_] <-- TextMatchComponent
 *
 * _search_
 * </pre>
 */
public final class TextMatchComponent implements ValueSpreadsheetTextBoxWrapper<TextMatchComponent, TextMatch> {

    public static TextMatchComponent empty() {
        return new TextMatchComponent();
    }

    private TextMatchComponent() {
        this.textBox = ValueSpreadsheetTextBox.with(
            TextMatch::parse,
            Object::toString
        );
    }
    // ValueSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ValueSpreadsheetTextBox<TextMatch> valueSpreadsheetTextBox() {
        return this.textBox;
    }

    private final ValueSpreadsheetTextBox<TextMatch> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}
