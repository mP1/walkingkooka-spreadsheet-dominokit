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
import walkingkooka.convert.provider.ConverterInfoSet;
import walkingkooka.convert.provider.ConverterProviders;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Optional;

public final class ConverterInfoSetComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, ConverterInfoSet, ConverterInfoSetComponent> {

    @Test
    public void testParseAndText() {
        final ConverterInfoSet infos = ConverterProviders.converters()
            .converterInfos();

        this.checkEquals(
            infos,
            ConverterInfoSet.parse(infos.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            ConverterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        ConverterProviders.converters()
                            .converterInfos()
                            .text()
                    )
                ),
            "ConverterInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://github.com/mP1/walkingkooka-convert-provider/Converter/boolean-to-number boolean-to-number,https://github.com/mP1/walkingkooka-convert-provider/Converter/chain chain,https://github.com/mP1/walkingkooka-convert-provider/Converter/character-or-char-sequence-or-has-text-or-string-to-character-or-char-sequence-or-string character-or-char-sequence-or-has-text-or-string-to-character-or-char-sequence-or-string,https://github.com/mP1/walkingkooka-convert-provider/Converter/character-or-string-to-string character-or-string-to-string,https://github.com/mP1/walkingkooka-convert-provider/Converter/collection collection,https://github.com/mP1/walkingkooka-convert-provider/Converter/collection-to collection-to,https://github.com/mP1/walkingkooka-convert-provider/Converter/collection-to-list collection-to-list,https://github.com/mP1/walkingkooka-convert-provider/Converter/custom-to-string custom-to-string,https://github.com/mP1/walkingkooka-convert-provider/Converter/has-text has-text,https://github.com/mP1/walkingkooka-convert-provider/Converter/local-date-time-to-local-date local-date-time-to-local-date,https://github.com/mP1/walkingkooka-convert-provider/Converter/local-date-time-to-local-time local-date-time-to-local-time,https://github.com/mP1/walkingkooka-convert-provider/Converter/local-date-time-to-number local-date-time-to-number,https://github.com/mP1/walkingkooka-convert-provider/Converter/local-date-time-to-string local-date-time-to-string,https://github.com/mP1/walkingkooka-convert-provider/Converter/local-date-to-local-date-time local-date-to-local-date-time,https://github.com/mP1/walkingkooka-convert-provider/Converter/local-date-to-number local-date-to-number,https://github.com/mP1/walkingkooka-convert-provider/Converter/local-date-to-string local-date-to-string,https://github.com/mP1/walkingkooka-convert-provider/Converter/local-time-to-local-date-time local-time-to-local-date-time,https://github.com/mP1/walkingkooka-convert-provider/Converter/local-time-to-number local-time-to-number,https://github.com/mP1/walkingkooka-convert-provider/Converter/local-time-to-string local-time-to-string,https://github.com/mP1/walkingkooka-convert-provider/Converter/mapper mapper,https://github.com/mP1/walkingkooka-convert-provider/Converter/never never,https://github.com/mP1/walkingkooka-convert-provider/Converter/number-to-boolean number-to-boolean,https://github.com/mP1/walkingkooka-convert-provider/Converter/number-to-local-date number-to-local-date,https://github.com/mP1/walkingkooka-convert-provider/Converter/number-to-local-date-time number-to-local-date-time,https://github.com/mP1/walkingkooka-convert-provider/Converter/number-to-local-time number-to-local-time,https://github.com/mP1/walkingkooka-convert-provider/Converter/number-to-number number-to-number,https://github.com/mP1/walkingkooka-convert-provider/Converter/number-to-string number-to-string,https://github.com/mP1/walkingkooka-convert-provider/Converter/object object,https://github.com/mP1/walkingkooka-convert-provider/Converter/object-to-string object-to-string,https://github.com/mP1/walkingkooka-convert-provider/Converter/optional-to optional-to,https://github.com/mP1/walkingkooka-convert-provider/Converter/parser parser,https://github.com/mP1/walkingkooka-convert-provider/Converter/simple simple,https://github.com/mP1/walkingkooka-convert-provider/Converter/string-to-character-or-string string-to-character-or-string,https://github.com/mP1/walkingkooka-convert-provider/Converter/string-to-local-date string-to-local-date,https://github.com/mP1/walkingkooka-convert-provider/Converter/string-to-local-date-time string-to-local-date-time,https://github.com/mP1/walkingkooka-convert-provider/Converter/string-to-local-time string-to-local-time,https://github.com/mP1/walkingkooka-convert-provider/Converter/string-to-number string-to-number,https://github.com/mP1/walkingkooka-convert-provider/Converter/text-to-boolean-list text-to-boolean-list,https://github.com/mP1/walkingkooka-convert-provider/Converter/text-to-csv-string-list text-to-csv-string-list,https://github.com/mP1/walkingkooka-convert-provider/Converter/text-to-local-date-list text-to-local-date-list,https://github.com/mP1/walkingkooka-convert-provider/Converter/text-to-local-date-time-list text-to-local-date-time-list,https://github.com/mP1/walkingkooka-convert-provider/Converter/text-to-local-time-list text-to-local-time-list,https://github.com/mP1/walkingkooka-convert-provider/Converter/text-to-number-list text-to-number-list,https://github.com/mP1/walkingkooka-convert-provider/Converter/text-to-string-list text-to-string-list,https://github.com/mP1/walkingkooka-convert-provider/Converter/to-boolean to-boolean]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            ConverterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "!"
                    )
                ),
            "ConverterInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [!]\n" +
                "      Errors\n" +
                "        no protocol: !\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid2() {
        this.treePrintAndCheck(
            ConverterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://example.com hello more more more"
                    )
                ),
            "ConverterInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://example.com hello more more more]\n" +
                "      Errors\n" +
                "        Invalid character 'm' at 26\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidSecondInfo() {
        this.treePrintAndCheck(
            ConverterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://example.com hello, https://example.com/2 !ad"
                    )
                ),
            "ConverterInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://example.com hello, https://example.com/2 !ad]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 49\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public ConverterInfoSetComponent createComponent() {
        return ConverterInfoSetComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<ConverterInfoSetComponent> type() {
        return ConverterInfoSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
