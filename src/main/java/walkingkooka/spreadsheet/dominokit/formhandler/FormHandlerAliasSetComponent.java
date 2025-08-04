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

package walkingkooka.spreadsheet.dominokit.formhandler;

import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;
import walkingkooka.validation.form.provider.FormHandlerAliasSet;

public final class FormHandlerAliasSetComponent implements ValueTextBoxComponentDelegator<FormHandlerAliasSetComponent, FormHandlerAliasSet> {

    public static FormHandlerAliasSetComponent empty() {
        return new FormHandlerAliasSetComponent();
    }

    private FormHandlerAliasSetComponent() {
        this.textBox = ValueTextBoxComponent.with(
            FormHandlerAliasSet::parse,
            FormHandlerAliasSet::text
        );
    }

    // ValueTextBoxComponentDelegator..................................................................................

    @Override
    public ValueTextBoxComponent<FormHandlerAliasSet> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<FormHandlerAliasSet> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}