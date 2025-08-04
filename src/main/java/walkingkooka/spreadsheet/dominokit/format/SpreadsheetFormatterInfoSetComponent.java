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
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfoSet;

public final class SpreadsheetFormatterInfoSetComponent implements ValueTextBoxComponentDelegator<SpreadsheetFormatterInfoSetComponent, SpreadsheetFormatterInfoSet> {

    public static SpreadsheetFormatterInfoSetComponent empty() {
        return new SpreadsheetFormatterInfoSetComponent();
    }

    private SpreadsheetFormatterInfoSetComponent() {
        this.textBox = ValueTextBoxComponent.with(
            SpreadsheetFormatterInfoSet::parse,
            SpreadsheetFormatterInfoSet::text
        );
    }

    // ValueTextBoxComponentDelegator..................................................................................

    @Override
    public ValueTextBoxComponent<SpreadsheetFormatterInfoSet> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<SpreadsheetFormatterInfoSet> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}