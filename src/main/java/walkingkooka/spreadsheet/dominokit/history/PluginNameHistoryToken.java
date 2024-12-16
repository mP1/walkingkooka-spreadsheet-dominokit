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

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.plugin.PluginName;

import java.util.Objects;

/**
 * Instances represent a token within a history hash including the {@link PluginName}
 */
public abstract class PluginNameHistoryToken extends PluginHistoryToken {

    PluginNameHistoryToken(final PluginName name) {
        super();

        this.name = Objects.requireNonNull(name, "name");
    }

    public final PluginName name() {
        return this.name;
    }

    private final PluginName name;

    /**
     * Creates a save {@link HistoryToken} after attempting to parse the value into a {@link PluginName}.
     */
    abstract HistoryToken save0(final String value);

    // UrlFragment......................................................................................................

    @Override
    final UrlFragment pluginUrlFragment() {
        return UrlFragment.with(name.value())
                .appendSlashThen(
                        this.pluginNameUrlFragment()
                );
    }

    // /plugin/PluginName/
    abstract UrlFragment pluginNameUrlFragment();
}