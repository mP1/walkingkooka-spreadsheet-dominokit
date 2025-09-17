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

package walkingkooka.spreadsheet.dominokit.export;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterInfoSet;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterProviders;

import java.util.Optional;

public final class SpreadsheetExporterInfoSetComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetExporterInfoSet, SpreadsheetExporterInfoSetComponent> {

    @Test
    public void testParseAndText() {
        final SpreadsheetExporterInfoSet infos = SpreadsheetExporterProviders.spreadsheetExport()
            .spreadsheetExporterInfos();

        this.checkEquals(
            infos,
            SpreadsheetExporterInfoSet.parse(infos.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            SpreadsheetExporterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        SpreadsheetExporterProviders.spreadsheetExport()
                            .spreadsheetExporterInfos()
                            .text()
                    )
                ),
            "SpreadsheetExporterInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetExporter/collection collection,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetExporter/empty empty,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetExporter/json json]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            SpreadsheetExporterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://www.example.com/Hello !"
                    )
                ),
            "SpreadsheetExporterInfoSetComponent\n" +
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
            SpreadsheetExporterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://www.example.com/1 Hello, bad:// Hello2"
                    )
                ),
            "SpreadsheetExporterInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://www.example.com/1 Hello, bad:// Hello2]\n" +
                "      Errors\n" +
                "        unknown protocol: bad\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidSecondSpreadsheetExporterName() {
        this.treePrintAndCheck(
            SpreadsheetExporterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://www.example.com/1 Hello, https://example.com/2 Bad!"
                    )
                ),
            "SpreadsheetExporterInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://www.example.com/1 Hello, https://example.com/2 Bad!]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 58\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetExporterInfoSetComponent createComponent() {
        return SpreadsheetExporterInfoSetComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExporterInfoSetComponent> type() {
        return SpreadsheetExporterInfoSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
