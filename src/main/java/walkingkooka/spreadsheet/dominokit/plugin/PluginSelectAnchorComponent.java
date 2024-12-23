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
import walkingkooka.spreadsheet.dominokit.history.PluginSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.value.ValueHistoryTokenAnchorComponent;

import java.util.Optional;

/**
 * An anchor that updates the {@link HistoryToken} with a given {@link PluginName}.
 */
public final class PluginSelectAnchorComponent implements HtmlElementComponent<HTMLAnchorElement, PluginSelectAnchorComponent>,
        AnchorComponentLikeDelegator<PluginSelectAnchorComponent> {

    public static PluginSelectAnchorComponent empty() {
        return new PluginSelectAnchorComponent();
    }

    private PluginSelectAnchorComponent() {
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
                .map(t -> t.cast(PluginSelectHistoryToken.class).name());
    }

    private void setter(final Optional<PluginName> value,
                        final HistoryTokenAnchorComponent anchor) {
        anchor.setHistoryToken(
                value.map(v -> HistoryToken.pluginSelect(v))
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
    public PluginSelectAnchorComponent setValue(final Optional<PluginName> value) {
        this.component.setValue(value);

        return this;
    }

    private final ValueHistoryTokenAnchorComponent<PluginName> component;
}
