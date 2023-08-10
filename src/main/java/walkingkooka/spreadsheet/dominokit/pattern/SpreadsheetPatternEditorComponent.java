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

package walkingkooka.spreadsheet.dominokit.pattern;

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.chips.Chip;
import org.dominokit.domino.ui.datatable.CellTextAlign;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.store.LocalListDataStore;
import org.dominokit.domino.ui.dialogs.Dialog;
import org.dominokit.domino.ui.dialogs.DialogSize;
import org.dominokit.domino.ui.dialogs.DialogType;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.icons.lib.Icons;
import org.dominokit.domino.ui.layout.NavBar;
import org.dominokit.domino.ui.style.Elevation;
import org.dominokit.domino.ui.style.StyleType;
import org.dominokit.domino.ui.tabs.Tab;
import org.dominokit.domino.ui.tabs.TabsPanel;
import org.dominokit.domino.ui.utils.ElementsFactory;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.dom.Anchor;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellPatternHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellPatternSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellPatternSelectHistoryToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatParserTokenKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextAlign;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A modal dialog with a text box that allows user entry of a {@link SpreadsheetPattern pattern}.
 * Buttons are available along the bottom that support SAVE, UNDO and CLOSE.
 */
public final class SpreadsheetPatternEditorComponent implements ComponentLifecycle {

    /**
     * Creates a new {@link SpreadsheetPatternEditorComponent}.
     */
    public static SpreadsheetPatternEditorComponent with(final SpreadsheetPatternEditorComponentContext context) {
        return new SpreadsheetPatternEditorComponent(context);
    }

    private SpreadsheetPatternEditorComponent(final SpreadsheetPatternEditorComponentContext context) {
        this.context = context;

        this.patternKindTabs = this.patternKindTabs();
        this.patternKindTabsPanel = this.patternKindTabsPanel();

        this.patternTextBox = this.patternTextBox();

        this.componentParent = Card.create();
        this.componentChipPatterns = Lists.array();

        this.appendParent = Card.create();
        this.appendLinks = Lists.array();

        final LocalListDataStore<SpreadsheetPatternEditorComponentSampleRow> localListDataStore = new LocalListDataStore<>();
        this.sampleDataTable = new DataTable<>(
                this.sampleTableConfig(),
                localListDataStore
        );
        this.sampleDataTableDataStore = localListDataStore;

        this.dialogNavBar = this.dialogNavBar();
        this.dialog = this.dialogCreate();
    }

    // dialogNavBar.....................................................................................................

    private NavBar dialogNavBar() {
        return NavBar.create() //
                .appendChild(
                        PostfixAddOn.of(
                                Icons.close()
                                        .clickable()
                                        .addClickListener(this::onDialogClose)
                        )
                );
    }

    private void onDialogClose(final Event event) {
        this.context.close();
    }

    /**
     * Includes the dialog title.
     */
    private final NavBar dialogNavBar;

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, loaded with the pattern textbox and some buttons.
     */
    private Dialog dialogCreate() {
        final Dialog dialog = Dialog.create()
                .setType(DialogType.DEFAULT) // large
                .setAutoClose(true)
                .setModal(true)
                .setStretchWidth(DialogSize.LARGE)
                .setStretchHeight(DialogSize.LARGE)
                .withHeader(
                        (d, header) ->
                                header.appendChild(this.dialogNavBar)
                );
        dialog.id(ID);

        dialog.appendChild(this.patternKindTabsPanel);

        dialog.appendChild(
                Card.create()
                        .appendChild(
                                this.sampleDataTable
                        )
        );

        this.sampleDataTable.headerElement().hide();

        dialog.appendChild(this.componentParent);
        dialog.appendChild(this.appendParent);

        dialog.appendChild(this.patternTextBox);

        dialog.appendChild(
                ElementsFactory.elements.div()
                        .appendChild(this.saveButton())
                        .appendChild(this.undoButton())
                        .appendChild(this.removeButton())
                        .appendChild(this.closeButton())
        );

        return dialog;
    }

