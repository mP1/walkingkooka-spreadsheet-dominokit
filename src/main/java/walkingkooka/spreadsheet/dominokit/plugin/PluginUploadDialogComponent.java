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
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.PluginUploadSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.PluginUploadSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.link.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.upload.UploadFileComponent;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoList;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.util.Objects;
import java.util.Optional;

/**
 * A dialog that supports uploading of a plugin archive.
 */
public final class PluginUploadDialogComponent implements DialogComponentLifecycle,
    PluginFetcherWatcher,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher {

    public static PluginUploadDialogComponent with(final PluginUploadDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new PluginUploadDialogComponent(context);
    }

    private PluginUploadDialogComponent(final PluginUploadDialogComponentContext context) {
        this.uploadFile = this.uploadFile();
        this.uploadLink = this.uploadLink();
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate(context);

        context.addHistoryTokenWatcher(this);
        context.addPluginFetcherWatcher(this);

        this.context = context;
    }

    // close............................................................................................................

    private void refreshClose(final HistoryContext context) {
        this.close.setHistoryToken(
            Optional.of(
                context.historyToken().close()
            )
        );
    }

    private final HistoryTokenAnchorComponent close;

    // upload...........................................................................................................

    private UploadFileComponent uploadFile() {
        return UploadFileComponent.empty(ID_PREFIX + "UploadFile")
            .setLabel("Drop files here or click to upload.")
            .addValueWatcher2(
                (value) -> this.refreshIfOpen(this.context)
            );
    }

    private void refreshUploadFile() {
        this.uploadFile.setDisabled(false);
    }

    // @VisibleForTesting
    final UploadFileComponent uploadFile;

    // uploadLink........................................................................................................

    private PluginUploadSaveAnchorComponent uploadLink() {
        return PluginUploadSaveAnchorComponent.empty(ID_PREFIX + "upload" + SpreadsheetElementIds.LINK)
            .setTextContent("Upload");
    }

    private void refreshUploadLink() {
        this.uploadLink.setValue(
            this.uploadFile.value()
        );
    }

    private final PluginUploadSaveAnchorComponent uploadLink;

    // DialogComponentLifecycle.........................................................................................

    private DialogComponent dialogCreate(final PluginUploadDialogComponentContext context) {
        return DialogComponent.largeEdit(
            ID + SpreadsheetElementIds.DIALOG, // id
            DialogComponent.INCLUDE_CLOSE,
            context
        ).appendChild(
            this.uploadFile
        ).appendChild(
            AnchorListComponent.empty()
                .appendChild(this.uploadLink)
                .appendChild(this.close)
        );
    }

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    private final DialogComponent dialog;

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    final static String ID = Plugin.class.getSimpleName() + "Upload";

    final static String ID_PREFIX = ID + '-';

    // PluginFetcherWatcher.............................................................................................

    @Override
    public void onJarEntryInfoList(final PluginName name,
                                   final Optional<JarEntryInfoList> list) {
        // NOP
    }

    @Override
    public void onJarEntryInfoName(final PluginName pluginName,
                                   final Optional<JarEntryInfoName> filename,
                                   final Optional<String> body) {
        // NOP
    }

    @Override
    public void onPlugin(final PluginName name,
                         final Optional<Plugin> plugin) {
        // change message telling user plugin upload was successful
    }

    @Override
    public void onPluginSet(final PluginSet plugins) {
        // ignore
    }

    // DialogComponentLifecycle.........................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof PluginUploadSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof PluginUploadSelectHistoryToken;
    }

    /**
     * Clear the text file viewer
     */
    @Override
    public void dialogReset() {
        this.uploadFile.clearValue();
        this.uploadLink.setDisabled(true);
        this.close.setDisabled(true);
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        this.uploadFile.focus();
    }

    @Override
    public void refresh(final RefreshContext context) {
        this.context.refreshDialogTitle(this);

        this.refreshUploadFile();
        this.refreshUploadLink();
        this.refreshClose(context);
    }

    private final PluginUploadDialogComponentContext context;

    @Override
    public  boolean shouldLogLifecycleChanges() {
        return PLUGIN_UPLOAD_DIALOG_COMPONENT;
    }
}
