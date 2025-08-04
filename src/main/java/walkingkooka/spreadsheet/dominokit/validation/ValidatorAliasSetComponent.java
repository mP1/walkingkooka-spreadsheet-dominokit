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

package walkingkooka.spreadsheet.dominokit.validation;

import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;
import walkingkooka.validation.provider.ValidatorAliasSet;

public final class ValidatorAliasSetComponent implements ValueSpreadsheetTextBoxWrapper<ValidatorAliasSetComponent, ValidatorAliasSet> {

    public static ValidatorAliasSetComponent empty() {
        return new ValidatorAliasSetComponent();
    }

    private ValidatorAliasSetComponent() {
        this.textBox = ValueTextBoxComponent.with(
            ValidatorAliasSet::parse,
            ValidatorAliasSet::text
        );
    }

    // ValueSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ValueTextBoxComponent<ValidatorAliasSet> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<ValidatorAliasSet> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}