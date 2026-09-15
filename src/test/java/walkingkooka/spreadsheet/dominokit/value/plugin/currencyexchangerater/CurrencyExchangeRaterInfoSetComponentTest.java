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
import walkingkooka.currency.provider.CurrencyExchangeRaterInfoSet;
import walkingkooka.currency.provider.CurrencyExchangeRaterProviders;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentLikeTesting;

import java.math.BigDecimal;
import java.util.Optional;

public final class CurrencyExchangeRaterInfoSetComponentTest implements ValueTextBoxComponentLikeTesting<CurrencyExchangeRaterInfoSetComponent, CurrencyExchangeRaterInfoSet> {

    @Test
    public void testParseAndText() {
        final CurrencyExchangeRaterInfoSet infos = CurrencyExchangeRaterInfoSet.EMPTY.setElements(
            CurrencyExchangeRaterProviders.currencyExchangeRaters(BigDecimal::new)
                .currencyExchangeRaterInfos()
        );

        this.checkEquals(
            infos,
            CurrencyExchangeRaterInfoSet.parse(infos.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            CurrencyExchangeRaterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        CurrencyExchangeRaterProviders.currencyExchangeRaters(BigDecimal::new)
                            .currencyExchangeRaterInfos()
                            .text()
                    )
                ),
            "CurrencyExchangeRaterInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://github.com/mP1/walkingkooka-currency-provider/CurrencyExchangeRater/empty empty, https://github.com/mP1/walkingkooka-currency-provider/CurrencyExchangeRater/properties properties] icons=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidCurrencyExchangeRaterName() {
        this.treePrintAndCheck(
            CurrencyExchangeRaterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://example.com hello more more"
                    )
                ),
            "CurrencyExchangeRaterInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://example.com hello more more] icons=mdi-close-circle REQUIRED\n" +
                "      Errors\n" +
                "        Invalid character 'm' at 26\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidSecondAbsoluteUrl() {
        this.treePrintAndCheck(
            CurrencyExchangeRaterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://example.com/1 good,bad://example.com/2 bad"
                    )
                ),
            "CurrencyExchangeRaterInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://example.com/1 good,bad://example.com/2 bad] icons=mdi-close-circle REQUIRED\n" +
                "      Errors\n" +
                "        unknown protocol: bad\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidSecondCurrencyExchangeRaterName() {
        this.treePrintAndCheck(
            CurrencyExchangeRaterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://example.com/1 good,https://example.com/2 bad!"
                    )
                ),
            "CurrencyExchangeRaterInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://example.com/1 good,https://example.com/2 bad!] icons=mdi-close-circle REQUIRED\n" +
                "      Errors\n" +
                "        Invalid character '!' at 52\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public CurrencyExchangeRaterInfoSetComponent createComponent() {
        return CurrencyExchangeRaterInfoSetComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<CurrencyExchangeRaterInfoSetComponent> type() {
        return CurrencyExchangeRaterInfoSetComponent.class;
    }
}
