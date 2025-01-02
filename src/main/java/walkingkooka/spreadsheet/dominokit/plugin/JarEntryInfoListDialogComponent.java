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
import walkingkooka.plugin.store.PluginSet;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.PluginSelectHistoryToken;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoList;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.util.Objects;
import java.util.Optional;

/**
 * A dialog that includes a table showing all the entries for a JAR file, along with delete, download and close links.
 */
public final class JarEntryInfoListDialogComponent implements SpreadsheetDialogComponentLifecycle,
        PluginFetcherWatcher,
        NopFetcherWatcher,
        NopEmptyResponseFetcherWatcher {

    public static JarEntryInfoListDialogComponent with(final JarEntryInfoListDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new JarEntryInfoListDialogComponent(context);
    }

    private JarEntryInfoListDialogComponent(final JarEntryInfoListDialogComponentContext context) {
        this.context = context;

        context.addHistoryTokenWatcher(this);
        context.addPluginFetcherWatcher(this);

        this.table = this.table();

        this.delete = PluginDeleteAnchorComponent.empty(
                        ID_PREFIX + "delete" + SpreadsheetElementIds.LINK
                ).setTextContent("Delete");
        this.download = PluginDownloadAnchorComponent.empty()
                .setTextContent("Download")
                .setId(ID_PREFIX + "download" + SpreadsheetElementIds.LINK);
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate(context);
    }

    // table............................................................................................................

    private JarEntryInfoListTableComponent table() {
        return JarEntryInfoListTableComponent.empty(
                ID_PREFIX,
                this.context
        );
    }

    private final JarEntryInfoListTableComponent table;

    // close............................................................................................................

    private void refreshClose(final HistoryTokenContext context) {
        this.close.setHistoryToken(
                Optional.of(
                        context.historyToken().close()
                )
        );
    }

    private final HistoryTokenAnchorComponent close;

    // delete.........................................................................................................

    private void refreshDelete() {
        this.delete.setValue(
                Optional.ofNullable(this.pluginName)
        );
    }

    private final PluginDeleteAnchorComponent delete;

    // download.........................................................................................................

    private void refreshDownload() {
        final PluginName pluginName = this.pluginName;
        this.download.setValue(
                Optional.ofNullable(
                        null == pluginName ?
                                null :
                                PluginDownload.with(
                                        pluginName,
                                        Optional.empty() // no file, want archive
                                )
                )
        );
    }

    private final PluginDownloadAnchorComponent download;

    // SpreadsheetDialogComponentLifecycle..............................................................................

    // TODO add DELETE, RENAME links
    private SpreadsheetDialogComponent dialogCreate(final JarEntryInfoListDialogComponentContext context) {
        return SpreadsheetDialogComponent.with(
                        ID + SpreadsheetElementIds.DIALOG, // id
                        "Plugin", // title
                        true, // includeClose
                        context
                ).appendChild(this.table)
                .appendChild(
                        SpreadsheetFlexLayout.row()
                                .appendChild(this.delete)
                                .appendChild(this.download)
                                .appendChild(this.close)
                );
    }

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    private final JarEntryInfoListDialogComponentContext context;

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    final static String ID = "plugin";

    final static String ID_PREFIX = ID + '-';

    // PluginFetcherWatcher.............................................................................................

    @Override
    public void onJarEntryInfoList(final PluginName name,
                                   final Optional<JarEntryInfoList> list,
                                   final AppContext context) {
        this.pluginName = name;
        this.list = list.orElse(JarEntryInfoList.EMPTY);

        this.refreshIfOpen(context);
    }

    @Override
    public void onJarEntryInfoName(final PluginName pluginName,
                                   final Optional<JarEntryInfoName> filename,
                                   final Optional<String> body,
                                   final AppContext context) {
        // NOP
    }

    @Override
    public void onPlugin(final PluginName name,
                         final Optional<Plugin> plugin,
                         final AppContext context) {
        // ignore
    }

    @Override
    public void onPluginSet(final PluginSet plugins,
                            final AppContext context) {
        // ignore
    }

    // SpreadsheetDialogComponentLifecycle..............................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return false;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof PluginSelectHistoryToken;
    }

    /**
     * Clear the JAR file listing
     */
    @Override
    public void dialogReset() {
        this.table.setList(JarEntryInfoList.EMPTY);
        this.pluginName = null;
        this.list = JarEntryInfoList.EMPTY;
    }

    @Override
    public void openGiveFocus(final AppContext context) {
        final PluginSelectHistoryToken select = context.historyToken()
                .cast(PluginSelectHistoryToken.class);
        this.pluginName = select.name();

        this.context.listJarEntries(
            this.pluginName
        );
    }

    @Override
    public void refresh(final AppContext context) {
        this.dialog.setTitle(
                Optional.ofNullable(this.pluginName)
                        .map(PluginName::value)
                        .orElse("Loading...")
        );
        this.table.setList(this.list);

        this.refreshDelete();
        this.refreshDownload();
        this.refreshClose(context);
    }

    private PluginName pluginName;

    private JarEntryInfoList list;
}
