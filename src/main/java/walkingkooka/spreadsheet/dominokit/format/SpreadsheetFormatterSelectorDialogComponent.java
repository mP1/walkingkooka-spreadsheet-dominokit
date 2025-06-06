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

package walkingkooka.spreadsheet.dominokit.format;

import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopSpreadsheetFormatterInfoSetFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetFormatterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.link.SpreadsheetLinkListComponent;
import walkingkooka.spreadsheet.dominokit.patternkind.SpreadsheetPatternKindTabsComponent;
import walkingkooka.spreadsheet.dominokit.selector.AppendPluginSelectorTokenComponent;
import walkingkooka.spreadsheet.dominokit.selector.RemoveOrReplacePluginSelectorTokenComponent;
import walkingkooka.spreadsheet.dominokit.value.HistoryTokenSaveValueAnchorComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSample;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelectorToken;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelectorTokenAlternative;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorEdit;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorMenuList;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A modal dialog that supports editing a {@link SpreadsheetFormatterSelector}.
 */
public final class SpreadsheetFormatterSelectorDialogComponent implements SpreadsheetDialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher,
    SpreadsheetDeltaFetcherWatcher,
    SpreadsheetFormatterFetcherWatcher,
    NopSpreadsheetFormatterInfoSetFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    ComponentLifecycleMatcherDelegator {

    /**
     * Creates a new {@link SpreadsheetFormatterSelectorDialogComponent}.
     */
    public static SpreadsheetFormatterSelectorDialogComponent with(final SpreadsheetFormatterSelectorDialogComponentContext context) {
        return new SpreadsheetFormatterSelectorDialogComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetFormatterSelectorDialogComponent(final SpreadsheetFormatterSelectorDialogComponentContext context) {
        this.context = context;
        context.addHistoryTokenWatcher(this);

        context.addSpreadsheetDeltaFetcherWatcher(this);
        context.addSpreadsheetFormatterFetcherWatcher(this);
        context.addSpreadsheetMetadataFetcherWatcher(this);

        this.tabs = context.shouldShowTabs() ?
            SpreadsheetPatternKindTabsComponent.empty(
                ID + SpreadsheetElementIds.TABS + "-",
                SpreadsheetPatternKind.formatValues(),
                context
            ) :
            null;

        this.formatterNames = SpreadsheetFormatterNameLinkListComponent.empty(ID + "-formatterNames-");

        this.table = SpreadsheetFormatterTableComponent.empty(
            ID + "-"
        );

        this.appender = AppendPluginSelectorTokenComponent.empty(ID + "-appender-");

        this.removeOrReplace = RemoveOrReplacePluginSelectorTokenComponent.empty(ID + "-removeOrReplace-");

        this.textBox = this.textBox();

        this.save = this.saveValueAnchor(context)
            .autoDisableWhenMissingValue();
        this.undo = this.undoAnchor(context);
        this.clear = this.clearValueAnchor(context);
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate();

        this.formatterName = Optional.empty();
    }

    // ids..............................................................................................................

    @Override
    public String idPrefix() {
        return ID + "-";
    }

    private final static String ID = "spreadsheetFormatterSelector";

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, loaded with the {@link SpreadsheetFormatterSelector} textbox and some links.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetFormatterSelectorDialogComponentContext context = this.context;

        SpreadsheetDialogComponent dialog = SpreadsheetDialogComponent.largeEdit(
            ID + SpreadsheetElementIds.DIALOG,
            context.dialogTitle(),
            SpreadsheetDialogComponent.INCLUDE_CLOSE,
            context
        );

        if (null != this.tabs) {
            dialog.appendChild(this.tabs);
        }

        return dialog.appendChild(this.formatterNames)
            .appendChild(this.table)
            .appendChild(this.appender)
            .appendChild(this.removeOrReplace)
            .appendChild(this.textBox)
            .appendChild(
                SpreadsheetLinkListComponent.empty()
                    .appendChild(this.save)
                    .appendChild(this.undo)
                    .appendChild(this.clear)
                    .appendChild(this.close)
            );
    }

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetFormatterSelectorDialogComponentContext context;

    // tabs............................................................................................................

    /**
     * This will be null when editing a cell formatter.
     */
    private final SpreadsheetPatternKindTabsComponent tabs;

    // formatterNames...................................................................................................

    private final SpreadsheetFormatterNameLinkListComponent formatterNames;

    // sample...........................................................................................................

    private final SpreadsheetFormatterTableComponent table;

    // appender.........................................................................................................

    private final AppendPluginSelectorTokenComponent<SpreadsheetFormatterSelectorToken, SpreadsheetFormatterSelectorTokenAlternative> appender;

    // removeOrReplace..................................................................................................

    private final RemoveOrReplacePluginSelectorTokenComponent<SpreadsheetFormatterSelectorToken, SpreadsheetFormatterSelectorTokenAlternative> removeOrReplace;

    // textBox..........................................................................................................

    /**
     * Creates a text box to edit the {@link SpreadsheetFormatterSelector} and installs a few value change type listeners
     */
    private SpreadsheetFormatterSelectorComponent textBox() {
        return SpreadsheetFormatterSelectorComponent.empty()
            .setId(ID + SpreadsheetElementIds.TEXT_BOX)
            .addKeyupListener(
                (event) -> this.refreshSaveLink(
                    this.textBox.value()
                )
            ).addChangeListener(
                (oldValue, newValue) ->
                    this.refreshSaveLink(newValue)
            );
    }

    // @VisibleForTesting
    void setText(final String text) {
        this.textBox.setStringValue(
            Optional.ofNullable(
                text.isEmpty() ?
                    null :
                    text
            )
        );
        this.refreshEdit(text);
    }

    /**
     * Handles updates to the {@link SpreadsheetFormatterSelectorComponent}
     */
    private void refreshEdit(final String text) {
        final SpreadsheetFormatterSelectorDialogComponentContext context = this.context;

        final SpreadsheetFormatterSelectorEdit edit = SpreadsheetFormatterSelectorEdit.parse(
            text,
            context
        );

        // couldnt get edit in browser, try server
        if (edit.message().startsWith("Unknown ")) {
            context.loadSpreadsheetFormattersEdit(text);
        } else {
            this.onSpreadsheetFormatterSelectorEdit(
                edit,
                context
            );
        }
    }

    /**
     * The {@link SpreadsheetFormatterSelectorComponent} that holds the {@link SpreadsheetFormatterSelector} in text form.
     */
    private final SpreadsheetFormatterSelectorComponent textBox;

    // SpreadsheetFormatterFetcherWatcher...............................................................................

    @Override
    public void onSpreadsheetFormatterSelectorEdit(final SpreadsheetId id,
                                                   final SpreadsheetFormatterSelectorEdit edit,
                                                   final AppContext context) {
        if (this.isOpen()) {
            this.onSpreadsheetFormatterSelectorEdit(
                edit,
                this.context
            );
        }
    }

    private void onSpreadsheetFormatterSelectorEdit(final SpreadsheetFormatterSelectorEdit edit,
                                                    final SpreadsheetFormatterSelectorDialogComponentContext context) {
        this.formatterName = edit.selector()
            .map(SpreadsheetFormatterSelector::name);

        final List<SpreadsheetFormatterSample> samples = edit.samples();
        this.table.setValue(
            Optional.ofNullable(
                samples.isEmpty() ?
                    null :
                    samples
            )
        );

        this.table.refresh(context);

        final SpreadsheetFormatterSelectorAppendComponentPluginSelectorTokenComponentContextRemoveOrReplacePluginSelectorTokenComponentContext appenderRemoveOrReplaceContext =
            SpreadsheetFormatterSelectorAppendComponentPluginSelectorTokenComponentContextRemoveOrReplacePluginSelectorTokenComponentContext.with(
                edit.selector()
                    .map(SpreadsheetFormatterSelector::name)
                    .orElse(null),
                context
            );

        this.appender.refresh(
            edit.tokens(),
            edit.next()
                .map(SpreadsheetFormatterSelectorToken::alternatives)
                .orElse(Lists.empty()),
            appenderRemoveOrReplaceContext
        );

        this.removeOrReplace.refresh(
            edit.tokens(),
            appenderRemoveOrReplaceContext
        );

        // clear or update the errors
        final String message = edit.message();
        final boolean hasNoError = CharSequences.isNullOrEmpty(message);
        this.textBox.setErrors(
            hasNoError ?
                Lists.empty() :
                Lists.of(message)
        );

        // enable SAVE if no error exists
        //final String text = this.text();
        //if (text.isEmpty() || hasNoError) {

        if (this.textBox.stringValue().isEmpty() || hasNoError) {
            this.save.setValue(
                edit.selector()
            );
        } else {
            this.save.clearValue();
        }

        this.refreshTitleTabsClearClose();
    }

    @Override
    public void onSpreadsheetFormatterSelectorMenuList(final SpreadsheetId id,
                                                       final SpreadsheetFormatterSelectorMenuList menu,
                                                       final AppContext context) {
        // nop
    }

    // dialog links.....................................................................................................

    void refreshSaveLink(final Optional<SpreadsheetFormatterSelector> selector) {
        final SpreadsheetFormatterSelectorComponent textBox = this.textBox;

        textBox.validate();
        if (textBox.hasErrors()) {
            this.save.disabled();
        } else {
            this.save.setValue(selector);
        }

        this.refreshEdit(
            textBox.stringValue()
                .orElse("")
        );
    }

    /**
     * A SAVE link which will be updated each time the {@link #textBox} is also updated.
     */
    private final HistoryTokenSaveValueAnchorComponent<SpreadsheetFormatterSelector> save;

    /**
     * A UNDO link which will be updated each time the {@link SpreadsheetFormatterSelector} is saved.
     */
    private final HistoryTokenSaveValueAnchorComponent<SpreadsheetFormatterSelector> undo;

    /**
     * A CLEAR link which will save an empty {@link SpreadsheetFormatterSelector}.
     */
    private final HistoryTokenSaveValueAnchorComponent<SpreadsheetFormatterSelector> clear;

    /**
     * A CLOSE link which will close the dialog.
     */
    private final HistoryTokenAnchorComponent close;

    // SpreadsheetDeltaFetcherWatcher................................................................................

    // eventually refresh will read the updated *CELL* from the cache
    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        this.refreshIfOpen(context);
    }

    // SpreadsheetMetadataFetcherWatcher................................................................................
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        this.refreshIfOpen(context);
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas,
                                         final AppContext context) {
        // Ignore many
    }

    // HistoryTokenAwareComponentLifecycle..............................................................................

    @Override
    public ComponentLifecycleMatcher componentLifecycleMatcher() {
        return this.context;
    }

    @Override
    public void dialogReset() {
        // NOP
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        context.giveFocus(
            this.textBox::focus
        );
    }

    /**
     * Refreshes the widget, typically done when the {@link SpreadsheetPatternKind} changes etc.
     */
    @Override
    public void refresh(final RefreshContext context) {
        // setText will trigger a refresh of table, appender, removeOrReplace
        final Optional<SpreadsheetFormatterSelector> undo = this.context.undo();
        this.textBox.setValue(undo);
        this.refreshSaveLink(undo);
        this.undo.setValue(undo);

        this.refreshTitleTabsClearClose();
    }

    private void refreshTitleTabsClearClose() {
        final SpreadsheetFormatterSelectorDialogComponentContext context = this.context;

        final HistoryToken historyToken = context.historyToken();

        this.dialog.setTitle(
            context.dialogTitle()
        );

        if (null != this.tabs) {
            this.tabs.refresh(context);
        }

        this.formatterNames.refresh(
            SpreadsheetFormatterSelectorDialogComponentSpreadsheetFormatterNameLinkListComponentContext.with(
                context, // HistoryContext
                context, // SpreadsheetFormatterProvider,
                this.formatterName
            )
        );

        this.clear.clearValue();

        this.close.setHistoryToken(
            Optional.of(
                historyToken.close()
            )
        );
    }

    private Optional<SpreadsheetFormatterName> formatterName;
}
