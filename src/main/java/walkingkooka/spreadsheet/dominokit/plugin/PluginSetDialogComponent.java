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
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.PluginDeleteHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.PluginListSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.link.SpreadsheetLinkListComponent;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoList;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.util.Objects;
import java.util.Optional;

/**
 * A dialog that includes a table showing all the entries for a JAR file, along with delete, download and close links.
 */
public final class PluginSetDialogComponent implements SpreadsheetDialogComponentLifecycle,
    PluginFetcherWatcher,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher {

    public static PluginSetDialogComponent with(final PluginSetDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new PluginSetDialogComponent(context);
    }

    private PluginSetDialogComponent(final PluginSetDialogComponentContext context) {
        this.context = context;

        context.addHistoryTokenWatcher(this);
        context.addPluginFetcherWatcher(this);

        this.table = this.table();

        this.uploadAnchor = this.uploadAnchor();
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate(context);
    }

    // table............................................................................................................

    private PluginSetTableComponent table() {
        return PluginSetTableComponent.empty(
            ID_PREFIX,
            this.context
        );
    }

    private final PluginSetTableComponent table;

    // upload...........................................................................................................

    private PluginUploadSelectAnchorComponent uploadAnchor() {
        return PluginUploadSelectAnchorComponent.empty(
            ID_PREFIX + "upload" + SpreadsheetElementIds.LINK
        ).setTextContent("Upload");
    }

    private void refreshUploadAnchor(final HistoryContext context) {
        this.uploadAnchor.setDisabled(false);
    }

    private final PluginUploadSelectAnchorComponent uploadAnchor;

    // close............................................................................................................

    private void refreshClose(final HistoryContext context) {
        this.close.setHistoryToken(
            Optional.of(
                context.historyToken().close()
            )
        );
    }

    private final HistoryTokenAnchorComponent close;

    // SpreadsheetDialogComponentLifecycle..............................................................................

    private SpreadsheetDialogComponent dialogCreate(final PluginSetDialogComponentContext context) {
        return SpreadsheetDialogComponent.largeEdit(
                ID + SpreadsheetElementIds.DIALOG, // id
                SpreadsheetDialogComponent.INCLUDE_CLOSE,
                context
            ).appendChild(this.table)
            .appendChild(
                SpreadsheetLinkListComponent.empty()
                    .appendChild(this.uploadAnchor)
                    .appendChild(this.close)
            );
    }

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    private final PluginSetDialogComponentContext context;

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    final static String ID = "pluginList";

    final static String ID_PREFIX = ID + '-';

    // PluginFetcherWatcher.............................................................................................

    @Override
    public void onJarEntryInfoList(final PluginName name,
                                   final Optional<JarEntryInfoList> list,
                                   final AppContext context) {
        // NOP
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
        this.refreshIfOpen(context);
    }

    @Override
    public void onPluginSet(final PluginSet plugins,
                            final AppContext context) {
        this.table.setValue(
            Optional.ofNullable(
                plugins.isEmpty() ?
                    null :
                    plugins
            )
        );
        this.refreshIfOpen(context);
    }

    // SpreadsheetDialogComponentLifecycle..............................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof PluginDeleteHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof PluginListSelectHistoryToken;
    }

    @Override
    public void dialogReset() {
        this.table.clearValue();
        this.uploadAnchor.setDisabled(true);
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        // NOP
    }

    @Override
    public void refresh(final RefreshContext context) {
        this.context.refreshDialogTitle(this);

        this.table.refresh(
            context.historyToken()
                .cast(PluginListSelectHistoryToken.class)
        );
        this.refreshUploadAnchor(context);
        this.refreshClose(context);
    }
}
