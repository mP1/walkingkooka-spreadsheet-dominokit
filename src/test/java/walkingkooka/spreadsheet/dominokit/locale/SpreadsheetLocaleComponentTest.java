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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetLocaleComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, Locale, SpreadsheetLocaleComponent> {

    private final static Locale ENAU = Locale.forLanguageTag("en-AU");
    private final static Locale ENNZ = Locale.forLanguageTag("en-NZ");

    @Test
    public void testTreePrintWithoutValue() {
        this.treePrintAndCheck(
            SpreadsheetLocaleComponent.empty()
                .addLocale("English (Australia)", ENAU)
                .addLocale("English (New Zealand)", ENNZ),
            "SpreadsheetLocaleComponent\n" +
                "  SpreadsheetSelectComponent\n" +
                "    []\n" +
                "      English (Australia)=en_AU\n" +
                "      English (New Zealand)=en_NZ\n"
        );
    }

    @Test
    public void testTreePrintWithEnAu() {
        this.treePrintAndCheck(
            SpreadsheetLocaleComponent.empty()
                .addLocale("English (Australia)", ENAU)
                .addLocale("English (New Zealand)", ENNZ)
                .setValue(
                    Optional.of(
                        ENAU
                    )
                ),
            "SpreadsheetLocaleComponent\n" +
                "  SpreadsheetSelectComponent\n" +
                "    [en_AU]\n" +
                "      English (Australia)=en_AU\n" +
                "      English (New Zealand)=en_NZ\n"
        );
    }

    @Override
    public void testAllMethodsVisibility() {
        throw new UnsupportedOperationException();
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetLocaleComponent createComponent() {
        return SpreadsheetLocaleComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetLocaleComponent> type() {
        return SpreadsheetLocaleComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
