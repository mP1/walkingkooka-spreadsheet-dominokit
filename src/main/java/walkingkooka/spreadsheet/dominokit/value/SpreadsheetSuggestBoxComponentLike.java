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

package walkingkooka.spreadsheet.dominokit.value;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.dominokit.ui.textbox.SpreadsheetTextBoxTreePrintable;
import walkingkooka.text.HasText;

import java.util.Optional;

/**
 * A text box component that includes support for finding a label.
 */
public interface SpreadsheetSuggestBoxComponentLike<T extends HasText> extends ValueComponent<HTMLFieldSetElement, T, SpreadsheetSuggestBoxComponent<T>>,
        SpreadsheetTextBoxTreePrintable<SpreadsheetSuggestBoxComponent<T>, T> {

    SpreadsheetSuggestBoxComponent<T> setStringValue(final Optional<String> value);

    Optional<String> stringValue();
}
