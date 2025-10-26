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

package walkingkooka.spreadsheet.dominokit.url;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Optional;

public final class AbsoluteUrlComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, AbsoluteUrl, AbsoluteUrlComponent> {

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            AbsoluteUrlComponent.empty()
                .clearValue(),
            "AbsoluteUrlComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      []\n" +
                "      Errors\n" +
                "        no protocol: \n"
        );
    }

    @Test
    public void testOptionalClearValue() {
        this.treePrintAndCheck(
            AbsoluteUrlComponent.empty()
                .optional()
                .clearValue(),
            "AbsoluteUrlComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      []\n"
        );
    }

    @Test
    public void testClearValueOptional() {
        this.treePrintAndCheck(
            AbsoluteUrlComponent.empty()
                .optional()
                .clearValue(),
            "AbsoluteUrlComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      []\n"
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            AbsoluteUrlComponent.empty()
                .setStringValue(
                    Optional.of("https://example.com")
                ),
            "AbsoluteUrlComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://example.com]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            AbsoluteUrlComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://"
                    )
                ),
            "AbsoluteUrlComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://]\n" +
                "      Errors\n" +
                "        Missing host name\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            AbsoluteUrlComponent.empty()
                .setValue(
                    Optional.of(
                        Url.parseAbsolute("https://example.com/path1/k2=v2")
                    )
                ),
            "AbsoluteUrlComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://example.com/path1/k2%3Dv2]\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public AbsoluteUrlComponent createComponent() {
        return AbsoluteUrlComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<AbsoluteUrlComponent> type() {
        return AbsoluteUrlComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
