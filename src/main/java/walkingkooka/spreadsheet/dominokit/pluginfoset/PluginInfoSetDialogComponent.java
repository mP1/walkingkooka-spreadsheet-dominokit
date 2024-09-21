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

package walkingkooka.spreadsheet.dominokit.pluginfoset;

import walkingkooka.naming.Name;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A modal dialog that supports editing a {@link walkingkooka.plugin.PluginInfoSetLike}.
 * <pre>
 * Enable SpreadsheetImporterInfoSet links
 *
 * Disable SpreadsheetImporterInfoSet links
 *
 * ValueSpreadsheetTextBoxWrapper<SpreadsheetImporterInfoSetComponent, SpreadsheetImporterInfoSet>
 *
 * SAVE
 * UNDO
 * DISABLE ALL
 * ENABLE ALL
 * CLOSE
 * </pre>
 */
public final class PluginInfoSetDialogComponent<N extends Name & Comparable<N>, I extends PluginInfoLike<I, N>, S extends PluginInfoSetLike<S, I, N>> implements SpreadsheetDialogComponentLifecycle,
        LoadedSpreadsheetMetadataRequired,
        NopFetcherWatcher,
        NopEmptyResponseFetcherWatcher,
        SpreadsheetMetadataFetcherWatcher {

    /**
     * Creates a new {@link PluginInfoSetDialogComponent}.
     */
    public static <N extends Name & Comparable<N>, I extends PluginInfoLike<I, N>, S extends PluginInfoSetLike<S, I, N>> PluginInfoSetDialogComponent<N, I, S> with(final PluginInfoSetDialogComponentContext<N, I, S> context) {
        return new PluginInfoSetDialogComponent<>(
                Objects.requireNonNull(context, "context")
        );
    }

    private PluginInfoSetDialogComponent(final PluginInfoSetDialogComponentContext<N, I, S> context) {
        this.context = context;
        context.addHistoryTokenWatcher(this);

        context.addSpreadsheetMetadataFetcherWatcher(this);
        context.addProviderFetcherWatcher(this::onProviderInfoSet);

        this.enable = EnablePluginInfoSetComponent.empty(ID + "-enable-");

        this.disable = DisablePluginInfoSetComponent.empty(ID + "-disable-");

        this.textBox = context.textBox()
                .setId(ID + SpreadsheetElementIds.TEXT_BOX)
                .addKeyupListener(
                        (e) -> this.onTextBox(this.text())
                ).addChangeListener(
                        (oldValue, newValue) -> this.onTextBox(this.text())
                );

        this.save = this.anchor("Save")
                .setDisabled(true);
        this.reset = this.anchor("Reset")
                .setDisabled(true);
        this.disableAll = this.anchor("Disable All")
                .setDisabled(true);
        this.enableAll = this.anchor("Enable All")
                .setDisabled(true);
        this.close = this.anchor("Close")
                .setDisabled(true);

        this.dialog = this.dialogCreate();
    }

    private void onProviderInfoSet(final S providerInfos) {
        if (this.isOpen()) {
            this.refreshNonResetLinks();
        }
    }

    // ids..............................................................................................................

    @Override
    public String idPrefix() {
        return ID + "-";
    }

    private final static String ID = PluginInfoSetDialogComponent.class.getSimpleName();

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, loaded with the {@link PluginInfoSetLike} textbox and some links.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final PluginInfoSetDialogComponentContext<N, I, S> context = this.context;

        return SpreadsheetDialogComponent.with(
                        ID,
                        context.dialogTitle(),
                        true, // includeClose
                        context
                ).setTitle(
                        context.dialogTitle()
                ).appendChild(this.enable)
                .appendChild(this.disable)
                .appendChild(this.textBox)
                .appendChild(
                        SpreadsheetFlexLayout.row()
                                .appendChild(this.save)
                                .appendChild(this.reset)
                                .appendChild(this.disableAll)
                                .appendChild(this.enableAll)
                                .appendChild(this.close)
                );
    }

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    // enable...........................................................................................................

    private final EnablePluginInfoSetComponent<N, I, S> enable;

    // disable..........................................................................................................

    private final DisablePluginInfoSetComponent<N, I, S> disable;

    // textBox..........................................................................................................

    /**
     * Handles updates to the {@link PluginInfoSetLike} component. If parsing fails all links are not refreshed.
     */
    private void onTextBox(final String text) {
        try {
            this.refreshNonResetLinks(
                    this.context.parse(text)
            );
        } catch (final RuntimeException parseFailed) {
            // ignore
        }
    }

    /**
     * Retrieves the current {@link PluginInfoSetLike} in text form.
     */
    private String text() {
        return this.textBox.stringValue()
                .orElse("");
    }

    // @VisibleForTesting
    void setText(final String text) {
        this.textBox.setStringValue(
                Optional.of(text)
        );
        this.onTextBox(text);
    }

    /**
     * The {@link ValueSpreadsheetTextBoxWrapper} that holds the {@link PluginInfoSetLike} in text form.
     */
    private final ValueSpreadsheetTextBoxWrapper<?, S> textBox;

    // dialog links.....................................................................................................

    /**
     * A SAVE link which will be updated each time the {@link #textBox} is also updated.
     */
    private final HistoryTokenAnchorComponent save;

    /**
     * A RESET link which saves the original {@link PluginInfoSetLike} value when the dialog appeared
     */
    private final HistoryTokenAnchorComponent reset;

    /**
     * A DISABLE ALL link which will save an empty {@link PluginInfoSetLike}
     */
    private final HistoryTokenAnchorComponent disableAll;

    /**
     * A ENABLE ALL link which will save a {@link PluginInfoSetLike} with all {@link PluginInfoLike}.
     */
    private final HistoryTokenAnchorComponent enableAll;

    /**
     * A CLOSE link which will close the dialog.
     */
    private final HistoryTokenAnchorComponent close;

    // SpreadsheetMetadataFetcherWatcher................................................................................
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        this.setText(
                this.context.metadataInfoSet()
                        .text()
        );
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas,
                                         final AppContext context) {
        // Ignore many
    }

    // ComponentLifecycle...............................................................................................

    // save should not open or close the dialog.
    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return this.context.isMatch(token);
    }

    @Override
    public void openGiveFocus(final AppContext context) {
        context.giveFocus(
                this.textBox::focus
        );

        this.refreshReset(this.context.providerInfoSet());

        // load the latest ProviderInfoSetLike
        this.context.loadProviderInfoSet();
    }

    private void refreshReset(final S infos) {
        this.reset.setHistoryToken(
                Optional.of(
                        this.context.historyToken()
                                .setSave(infos.text())
                )
        );
    }

    /**
     * Refreshes ALL links except for RESET.
     */
    @Override
    public void refresh(final AppContext context) {
        this.refreshNonResetLinks();
    }

    private void refreshNonResetLinks() {
        final Optional<S> metadataInfos = this.textBox.value();
        this.refreshNonResetLinks(
                metadataInfos.orElse(this.context.emptyInfoSet())
        );
    }

    private void refreshNonResetLinks(final S metadataInfos) {
        final PluginInfoSetDialogComponentContext<N, I, S> context = this.context;

        final HistoryToken historyToken = context.historyToken();

        final S providerInfos = context.providerInfoSet();

        this.enable.refresh(
                metadataInfos,
                providerInfos,
                context
        );

        this.disable.refresh(
                metadataInfos,
                providerInfos,
                context
        );

        this.save.setHistoryToken(
                Optional.of(
                        historyToken.setSave(
                                metadataInfos.text()
                        )
                )
        );

        this.disableAll.setHistoryToken(
                Optional.of(
                        historyToken.clearSave()
                )
        );

        this.enableAll.setHistoryToken(
                Optional.of(
                        historyToken.setSave(
                                providerInfos.text()
                        )
                )
        );

        this.close.setHistoryToken(
                Optional.of(
                        historyToken.close()
                )
        );
    }

    private final PluginInfoSetDialogComponentContext<N, I, S> context;
}