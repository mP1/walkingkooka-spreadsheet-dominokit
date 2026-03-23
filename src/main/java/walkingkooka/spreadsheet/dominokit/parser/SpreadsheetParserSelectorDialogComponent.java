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

package walkingkooka.spreadsheet.dominokit.parser;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dialog.DialogAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopSpreadsheetParserInfoSetFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetParserFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.format.SpreadsheetFormatterTableComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.meta.SpreadsheetMetadataPropertyNameTabsComponent;
import walkingkooka.spreadsheet.dominokit.selector.AppendPluginSelectorTokenComponent;
import walkingkooka.spreadsheet.dominokit.selector.RemoveOrReplacePluginSelectorTokenComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSample;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelectorToken;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelectorTokenAlternative;
import walkingkooka.spreadsheet.server.parser.SpreadsheetParserSelectorEdit;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A modal dialog that supports editing a {@link SpreadsheetParserSelector}.
 */
public final class SpreadsheetParserSelectorDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher,
    SpreadsheetDeltaFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    SpreadsheetParserFetcherWatcher,
    NopSpreadsheetParserInfoSetFetcherWatcher,
    ComponentLifecycleMatcherDelegator {

    /**
     * Creates a new {@link SpreadsheetParserSelectorDialogComponent}.
     */
    public static SpreadsheetParserSelectorDialogComponent with(final SpreadsheetParserSelectorDialogComponentContext context) {
        return new SpreadsheetParserSelectorDialogComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetParserSelectorDialogComponent(final SpreadsheetParserSelectorDialogComponentContext context) {
        this.context = context;
        context.addHistoryTokenWatcher(this);

        context.addSpreadsheetDeltaFetcherWatcher(this);
        context.addSpreadsheetMetadataFetcherWatcher(this);

        this.tabs = context.shouldShowTabs() ?
            SpreadsheetMetadataPropertyNameTabsComponent.empty(
                ID + SpreadsheetElementIds.TABS + "-",
                Cast.to(
                    SpreadsheetMetadataPropertyName.parsers()
                ),
                context
            ) :
            null;

        this.parserNames = SpreadsheetParserNameLinkListComponent.empty(ID + "-parserNames-");
        this.parserName = Optional.empty();

        this.table = SpreadsheetFormatterTableComponent.empty(ID + "-");

        this.appender = AppendPluginSelectorTokenComponent.empty(ID + "-appender-");

        this.removeOrReplace = RemoveOrReplacePluginSelectorTokenComponent.empty(ID + "-removeOrReplace-");

        this.selector = this.selector();

        this.links = this.links();

        this.dialog = this.dialogCreate();
    }

    // ids..............................................................................................................

    @Override
    public String idPrefix() {
        return ID + "-";
    }

    private final static String ID = SpreadsheetParserSelector.class.getSimpleName();

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, loaded with the {@link SpreadsheetParserSelector} textbox and some links.
     */
    private DialogComponent dialogCreate() {
        final SpreadsheetParserSelectorDialogComponentContext context = this.context;

        DialogComponent dialog = DialogComponent.largeEdit(
            ID + SpreadsheetElementIds.DIALOG,
            DialogComponent.INCLUDE_CLOSE,
            context
        );

        if (null != this.tabs) {
            dialog.appendChild(this.tabs);
        }

        return dialog.appendChild(this.parserNames)
            .appendChild(this.table)
            .appendChild(this.appender)
            .appendChild(this.removeOrReplace)
            .appendChild(this.selector)
            .appendChild(this.links);
    }

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    private final DialogComponent dialog;

    private final SpreadsheetParserSelectorDialogComponentContext context;

    // tabs............................................................................................................

    /**
     * This will be null when editing a cell formatter.
     */
    private final SpreadsheetMetadataPropertyNameTabsComponent tabs;

    // tabs............................................................................................................

    private final SpreadsheetParserNameLinkListComponent parserNames;

    // sample...........................................................................................................

    private final SpreadsheetFormatterTableComponent table;

    // appender.........................................................................................................

    private final AppendPluginSelectorTokenComponent<SpreadsheetParserSelectorToken, SpreadsheetParserSelectorTokenAlternative> appender;

    // removeOrReplace..................................................................................................

    private final RemoveOrReplacePluginSelectorTokenComponent<SpreadsheetParserSelectorToken, SpreadsheetParserSelectorTokenAlternative> removeOrReplace;

    // selector..........................................................................................................

    /**
     * Creates a text box to edit the {@link SpreadsheetFormatterSelector} and installs a few value change type listeners
     */
    private SpreadsheetParserSelectorComponent selector() {
        return SpreadsheetParserSelectorComponent.empty()
            .setId(ID + SpreadsheetElementIds.TEXT_BOX)
            .addValueWatcher2(
                this::onSelectorValue
            );
    }

    private void onSelectorValue(final Optional<SpreadsheetParserSelector> selector) {
        final SpreadsheetParserSelectorDialogComponentContext context = this.context;

        final String text = selector.map(SpreadsheetParserSelector::text)
            .orElse("");

        final SpreadsheetParserSelectorEdit edit = SpreadsheetParserSelectorEdit.parse(
            text,
            context
        );

        if (this.selector.hasErrors()) {
            this.links.disableSave();
        } else {
            this.links.setValue(selector);
        }

        // couldnt get edit in browser, try server
        if (edit.message().startsWith("Unknown ")) {
            context.loadSpreadsheetParsersEdit(text);
        } else {
            this.onSpreadsheetParserSelectorEdit(
                edit,
                context
            );
        }
    }

    /**
     * The {@link SpreadsheetParserSelectorComponent} that holds the {@link SpreadsheetParserSelector} in text form.
     */
    // @VisibleForTesting
    final SpreadsheetParserSelectorComponent selector;

    // SpreadsheetParserFetcherWatcher..................................................................................

    @Override
    public void onSpreadsheetParserSelectorEdit(final SpreadsheetId id,
                                                final SpreadsheetParserSelectorEdit edit) {
        if (this.isOpen()) {
            this.onSpreadsheetParserSelectorEdit(
                edit,
                this.context
            );
        }
    }

    private void onSpreadsheetParserSelectorEdit(final SpreadsheetParserSelectorEdit edit,
                                                 final SpreadsheetParserSelectorDialogComponentContext context) {
        this.parserName = edit.selector()
            .map(SpreadsheetParserSelector::name);

        final List<SpreadsheetFormatterSample> samples = edit.samples();
        this.table.setValue(
            Optional.ofNullable(
                samples.isEmpty() ?
                    null :
                    samples
            )
        );
        this.table.refresh(
            SpreadsheetParserSelectorDialogComponentSpreadsheetFormatterTableComponentContext.with(
                edit.selector()
                    .orElse(null),
                context
            )
        );

        final SpreadsheetParserSelectorAppendPluginSelectorTokenComponentContextRemoveOrReplacePluginSelectorTokenComponentContext appenderRemoveOrReplaceContext =
            SpreadsheetParserSelectorAppendPluginSelectorTokenComponentContextRemoveOrReplacePluginSelectorTokenComponentContext.with(
                edit.selector()
                    .map(SpreadsheetParserSelector::name)
                    .orElse(null),
                context
            );

        this.appender.refresh(
            edit.tokens(),
            edit.next()
                .map(SpreadsheetParserSelectorToken::alternatives)
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
        this.selector.setErrors(
            hasNoError ?
                Lists.empty() :
                Lists.of(message)
        );

        this.links.setValue(
            this.selector.stringValue().isEmpty() || hasNoError ?
                edit.selector() :
                Optional.<SpreadsheetParserSelector>empty()
        );

        this.refreshTitleTabsClearClose();
    }

    // dialog links.....................................................................................................

    private DialogAnchorListComponent<SpreadsheetParserSelector> links() {
        return DialogAnchorListComponent.empty(
                this.idPrefix(),
                this.context // DialogAnchorListComponentContext
            ).saveAutoDisableWhenMissingValue()
            .undo()
            .clearLink()
            .close()
            .setComponentWithErrors(this.selector);
    }

    private final DialogAnchorListComponent<SpreadsheetParserSelector> links;

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    // eventually refresh will read the updated *CELL* from the cache following a SAVE
    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta) {
        this.refreshIfOpen(this.context);
    }

    // SpreadsheetMetadataFetcherWatcher................................................................................
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata) {
        this.refreshIfOpen(this.context);
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas) {
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
            this.selector::focus
        );
    }

    /**
     * Refreshes the widget, typically done when the {@link SpreadsheetMetadataPropertyName} changes etc.
     */
    @Override
    public void refresh(final RefreshContext context) {
        this.selector.setValue(
            this.context.undo()
        );

        this.refreshTitleTabsClearClose();
    }

    private void refreshTitleTabsClearClose() {
        final SpreadsheetParserSelectorDialogComponentContext context = this.context;
        context.refreshDialogTitle(this);

        if (null != this.tabs) {
            this.tabs.refresh(context);
        }

        this.parserNames.refresh(
            SpreadsheetParserSelectorDialogComponentSpreadsheetParserNameLinkListComponentContext.with(
                context, // HistoryContext
                context, // SpreadsheetParserProvider,
                this.parserName
            )
        );

        this.links.refresh(context);
    }

    private Optional<SpreadsheetParserName> parserName;

    @Override
    public boolean shouldLogLifecycleChanges() {
        return SPREADSHEET_PARSER_SELECTOR_DIALOG_COMPONENT;
    }
}
