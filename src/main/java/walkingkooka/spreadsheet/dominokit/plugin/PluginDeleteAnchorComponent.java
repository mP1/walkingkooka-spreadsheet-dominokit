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
import walkingkooka.plugin.PluginName;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentLike;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentLikeDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.PluginDeleteHistoryToken;
import walkingkooka.spreadsheet.dominokit.value.ValueHistoryTokenAnchorComponent;

import java.util.Optional;

/**
 * Creates a {@link PluginDeleteAnchorComponent}.
 */
public final class PluginDeleteAnchorComponent implements HtmlElementComponent<HTMLAnchorElement, PluginDeleteAnchorComponent>,
        AnchorComponentLikeDelegator<PluginDeleteAnchorComponent> {

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

    // AnchorComponentLikeDelegator......................................................................................

    @Override
    public AnchorComponentLike<?> anchorComponentLike() {
        return this.component;
    }

    // HtmlElementComponent.............................................................................................

    /**
     * Setter that updates the wrapped anchor with the {@link PluginName}.
     */
    public PluginDeleteAnchorComponent setValue(final Optional<PluginName> value) {
        this.component.setValue(value);

        return this;
    }

    // @VisibleForTesting
    final ValueHistoryTokenAnchorComponent<PluginName> component;
}
