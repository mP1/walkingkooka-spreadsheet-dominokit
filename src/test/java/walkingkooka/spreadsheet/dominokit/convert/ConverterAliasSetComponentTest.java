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

package walkingkooka.spreadsheet.dominokit.convert;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.convert.provider.ConverterAliasSet;
import walkingkooka.convert.provider.ConverterProviders;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Optional;

public final class ConverterAliasSetComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, ConverterAliasSet, ConverterAliasSetComponent> {

    @Test
    public void testParseAndText() {
        final ConverterAliasSet aliases = ConverterProviders.converters()
            .converterInfos()
            .aliasSet();

        this.checkEquals(
            aliases,
            ConverterAliasSet.parse(aliases.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            ConverterAliasSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        ConverterProviders.converters()
                            .converterInfos()
                            .aliasSet()
                            .text()
                    )
                ),
            "ConverterAliasSetComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [boolean-to-number, chain, character-or-char-sequence-or-has-text-or-string-to-character-or-char-sequence-or-string, character-or-string-to-string, collection, custom-to-string, has-text-to-string, local-date-time-to-local-date, local-date-time-to-local-time, local-date-time-to-number, local-date-time-to-string, local-date-to-local-date-time, local-date-to-number, local-date-to-string, local-time-to-local-date-time, local-time-to-number, local-time-to-string, mapper, never, number-to-boolean, number-to-local-date, number-to-local-date-time, number-to-local-time, number-to-number, number-to-string, object, object-to-string, parser, simple, string-to-character-or-string, string-to-local-date, string-to-local-date-time, string-to-local-time, string-to-number, to-boolean]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            ConverterAliasSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "hello, !"
                    )
                ),
            "ConverterAliasSetComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [hello, !]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 7\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public ConverterAliasSetComponent createComponent() {
        return ConverterAliasSetComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<ConverterAliasSetComponent> type() {
        return ConverterAliasSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