    private final Dialog dialog;

    private final SpreadsheetPatternEditorComponentContext context;

    // tabs............................................................................................................

    /**
     * Factory that creates a TAB for each {@link SpreadsheetPatternKind}. Note the anchors links are not set and need to be
     * {@link #patternKindTabsRefresh()}.
     */
    private Tab[] patternKindTabs() {
        final SpreadsheetPatternEditorComponentContext context = this.context;

        final SpreadsheetPatternKind[] kinds = SpreadsheetPatternKind.values();
        final Tab[] tabs = new Tab[kinds.length];

        int i = 0;
        for (final SpreadsheetPatternKind kind : kinds) {
            final String text = context.patternKindButtonText(kind);
            final Tab tab = Tab.create(text);

            Anchor.with(
                            (HTMLAnchorElement)
                                    tab.getTab()
                                            .element()
                                            .firstElementChild
                    ).setId(spreadsheetPatternKindId(kind))
                    .addPushHistoryToken(context)
                    .setDisabled(false);

            tabs[i++] = tab;
        }

        return tabs;
    }

    private Tab[] patternKindTabs;

    /**
     * Returns a {@link TabsPanel} with tabs for each of the possible {@link SpreadsheetPatternKind}, with each
     * tab holding a link which will switch to that pattern.
     */
    private TabsPanel patternKindTabsPanel() {
        final TabsPanel tabsPanel = TabsPanel.create();

        for (final Tab tab : this.patternKindTabs) {
            tabsPanel.appendChild(tab);
        }

        return tabsPanel;
    }

    private final TabsPanel patternKindTabsPanel;

    /**
     * Iterates over the links in each tab updating the link, disabling and activating as necessary.
     */
    private void patternKindTabsRefresh() {
        final SpreadsheetPatternKind kind = this.context.patternKind();

        int i = 0;
        final Tab[] tabs = this.patternKindTabs;
        for (final SpreadsheetPatternKind possible : SpreadsheetPatternKind.values()) {
            final Tab tab = tabs[i++];
            final Anchor anchor = Anchor.with(
                    (HTMLAnchorElement)
                            tab.getTab()
                                    .element()
                                    .firstElementChild
            );
            final boolean match = kind.equals(possible);
            anchor.setDisabled(match);
            tab.activate(match);

            if (false == match) {
                final SpreadsheetCellPatternSelectHistoryToken historyToken = context.historyToken()
                        .cast(
                                SpreadsheetCellPatternSelectHistoryToken.class
                        );
                anchor.setHistoryToken(
                        historyToken.setPatternKind(
                                Optional.of(possible)
                        )
                );
            }
        }
    }

    // sample...........................................................................................................

    private TableConfig<SpreadsheetPatternEditorComponentSampleRow> sampleTableConfig() {
        return new TableConfig<SpreadsheetPatternEditorComponentSampleRow>()
                .addColumn(
                        columnConfig(
                                "label",
                                TextAlign.LEFT,
                                d -> Doms.textNode(d.label())
                        )
                ).addColumn(
                        columnConfig(
                                "pattern-text",
                                TextAlign.CENTER,
                                d -> this.patternAnchor(d.patternText())
                        )
                ).addColumn(
                        columnConfig(
                                "default-format",
                                TextAlign.CENTER,
                                (d) -> Doms.node(
                                        d.defaultFormattedValue()
                                                .toTextNode()
                                )
                        )
                ).addColumn(
                        columnConfig(
                                "formatted",
                                TextAlign.CENTER,
                                d -> Doms.node(
                                        d.patternFormattedValue().toTextNode()
                                )
                        )
                );
    }

    private static ColumnConfig<SpreadsheetPatternEditorComponentSampleRow> columnConfig(final String columnName,
                                                                                         final TextAlign textAlign,
                                                                                         final Function<SpreadsheetPatternEditorComponentSampleRow, Node> nodeMapper) {
        return ColumnConfig.<SpreadsheetPatternEditorComponentSampleRow>create(columnName)
                .setFixed(true)
                .minWidth("25%")
                .setTextAlign(
                        CellTextAlign.valueOf(
                                textAlign.name()
                        )
                )
                .setCellRenderer(cell -> nodeMapper.apply(
                        cell.getTableRow()
                                .getRecord()
                        )
                );
    }

