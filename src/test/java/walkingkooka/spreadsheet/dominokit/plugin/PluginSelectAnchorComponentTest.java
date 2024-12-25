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

import elemental2.dom.HTMLAnchorElement;
import org.junit.jupiter.api.Test;
import walkingkooka.plugin.PluginName;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponentTesting;

import java.util.Optional;

public final class PluginSelectAnchorComponentTest implements HtmlElementComponentTesting<PluginSelectAnchorComponent, HTMLAnchorElement> {

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
                PluginSelectAnchorComponent.empty()
                        .setId("plugin-select-anchor-id")
                        .setTextContent("View123!"),
                "\"View123!\" DISABLED id=plugin-select-anchor-id"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
                PluginSelectAnchorComponent.empty()
                        .setId("plugin-select-anchor-id")
                        .setTextContent("View456!")
                        .setValue(
                                Optional.of(
                                        PluginName.with("PluginName456")
                                )
                        ),
                "\"View456!\" [#/plugin/PluginName456] id=plugin-select-anchor-id"
        );
    }

    // class............................................................................................................

    @Override
    public Class<PluginSelectAnchorComponent> type() {
        return PluginSelectAnchorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
