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

package walkingkooka.spreadsheet.dominokit.pluginaliassetlike;

import walkingkooka.naming.Name;
import walkingkooka.plugin.PluginAliasLike;
import walkingkooka.plugin.PluginAliasSetLike;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.plugin.PluginSelectorLike;
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
 * A modal dialog that supports editing a {@link PluginInfoSetLike}.
 * <pre>
 * Enable SpreadsheetImporterInfoSet links
 *
 * Disable SpreadsheetImporterInfoSet links
 *
 * ValueSpreadsheetTextBoxWrapper<SpreadsheetImporterInfoSetComponent, SpreadsheetImporterInfoSet>
 *
 * SAVE
 * UNDO
 * REMOVE ALL
 * ADD ALL
 * CLOSE
 * </pre>
 */
public final class PluginAliasSetLikeDialogComponent<N extends Name & Comparable<N>,
        I extends PluginInfoLike<I, N>,
        IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
        S extends PluginSelectorLike<N>,
        A extends PluginAliasLike<N, S, A>,
        AS extends PluginAliasSetLike<N, I, IS, S, A, AS>> implements SpreadsheetDialogComponentLifecycle,
        LoadedSpreadsheetMetadataRequired,
        NopFetcherWatcher,
        NopEmptyResponseFetcherWatcher,
        SpreadsheetMetadataFetcherWatcher {

    /**
     * Creates a new {@link PluginAliasSetLikeDialogComponent}.
     */
    public static <N extends Name & Comparable<N>,
            I extends PluginInfoLike<I, N>,
            IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
            S extends PluginSelectorLike<N>,
            A extends PluginAliasLike<N, S, A>,
            AS extends PluginAliasSetLike<N, I, IS, S, A, AS>>
    PluginAliasSetLikeDialogComponent<N, I, IS, S, A, AS> with(final PluginAliasSetLikeDialogComponentContext<N, I, IS, S, A, AS> context) {
        return new PluginAliasSetLikeDialogComponent<>(
                Objects.requireNonNull(context, "context")
        );
    }

    private PluginAliasSetLikeDialogComponent(final PluginAliasSetLikeDialogComponentContext<N, I, IS, S, A, AS> context) {
        this.context = context;
        context.addHistoryTokenWatcher(this);

        context.addSpreadsheetMetadataFetcherWatcher(this);
        context.addProviderFetcherWatcher(this::onProviderAliasSet);

        this.add = AddPluginAliasSetLikeComponent.empty(ID + "-add-");

        this.remove = RemovePluginAliasSetLikeComponent.empty(ID + "-remove-");

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
        this.removeAll = this.anchor("Remove All")
                .setDisabled(true);
        this.addAll = this.anchor("Add All")
                .setDisabled(true);
        this.close = this.anchor("Close")
                .setDisabled(true);

        this.dialog = this.dialogCreate();
    }

    private void onProviderAliasSet(final AS providerInfos) {
        if (this.isOpen()) {
            this.refreshNonResetLinks();
        }
    }

    // ids..............................................................................................................

    @Override
    public String idPrefix() {
        return ID + "-";
    }

    private final static String ID = PluginAliasSetLikeDialogComponent.class.getSimpleName();

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, loaded with the {@link PluginInfoSetLike} textbox and some links.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final PluginAliasSetLikeDialogComponentContext<N, I, IS, S, A, AS> context = this.context;

        return SpreadsheetDialogComponent.with(
                        ID,
                        context.dialogTitle(),
                        true, // includeClose
                        context
                ).setTitle(
                        context.dialogTitle()
                ).appendChild(this.add)
                .appendChild(this.remove)
                .appendChild(this.textBox)
                .appendChild(
                        SpreadsheetFlexLayout.row()
                                .appendChild(this.save)
                                .appendChild(this.reset)
                                .appendChild(this.removeAll)
                                .appendChild(this.addAll)
                                .appendChild(this.close)
                );
    }

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    // add..............................................................................................................

    private final AddPluginAliasSetLikeComponent<N, I, IS, S, A, AS> add;

    // remove...........................................................................................................

    private final RemovePluginAliasSetLikeComponent<N, I, IS, S, A, AS> remove;

    // textBox..........................................................................................................

    /**
     * Handles updates to the {@link PluginAliasSetLike} component. If parsing fails all links are not refreshed.
     */
    private void onTextBox(final String text) {
        try {
            this.refreshNonResetLinks(
                    this.context.parseAliasSetLike(text)
            );
        } catch (final RuntimeException parseFailed) {
            // ignore
        }
    }

    /**
     * Retrieves the current {@link PluginAliasSetLike} in text form.
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
     * The {@link ValueSpreadsheetTextBoxWrapper} that holds the {@link PluginAliasSetLike} in text form.
     */
    private final ValueSpreadsheetTextBoxWrapper<?, AS> textBox;

    // dialog links.....................................................................................................

    /**
     * A SAVE link which will be updated each time the {@link #textBox} is also updated.
     */
    private final HistoryTokenAnchorComponent save;

    /**
     * A RESET link which saves the original {@link PluginAliasSetLike} value when the dialog appeared
     */
    private final HistoryTokenAnchorComponent reset;

    /**
     * A REMOVE ALL link which will save an empty {@link PluginAliasSetLike}
     */
    private final HistoryTokenAnchorComponent removeAll;

    /**
     * A ADD ALL link which will save a {@link PluginAliasSetLike} with all {@link PluginAliasLike}.
     */
    private final HistoryTokenAnchorComponent addAll;

    /**
     * A CLOSE link which will close the dialog.
     */
    private final HistoryTokenAnchorComponent close;

    // SpreadsheetMetadataFetcherWatcher................................................................................
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        this.setText(
                this.context.metadataAliasSetLike()
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

        this.refreshReset(this.context.providerAliasSetLike());

        // load the latest ProviderAliasSetLike
        this.context.loadProviderInfoSetLike();
    }

    private void refreshReset(final AS aliases) {
        this.reset.setHistoryToken(
                Optional.of(
                        this.context.historyToken()
                                .setSave(aliases.text())
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
        final Optional<AS> metadataAliases = this.textBox.value();
        this.refreshNonResetLinks(
                metadataAliases.orElse(this.context.emptyAliasSetLike())
        );
    }

    private void refreshNonResetLinks(final AS metadataAliases) {
        final PluginAliasSetLikeDialogComponentContext<N, I, IS, S, A, AS> context = this.context;

        final HistoryToken historyToken = context.historyToken();

        final AS providerAliases = context.providerAliasSetLike();

        this.add.refresh(
                metadataAliases,
                providerAliases,
                context
        );

        this.remove.refresh(
                metadataAliases,
                providerAliases,
                context
        );

        this.save.setHistoryToken(
                Optional.of(
                        historyToken.setSave(
                                metadataAliases.text()
                        )
                )
        );

        this.removeAll.setHistoryToken(
                Optional.of(
                        historyToken.clearSave()
                )
        );

        this.addAll.setHistoryToken(
                Optional.of(
                        historyToken.setSave(
                                providerAliases.text()
                        )
                )
        );

        this.close.setHistoryToken(
                Optional.of(
                        historyToken.close()
                )
        );
    }

    private final PluginAliasSetLikeDialogComponentContext<N, I, IS, S, A, AS> context;
}