    /**
     * Creates an anchor which will appear in the pattern column, which when clicked updates the pattern text box.
     * The history token is not updated.
     */
    private HTMLAnchorElement patternAnchor(final String patternText) {
        return Anchor.empty()
                .setHref(
                        Url.EMPTY_RELATIVE_URL.setFragment(
                                this.context.historyToken()
                                        .setSave(patternText)
                                        .urlFragment()
                        )
                ).setTextContent(patternText)
                .addClickAndKeydownEnterListener(
                        e ->
                        {
                            e.preventDefault();
                            this.setPatternText(patternText);
                        }).element();
    }

    private void sampleDataPrepare() {
        final SpreadsheetPatternEditorComponentContext context = this.context;
        final SpreadsheetPatternKind patternKind = context.patternKind();
        context.debug("sample: " + patternKind);

        this.sampleDataTableDataStore.setData(
                SpreadsheetPatternEditorComponentSampleRowProvider.spreadsheetPatternKind(patternKind)
                        .apply(
                                this.patternText(),
                                SpreadsheetPatternEditorComponentSampleRowProviderContexts.basic(
                                        patternKind,
                                        context.spreadsheetFormatterContext()
                                )
                        )
        );
        this.sampleDataTable.load();
    }

    private final DataTable<SpreadsheetPatternEditorComponentSampleRow> sampleDataTable;

    private final LocalListDataStore<SpreadsheetPatternEditorComponentSampleRow> sampleDataTableDataStore;

    // componentChips...................................................................................................

    /**
     * This is called anytime the pattern text is changed.
     */
    private void componentChipsRebuild(
            final SpreadsheetPattern pattern,
            final String errorPattern) {
        final Card parent = this.componentParent.clearElement();
        final SpreadsheetPatternEditorComponentContext context = this.context;

        final List<String> componentChipPatterns = this.componentChipPatterns;
        componentChipPatterns.clear();

        // pattern will be null when pattern is empty
        if (null == pattern) {
            context.debug("SpreadsheetPatternEditorComponent.componentChipsRebuild no chips");
        } else {
            pattern.components(
                    (kind, tokenPatternText) -> componentChipPatterns.add(tokenPatternText)
            );

            context.debug("SpreadsheetPatternEditorComponent.componentChipsRebuild " + componentChipPatterns.size() + " chips ", componentChipPatterns);

            if (false == errorPattern.isEmpty()) {
                componentChipPatterns.add(errorPattern);
            }

            // now build the chips
            int i = 0;
            for (final String componentChipPattern : componentChipPatterns) {
                final int ii = i;
                parent.appendChild(
                        Chip.create(componentChipPattern)
                                .setRemovable(true)
                                .addOnRemoveListener(this.componentChipOnRemove(ii))
                );

                i++;
            }
        }
    }

    /**
     * This listener is fired when a chip is removed by clicking the X. It will recompute a new pattern and update the pattern text.
     */
    private Consumer<Chip> componentChipOnRemove(final int index) {
        return (chip) -> {
            final String removed = this.componentChipPatterns.remove(index);
            this.context.debug("SpreadsheetPatternEditorComponent.componentChipOnRemove removed " + CharSequences.quoteAndEscape(removed));
            this.setPatternText(
                    this.componentChipPatterns.stream().collect(Collectors.joining())
            );
        };
    }

    /**
     * THe parent holding all the current component pattern chips.
     */
    private final Card componentParent;

    private final List<String> componentChipPatterns;

    // appendLinks......................................................................................................

