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
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponentCellRenderer;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.SpreadsheetTextComponent;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfo;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;
import walkingkooka.text.CharSequences;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

final class JarEntryInfoListTableComponentSpreadsheetDataTableComponentCellRenderer implements SpreadsheetDataTableComponentCellRenderer<JarEntryInfo> {

    static JarEntryInfoListTableComponentSpreadsheetDataTableComponentCellRenderer with(final String id,
                                                                                        final JarEntryInfoListTableComponentContext context) {
        return new JarEntryInfoListTableComponentSpreadsheetDataTableComponentCellRenderer(
                CharSequences.failIfNullOrEmpty(id, "id"),
                Objects.requireNonNull(context, "context")
        );
    }

    private JarEntryInfoListTableComponentSpreadsheetDataTableComponentCellRenderer(final String id,
                                                                                    final JarEntryInfoListTableComponentContext context) {
        this.id = id;
        this.context = context;
    }

    @Override
    public HtmlElementComponent<?, ?> render(final int column,
                                             final JarEntryInfo info) {
        final HtmlElementComponent<?, ?> component;

        switch (column) {
            case 0: // filename
                component = this.name(info);
                break;
            case 1: // size + compressed
                component = this.sizeAndCompressed(info);
                break;
            case 2: // method
                component = this.method(info);
                break;
            case 3: // method
                component = this.crc(info);
                break;
            case 4: // create-date-time
                component = this.dateTime(
                        info.create()
                );
                break;
            case 5: // lastmod
                component = this.dateTime(
                        info.lastModified()
                );
                break;
            case 6: // links
                component = this.links(info);
                break;
            default:
                throw new IllegalArgumentException("Unknown column " + column);
        }


        return component;
    }

    private SpreadsheetTextComponent name(final JarEntryInfo info) {
        return this.text(
                info.name()
                        .value()
        );
    }

    // 123 (4560
    private SpreadsheetTextComponent sizeAndCompressed(final JarEntryInfo info) {
        final StringBuilder b = new StringBuilder();

        {
            final OptionalLong size = info.size();
            if (size.isPresent()) {
                b.append(size.getAsLong());
            }
        }

        {
            final OptionalLong compressed = info.compressedSize();
            if (compressed.isPresent()) {
                if (b.length() > 0) {
                    b.append(" (");
                }
                b.append(compressed.getAsLong());
                b.append(')');
            }
        }

        return this.text(
                Optional.ofNullable(
                        b.length() > 0 ?
                                b.toString() :
                                null
                )
        );
    }

    private SpreadsheetTextComponent method(final JarEntryInfo info) {
        final OptionalInt method = info.method();

        return this.text(
                Optional.ofNullable(
                        method.isPresent() ?
                                String.valueOf(method.getAsInt()) :
                                null
                )
        );
    }

    // 0x12345678
    private SpreadsheetTextComponent crc(final JarEntryInfo info) {
        final OptionalLong crc = info.crc();

        return this.text(
                Optional.ofNullable(
                        crc.isPresent() ?
                                Long.toHexString(crc.getAsLong()) :
                                null
                )
        );
    }


    private SpreadsheetTextComponent dateTime(final Optional<LocalDateTime> dateTime) {
        return text(
                dateTime.map(
                        this.context::formatDateTime
                )
        );
    }

    private SpreadsheetTextComponent text(final String text) {
        return this.text(
                Optional.of(text)
        );
    }

    // download, view
    // TODO need to include row number in id to make it really unique
    private SpreadsheetFlexLayout links(final JarEntryInfo info) {
        final JarEntryInfoListTableComponentContext context = this.context;

        final PluginName pluginName = context.pluginName();
        final JarEntryInfoName filename = info.name();

        final PluginDownloadAnchorComponent download = PluginDownloadAnchorComponent.empty()
                .setId(this.id + "download" + SpreadsheetElementIds.LINK)
                .setTextContent("Download")
                .setValue(
                        Optional.of(
                                PluginDownload.with(
                                        pluginName,
                                        Optional.of(filename)
                                )
                        )
                );

        final HistoryTokenAnchorComponent view = HistoryToken.pluginFileView(
                        pluginName,
                        Optional.of(filename)
                ).link(this.id + "view")
                .setTextContent("View");

        return SpreadsheetFlexLayout.row()
                .appendChild(download)
                .appendChild(view);
    }

    private SpreadsheetTextComponent text(final Optional<String> text) {
        return SpreadsheetTextComponent.with(text);
    }

    private final String id;

    private final JarEntryInfoListTableComponentContext context;
}
