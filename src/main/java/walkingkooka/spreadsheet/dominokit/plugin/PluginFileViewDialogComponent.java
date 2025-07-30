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
import walkingkooka.spreadsheet.dominokit.history.PluginFileViewHistoryToken;
import walkingkooka.spreadsheet.dominokit.link.SpreadsheetLinkListComponent;
import walkingkooka.spreadsheet.dominokit.text.SpreadsheetTextViewComponent;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoList;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.util.Objects;
import java.util.Optional;

/**
 * A dialog that displays a file from a plugin archive.
 */
public final class PluginFileViewDialogComponent implements SpreadsheetDialogComponentLifecycle,
    PluginFetcherWatcher,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher {

    public static PluginFileViewDialogComponent with(final PluginFileViewDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new PluginFileViewDialogComponent(context);
    }

    private PluginFileViewDialogComponent(final PluginFileViewDialogComponentContext context) {
        this.context = context;

        this.textView = this.textView();

        this.download = PluginDownloadAnchorComponent.empty(
            "plugin-download" + SpreadsheetElementIds.LINK
        ).setTextContent("Download");
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate(context);

        context.addHistoryTokenWatcher(this);
        context.addPluginFetcherWatcher(this);
    }

    // textView.........................................................................................................

    /**
     * Initially empty, and will be populated by the text response for the selected {@link PluginName} and {@link JarEntryInfoName}.
     */
    private SpreadsheetTextViewComponent textView() {
        return SpreadsheetTextViewComponent.empty()
            .setId(ID_PREFIX + "Text");
    }

    private void refreshTextView() {
        final PluginFileViewHistoryToken historyToken = context.historyToken()
            .cast(PluginFileViewHistoryToken.class);

        final PluginName pluginName = historyToken.name();
        final JarEntryInfoName filename = historyToken.file()
            .get();

        if (false == pluginName.equals(this.pluginName) || false == filename.equals(this.filename)) {
            this.pluginName = pluginName;
            this.filename = filename;

            this.context.loadJarTextFile(
                pluginName,
                filename
            );
        }
    }

    private void resetTextView() {
        this.textView.clearValue();
        this.pluginName = null;
        this.filename = null;
    }

    private final SpreadsheetTextViewComponent textView;

    private PluginName pluginName;

    private JarEntryInfoName filename;

    // close............................................................................................................

    private void refreshClose(final HistoryContext context) {
        this.close.setHistoryToken(
            Optional.of(
                context.historyToken().close()
            )
        );
    }

    private final HistoryTokenAnchorComponent close;

    // download.........................................................................................................

    private void refreshDownload() {
        final PluginFileViewHistoryToken historyToken = context.historyToken()
            .cast(PluginFileViewHistoryToken.class);

        final PluginName pluginName = historyToken.name();
        final JarEntryInfoName filename = historyToken.file()
            .get();

        this.download.setValue(
            Optional.ofNullable(
                PluginDownload.with(
                    pluginName,
                    Optional.of(filename)
                )
            )
        );
    }

    private final PluginDownloadAnchorComponent download;

    // SpreadsheetDialogComponentLifecycle..............................................................................

    private SpreadsheetDialogComponent dialogCreate(final PluginFileViewDialogComponentContext context) {
        return SpreadsheetDialogComponent.largeEdit(
                ID + SpreadsheetElementIds.DIALOG, // id
                context.dialogTitle(), // title
                SpreadsheetDialogComponent.INCLUDE_CLOSE,
                context
            ).appendChild(this.textView)
            .appendChild(
                SpreadsheetLinkListComponent.empty()
                    .appendChild(this.download)
                    .appendChild(this.close)
            );
    }

    private void refreshDialog() {
        final PluginFileViewHistoryToken historyToken = context.historyToken()
            .cast(PluginFileViewHistoryToken.class);

        this.dialog.setTitle(
            historyToken.file()
                .get()
                .value()
        );
    }

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    private final PluginFileViewDialogComponentContext context;

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    final static String ID = "pluginFileView";

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
        this.textView.setValue(body);

        this.refreshIfOpen(context);
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
        return token instanceof PluginFileViewHistoryToken &&
            token.cast(PluginFileViewHistoryToken.class)
                .file()
                .isPresent();
    }

    /**
     * Clear the text file viewer
     */
    @Override
    public void dialogReset() {
        this.dialog.setTitle("");

        this.resetTextView();

        this.download.setDisabled(true);
        this.close.setDisabled(true);
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        // NOP
    }

    @Override
    public void refresh(final RefreshContext context) {
        this.refreshDialog();
        this.refreshTextView();
        this.refreshDownload();
        this.refreshClose(context);
    }
}
