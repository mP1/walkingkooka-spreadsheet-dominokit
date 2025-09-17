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
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterAliasSet;

import java.util.Optional;

public final class SpreadsheetImporterAliasSetComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetImporterAliasSet, SpreadsheetImporterAliasSetComponent> {

    @Test
    public void testParseAndText() {
        final SpreadsheetImporterAliasSet alias = SpreadsheetImporterAliasSet.parse("alias1 plugin1, plugin2");

        this.checkEquals(
            alias,
            SpreadsheetImporterAliasSet.parse(alias.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            SpreadsheetImporterAliasSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "alias1 importer1, importer2"
                    )
                ),
            "SpreadsheetImporterAliasSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [alias1 importer1, importer2]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            SpreadsheetImporterAliasSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "alias1 importer1, 9"
                    )
                ),
            "SpreadsheetImporterAliasSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [alias1 importer1, 9]\n" +
                "      Errors\n" +
                "        Invalid character '9' at 18\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetImporterAliasSetComponent createComponent() {
        return SpreadsheetImporterAliasSetComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetImporterAliasSetComponent> type() {
        return SpreadsheetImporterAliasSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
