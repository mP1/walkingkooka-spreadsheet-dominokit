
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

package walkingkooka.spreadsheet.dominokit.format;

import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;

/**
 * A text box that accepts entry and validates it as a {@link SpreadsheetFormatterSelector}.
 */
public final class SpreadsheetFormatterSelectorComponent implements ValueTextBoxComponentDelegator<SpreadsheetFormatterSelectorComponent, SpreadsheetFormatterSelector> {

    public static SpreadsheetFormatterSelectorComponent empty() {
        return new SpreadsheetFormatterSelectorComponent();
    }

    private SpreadsheetFormatterSelectorComponent() {
        this.textBox = ValueTextBoxComponent.with(
            SpreadsheetFormatterSelector::parse,
            SpreadsheetFormatterSelector::toString
        );
    }

    // ValueTextBoxComponentDelegator..................................................................................

    @Override
    public ValueTextBoxComponent<SpreadsheetFormatterSelector> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<SpreadsheetFormatterSelector> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}