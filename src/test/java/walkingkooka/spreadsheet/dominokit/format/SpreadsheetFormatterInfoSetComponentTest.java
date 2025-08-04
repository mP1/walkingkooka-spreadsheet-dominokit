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

package walkingkooka.spreadsheet.dominokit.format;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterProviders;

import java.util.Optional;

public final class SpreadsheetFormatterInfoSetComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetFormatterInfoSet, SpreadsheetFormatterInfoSetComponent> {

    @Test
    public void testParseAndText() {
        final SpreadsheetFormatterInfoSet infos = SpreadsheetFormatterProviders.spreadsheetFormatters()
            .spreadsheetFormatterInfos();

        this.checkEquals(
            infos,
            SpreadsheetFormatterInfoSet.parse(infos.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            SpreadsheetFormatterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        SpreadsheetFormatterProviders.spreadsheetFormatters()
                            .spreadsheetFormatterInfos()
                            .text()
                    )
                ),
            "SpreadsheetFormatterInfoSetComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetFormatter/automatic automatic,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetFormatter/collection collection,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetFormatter/date-format-pattern date-format-pattern,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetFormatter/date-time-format-pattern date-time-format-pattern,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetFormatter/default-text default-text,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetFormatter/expression expression,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetFormatter/general general,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetFormatter/number-format-pattern number-format-pattern,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetFormatter/spreadsheet-pattern-collection spreadsheet-pattern-collection,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetFormatter/text-format-pattern text-format-pattern,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetFormatter/time-format-pattern time-format-pattern]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            SpreadsheetFormatterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://www.example.com/Hello !"
                    )
                ),
            "SpreadsheetFormatterInfoSetComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [https://www.example.com/Hello !]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 30\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidSecondUrl() {
        this.treePrintAndCheck(
            SpreadsheetFormatterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://www.example.com/Hello Hello, bad://www.example.com Hello2"
                    )
                ),
            "SpreadsheetFormatterInfoSetComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [https://www.example.com/Hello Hello, bad://www.example.com Hello2]\n" +
                "      Errors\n" +
                "        unknown protocol: bad\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidSecondSpreadsheetFormatterName() {
        this.treePrintAndCheck(
            SpreadsheetFormatterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://www.example.com/Hello Good, https://www.example.com Bad!"
                    )
                ),
            "SpreadsheetFormatterInfoSetComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [https://www.example.com/Hello Good, https://www.example.com Bad!]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 63\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetFormatterInfoSetComponent createComponent() {
        return SpreadsheetFormatterInfoSetComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFormatterInfoSetComponent> type() {
        return SpreadsheetFormatterInfoSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