    /**
     * Uses the current {@link SpreadsheetPatternKind} to recreates all links for each and every pattern for each and every {@link SpreadsheetFormatParserTokenKind}.
     * Note a few {@link SpreadsheetFormatParserTokenKind} are skipped for now for technical and other reasons.
     */
    private void appendLinksRebuild() {
        final SpreadsheetPatternEditorComponentContext context = this.context;
        context.debug("SpreadsheetPatternEditorComponent.appendLinksRebuild");

        final Card parent = this.appendParent.clearElement();
        final List<SpreadsheetPatternEditorComponentAppendLink> appendLinks = this.appendLinks;
        appendLinks.clear();

        for (final SpreadsheetFormatParserTokenKind formatParserTokenKind : context.patternKind().spreadsheetFormatParserTokenKinds()) {

            switch (formatParserTokenKind) {
                case COLOR_NAME:
                case COLOR_NUMBER:
                    break; // skip for now insert color pick instead
                case CONDITION:
                    break;
                case GENERAL:
                    break; // skip GENERAL for now
                case TEXT_LITERAL:
                    break; // skip - let the user insert the text literal into the patternTextBox

                default:
                    for (final String pattern : formatParserTokenKind.patterns()) {
                        final Anchor anchor = Anchor.empty()
                                .setTextContent(pattern);
                        anchor.addClickAndKeydownEnterListener(
                                (e) -> {
                                    e.preventDefault();
                                    this.setPatternText(
                                            anchor.historyToken()
                                                    .cast(SpreadsheetCellPatternSaveHistoryToken.class)
                                                    .pattern()
                                                    .orElse(null)
                                                    .text()
                                    );
                                }
                        );
                        appendLinks.add(
                                SpreadsheetPatternEditorComponentAppendLink.with(
                                        formatParserTokenKind,
                                        pattern,
                                        anchor
                                )
                        );
                        parent.appendChild(anchor);
                    }
                    break;
            }
        }
    }

    /**
     * This should be invoked each time the pattern text is updated, and will update the HREF for each append link.
     * The updated href is not strictly needed and is merely cosmetic.
     */
    private void appendLinksHrefRefresh(final String patternText,
                                        final SpreadsheetPattern pattern) {
        final SpreadsheetPatternEditorComponentContext context = this.context;

        final SpreadsheetCellPatternHistoryToken historyToken = context.historyToken();

        final List<SpreadsheetPatternEditorComponentAppendLink> patternAppendLinks = this.appendLinks;
        context.debug("SpreadsheetPatternEditorComponent.appendLinksHrefRefresh " + patternAppendLinks.size() + " links patternText: " + CharSequences.quoteAndEscape(patternText));

        for (final SpreadsheetPatternEditorComponentAppendLink link : patternAppendLinks) {
            String savePatternText = null;

            if (patternText.isEmpty()) {
                savePatternText = link.pattern;
            } else {
                if (null != pattern) {
                    // get last SpreadsheetFormatPatternKind
                    final SpreadsheetFormatParserTokenKind[] lastPatternKind = new SpreadsheetFormatParserTokenKind[1];
                    final String[] lastPatternText = new String[1];

                    pattern.components(
                            (kk, tt) -> {
                                lastPatternKind[0] = kk;
                                lastPatternText[0] = tt;
                            }
                    );

                    savePatternText = patternText;

                    // this exists so if a pattern text ends in "d" then "dd" should replace the "d" not append and make it "ddd".
                    if (lastPatternKind[0].isDuplicate(link.kind)) {
                        // replace last
                        savePatternText = patternText.substring(
                                0,
                                patternText.length() - lastPatternText[0].length()
                        ) + link.pattern;
                    } else {
                        savePatternText = savePatternText + link.pattern;
                    }
                }
            }

            HistoryToken save = null;
            if (null != savePatternText) {
                try {
                    save = historyToken.setSave(savePatternText);
                } catch (final RuntimeException invalidPattern) {
                    // ignore save is already null
                }
            }

            context.debug("SpreadsheetPatternEditorComponent.appendLinksHrefRefresh: " + link.pattern + "=" + save);
            link.anchor.setHistoryToken(save);
        }
    }

