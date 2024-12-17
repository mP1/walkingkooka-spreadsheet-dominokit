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
import walkingkooka.spreadsheet.dominokit.AppContext;

import java.util.Objects;

/**
 * Represents the selection of a single {@link walkingkooka.plugin.store.Plugin}.
 */
public final class PluginSelectHistoryToken extends PluginNameHistoryToken {

    static PluginSelectHistoryToken with(final PluginName name) {
        return new PluginSelectHistoryToken(
                Objects.requireNonNull(name, "name")
        );
    }

    private PluginSelectHistoryToken(final PluginName name) {
        super(name);
    }

    // HistoryToken.....................................................................................................

    //
    // /plugin/view/PluginName123
    //
    @Override
    UrlFragment pluginNameUrlFragment() {
        return UrlFragment.EMPTY;
    }

    @Override
    HistoryToken save0(final String value) {
        return this; // not a SAVE HistoryToken return this
    }

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        // NOP
    }
}
