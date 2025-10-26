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

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Optional;

public final class EmailAddressComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, EmailAddress, EmailAddressComponent> {

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            EmailAddressComponent.empty()
                .clearValue(),
            "EmailAddressComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      []\n" +
                "      Errors\n" +
                "        Empty \"EmailAddress\"\n"
        );
    }

    @Test
    public void testOptionalClearValue() {
        this.treePrintAndCheck(
            EmailAddressComponent.empty()
                .optional()
                .clearValue(),
            "EmailAddressComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      []\n"
        );
    }

    @Test
    public void testClearValueOptional() {
        this.treePrintAndCheck(
            EmailAddressComponent.empty()
                .optional()
                .clearValue(),
            "EmailAddressComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      []\n"
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            EmailAddressComponent.empty()
                .setStringValue(
                    Optional.of("user111@example.com")
                ),
            "EmailAddressComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [user111@example.com]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            EmailAddressComponent.empty()
                .setStringValue(
                    Optional.of(
                        "user222@"
                    )
                ),
            "EmailAddressComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [user222@]\n" +
                "      Errors\n" +
                "        Email missing host\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            EmailAddressComponent.empty()
                .setValue(
                    Optional.of(
                        EmailAddress.parse("user333@example.com")
                    )
                ),
            "EmailAddressComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [user333@example.com]\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public EmailAddressComponent createComponent() {
        return EmailAddressComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<EmailAddressComponent> type() {
        return EmailAddressComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
