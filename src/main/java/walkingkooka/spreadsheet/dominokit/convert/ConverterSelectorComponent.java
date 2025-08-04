
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

package walkingkooka.spreadsheet.dominokit.convert;

import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;

/**
 * A text box that accepts entry and validates it as a {@link ConverterSelector}.
 */
public final class ConverterSelectorComponent implements ValueTextBoxComponentDelegator<ConverterSelectorComponent, ConverterSelector> {

    public static ConverterSelectorComponent empty() {
        return new ConverterSelectorComponent();
    }

    private ConverterSelectorComponent() {
        this.textBox = ValueTextBoxComponent.with(
            ConverterSelector::parse,
            ConverterSelector::toString
        );
    }

    // ValueTextBoxComponentDelegator..................................................................................

    @Override
    public ValueTextBoxComponent<ConverterSelector> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<ConverterSelector> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}