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

package walkingkooka.spreadsheet.dominokit.find;


import walkingkooka.predicate.Predicates;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;
import walkingkooka.text.CaseSensitivity;

import java.util.function.Predicate;

public final class TextMatchComponent implements ValueSpreadsheetTextBoxWrapper<TextMatchComponent, Predicate<CharSequence>> {

    public static TextMatchComponent empty() {
        return new TextMatchComponent();
    }

    private TextMatchComponent() {
        this.textBox = ValueSpreadsheetTextBox.with(
                TextMatchComponent::parse,
                Object::toString
        );
    }

    private static Predicate<CharSequence> parse(final String text) {
        return Predicates.globPatterns(
                text,
                CaseSensitivity.INSENSITIVE,
                '\\'
        );
    }

    // ValueSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ValueSpreadsheetTextBox<Predicate<CharSequence>> parserSpreadsheetTextBox() {
        return this.textBox;
    }

    private final ValueSpreadsheetTextBox<Predicate<CharSequence>> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}
