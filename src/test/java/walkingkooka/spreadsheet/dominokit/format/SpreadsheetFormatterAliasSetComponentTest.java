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
import walkingkooka.spreadsheet.format.SpreadsheetFormatterAliasSet;

import java.util.Optional;

public final class SpreadsheetFormatterAliasSetComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetFormatterAliasSet, SpreadsheetFormatterAliasSetComponent> {

    @Test
    public void testParseAndText() {
        final SpreadsheetFormatterAliasSet alias = SpreadsheetFormatterAliasSet.parse("alias1 plugin1, plugin2");

        this.checkEquals(
            alias,
            SpreadsheetFormatterAliasSet.parse(alias.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            SpreadsheetFormatterAliasSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "alias1 formatter1, formatter2"
                    )
                ),
            "SpreadsheetFormatterAliasSetComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [alias1 formatter1, formatter2]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            SpreadsheetFormatterAliasSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "alias1 formatter1, 9"
                    )
                ),
            "SpreadsheetFormatterAliasSetComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [alias1 formatter1, 9]\n" +
                "      Errors\n" +
                "        Invalid character '9' at 19\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetFormatterAliasSetComponent createComponent() {
        return SpreadsheetFormatterAliasSetComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFormatterAliasSetComponent> type() {
        return SpreadsheetFormatterAliasSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
