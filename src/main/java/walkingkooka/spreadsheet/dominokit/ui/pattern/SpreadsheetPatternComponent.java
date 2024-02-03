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

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import elemental2.dom.Event;
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
import org.dominokit.domino.ui.menu.Menu;
import org.dominokit.domino.ui.style.StyleType;
import org.dominokit.domino.ui.tabs.Tab;
import org.dominokit.domino.ui.tabs.TabsPanel;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.history.CloseableHistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.Anchor;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.textbox.SpreadsheetTextBox;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatParserTokenKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextAlign;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A modal dialog with a text box that allows user entry of a {@link SpreadsheetPattern pattern}.
 * Buttons are available along the bottom that support SAVE, UNDO and CLOSE.
 */
public abstract class SpreadsheetPatternComponent implements SpreadsheetDialogComponentLifecycle,
        NopFetcherWatcher,
        SpreadsheetDeltaFetcherWatcher,
        SpreadsheetMetadataFetcherWatcher {

    /**
     * Creates a new {@link SpreadsheetPatternComponentFormat}.
     */
    public static SpreadsheetPatternComponent format(final SpreadsheetPatternComponentContext context) {
        return SpreadsheetPatternComponentFormat.with(context);
    }

    /**
     * Creates a new {@link SpreadsheetPatternComponentParse}.
     */
    public static SpreadsheetPatternComponent parse(final SpreadsheetPatternComponentContext context) {
        return SpreadsheetPatternComponentParse.with(context);
    }

    SpreadsheetPatternComponent(final SpreadsheetPatternComponentContext context) {
        this.context = context;
        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetDeltaWatcher(this);
        context.addSpreadsheetMetadataWatcher(this);

        this.patternKindTabs = this.patternKindTabs();
        this.patternKindTabsPanel = this.patternKindTabsPanel();

        this.patternTextBox = this.patternTextBox();

        this.patternComponentParent = Card.create();
        this.patternComponentKinds = Lists.array();
        this.patternComponentTexts = Lists.array();

        this.appendParent = Card.create();
        this.patternAppendLinks = Lists.array();

        final LocalListDataStore<SpreadsheetPatternComponentSampleRow> localListDataStore = new LocalListDataStore<>();
        this.sampleDataTable = new DataTable<>(
                this.sampleTableConfig(),
                localListDataStore
        );
        this.sampleDataTableDataStore = localListDataStore;

        this.dialog = this.dialogCreate();
    }

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, loaded with the pattern textbox and some buttons.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetDialogComponent dialog = SpreadsheetDialogComponent.create(this.context);
        dialog.id(ID);

        dialog.appendChild(this.patternKindTabsPanel);

        dialog.appendChild(
                Card.create()
                        .appendChild(
                                this.sampleDataTable
                        )
        );

        this.sampleDataTable.headerElement().hide();

        dialog.appendChild(this.patternComponentParent);
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

    @Override
    public final SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    @Override
    public final CloseableHistoryTokenContext closeableHistoryTokenContext() {
        return this.context;
    }

    private final SpreadsheetPatternComponentContext context;

    @Override
    public final String idPrefix() {
        return ID_PREFIX;
    }

    // tabs............................................................................................................

    /**
     * Factory that creates a TAB for each {@link SpreadsheetPatternKind}. Note the anchors links are not set and need to be
     * {@link #patternKindTabsRefresh()}.
     */
    private Tab[] patternKindTabs() {
        final SpreadsheetPatternComponentContext context = this.context;

        final SpreadsheetPatternKind[] kinds = this.spreadsheetPatternKinds();
        final Tab[] tabs = new Tab[kinds.length];

        int i = 0;
        for (final SpreadsheetPatternKind kind : kinds) {
            final Tab tab = Tab.create(
                    tabTitle(kind)
            );

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

    /**
     * Returns the text that will appear on a tab that when clicked switches to the given {@link SpreadsheetPatternKind}.
     * <pre>
     * SpreadsheetPatternKind.TEXT_FORMAT -> Text Format
     * </pre>
     */
    static String tabTitle(final SpreadsheetPatternKind kind) {
        return CaseKind.SNAKE.change(
                kind.name()
                        .replace("FORMAT_PATTERN", "")
                        .replace("PARSE_PATTERN", ""),
                CaseKind.TITLE
        ).trim();
    }

    private final Tab[] patternKindTabs;

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
        final SpreadsheetPatternComponentContext context = this.context;
        final SpreadsheetPatternKind kind = context.patternKind();

        int i = 0;
        final Tab[] tabs = this.patternKindTabs;
        for (final SpreadsheetPatternKind possible : this.spreadsheetPatternKinds()) {
            final Tab tab = tabs[i++];
            final Anchor anchor = Anchor.with(
                    (HTMLAnchorElement)
                            tab.getTab()
                                    .element()
                                    .firstElementChild
            ).setId(spreadsheetPatternKindId(possible));

            final boolean match = kind.equals(possible);
            anchor.setDisabled(match);

            if (match) {
                tab.activate(true); // true=silent
            } else {
                tab.deActivate(true); // true=silent

                final HistoryToken historyToken = context.historyToken();
                final HistoryToken historyTokenWithPatternKind = historyToken.setPatternKind(
                        Optional.of(possible)
                );
                anchor.setHistoryToken(
                        Optional.of(historyTokenWithPatternKind)
                );

                context.debug(this.getClass().getSimpleName() + ".patternKindTabsRefresh " + historyTokenWithPatternKind);
            }
        }
    }

    // sample...........................................................................................................

    private TableConfig<SpreadsheetPatternComponentSampleRow> sampleTableConfig() {
        return new TableConfig<SpreadsheetPatternComponentSampleRow>()
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
                                d -> this.patternAnchor(
                                        d.label(),
                                        d.pattern()
                                                .map(SpreadsheetPattern::text)
                                                .orElse("")
                                )
                        )
                ).addColumn(
                        columnConfig(
                                "default-format",
                                TextAlign.CENTER,
                                (d) -> Doms.node(
                                        d.defaultFormattedValue()
                                )
                        )
                ).addColumn(
                        columnConfig(
                                "formatted",
                                TextAlign.CENTER,
                                d -> Doms.node(
                                        d.patternFormattedValue()
                                )
                        )
                );
    }

    private static ColumnConfig<SpreadsheetPatternComponentSampleRow> columnConfig(final String columnName,
                                                                                   final TextAlign textAlign,
                                                                                   final Function<SpreadsheetPatternComponentSampleRow, Node> nodeMapper) {
        return ColumnConfig.<SpreadsheetPatternComponentSampleRow>create(columnName)
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
    private HTMLAnchorElement patternAnchor(final String label,
                                            final String patternText) {
        return Anchor.empty()
                .setHref(
                        Url.EMPTY_RELATIVE_URL.setFragment(
                                this.context.historyToken()
                                        .setSave(patternText)
                                        .urlFragment()
                        )
                ).setTextContent(patternText)
                .setId(
                        ID_PREFIX +
                                label.toLowerCase() +
                                SpreadsheetIds.LINK
                ).addClickAndKeydownEnterListener(
                        e ->
                        {
                            e.preventDefault();
                            this.setPatternText(patternText);
                        }).element();
    }

    private void sampleDataPrepare() {
        final SpreadsheetPatternComponentContext context = this.context;
        final SpreadsheetPatternKind patternKind = context.patternKind();

        this.sampleDataTableDataStore.setData(
                SpreadsheetPatternComponentSampleRowProvider.spreadsheetPatternKind(patternKind)
                        .apply(
                                this.patternText(),
                                SpreadsheetPatternComponentSampleRowProviderContexts.basic(
                                        patternKind,
                                        context.spreadsheetFormatterContext(),
                                        context
                                )
                        )
        );
        this.sampleDataTable.load();
    }

    private final DataTable<SpreadsheetPatternComponentSampleRow> sampleDataTable;

    private final LocalListDataStore<SpreadsheetPatternComponentSampleRow> sampleDataTableDataStore;

    // componentChips...................................................................................................

    /**
     * This is called anytime the pattern text is changed.
     */
    private void patternComponentRebuild(final SpreadsheetPattern pattern,
                                         final String errorPattern) {
        final Card parent = this.patternComponentParent.clearElement();
        final SpreadsheetPatternComponentContext context = this.context;

        final List<SpreadsheetFormatParserTokenKind> patternComponentKinds = this.patternComponentKinds;
        patternComponentKinds.clear();

        final List<String> patternComponentTexts = this.patternComponentTexts;
        patternComponentTexts.clear();

        // pattern will be null when pattern is empty
        if (null == pattern) {
            context.debug(this.getClass().getSimpleName() + ".patternComponentRebuild no components");
        } else {
            pattern.components(
                    (kind, tokenPatternText) -> {
                        patternComponentKinds.add(kind);
                        patternComponentTexts.add(tokenPatternText);
                    }
            );

            context.debug(this.getClass().getSimpleName() + ".patternComponentRebuild " + patternComponentTexts.size() + " text ", patternComponentTexts);

            if (false == errorPattern.isEmpty()) {
                patternComponentTexts.add(errorPattern);
            }

            // now build the chips
            int i = 0;

            for (final String patternComponentText : patternComponentTexts) {
                final Chip chip = Chip.create(patternComponentText)
                        .setId(
                                ID_PREFIX +
                                        i +
                                        SpreadsheetIds.CHIP
                        ).setRemovable(true)
                        .addOnRemoveListener(this.patternComponentOnRemove(i))
                        .setMarginRight("5px");

                final Set<String> alternatives = patternComponentKinds.get(i)
                        .alternatives();

                if (false == alternatives.isEmpty()) {
                    final HistoryToken historyToken = context.historyToken();
                    final Menu<Void> menu = Menu.create();
                    SpreadsheetContextMenu contextMenu = SpreadsheetContextMenu.menu(
                            menu,
                            context
                    );

                    final int ii = i;
                    int j = 0;
                    int selectedItem = -1;
                    for (final String alternative : alternatives) {

                        final String newPattern = IntStream.range(0, patternComponentTexts.size())
                                .mapToObj(k -> ii == k ? alternative : patternComponentTexts.get(k))
                                .collect(Collectors.joining());

                        contextMenu = contextMenu.item(
                                ID_PREFIX + "alternative-" + j,
                                alternative,
                                historyToken.setSave(newPattern)
                        );

                        if (alternative.equalsIgnoreCase(patternComponentText)) {
                            selectedItem = j;
                        }

                        j++;
                    }

                    chip.setDropMenu(menu);
                    menu.selectAt(
                            selectedItem,
                            true // silent = dont fire selection events
                    );
                }
                parent.appendChild(chip);

                i++;
            }
        }
    }

    /**
     * This listener is fired when a chip is removed by clicking the X. It will recompute a new pattern and update the pattern text.
     */
    private Consumer<Chip> patternComponentOnRemove(final int index) {
        return (chip) -> {
            final String removed = this.patternComponentTexts.remove(index);
            this.context.debug(this.getClass().getSimpleName() + ".patternComponentOnRemove removed " + CharSequences.quoteAndEscape(removed));
            this.setPatternText(
                    this.patternComponentTexts.stream().collect(Collectors.joining())
            );
        };
    }

    /**
     * THe parent holding all the current ui pattern chips.
     */
    private final Card patternComponentParent;

    private final List<SpreadsheetFormatParserTokenKind> patternComponentKinds;

    private final List<String> patternComponentTexts;

    // patternAppendLinks......................................................................................................

    /**
     * Uses the current {@link SpreadsheetPatternKind} to recreates all links for each and every pattern for each and every {@link SpreadsheetFormatParserTokenKind}.
     * Note a few {@link SpreadsheetFormatParserTokenKind} are skipped for now for technical and other reasons.
     */
    private void patternAppendLinksRebuild() {
        final SpreadsheetPatternComponentContext context = this.context;
        context.debug(this.getClass().getSimpleName() + ".patternAppendLinksRebuild");

        final Card parent = this.appendParent.clearElement();
        final List<SpreadsheetPatternComponentPatternAppendLink> patternAppendLinks = this.patternAppendLinks;
        patternAppendLinks.clear();

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
                                .setId(
                                        ID_PREFIX +
                                                "append-" +
                                                pattern +
                                                SpreadsheetIds.LINK
                                ).setTextContent(pattern);
                        anchor.addClickAndKeydownEnterListener(
                                (e) -> {
                                    e.preventDefault();
                                    this.setPatternText(
                                            anchor.historyToken()
                                                    .map(t -> t.pattern()
                                                            .map(SpreadsheetPattern::text)
                                                            .orElse("")
                                                    ).orElse("")
                                    );
                                }
                        );
                        patternAppendLinks.add(
                                SpreadsheetPatternComponentPatternAppendLink.with(
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
    private void patternAppendLinksHrefRefresh(final String patternText,
                                        final SpreadsheetPattern pattern) {
        final SpreadsheetPatternComponentContext context = this.context;

        final HistoryToken historyToken = context.historyToken();

        final List<SpreadsheetPatternComponentPatternAppendLink> patternAppendLinks = this.patternAppendLinks;
        context.debug(this.getClass().getSimpleName() + ".patternAppendLinksHrefRefresh " + patternAppendLinks.size() + " links patternText: " + CharSequences.quoteAndEscape(patternText));

        for (final SpreadsheetPatternComponentPatternAppendLink link : patternAppendLinks) {
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

            context.debug(this.getClass().getSimpleName() + ".patternAppendLinksHrefRefresh: " + link.pattern + "=" + save);
            link.anchor.setHistoryToken(
                    Optional.ofNullable(save)
            );
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
    private final List<SpreadsheetPatternComponentPatternAppendLink> patternAppendLinks;

    // patternTextBox...................................................................................................

    /**
     * Creates the pattern text box and installs a value change listener.
     */
    private SpreadsheetTextBox patternTextBox() {
        return SpreadsheetTextBox.empty()
                .setId(ID_PREFIX + "TextBox")
                .disableSpellcheck()
                .clearIcon()
                .addKeydownListener(
                        (e) -> this.onPatternTextBox(this.patternText())
                );
    }

    /**
     * Tries to parse the text box text into a {@link SpreadsheetPattern}.
     * If that fails an error message will be displayed and the SAVE button disabled.
     */
    private void onPatternTextBox(final String patternText) {
        final SpreadsheetPatternComponentContext context = this.context;
        final SpreadsheetPatternKind patternKind = context.patternKind();
        final SpreadsheetTextBox patternTextBox = this.patternTextBox;

        context.debug(this.getClass().getSimpleName() + ".onPatternTextBox " + CharSequences.quoteAndEscape(patternText));

        SpreadsheetPattern pattern = null;
        String errorMessage = null;
        String errorPattern = "";

        final int patternTextLength = patternText.length();
        int last = patternTextLength;

        while (last > 0) {
            final String tryingPatternText = patternText.substring(0, last);
            context.debug(this.getClass().getSimpleName() + ".onPatternTextBox trying to parse " + CharSequences.quoteAndEscape(tryingPatternText));

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
                context.debug(this.getClass().getSimpleName() + ".onPatternTextBox parsing failed " + CharSequences.quoteAndEscape(tryingPatternText), failed);
            }
        }

        context.debug(this.getClass().getSimpleName() + ".onPatternTextBox " + CharSequences.quoteAndEscape(patternText) + " errorMessage: " + errorMessage + " pattern: " + pattern);

        // clear or update the errors
        patternTextBox.setHelperText(
                Optional.ofNullable(errorMessage)
        );

        this.patternComponentRebuild(
                pattern,
                errorPattern
        );

        this.patternAppendLinksHrefRefresh(
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
        return this.patternTextBox.value()
                .get();
    }

    private void setPatternText(final String patternText) {
        this.patternTextBox.setValue(
                Optional.of(patternText)
        );
        this.onPatternTextBox(patternText);
    }

    /**
     * The {@link SpreadsheetTextBox} that holds the pattern in text form.
     */
    private final SpreadsheetTextBox patternTextBox;

    // buttons..........................................................................................................

    /**
     * When clicked the SAVE button invokes {@link SpreadsheetPatternComponentContext#save(Object)}.
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
        final SpreadsheetPatternComponentContext context = this.context;

        try {
            final SpreadsheetPattern pattern = context.patternKind().parse(patternText);
            context.debug(this.getClass().getSimpleName() + ".onSaveButtonClick " + CharSequences.quoteAndEscape(patternText));
            context.save(pattern);
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
        final SpreadsheetPatternComponentContext context = this.context;

        final String patternText = context.loaded();
        context.debug(this.getClass().getSimpleName() + ".onUndoButtonClick " + CharSequences.quoteAndEscape(patternText));

        this.setPatternText(patternText);
    }

    /**
     * When clicked the REMOVE button invokes {@link SpreadsheetPatternComponentContext#delete()}.
     */
    private Button removeButton() {
        return this.button(
                "Remove",
                StyleType.DANGER,
                this::onRemoveButtonClick
        );
    }

    private void onRemoveButtonClick(final Event event) {
        final SpreadsheetPatternComponentContext context = this.context;

        context.debug(this.getClass().getSimpleName() + ".onRemoveButtonClick");
        context.delete();
    }

    // SpreadsheetDeltaFetcherWatcher..........................................................................................

    @Override
    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                   final AppContext context) {
        this.refreshIfOpen(context);
    }

    // SpreadsheetMetadataFetcherWatcher..........................................................................................

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        this.refreshIfOpen(context);
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
                this.patternTextBox::focus
        );
    }

    /**
     * Refreshes the widget, typically done when the {@link SpreadsheetPatternKind} changes etc.
     */
    @Override
    public void refresh(final AppContext context) {
        final SpreadsheetPatternComponentContext componentContext = this.context;

        this.dialog.setTitle(
                title(
                        componentContext.patternKind()
                )
        );
        this.patternKindTabsRefresh();
        this.patternAppendLinksRebuild();
        this.setPatternText(componentContext.loaded());
    }

    abstract SpreadsheetPatternKind[] spreadsheetPatternKinds();

    // Date/Time format
    // Text format
    static String title(final SpreadsheetPatternKind kind) {
        return CaseKind.SNAKE.change(
                kind.name(),
                CaseKind.TITLE
        ).replace("Pattern", "pattern");
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
