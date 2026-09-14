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
import walkingkooka.currency.provider.CurrencyExchangeRaterAliasSet;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentLikeTesting;

import java.util.Optional;

public final class CurrencyExchangeRaterAliasSetComponentTest implements ValueTextBoxComponentLikeTesting<CurrencyExchangeRaterAliasSetComponent, CurrencyExchangeRaterAliasSet> {

    @Test
    public void testParseAndText() {
        final CurrencyExchangeRaterAliasSet alias = CurrencyExchangeRaterAliasSet.parse("alias1 plugin1, plugin2");

        this.checkEquals(
            alias,
            CurrencyExchangeRaterAliasSet.parse(alias.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            CurrencyExchangeRaterAliasSetComponent.empty()
                .setStringValue(
                    Optional.of("alias1 name1, alias2, name2")
                ),
            "CurrencyExchangeRaterAliasSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [alias1 name1, alias2, name2] icons=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidValue() {
        this.treePrintAndCheck(
            CurrencyExchangeRaterAliasSetComponent.empty()
                .setStringValue(
                    Optional.of("alias1 name1, alias2 !")
                ),
            "CurrencyExchangeRaterAliasSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [alias1 name1, alias2 !] icons=mdi-close-circle REQUIRED\n" +
                "      Errors\n" +
                "        Invalid character '!' at 21\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public CurrencyExchangeRaterAliasSetComponent createComponent() {
        return CurrencyExchangeRaterAliasSetComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<CurrencyExchangeRaterAliasSetComponent> type() {
        return CurrencyExchangeRaterAliasSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
