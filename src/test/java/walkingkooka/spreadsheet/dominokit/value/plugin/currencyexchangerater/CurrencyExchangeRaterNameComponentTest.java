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

package walkingkooka.spreadsheet.dominokit.value.plugin.currencyexchangerater;

import org.junit.jupiter.api.Test;
import walkingkooka.currency.provider.CurrencyExchangeRaterName;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentLikeTesting;

import java.util.Optional;

public final class CurrencyExchangeRaterNameComponentTest implements ValueTextBoxComponentLikeTesting<CurrencyExchangeRaterNameComponent, CurrencyExchangeRaterName> {

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            CurrencyExchangeRaterNameComponent.empty()
                .setStringValue(
                    Optional.of(
                        "hello"
                    )
                ),
            "CurrencyExchangeRaterNameComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [hello] icons=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            CurrencyExchangeRaterNameComponent.empty()
                .setStringValue(
                    Optional.of(
                        "invalid123!"
                    )
                ),
            "CurrencyExchangeRaterNameComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [invalid123!] icons=mdi-close-circle REQUIRED\n" +
                "      Errors\n" +
                "        Invalid character '!' at 10\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public CurrencyExchangeRaterNameComponent createComponent() {
        return CurrencyExchangeRaterNameComponent.empty();
    }

    // class............................................................................................................
    @Override
    public Class<CurrencyExchangeRaterNameComponent> type() {
        return CurrencyExchangeRaterNameComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
