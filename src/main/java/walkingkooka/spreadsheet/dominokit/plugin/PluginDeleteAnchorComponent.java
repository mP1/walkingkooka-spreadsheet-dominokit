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

import walkingkooka.plugin.PluginName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.PluginDeleteHistoryToken;
import walkingkooka.spreadsheet.dominokit.value.ValueHistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueHistoryTokenAnchorComponentDelegator;

import java.util.Optional;

/**
 * Creates a {@link PluginDeleteAnchorComponent}.
 */
public final class PluginDeleteAnchorComponent implements ValueHistoryTokenAnchorComponentDelegator<PluginDeleteAnchorComponent, PluginName> {

    public static PluginDeleteAnchorComponent empty(final String id) {
        return new PluginDeleteAnchorComponent()
            .setId(id);
    }

    private PluginDeleteAnchorComponent() {
        this.component = ValueHistoryTokenAnchorComponent.with(
            HistoryTokenAnchorComponent.empty(),
            this::getter,
            this::setter
        );
    }

    /**
     * Getter that returns the {@link PluginName}.
     */
    private Optional<PluginName> getter(final HistoryTokenAnchorComponent anchor) {
        return anchor.historyToken()
            .map(t -> t.cast(PluginDeleteHistoryToken.class).name());
    }

    private void setter(final Optional<PluginName> value,
                        final HistoryTokenAnchorComponent anchor) {
        anchor.setHistoryToken(
            value.map(v -> HistoryToken.pluginDelete(v))
        );
    }

    // ValueHistoryTokenAnchorComponentDelegator........................................................................

    @Override
    public ValueHistoryTokenAnchorComponent<PluginName> valueHistoryTokenAnchorComponent() {
        return this.component;
    }

    // @VisibleForTesting
    final ValueHistoryTokenAnchorComponent<PluginName> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
