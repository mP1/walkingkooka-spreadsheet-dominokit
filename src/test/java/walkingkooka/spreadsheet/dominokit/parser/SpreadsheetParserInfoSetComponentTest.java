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

package walkingkooka.spreadsheet.dominokit.parser;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserInfoSet;

import java.util.Optional;

public final class SpreadsheetParserInfoSetComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetParserInfoSet, SpreadsheetParserInfoSetComponent>,
    SpreadsheetMetadataTesting {

    @Test
    public void testParseAndText() {
        final SpreadsheetParserInfoSet infos = SPREADSHEET_PARSER_PROVIDER.spreadsheetParserInfos();

        this.checkEquals(
            infos,
            SpreadsheetParserInfoSet.parse(infos.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            SpreadsheetParserInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        SPREADSHEET_PARSER_PROVIDER.spreadsheetParserInfos()
                            .text()
                    )
                ),
            "SpreadsheetParserInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetParser/date date,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetParser/date-time date-time,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetParser/general general,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetParser/number number,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetParser/time time,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetParser/whole-number whole-number]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            SpreadsheetParserInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://www.example.com/Hello !"
                    )
                ),
            "SpreadsheetParserInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://www.example.com/Hello !]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 30\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidSecondUrl() {
        this.treePrintAndCheck(
            SpreadsheetParserInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://www.example.com/hello hello, bad://example.com/2"
                    )
                ),
            "SpreadsheetParserInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://www.example.com/hello hello, bad://example.com/2]\n" +
                "      Errors\n" +
                "        unknown protocol: bad\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidSecondSpreadsheetParserName() {
        this.treePrintAndCheck(
            SpreadsheetParserInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://www.example.com/1 good, https://example.com/2 bad!"
                    )
                ),
            "SpreadsheetParserInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://www.example.com/1 good, https://example.com/2 bad!]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 57\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetParserInfoSetComponent createComponent() {
        return SpreadsheetParserInfoSetComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetParserInfoSetComponent> type() {
        return SpreadsheetParserInfoSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
