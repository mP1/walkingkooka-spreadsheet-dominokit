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

import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.format.SpreadsheetFormatterTableComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.net.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.NopSpreadsheetParserInfoSetFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetParserFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.patternkind.SpreadsheetPatternKindTabsComponent;
import walkingkooka.spreadsheet.dominokit.selector.AppendPluginSelectorTokenComponent;
import walkingkooka.spreadsheet.dominokit.selector.RemoveOrReplacePluginSelectorTokenComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelectorToken;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelectorTokenAlternative;
import walkingkooka.spreadsheet.server.parser.SpreadsheetParserSelectorEdit;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A modal dialog that supports editing a {@link SpreadsheetParserSelector}.
 */
public final class SpreadsheetParserSelectorDialogComponent implements SpreadsheetDialogComponentLifecycle,
        LoadedSpreadsheetMetadataRequired,
        NopFetcherWatcher,
        NopEmptyResponseFetcherWatcher,
        SpreadsheetDeltaFetcherWatcher,
        SpreadsheetMetadataFetcherWatcher,
        SpreadsheetParserFetcherWatcher,
        NopSpreadsheetParserInfoSetFetcherWatcher {

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
                SpreadsheetPatternKindTabsComponent.empty(
                        ID + SpreadsheetElementIds.TABS + "-",
                        SpreadsheetPatternKind.parseValues(),
                        context
                ) :
                null;

        this.parserNames = SpreadsheetParserNameLinkListComponent.empty(ID + "-parserNames-");
        this.parserName = Optional.empty();

        this.table = SpreadsheetFormatterTableComponent.empty(
                ID + SpreadsheetElementIds.TABLE + "-"
        );

        this.appender = AppendPluginSelectorTokenComponent.empty(ID + "-appender-");

        this.removeOrReplace = RemoveOrReplacePluginSelectorTokenComponent.empty(ID + "-removeOrReplace-");

        this.textBox = this.textBox(ID);

        this.save = this.anchor("Save")
                .setDisabled(true);
        this.undo = this.anchor("Undo")
                .setDisabled(true);
        this.clear = this.anchor("Clear")
                .setDisabled(true);
        this.close = this.anchor("Close")
                .setDisabled(true);

        this.dialog = this.dialogCreate();
    }

    // ids..............................................................................................................

    @Override
    public String idPrefix() {
        return ID + "-";
    }

    private final static String ID = SpreadsheetParserSelectorDialogComponent.class.getSimpleName();

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, loaded with the {@link SpreadsheetParserSelector} textbox and some buttons.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetParserSelectorDialogComponentContext context = this.context;

        SpreadsheetDialogComponent dialog = SpreadsheetDialogComponent.with(
                ID,
                context.dialogTitle(),
                true, // includeClose
                context
        );

        if (null != this.tabs) {
            dialog.appendChild(this.tabs);
        }

        return dialog.appendChild(this.parserNames)
                .appendChild(this.table)
                .appendChild(this.appender)
                .appendChild(this.removeOrReplace)
                .appendChild(this.textBox)
                .appendChild(
                        SpreadsheetFlexLayout.row()
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

    private final SpreadsheetParserSelectorDialogComponentContext context;

    // tabs............................................................................................................

    /**
     * This will be null when editing a cell formatter.
     */
    private final SpreadsheetPatternKindTabsComponent tabs;

    // tabs............................................................................................................

    private final SpreadsheetParserNameLinkListComponent parserNames;

    // sample...........................................................................................................

    private final SpreadsheetFormatterTableComponent table;

    // appender.........................................................................................................

    private final AppendPluginSelectorTokenComponent<SpreadsheetParserSelectorToken, SpreadsheetParserSelectorTokenAlternative> appender;

    // removeOrReplace..................................................................................................

    private final RemoveOrReplacePluginSelectorTokenComponent<SpreadsheetParserSelectorToken, SpreadsheetParserSelectorTokenAlternative> removeOrReplace;

    // textBox..........................................................................................................

    /**
     * Creates a text box to edit the {@link SpreadsheetParserSelector} and installs a few value change type listeners
     */
    private SpreadsheetParserSelectorComponent textBox(final String id) {
        return SpreadsheetParserSelectorComponent.empty()
                .setId(ID + SpreadsheetElementIds.TEXT_BOX)
                .addKeyupListener(
                        (e) -> this.onTextBox(this.text())
                ).addChangeListener(
                        (oldValue, newValue) -> this.onTextBox(this.text())
                );
    }

    /**
     * Handles updates to the {@link SpreadsheetParserSelectorComponent}
     */
    private void onTextBox(final String text) {
        final SpreadsheetParserSelectorDialogComponentContext context = this.context;

        final SpreadsheetParserSelectorEdit edit = SpreadsheetParserSelectorEdit.parse(
                text,
                context
        );

        // couldnt get edit in browser, try server
        if (edit.message().startsWith("Unknown ")) {
            context.spreadsheetParsersEdit(text);
        } else {
            this.onSpreadsheetParserSelectorEdit(
                    edit,
                    context
            );
        }
    }

    /**
     * Retrieves the current {@link SpreadsheetParserSelector}.
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
     * The {@link SpreadsheetParserSelectorComponent} that holds the {@link SpreadsheetParserSelector} in text form.
     */
    private final SpreadsheetParserSelectorComponent textBox;

    // SpreadsheetParserFetcherWatcher..................................................................................

    @Override
    public void onSpreadsheetParserSelectorEdit(final SpreadsheetId id,
                                                final SpreadsheetParserSelectorEdit edit,
                                                final AppContext context) {
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

        this.table.refresh(
                edit.samples(),
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
        this.textBox.setErrors(
                hasNoError ?
                        Lists.empty() :
                        Lists.of(message)
        );

        // enable SAVE if no error exists
        final String text = this.text();
        if (text.isEmpty() || hasNoError) {
            this.save.setHistoryToken(
                    Optional.of(
                            context.historyToken()
                                    .setSave(
                                            Optional.of(
                                                    edit.selector()
                                                            .map(SpreadsheetParserSelector::toString)
                                                            .orElse("")
                                            )
                                    )
                    )
            );
        } else {
            this.save.setDisabled(true);
        }

        this.refreshTitleTabsClearClose();
    }

    // dialog links.....................................................................................................

    /**
     * A SAVE link which will be updated each time the {@link #textBox} is also updated.
     */
    private final HistoryTokenAnchorComponent save;

    /**
     * A UNDO link which will be updated each time the {@link #textBox} is saved.
     */
    private final HistoryTokenAnchorComponent undo;

    /**
     * A CLEAR link which will save an empty {@link SpreadsheetParserSelector}.
     */
    private final HistoryTokenAnchorComponent clear;

    /**
     * A CLOSE link which will close the dialog.
     */
    private final HistoryTokenAnchorComponent close;

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    // eventually refresh will read the updated *CELL* from the cache following a SAVE
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

    // ComponentLifecycle...............................................................................................

    // save should not open or close the dialog.
    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return this.context.shouldIgnore(token);
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
    }

    /**
     * Refreshes the widget, typically done when the {@link SpreadsheetPatternKind} changes etc.
     */
    @Override
    public void refresh(final AppContext context) {
        // setText will trigger a refresh of table, appender, removeOrReplace
        final String undo = this.context.undo();
        this.setText(undo);

        this.undo.setHistoryToken(
                Optional.of(
                        context.historyToken()
                                .setSave(undo)
                )
        );

        this.refreshTitleTabsClearClose();
    }

    private void refreshTitleTabsClearClose() {
        final SpreadsheetParserSelectorDialogComponentContext context = this.context;

        final HistoryToken historyToken = context.historyToken();

        this.dialog.setTitle(
                context.dialogTitle()
        );

        if (null != this.tabs) {
            this.tabs.refresh(context);
        }

        this.parserNames.refresh(
                SpreadsheetParserSelectorDialogComponentSpreadsheetParserNameLinkListComponentContext.with(
                        context, // HistoryTokenContext
                        context, // SpreadsheetParserProvider,
                        this.parserName
                )
        );

        this.clear.setHistoryToken(
                Optional.of(
                        historyToken.clearSave()
                )
        );

        this.close.setHistoryToken(
                Optional.of(
                        historyToken.close()
                )
        );
    }

    private Optional<SpreadsheetParserName> parserName;
}
