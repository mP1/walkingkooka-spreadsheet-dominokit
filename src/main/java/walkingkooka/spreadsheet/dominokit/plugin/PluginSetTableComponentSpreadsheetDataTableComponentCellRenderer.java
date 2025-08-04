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
import walkingkooka.plugin.store.Plugin;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponentCellRenderer;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.text.TextComponent;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Displays a table with columns from the {@link Plugin}.
 */
final class PluginSetTableComponentSpreadsheetDataTableComponentCellRenderer implements SpreadsheetDataTableComponentCellRenderer<Plugin> {

    static PluginSetTableComponentSpreadsheetDataTableComponentCellRenderer with(final String id,
                                                                                 final PluginSetTableComponentContext context) {
        return new PluginSetTableComponentSpreadsheetDataTableComponentCellRenderer(
            CharSequences.failIfNullOrEmpty(id, "id"),
            Objects.requireNonNull(context, "context")
        );
    }

    private PluginSetTableComponentSpreadsheetDataTableComponentCellRenderer(final String id,
                                                                             final PluginSetTableComponentContext context) {
        this.id = id;
        this.context = context;
    }

    // plugin-name | filename | user | timestamp | links
    @Override
    public HtmlComponent<?, ?> render(final int column,
                                      final Plugin plugin) {
        final HtmlComponent<?, ?> component;

        switch (column) {
            case 0: // pluginName
                component = this.pluginName(plugin);
                break;
            case 1: // filename
                component = this.filename(plugin);
                break;
            case 2: // user
                component = this.user(plugin);
                break;

            case 3: // timestamp
                component = this.timestamp(plugin);
                break;
            case 4: // links
                component = this.links(plugin);
                break;
            default:
                throw new IllegalArgumentException("Unknown column " + column);
        }


        return component;
    }

    private TextComponent pluginName(final Plugin plugin) {
        return this.text(
            plugin.name()
                .value()
        );
    }

    private TextComponent filename(final Plugin plugin) {
        return this.text(
            plugin.filename()
        );
    }

    private TextComponent user(final Plugin plugin) {
        return this.text(
            plugin.user()
                .value()
        );
    }

    private TextComponent timestamp(final Plugin plugin) {
        return text(
            this.context.formatDateTime(
                plugin.timestamp()
            )
        );
    }

    private TextComponent text(final String text) {
        return this.text(
            Optional.of(text)
        );
    }

    // download, view, delete
    private FlexLayoutComponent links(final Plugin plugin) {
        final PluginName pluginName = plugin.name();

        final PluginDeleteAnchorComponent delete = PluginDeleteAnchorComponent.empty(this.id + "delete" + SpreadsheetElementIds.LINK)
            .setTextContent("Delete")
            .setValue(
                Optional.of(pluginName)
            );

        final PluginDownloadAnchorComponent download = PluginDownloadAnchorComponent.empty(this.id + "download" + SpreadsheetElementIds.LINK)
            .setTextContent("Download")
            .setValue(
                Optional.of(
                    PluginDownload.with(
                        pluginName,
                        Optional.<JarEntryInfoName>empty()
                    )
                )
            );

        final PluginSelectAnchorComponent view = PluginSelectAnchorComponent.empty(
                this.id + "view" + SpreadsheetElementIds.LINK
            ).setTextContent("View")
            .setValue(
                Optional.of(pluginName)
            );

        return FlexLayoutComponent.row()
            .appendChild(delete)
            .appendChild(download)
            .appendChild(view);
    }

    private TextComponent text(final Optional<String> text) {
        return TextComponent.with(text);
    }

    private final String id;

    private final PluginSetTableComponentContext context;
}