    /**
     * THe parent holding all the append-pattern links.
     */
    private final Card appendParent;

    /**
     * A cache of a single pattern from a {@link SpreadsheetFormatParserTokenKind} to its matching ANCHOR.
     * This is kept to support updates o the ANCHOR link as the {@link #patternTextBox} changes.
     */
    private final List<SpreadsheetPatternEditorComponentAppendLink> appendLinks;

    // patternTextBox...................................................................................................

    /**
     * Creates the pattern text box and installs a value change listener.
     */
    private TextBox patternTextBox() {
        final TextBox textBox = new TextBox();

        textBox.id(ID_PREFIX + "pattern-TextBox");
        textBox.element().spellcheck = false;
        textBox.element().type = "text";
        textBox.apply(
                self -> self.appendChild(
                        PostfixAddOn.of(
                                Icons.close_circle()
                                        .clickable()
                                        .addClickListener(event -> this.setPatternText(""))
                        )
                )
        );

        textBox.addEventListener(
                EventType.input,
                (e) -> this.onPatternTextBox(this.patternText())
        );
        return textBox;
    }

    /**
     * Tries to parse the text box text into a {@link SpreadsheetPattern}.
     * If that fails an error message will be displayed and the SAVE button disabled.
     */
    private void onPatternTextBox(final String patternText) {
        final SpreadsheetPatternEditorComponentContext context = this.context;
        final SpreadsheetPatternKind patternKind = context.patternKind();
        final TextBox patternTextBox = this.patternTextBox;

        context.debug("SpreadsheetPatternEditorComponent.onPatternTextBox " + CharSequences.quoteAndEscape(patternText));

        SpreadsheetPattern pattern = null;
        String errorMessage = null;
        String errorPattern = "";

        final int patternTextLength = patternText.length();
        int last = patternTextLength;

        while (last > 0) {
            final String tryingPatternText = patternText.substring(0, last);
            context.debug("SpreadsheetPatternEditorComponent.onPatternTextBox trying to parse " + CharSequences.quoteAndEscape(tryingPatternText));

            // try parsing...
            try {
                pattern = patternKind.parse(tryingPatternText);
                errorPattern = patternText.substring(last);
                break; // best attempt stop

            } catch (final Exception failed) {
                if (null == errorMessage) {
                    errorMessage = failed.getMessage();
                }

                last--;
                context.debug("SpreadsheetPatternEditorComponent.onPatternTextBox parsing failed " + CharSequences.quoteAndEscape(tryingPatternText), failed);
            }
        }

        context.debug("SpreadsheetPatternEditorComponent.onPatternTextBox " + CharSequences.quoteAndEscape(patternText) + " errorMessage: " + errorMessage + " pattern: " + pattern);

        // clear or update the errors
        patternTextBox.setHelperText(
                CharSequences.nullToEmpty(
                        errorMessage
                ).toString()
        );

        this.componentChipsRebuild(
                pattern,
                errorPattern
        );

        this.appendLinksHrefRefresh(
                patternText,
                CharSequences.nullToEmpty(
                        errorMessage
                ).length() > 0 ?
                        null :
                        pattern
        );

        this.sampleDataPrepare();
    }

    /**
     * Retrieves the current pattern.
     */
    private String patternText() {
        return this.patternTextBox.getValue();
    }

    private void setPatternText(final String patternText) {
        this.patternTextBox.setValue(patternText);
        this.onPatternTextBox(patternText);
    }

    /**
     * The {@link TextBox} that holds the pattern in text form.
     */
    private final TextBox patternTextBox;

    // buttons..........................................................................................................

    /**
     * When clicked the CLOSE button invokes {@link #close}.
     */
    private Button closeButton() {
        return this.button(
                "Close",
                StyleType.DANGER,
                this::onCloseButtonClick
        );
    }

    private void onCloseButtonClick(final Event event) {
        final SpreadsheetPatternEditorComponentContext context = this.context;
        context.debug("SpreadsheetPatternEditorComponent.onCloseButtonClick");
        context.close();
        this.dialog.close();
    }

