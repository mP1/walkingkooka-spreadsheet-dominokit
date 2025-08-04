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

package walkingkooka.spreadsheet.dominokit.decimalnumbersymbols;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Optional;

public final class DecimalNumberSymbolsComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, DecimalNumberSymbols, DecimalNumberSymbolsComponent> {

    @Test
    public void testParseAndText() {
        final DecimalNumberSymbols symbols = DecimalNumberSymbols.fromDecimalFormatSymbols(
            '+',
            new DecimalFormatSymbols(Locale.ENGLISH)
        );

        this.checkEquals(
            symbols,
            DecimalNumberSymbols.parse(symbols.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            DecimalNumberSymbolsComponent.empty()
                .setStringValue(
                    Optional.of(
                        DecimalNumberSymbols.fromDecimalFormatSymbols(
                            '+',
                            new DecimalFormatSymbols(Locale.ENGLISH)
                        ).text()
                    )
                ),
            "DecimalNumberSymbolsComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [-,+,0,¤,.,E,\",\",∞,.,NaN,%,‰]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            DecimalNumberSymbolsComponent.empty()
                .setStringValue(
                    Optional.of(
                        "hello, !"
                    )
                ),
            "DecimalNumberSymbolsComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [hello, !]\n" +
                "      Errors\n" +
                "        Expected 12 tokens but got 2\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public DecimalNumberSymbolsComponent createComponent() {
        return DecimalNumberSymbolsComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<DecimalNumberSymbolsComponent> type() {
        return DecimalNumberSymbolsComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
