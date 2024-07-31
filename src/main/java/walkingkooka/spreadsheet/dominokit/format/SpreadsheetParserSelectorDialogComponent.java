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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.NopNoResponseWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.selector.AppendPluginSelectorTextComponent;
import walkingkooka.spreadsheet.dominokit.selector.RemoveOrReplacePluginSelectorTextComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelectorTextComponent;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelectorTextComponentAlternative;
import walkingkooka.spreadsheet.server.parser.SpreadsheetParserSelectorEdit;
import walkingkooka.text.CaseKind;
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
        NopNoResponseWatcher,
        SpreadsheetDeltaFetcherWatcher,
        SpreadsheetMetadataFetcherWatcher {

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

        this.tabs = SpreadsheetPatternKindTabsComponent.empty(
                ID + SpreadsheetElementIds.TABS + "-",
                SpreadsheetPatternKind.parseValues(),
                context
        );

        this.table = SpreadsheetFormatterTableComponent.empty(
                ID + SpreadsheetElementIds.TABLE + "-"
        );

        this.appender = AppendPluginSelectorTextComponent.empty(ID + "-appender-");

        this.removeOrReplace = RemoveOrReplacePluginSelectorTextComponent.empty(ID + "-removeOrReplace-");

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
     * Creates the modal dialog, loaded with the pattern textbox and some buttons.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetParserSelectorDialogComponentContext context = this.context;

        // patternKind might not always be present so default to empty title when absent. title will be non empty when it is actually shown.
        return SpreadsheetDialogComponent.with(
                        ID,
                        context.historyToken()
                                .patternKind()
                                .map(SpreadsheetParserSelectorDialogComponent::title)
                                .orElse(""),
                        true, // includeClose
                        context
                ).appendChild(this.tabs)
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

    private final SpreadsheetPatternKindTabsComponent tabs;

    // sample...........................................................................................................

    private final SpreadsheetFormatterTableComponent table;

    // appender.........................................................................................................

    private final AppendPluginSelectorTextComponent<SpreadsheetParserSelectorTextComponent, SpreadsheetParserSelectorTextComponentAlternative> appender;

    // removeOrReplace..................................................................................................

    private final RemoveOrReplacePluginSelectorTextComponent<SpreadsheetParserSelectorTextComponent, SpreadsheetParserSelectorTextComponentAlternative> removeOrReplace;

    // textBox..........................................................................................................

    /**
     * Creates the pattern text box and installs a value change listener.
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

        this.table.refresh(
                edit.samples(),
                SpreadsheetParserSelectorDialogComponentSpreadsheetFormatterTableComponentContext.with(
                        edit.selector()
                                .orElse(null),
                        context
                )
        );

        final SpreadsheetParserSelectorAppendPluginSelectorTextComponentContextRemoveOrReplacePluginSelectorTextComponentContext appenderRemoveOrReplaceContext =
                SpreadsheetParserSelectorAppendPluginSelectorTextComponentContextRemoveOrReplacePluginSelectorTextComponentContext.with(
                        edit.selector()
                                .map(SpreadsheetParserSelector::name)
                                .orElse(null),
                        context
                );

        this.appender.refresh(
                edit.textComponents(),
                edit.next()
                        .map(SpreadsheetParserSelectorTextComponent::alternatives)
                        .orElse(Lists.empty()),
                appenderRemoveOrReplaceContext
        );

        this.removeOrReplace.refresh(
                edit.textComponents(),
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
        this.save.setHistoryToken(
                Optional.of(
                        context.historyToken()
                                .setSave(
                                        Optional.ofNullable(
                                                hasNoError ?
                                                        text :
                                                        null
                                        )
                                )
                )
        );
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
     * The {@link SpreadsheetParserSelectorComponent} that holds the pattern in text form.
     */
    private final SpreadsheetParserSelectorComponent textBox;

    // dialog links.....................................................................................................

    /**
     * A SAVE link which will be updated each time the pattern box is also updated.
     */
    private final HistoryTokenAnchorComponent save;

    /**
     * A UNDO link which will be updated each time the pattern is saved.
     */
    private final HistoryTokenAnchorComponent undo;

    /**
     * A CLEAR link which will save an empty pattern.
     */
    private final HistoryTokenAnchorComponent clear;

    /**
     * A CLOSE link which will close the dialog.
     */
    private final HistoryTokenAnchorComponent close;

    // SpreadsheetDeltaFetcherWatcher...................................................................................

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
        final SpreadsheetParserSelectorDialogComponentContext componentContext = this.context;
        final HistoryToken historyToken = context.historyToken();

        this.dialog.setTitle(
                title(
                        historyToken.patternKind()
                                .get()
                )
        );
        this.tabs.refresh(
                componentContext
        );

        // setText will trigger a refresh of table, appender, removeOrReplace
        final String undo = componentContext.undo();
        this.setText(undo);

        this.undo.setHistoryToken(
                Optional.of(
                        historyToken.setSave(undo)
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

    // Date/Time format
    // Text format
    static String title(final SpreadsheetPatternKind kind) {
        return CaseKind.SNAKE.change(
                        kind.name(),
                        CaseKind.TITLE
                ).replace("Pattern", "")
                .replace("Format", "formatter")
                .replace("Parse", "parser")
                .trim();
    }
}
