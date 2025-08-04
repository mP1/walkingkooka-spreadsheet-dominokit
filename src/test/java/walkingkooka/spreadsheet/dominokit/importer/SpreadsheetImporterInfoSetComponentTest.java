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

package walkingkooka.spreadsheet.dominokit.importer;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterInfoSet;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterProviders;

import java.util.Optional;

public final class SpreadsheetImporterInfoSetComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetImporterInfoSet, SpreadsheetImporterInfoSetComponent> {

    @Test
    public void testParseAndText() {
        final SpreadsheetImporterInfoSet infos = SpreadsheetImporterProviders.spreadsheetImport()
            .spreadsheetImporterInfos();

        this.checkEquals(
            infos,
            SpreadsheetImporterInfoSet.parse(infos.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            SpreadsheetImporterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        SpreadsheetImporterProviders.spreadsheetImport()
                            .spreadsheetImporterInfos()
                            .text()
                    )
                ),
            "SpreadsheetImporterInfoSetComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetImporter/collection collection,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetImporter/empty empty,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetImporter/json json]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            SpreadsheetImporterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://www.example.com/Hello !"
                    )
                ),
            "SpreadsheetImporterInfoSetComponent\n" +
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
            SpreadsheetImporterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://www.example.com/Hello Hello, bad://example.com"
                    )
                ),
            "SpreadsheetImporterInfoSetComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [https://www.example.com/Hello Hello, bad://example.com]\n" +
                "      Errors\n" +
                "        unknown protocol: bad\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidSecondSpreadsheetImporterName() {
        this.treePrintAndCheck(
            SpreadsheetImporterInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://www.example.com/1 Good, https://example.com/2 Bad!"
                    )
                ),
            "SpreadsheetImporterInfoSetComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [https://www.example.com/1 Good, https://example.com/2 Bad!]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 57\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetImporterInfoSetComponent createComponent() {
        return SpreadsheetImporterInfoSetComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetImporterInfoSetComponent> type() {
        return SpreadsheetImporterInfoSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
