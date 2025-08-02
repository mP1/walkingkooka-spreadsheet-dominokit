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

package walkingkooka.spreadsheet.dominokit.locale;

import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.menu.MenuItem;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;
import walkingkooka.locale.FakeLocaleContext;
import walkingkooka.locale.LocaleContext;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.suggestbox.FakeSpreadsheetSuggestBoxComponentSuggestionsProvider;
import walkingkooka.spreadsheet.dominokit.suggestbox.SpreadsheetSuggestBoxComponent;
import walkingkooka.spreadsheet.dominokit.suggestbox.SpreadsheetSuggestBoxComponentSuggestionsProvider;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public final class SpreadsheetLocaleComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, Locale, SpreadsheetLocaleComponent<Locale>> {

    private final static Locale ENAU = Locale.forLanguageTag("en-AU");
    private final static Locale ENNZ = Locale.forLanguageTag("en-NZ");
    private final static Locale FR = Locale.FRENCH;

    private final static String ENGLISH_AUSTRALIA_TEXT = "English (Australia)";
    private final static String ENGLISH_NEW_ZEALAND_TEXT = "English (New Zealand)";
    private final static String FRENCH_TEXT = "French 123";

    private final static SpreadsheetSuggestBoxComponentSuggestionsProvider<SpreadsheetLocaleComponentSuggestionsValue<Locale>> SUGGESTIONS_PROVIDER = new FakeSpreadsheetSuggestBoxComponentSuggestionsProvider<>();

    private final static Function<SpreadsheetLocaleComponentSuggestionsValue<Locale>, MenuItem<SpreadsheetLocaleComponentSuggestionsValue<Locale>>> OPTION_MENU_ITEM_CREATOR = (v) -> {
        throw new UnsupportedOperationException(); // never actually called within a test
    };

    private final static LocaleContext CONTEXT = new FakeLocaleContext() {
        @Override
        public Set<Locale> findByLocaleText(final String text,
                                            final int offset,
                                            final int count) {
            if (LocaleContexts.CASE_SENSITIVITY.equals(text, ENGLISH_AUSTRALIA_TEXT)) {
                return Sets.of(ENAU);
            }
            if (LocaleContexts.CASE_SENSITIVITY.equals(text, ENGLISH_NEW_ZEALAND_TEXT)) {
                return Sets.of(ENNZ);
            }
            if (LocaleContexts.CASE_SENSITIVITY.equals(text, FRENCH_TEXT)) {
                return Sets.of(FR);
            }

            return Sets.empty();
        }

        @Override
        public Optional<String> localeText(final Locale locale) {
            return Optional.ofNullable(
                ENAU.equals(locale) ?
                    ENGLISH_AUSTRALIA_TEXT :
                    ENNZ.equals(locale) ?
                        ENGLISH_NEW_ZEALAND_TEXT :
                        FR.equals(locale) ?
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
            "SpreadsheetLocaleComponent\n" +
                "  SpreadsheetSuggestBoxComponent\n" +
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
                        ENAU
                    )
                ),
            "SpreadsheetLocaleComponent\n" +
                "  SpreadsheetSuggestBoxComponent\n" +
                "    [English (Australia)] REQUIRED\n"
        );
    }

    @Override
    public void testAllMethodsVisibility() {
        throw new UnsupportedOperationException();
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetLocaleComponent<Locale> createComponent() {
        return SpreadsheetLocaleComponent.empty(
            new FakeSpreadsheetLocaleComponentContext<>() {

                @Override
                public Optional<SpreadsheetLocaleComponentSuggestionsValue<Locale>> toValue(final Locale locale) {
                    return CONTEXT.localeText(locale)
                        .map(t -> SpreadsheetLocaleComponentSuggestionsValue.with(
                            locale,
                            t,
                            locale
                        ));
                }

                @Override
                public void verifyOption(final SpreadsheetLocaleComponentSuggestionsValue<Locale> value,
                                         final SpreadsheetSuggestBoxComponent<SpreadsheetLocaleComponentSuggestionsValue<Locale>> suggestBox) {
                    suggestBox.setVerifiedOption(value);
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetLocaleComponent<Locale>> type() {
        return Cast.to(SpreadsheetLocaleComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
