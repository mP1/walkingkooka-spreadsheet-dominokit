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

package walkingkooka.spreadsheet.dominokit.currency;

import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.menu.MenuItem;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;
import walkingkooka.currency.CurrencyContext;
import walkingkooka.currency.CurrencyContexts;
import walkingkooka.currency.FakeCurrencyContext;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.suggestbox.FakeSuggestBoxComponentSuggestionsProvider;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponent;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponentSuggestionsProvider;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Currency;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public final class CurrencyComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, Currency, CurrencyComponent<Currency>> {

    private final static Currency AUD = Currency.getInstance("AUD");
    private final static Currency NZD = Currency.getInstance("NZD");
    private final static Currency EUR = Currency.getInstance("EUR");

    private final static String ENGLISH_AUSTRALIA_TEXT = "English (Australia)";
    private final static String ENGLISH_NEW_ZEALAND_TEXT = "English (New Zealand)";
    private final static String FRENCH_TEXT = "French 123";

    private final static SuggestBoxComponentSuggestionsProvider<CurrencyComponentSuggestionsValue<Currency>> SUGGESTIONS_PROVIDER = new FakeSuggestBoxComponentSuggestionsProvider<>();

    private final static Function<CurrencyComponentSuggestionsValue<Currency>, MenuItem<CurrencyComponentSuggestionsValue<Currency>>> OPTION_MENU_ITEM_CREATOR = (v) -> {
        throw new UnsupportedOperationException(); // never actually called within a test
    };

    private final static CurrencyContext CONTEXT = new FakeCurrencyContext() {
        @Override
        public Set<Currency> findByCurrencyText(final String text,
                                                final int offset,
                                                final int count) {
            if (CurrencyContexts.CASE_SENSITIVITY.equals(text, ENGLISH_AUSTRALIA_TEXT)) {
                return Sets.of(AUD);
            }
            if (CurrencyContexts.CASE_SENSITIVITY.equals(text, ENGLISH_NEW_ZEALAND_TEXT)) {
                return Sets.of(NZD);
            }
            if (CurrencyContexts.CASE_SENSITIVITY.equals(text, FRENCH_TEXT)) {
                return Sets.of(EUR);
            }

            return Sets.empty();
        }

        @Override
        public Optional<String> currencyText(final Currency currency) {
            return Optional.ofNullable(
                AUD.equals(currency) ?
                    ENGLISH_AUSTRALIA_TEXT :
                    NZD.equals(currency) ?
                        ENGLISH_NEW_ZEALAND_TEXT :
                        EUR.equals(currency) ?
                            FRENCH_TEXT :
                            null
            );
        }
    };

    // TreePrint........................................................................................................

    @Test
    public void testTreePrintWithoutValue() {
        this.treePrintAndCheck(
            this.createComponent(),
            "CurrencyComponent\n" +
                "  SuggestBoxComponent\n" +
                "    [] REQUIRED\n" +
                "    Errors\n" +
                "      Required\n"
        );
    }

    @Test
    public void testTreePrintWithEnAu() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        AUD
                    )
                ),
            "CurrencyComponent\n" +
                "  SuggestBoxComponent\n" +
                "    [English (Australia)] REQUIRED\n"
        );
    }

    @Override
    public void testAllMethodsVisibility() {
        throw new UnsupportedOperationException();
    }

    // ValueComponent...................................................................................................

    @Override
    public CurrencyComponent<Currency> createComponent() {
        return CurrencyComponent.empty(
            new FakeCurrencyComponentContext<>() {

                @Override
                public Optional<CurrencyComponentSuggestionsValue<Currency>> toValue(final Currency currency) {
                    return CONTEXT.currencyText(currency)
                        .map(t -> CurrencyComponentSuggestionsValue.with(
                            currency,
                            t,
                            currency
                        ));
                }

                @Override
                public void verifyOption(final CurrencyComponentSuggestionsValue<Currency> value,
                                         final SuggestBoxComponent<CurrencyComponentSuggestionsValue<Currency>> suggestBox) {
                    suggestBox.setVerifiedOption(value);
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<CurrencyComponent<Currency>> type() {
        return Cast.to(CurrencyComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
