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
import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.locale.FakeLocaleContext;
import walkingkooka.locale.LocaleContext;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.server.locale.LocaleHateosResource;
import walkingkooka.spreadsheet.server.locale.LocaleTag;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public final class SpreadsheetLocaleComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, Locale, SpreadsheetLocaleComponent> {

    private final static Locale ENAU = Locale.forLanguageTag("en-AU");
    private final static Locale ENNZ = Locale.forLanguageTag("en-NZ");
    private final static Locale FR = Locale.FRENCH;

    private final static String ENGLISH_AUSTRALIA_TEXT = "English (Australia)";
    private final static String ENGLISH_NEW_ZEALAND_TEXT = "English (New Zealand)";
    private final static String FRENCH_TEXT = "French 123";

    private final static LocaleContext CONTEXT = new FakeLocaleContext() {

        @Override
        public Set<Locale> availableLocales() {
            return Sets.of(
                ENAU,
                ENNZ,
                FR
            );
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

    // filter...........................................................................................................

    @Test
    public void testFilterMatchesNone() {
        this.filterAndCheck(
            "Z",
            CONTEXT
        );
    }

    @Test
    public void testFilterMatchesSome() {
        this.filterAndCheck(
            "English",
            CONTEXT,
            LocaleHateosResource.with(
                LocaleTag.with(ENAU),
                ENGLISH_AUSTRALIA_TEXT
            ),
            LocaleHateosResource.with(
                LocaleTag.with(ENNZ),
                ENGLISH_NEW_ZEALAND_TEXT
            )
        );
    }

    @Test
    public void testFilterMatchesSome2() {
        this.filterAndCheck(
            FRENCH_TEXT,
            CONTEXT,
            LocaleHateosResource.with(
                LocaleTag.with(FR),
                FRENCH_TEXT
            )
        );
    }

    private void filterAndCheck(final String startsWith,
                                final LocaleContext context,
                                final LocaleHateosResource...expected) {
        this.filterAndCheck(
            startsWith,
            context,
            Sets.of(expected)
        );
    }

    private void filterAndCheck(final String startsWith,
                                final LocaleContext context,
                                final Set<LocaleHateosResource> expected) {
        this.checkEquals(
            expected,
            SpreadsheetLocaleComponent.filter(
                startsWith,
                context
            )
        );
    }

    // spreadsheetLocaleComponentValue..................................................................................

    @Test
    public void testSpreadsheetLocaleComponentValue() {
        this.spreadsheetLocaleComponentValueAndCheck(
            ENGLISH_AUSTRALIA_TEXT,
            CONTEXT,
            SpreadsheetLocaleComponentValue.with(
                ENAU,
                ENGLISH_AUSTRALIA_TEXT
            )
        );
    }

    @Test
    public void testSpreadsheetLocaleComponentValueDifferentCase() {
        final String text = ENGLISH_NEW_ZEALAND_TEXT.toLowerCase();

        this.checkNotEquals(
            text,
            ENGLISH_NEW_ZEALAND_TEXT
        );

        this.spreadsheetLocaleComponentValueAndCheck(
            text,
            CONTEXT,
            SpreadsheetLocaleComponentValue.with(
                ENNZ,
                ENGLISH_NEW_ZEALAND_TEXT
            )
        );
    }

    private void spreadsheetLocaleComponentValueAndCheck(final String localeText,
                                                         final LocaleContext context,
                                                         final SpreadsheetLocaleComponentValue expected) {
        this.checkEquals(
            expected,
            SpreadsheetLocaleComponent.spreadsheetLocaleComponentValue(
                localeText,
                context
            )
        );
    }

    // TreePrint........................................................................................................

    @Test
    public void testTreePrintWithoutValue() {
        this.treePrintAndCheck(
            SpreadsheetLocaleComponent.empty(CONTEXT),
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
            SpreadsheetLocaleComponent.empty(CONTEXT)
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
    public SpreadsheetLocaleComponent createComponent() {
        return SpreadsheetLocaleComponent.empty(CONTEXT);
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetLocaleComponent> type() {
        return SpreadsheetLocaleComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
