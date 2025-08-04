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

package walkingkooka.spreadsheet.dominokit.plugin;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.plugin.PluginNameSet;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Optional;

public final class PluginNameSetComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, PluginNameSet, PluginNameSetComponent> {

    @Test
    public void testParseAndText() {
        final PluginNameSet alias = PluginNameSet.parse("plugin1, plugin2");

        this.checkEquals(
            alias,
            PluginNameSet.parse(alias.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            PluginNameSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "plugin1, plugin2"
                    )
                ),
            "PluginNameSetComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [plugin1, plugin2]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            PluginNameSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "plugin111, 9"
                    )
                ),
            "PluginNameSetComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [plugin111, 9]\n" +
                "      Errors\n" +
                "        Invalid character '9' at 11\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public PluginNameSetComponent createComponent() {
        return PluginNameSetComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<PluginNameSetComponent> type() {
        return PluginNameSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
