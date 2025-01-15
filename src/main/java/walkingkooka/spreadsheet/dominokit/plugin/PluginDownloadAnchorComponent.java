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

import walkingkooka.net.RelativeUrl;
import walkingkooka.plugin.PluginName;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponent;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueHistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.util.Optional;

/**
 * An anchor that uses given {@link PluginName} and {@link JarEntryInfoName} to set the HREF to download the plugin archive or a file within.
 */
public final class PluginDownloadAnchorComponent implements AnchorComponentDelegator<PluginDownloadAnchorComponent, PluginDownload> {

    public static PluginDownloadAnchorComponent empty(final String id) {
        return new PluginDownloadAnchorComponent()
            .setId(id);
    }

    private PluginDownloadAnchorComponent() {
        this.component = ValueHistoryTokenAnchorComponent.with(
            HistoryTokenAnchorComponent.empty(),
            this::getter,
            this::setter
        );
    }

    /**
     * Getter that extracts the plugin name and filename from the {@link HistoryTokenAnchorComponent#href()}.
     */
    private Optional<PluginDownload> getter(final HistoryTokenAnchorComponent anchor) {
        final RelativeUrl url = (RelativeUrl) anchor.href();

        return Optional.ofNullable(
            null != url ?
                null :
                PluginDownload.extract(
                    url.path()
                )
        );
    }

    private void setter(final Optional<PluginDownload> value,
                        final HistoryTokenAnchorComponent anchor) {
        anchor.setHref(
            value.map(
                pd -> PluginFetcher.pluginDownloadUrl(
                    pd.pluginName(),
                    pd.filename()
                )
            ).orElse(null)
        );
    }

    @Override
    public PluginDownloadAnchorComponent setValue(final Optional<PluginDownload> value) {
        this.component.setValue(value);
        return this;
    }

    @Override
    public Optional<PluginDownload> value() {
        return this.component.value();
    }

    // AnchorComponentDelegator......................................................................................

    @Override
    public AnchorComponent<?, PluginDownload> anchorComponent() {
        return this.component;
    }

    private final ValueHistoryTokenAnchorComponent<PluginDownload> component;
}
