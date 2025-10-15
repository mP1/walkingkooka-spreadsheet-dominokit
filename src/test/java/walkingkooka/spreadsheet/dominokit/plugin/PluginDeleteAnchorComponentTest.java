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

import org.junit.jupiter.api.Test;
import walkingkooka.plugin.PluginName;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentTesting;

import java.util.Optional;

public final class PluginDeleteAnchorComponentTest implements AnchorComponentTesting<PluginDeleteAnchorComponent, PluginName> {

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            PluginDeleteAnchorComponent.empty("plugin-delete-anchor-id")
                .setTextContent("View123!"),
            "\"View123!\" DISABLED id=plugin-delete-anchor-id"
        );
    }

    @Test
    public void testSetValue() {
        final PluginName pluginName = PluginName.with("plugin-name-456");

        this.treePrintAndCheck(
            PluginDeleteAnchorComponent.empty("plugin-delete-anchor-id")
                .setTextContent("View456!")
                .setValue(
                    Optional.of(
                        pluginName
                    )
                ),
            "\"View456!\" [#/plugin/plugin-name-456/delete] id=plugin-delete-anchor-id"
        );
    }

    @Override
    public PluginDeleteAnchorComponent createComponent() {
        return PluginDeleteAnchorComponent.empty("plugin-delete-anchor-id");
    }

    // class............................................................................................................

    @Override
    public Class<PluginDeleteAnchorComponent> type() {
        return PluginDeleteAnchorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
