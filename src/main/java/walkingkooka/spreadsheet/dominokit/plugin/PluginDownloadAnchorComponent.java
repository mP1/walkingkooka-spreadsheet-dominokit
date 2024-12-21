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
import elemental2.dom.Node;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.UrlPathName;
import walkingkooka.plugin.PluginName;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentLike;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentLikeDelegate;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueHistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An anchor that uses given {@link PluginName} and {@link JarEntryInfoName} to set the HREF to download the plugin archive or a file within.
 */
public final class PluginDownloadAnchorComponent implements HtmlElementComponent<HTMLAnchorElement, PluginDownloadAnchorComponent>,
        AnchorComponentLikeDelegate<PluginDownloadAnchorComponent> {

    public static PluginDownloadAnchorComponent empty() {
        return new PluginDownloadAnchorComponent();
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

        PluginDownload pluginDownload = null;
        if (null != url) {
            // 1=api / 2=plugin / 3=download / 4=path
            final List<UrlPathName> names = url.path()
                    .namesList();

            pluginDownload = PluginDownload.with(
                    PluginName.with(
                            names.get(2)
                                    .value()
                    ),
                    Optional.ofNullable(
                            3 == names.size() ?
                                    null :
                                    JarEntryInfoName.with(
                                            names
                                                    .stream()
                                                    .skip(5)
                                                    .map(n -> n.value())
                                                    .collect(
                                                            Collectors.joining(
                                                                    JarEntryInfoName.SEPARATOR.string(),
                                                                    JarEntryInfoName.SEPARATOR.string(),
                                                                    ""
                                                            )
                                                    )
                                    )
                    )
            );
        }

        return Optional.ofNullable(pluginDownload);
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

    // AnchorComponentLikeDelegate......................................................................................

    @Override
    public AnchorComponentLike<?> anchorComponentLike() {
        return this.component;
    }

    // HtmlElementComponent.............................................................................................

    /**
     * Setter that updates the wrapped anchor with the new plugin name and filename.
     */
    public PluginDownloadAnchorComponent setValue(final Optional<PluginDownload> value) {
        this.component.setValue(value);

        return this;
    }

    @Override
    public PluginDownloadAnchorComponent setCssText(final String css) {
        this.component.setCssText(css);
        return this;
    }

    @Override
    public Node node() {
        return this.component.node();
    }

    @Override
    public HTMLAnchorElement element() {
        return this.component.element();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        this.component.printTree(printer);
    }

    private final ValueHistoryTokenAnchorComponent<PluginDownload> component;
}
