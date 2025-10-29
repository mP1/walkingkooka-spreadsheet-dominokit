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
import walkingkooka.plugin.PluginNameSet;
import walkingkooka.plugin.store.Plugin;
import walkingkooka.plugin.store.PluginSet;
import walkingkooka.predicate.Predicates;
import walkingkooka.spreadsheet.SpreadsheetStrings;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.anchor.HistoryTokenSaveValueAnchorComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.link.AnchorListComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoList;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A modal dialog that supports editing a {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName#PLUGINS}
 */
public final class PluginNameSetDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    PluginFetcherWatcher {

    /**
     * Creates a new {@link PluginNameSetDialogComponent}.
     */
    public static PluginNameSetDialogComponent with(final PluginNameSetDialogComponentContext context) {
        return new PluginNameSetDialogComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private PluginNameSetDialogComponent(final PluginNameSetDialogComponentContext context) {
        this.context = context;
        context.addHistoryTokenWatcher(this);
        context.addPluginFetcherWatcher(this);
        context.addSpreadsheetMetadataFetcherWatcher(this);

        this.add = AddPluginNameSetComponent.empty(ID + "-add-");

        this.remove = RemovePluginNameSetComponent.empty(ID + "-remove-");

        this.textBox = PluginNameSetComponent.empty()
            .setId(ID + SpreadsheetElementIds.TEXT_BOX)
            .addKeyUpListener(
                (e) -> this.onTextBox(this.text())
            ).addChangeListener(
                (oldValue, newValue) -> this.onTextBox(this.text())
            );

        this.save = this.<PluginNameSet>saveValueAnchor(context)
            .autoDisableWhenMissingValue();
        this.undo = this.undoAnchor(context);
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate();
    }

    // ids..............................................................................................................

    @Override
    public String idPrefix() {
        return ID + "-";
    }

    private final static String ID = PluginNameSet.class.getSimpleName();

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, loaded with the {@link PluginNameSet} textbox and some links.
     */
    private DialogComponent dialogCreate() {
        final PluginNameSetDialogComponentContext context = this.context;

        return DialogComponent.smallerPrompt(
                ID + SpreadsheetElementIds.DIALOG,
                DialogComponent.INCLUDE_CLOSE,
                context
            ).setTitle(
                context.dialogTitle()
            ).appendChild(this.add.setFilterValueChangeListener(this::addFilterOnValueChange))
            .appendChild(this.remove.setFilterValueChangeListener(this::removeFilterOnValueChange))
            .appendChild(this.textBox)
            .appendChild(
                AnchorListComponent.empty()
                    .appendChild(this.save)
                    .appendChild(this.undo)
                    .appendChild(this.close)
            );
    }

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    private final DialogComponent dialog;

    // add..............................................................................................................

    /**
     * When the ADD card filter changes, execute a {@link walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatcher#}.
     * When the {@link walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatcher#onPluginSet(PluginSet, AppContext)}.
     */
    private void addFilterOnValueChange(final Optional<String> oldValue,
                                        final Optional<String> newValue) {
        this.context.pluginFilter(
            this.add.filterValue()
                .orElse("*"),
            OptionalInt.of(0),
            OptionalInt.of(50)
        );
    }

    private final AddPluginNameSetComponent add;

    // remove...........................................................................................................

    private void removeFilterOnValueChange(final Optional<String> oldValue,
                                           final Optional<String> newValue) {
        this.remove.setFilter(
            this.predicate(
                newValue.orElse(null)
            )
        );
        this.refreshLinks();
    }

    private final RemovePluginNameSetComponent remove;

    private Predicate<CharSequence> predicate(final String filterText) {
        return CharSequences.isNullOrEmpty(filterText) ?
            null :
            predicateNotEmptyFilterText(filterText);
    }

    private Predicate<CharSequence> predicateNotEmptyFilterText(final String text) {
        return Predicates.globPatterns(
            text,
            SpreadsheetStrings.CASE_SENSITIVITY
        );
    }

    // textBox..........................................................................................................

    /**
     * Handles updates to the {@link PluginNameSet} component. If parsing fails all links are not refreshed.
     */
    private void onTextBox(final String text) {
        try {
            this.refreshLinks(
                PluginNameSet.parse(text)
            );
        } catch (final RuntimeException parseFailed) {
            // ignore
        }
    }

    /**
     * Retrieves the current {@link PluginNameSet} in text form.
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
     * A text box that supports editing the {@link PluginNameSet}.
     */
    private final PluginNameSetComponent textBox;

    // dialog links.....................................................................................................

    /**
     * A SAVE link which will be updated each time the {@link #textBox} is also updated.
     */
    private final HistoryTokenSaveValueAnchorComponent<PluginNameSet> save;

    /**
     * A RESET link which saves the original {@link PluginNameSet} value when the dialog appeared
     */
    private final HistoryTokenSaveValueAnchorComponent<PluginNameSet> undo;

    /**
     * A CLOSE link which will close the dialog.
     */
    private final HistoryTokenAnchorComponent close;

    // SpreadsheetMetadataFetcherWatcher................................................................................
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        // maybe should ignore metadata if it has the wrong SpreadsheetMetadata
        this.setText(
            metadata.get(SpreadsheetMetadataPropertyName.PLUGINS)
                .orElse(PluginNameSet.EMPTY)
                .text()
        );
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas,
                                         final AppContext context) {
        // Ignore many
    }

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
        // NOP
    }

    @Override
    public void onPluginSet(final PluginSet plugins,
                            final AppContext context) {
        this.filterMatchPluginNames = plugins.names();
        this.refreshIfOpen(context);
    }

    /**
     * This will hold the {@link PluginNameSet} after executing the ADD filter query.
     */
    private PluginNameSet filterMatchPluginNames;

    // HistoryTokenAwareComponentLifecycle..............................................................................

    // save should not open or close the dialog.
    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySaveHistoryToken;
    }

    // history token must be metadata select of PLUGINS
    @Override
    public boolean isMatch(final HistoryToken token) {
        boolean match = false;

        if (token instanceof SpreadsheetMetadataPropertySelectHistoryToken) {
            match = SpreadsheetMetadataPropertyName.PLUGINS.equals(
                token.metadataPropertyName()
                    .orElse(null)
            );
        }

        return match;
    }

    @Override
    public void dialogReset() {
        this.filterMatchPluginNames = PluginNameSet.EMPTY;
    }

    /**
     * Load the latest {@link SpreadsheetMetadata}, execute a remote {@link walkingkooka.plugin.store.PluginStore#filter(String, int, int)} and give focus to the {@link #textBox}.
     */
    @Override
    public void openGiveFocus(final RefreshContext context) {
        final PluginNameSetDialogComponentContext dialogContext = this.context;

        this.refreshUndo(
            dialogContext.spreadsheetMetadata()
                .get(SpreadsheetMetadataPropertyName.PLUGINS)
                .orElse(PluginNameSet.EMPTY)
        );

        final SpreadsheetMetadataPropertySelectHistoryToken<?> propertySelectHistoryToken = context.historyToken()
            .cast(SpreadsheetMetadataPropertySelectHistoryToken.class);

        // load latest metadata
        dialogContext.loadSpreadsheetMetadata(
            propertySelectHistoryToken.id()
        );

        dialogContext.pluginFilter(
            this.add.filterValue()
                .orElse("*"),
            OptionalInt.of(0),
            OptionalInt.of(50)
        );

        context.giveFocus(
            this.textBox::focus
        );
    }

    private void refreshUndo(final PluginNameSet plugins) {
        this.undo.setValue(
            Optional.of(plugins)
        );
    }

    /**
     * Refreshes ALL links except for RESET.
     */
    @Override
    public void refresh(final RefreshContext context) {
        this.context.refreshDialogTitle(this);

        this.refreshLinks();
    }

    private void refreshLinks() {
        final Optional<PluginNameSet> pluginNames = this.textBox.value();
        this.refreshLinks(
            pluginNames.orElse(PluginNameSet.EMPTY)
        );
    }

    private void refreshLinks(final PluginNameSet currentPluginNames) {
        final PluginNameSetDialogComponentContext context = this.context;

        final HistoryToken historyToken = context.historyToken();

        final PluginNameSet filterMatchPluginNames = this.filterMatchPluginNames;

        this.add.refresh(
            currentPluginNames,
            filterMatchPluginNames,
            context
        );

        this.remove.refresh(
            currentPluginNames,
            filterMatchPluginNames,
            context
        );

        this.save.setValue(
            Optional.of(currentPluginNames)
        );

        this.close.setHistoryToken(
            Optional.of(
                historyToken.close()
            )
        );
    }

    private final PluginNameSetDialogComponentContext context;

    @Override
    public  boolean shouldLogLifecycleChanges() {
        return PLUGIN_NAME_SET_DIALOG_COMPONENT;
    }
}
