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

package walkingkooka.spreadsheet.dominokit.email;


import walkingkooka.net.email.EmailAddress;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;

/**
 * A text box that supports and validates {@link EmailAddress}.
 */
public final class EmailAddressComponent implements ValueTextBoxComponentDelegator<EmailAddressComponent, EmailAddress> {

    public static EmailAddressComponent empty() {
        return new EmailAddressComponent();
    }

    private EmailAddressComponent() {
        this.textBox = ValueTextBoxComponent.with(
            EmailAddress::parse,
            EmailAddress::text
        );
    }

    // ValueTextBoxComponentDelegator..................................................................................

    @Override
    public ValueTextBoxComponent<EmailAddress> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<EmailAddress> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}