    /**
     * When clicked the SAVE button invokes {@link SpreadsheetPatternEditorComponentContext#save(String)}.
     */
    private Button saveButton() {
        return this.button(
                "Save",
                StyleType.DEFAULT,
                this::onSaveButtonClick
        );
    }

    private void onSaveButtonClick(final Event event) {
        final String patternText = this.patternText();
        final SpreadsheetPatternEditorComponentContext context = this.context;

        try {
            context.patternKind().parse(patternText);
            context.debug("SpreadsheetPatternEditorComponent.onSaveButtonClick " + CharSequences.quoteAndEscape(patternText));
            context.save(patternText);
        } catch (final Exception cause) {
            this.context.error(cause.getMessage());
        }
    }

    /**
     * When clicked the undo button invokes {@link #onUndoButtonClick}.
     */
    private Button undoButton() {
        return this.button(
                "undo",
                StyleType.PRIMARY,
                this::onUndoButtonClick
        );
    }

    /**
     * Reloads the last saved pattern text.
     */
    private void onUndoButtonClick(final Event event) {
        final SpreadsheetPatternEditorComponentContext context = this.context;

        final String patternText = context.loaded();
        context.debug("SpreadsheetPatternEditorComponent.onUndoButtonClick " + CharSequences.quoteAndEscape(patternText));

        this.setPatternText(patternText);
    }

    /**
     * When clicked the REMOVE button invokes {@link SpreadsheetPatternEditorComponentContext#remove()}.
     */
    private Button removeButton() {
        return this.button(
                "Remove",
                StyleType.DANGER,
                this::onRemoveButtonClick
        );
    }

    private void onRemoveButtonClick(final Event event) {
        final SpreadsheetPatternEditorComponentContext context = this.context;

        context.debug("SpreadsheetPatternEditorComponent.onRemoveButtonClick");
        context.remove();
    }

    /**
     * Creates one of the modal action buttons that appear at the bottom of the modal dialog.
     */
    private Button button(final String text,
                          final StyleType type,
                          final EventListener listener) {
        final Button button = new Button(text);

        button.id(ID_PREFIX + text.toLowerCase() + "-Button");
        button.addCss("dui-" + type.getStyle());
        button.elevate(Elevation.LEVEL_1);

        button.addEventListener(
                EventType.click,
                listener
        );

        return button;
    }

    // ComponentLifecycle...............................................................................................

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellPatternSelectHistoryToken;
    }

    /**
     * Returns true if the dialog is open.
     */
    @Override
    public boolean isOpen() {
        return this.dialog.isOpen();
    }

    @Override
    public void open(final AppContext context) {
        this.context.debug("SpreadsheetPatternEditorComponent.open");
        this.dialog.open();

        this.context.giveFocus(
                () -> this.patternTextBox.focus()
        );
    }

    /**
     * Closes or hides the {@link Dialog}. THis is necessary when the history token changes and editing a pattern
     * is no longer true.
     */
    @Override
    public void close(final AppContext context) {
        this.dialog.close();
    }

    /**
     * Refreshes the widget, typically done when the {@link SpreadsheetPatternKind} changes etc.
     */
    @Override
    public void refresh(final AppContext context) {
        final SpreadsheetPatternEditorComponentContext componentContext = this.context;

        context.debug("SpreadsheetPatternEditorComponent.refresh");

        this.dialogNavBar.setTitle(componentContext.title());
        this.patternKindTabsRefresh();
        this.appendLinksRebuild();
        this.setPatternText(componentContext.loaded());
    }

    // ids..............................................................................................................

    /**
     * The ID assigned to the container TABLE element.
     */
    private final static String ID = "pattern";

    private final static String ID_PREFIX = ID + "-";

    // @VisibleForTesting
    static String spreadsheetPatternKindId(final SpreadsheetPatternKind kind) {
        return ID_PREFIX +
                CaseKind.SNAKE.change(
                        kind.name(),
                        CaseKind.KEBAB
                ).replace("-pattern", "");
    }
}
