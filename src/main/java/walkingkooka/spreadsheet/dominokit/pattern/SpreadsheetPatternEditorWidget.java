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
import elemental2.dom.Node;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.button.DropdownButton;
import org.dominokit.domino.ui.chips.Chip;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.store.LocalListDataStore;
import org.dominokit.domino.ui.dropdown.DropDownPosition;
import org.dominokit.domino.ui.dropdown.DropdownAction;
import org.dominokit.domino.ui.forms.FieldStyle;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.modals.ModalDialog;
import org.dominokit.domino.ui.style.ColorScheme;
import org.dominokit.domino.ui.style.Elevation;
import org.dominokit.domino.ui.style.StyleType;
import org.dominokit.domino.ui.utils.HasRemoveHandler.RemoveHandler;
import org.jboss.elemento.EventType;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.dom.Anchor;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.dom.Span;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellPatternHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellPatternSaveHistoryToken;
import walkingkooka.spreadsheet.format.SpreadsheetFormatters;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatParserToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatParserTokenKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextAlign;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.dominokit.domino.ui.style.Unit.px;

/**
 * A modal dialog with a text box that allows user entry of a {@link SpreadsheetPattern pattern}.
 * Buttons are available along the bottom that support SAVE, UNDO and CLOSE.
 */
public final class SpreadsheetPatternEditorWidget {

    /**
     * Creates a new {@link SpreadsheetPatternEditorWidget}.
     */
    public static SpreadsheetPatternEditorWidget with(final SpreadsheetPatternEditorWidgetContext context) {
        return new SpreadsheetPatternEditorWidget(context);
    }

    private SpreadsheetPatternEditorWidget(final SpreadsheetPatternEditorWidgetContext context) {
        this.context = context;
        this.patternTextBox = this.patternTextBox();

        this.patternComponentParent = Span.empty();
        this.patternComponentChipPatternTexts = Lists.array();

        this.patternAppendParent = Span.empty();
        this.patternAppendLinks = Lists.array();

        final LocalListDataStore<SpreadsheetPatternEditorWidgetSampleRow> localListDataStore = new LocalListDataStore<>();
        this.sampleDataTable = new DataTable<>(
                this.sampleTableConfig(),
                localListDataStore
        );

        final List<SpreadsheetPatternEditorWidgetSampleRow> sampleRowDataList = Lists.array();
        sampleRowDataList.add(
                SpreadsheetPatternEditorWidgetSampleRow.with(
                        "Text",
                        this::patternText, // patternText
                        "abc123", // value
                        SpreadsheetFormatters.text(
                                SpreadsheetFormatParserToken.text(
                                        Lists.of(
                                                SpreadsheetFormatParserToken.textLiteral(
                                                        "@",
                                                        "@"
                                                )
                                        ),
                                        "@"
                                )
                        ), // default text formatter
                        SpreadsheetPatternEditorWidgetSampleRow.formatPatternSupplier(
                                this::patternText,
                                SpreadsheetPattern::parseTextFormatPattern
                        ),
                        context.spreadsheetFormatterContext()
                )
        );
        localListDataStore.setData(sampleRowDataList);
        this.sampleData = sampleRowDataList;

        this.modalDialog = this.modalDialogCreate(context.title());

        this.patternAppendLinksRebuild();
        this.setPatternText(context.loaded());
    }

    // sample...........................................................................................................

    private TableConfig<SpreadsheetPatternEditorWidgetSampleRow> sampleTableConfig() {
        return new TableConfig<SpreadsheetPatternEditorWidgetSampleRow>()
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
                                d -> Doms.textNode(d.patternText())
                        )
                ).addColumn(
                        columnConfig(
                                "default-format",
                                TextAlign.CENTER,
                                (d) -> Doms.textNode(d.defaultFormattedValue())
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

    private static ColumnConfig<SpreadsheetPatternEditorWidgetSampleRow> columnConfig(final String columnName,
                                                                                      final TextAlign textAlign,
                                                                                      final Function<SpreadsheetPatternEditorWidgetSampleRow, Node> nodeMapper) {
        return ColumnConfig.<SpreadsheetPatternEditorWidgetSampleRow>create(columnName)
                .textAlign(CaseKind.kebabEnumName(textAlign))
                .asHeader()
                .setCellRenderer(cell -> nodeMapper.apply(
                                cell.getTableRow()
                                        .getRecord()
                        )
                );
    }

    private final List<SpreadsheetPatternEditorWidgetSampleRow> sampleData;

    private final DataTable<SpreadsheetPatternEditorWidgetSampleRow> sampleDataTable;

    // patternTextBox...................................................................................................

    /**
     * Creates the pattern text box and installs a value change listener.
     */
    private TextBox patternTextBox() {
        final TextBox textBox = new TextBox();

        textBox.id(ID_PREFIX + "pattern-TextBox");
        textBox.setSpellCheck(false);
        textBox.setFieldStyle(FieldStyle.ROUNDED);
        textBox.setType("text");
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
        final SpreadsheetPatternEditorWidgetContext context = this.context;
        final SpreadsheetPatternKind patternKind = context.patternKind();
        final TextBox patternTextBox = this.patternTextBox;

        context.debug("SpreadsheetPatternEditorWidget.onPatternTextBox " + CharSequences.quoteAndEscape(patternText));

        SpreadsheetPattern pattern = null;
        String errorMessage = null;
        String errorPattern = "";

        final int patternTextLength = patternText.length();
        int last = patternTextLength;

        while (last > 0) {
            final String tryingPatternText = patternText.substring(0, last);
            context.debug("SpreadsheetPatternEditorWidget.onPatternTextBox trying to parse " + CharSequences.quoteAndEscape(tryingPatternText));

            // try parsing...
            try {
                pattern = patternKind.parse(tryingPatternText);
                errorPattern = patternText.substring(last);
                break; // best attempt stop

            } catch (final Exception failed) {
                if(null == errorMessage) {
                    errorMessage = failed.getMessage();
                }

                last--;
                context.debug("SpreadsheetPatternEditorWidget.onPatternTextBox parsing failed " + CharSequences.quoteAndEscape(tryingPatternText), failed);
            }
        }

        context.debug("SpreadsheetPatternEditorWidget.onPatternTextBox " + CharSequences.quoteAndEscape(patternText) + " errorMessage: " + errorMessage + " pattern: " + pattern);

        // clear or update the errors
        patternTextBox.setHelperText(
                CharSequences.nullToEmpty(
                        errorMessage
                ).toString()
        );

        this.patternComponentChipsRebuild(
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

        this.sampleDataTable.load();
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

    // modalDialog......................................................................................................

    /**
     * Creates the modal dialog, loaded with the pattern textbox and some buttons.
     */
    private ModalDialog modalDialogCreate(final String title) {
        final ModalDialog modal = ModalDialog.create(title)
                .large()
                .setAutoClose(true);
        modal.id(ID);

        modal.appendChild(this.sampleDataTable);

        modal.appendChild(this.patternComponentParent);
        modal.appendChild(this.patternAppendParent);

        modal.appendChild(this.patternTextBox);

        modal.appendFooterChild(this.spreadsheetPatternKindDropDownCreate());

        modal.appendFooterChild(Doms.textNode(""));

        modal.appendFooterChild(this.saveButton());
        modal.appendFooterChild(this.undoButton());
        modal.appendFooterChild(this.removeButton());
        modal.appendFooterChild(this.closeButton());

        modal.open();

        return modal;
    }

    // componentPattern.................................................................................................

    /**
     * This is called anytime the pattern text is changed.
     */
    private void patternComponentChipsRebuild(
            final SpreadsheetPattern pattern,
            final String errorPattern) {
        final Span parent = this.patternComponentParent.removeAllChildren();

        final List<String> componentChipPatternTexts = this.patternComponentChipPatternTexts;
        componentChipPatternTexts.clear();

        // pattern will be null when pattern is empty
        if(null == pattern) {
            this.context.debug("SpreadsheetPatternEditorWidget.patternComponentChipsRebuild no chips");
        } else {
            pattern.components(
                    (kind, tokenPatternText) -> componentChipPatternTexts.add(tokenPatternText)
            );

            this.context.debug("SpreadsheetPatternEditorWidget.patternComponentChipsRebuild " +componentChipPatternTexts.size() + " chips ", componentChipPatternTexts);

            if (false == errorPattern.isEmpty()) {
                componentChipPatternTexts.add(errorPattern);
            }

            // now build the chips
            int i = 0;
            for (final String componentChipPatternText : componentChipPatternTexts) {
                parent.append(
                        Chip.create()
                                .setRemovable(true)
                                .setColorScheme(ColorScheme.PINK)
                                .setValue(componentChipPatternText)
                                .addRemoveHandler(
                                        this.patternComponentChipOnRemove(i)
                                )
                );

                i++;
            }
        }
    }

    /**
     * This listener is fired when a chip is removed by clicking the X. It will recompute a new pattern and update the pattern text.
     */
    private RemoveHandler patternComponentChipOnRemove(final int index) {
        return () -> {
            final String removed = this.patternComponentChipPatternTexts.remove(index);
            this.context.debug("SpreadsheetPatternEditorWidget.patternComponentChipOnRemove removed " + CharSequences.quoteAndEscape(removed));
            this.setPatternText(
                    this.patternComponentChipPatternTexts.stream().collect(Collectors.joining())
            );
        };
    }

    /**
     * THe parent holding all the current component pattern chips.
     */
    private final Span patternComponentParent;

    private final List<String> patternComponentChipPatternTexts;

    // appendPattern....................................................................................................

    /**
     * Uses the current {@link SpreadsheetPatternKind} to recreates all links for each and every pattern for each and every {@link SpreadsheetFormatParserTokenKind}.
     * Note a few {@link SpreadsheetFormatParserTokenKind} are skipped for now for technical and other reasons.
     */
    private void patternAppendLinksRebuild() {
        final SpreadsheetPatternEditorWidgetContext context = this.context;
        context.debug("SpreadsheetPatternEditorWidget.patternAppendLinksRebuild");

        final Span parent = this.patternAppendParent.removeAllChildren();
        final List<SpreadsheetPatternEditorWidgetAppendLink> patternAppendLinks = this.patternAppendLinks;
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
                        patternAppendLinks.add(
                                SpreadsheetPatternEditorWidgetAppendLink.with(
                                        formatParserTokenKind,
                                        pattern,
                                        anchor
                                )
                        );
                        parent.append(anchor);
                    }
                    break;
            }
        }
    }

    /**
     * This should be invoked each time the pattern text is updated, and will update the link for each append link.
     * The updated href is not strictly needed and is merely cosmetic.
     */
    private void patternAppendLinksHrefRefresh(final String patternText,
                                               final SpreadsheetPattern pattern) {
        final SpreadsheetPatternEditorWidgetContext context = this.context;

        final SpreadsheetCellPatternHistoryToken historyToken = context.historyToken();

        final List<SpreadsheetPatternEditorWidgetAppendLink> patternAppendLinks = this.patternAppendLinks;
        context.debug("SpreadsheetPatternEditorWidget.patternAppendLinksHrefRefresh " + patternAppendLinks.size() + " links patternText: " + CharSequences.quoteAndEscape(patternText));

        for (final SpreadsheetPatternEditorWidgetAppendLink link : patternAppendLinks) {
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

            context.debug("SpreadsheetPatternEditorWidget.patternAppendLinksHrefRefresh: " + link.pattern + "=" + save);
            link.anchor.setHistoryToken(save);
        }
    }

    /**
     * THe parent holding all the append-pattern links.
     */
    private final Span patternAppendParent;

    /**
     * A cache of a single pattern from a {@link SpreadsheetFormatParserTokenKind} to its matching ANCHOR.
     * This is kept to support updates o the ANCHOR link as the {@link #patternTextBox} changes.
     */
    private final List<SpreadsheetPatternEditorWidgetAppendLink> patternAppendLinks;

    // switch pattern kind..............................................................................................

    /**
     * Creates a drop down holding links for each {@link SpreadsheetPatternKind}. Each link when clicked will update the {@link SpreadsheetPatternKind}.
     */
    private DropdownButton spreadsheetPatternKindDropDownCreate() {
        final SpreadsheetPatternEditorWidgetContext context = this.context;
        final SpreadsheetCellPatternHistoryToken historyToken = context.historyToken();

        final DropdownButton dropdownButton = DropdownButton.create("Pattern")
                .setPosition(DropDownPosition.BOTTOM);

        dropdownButton.style()
                .setMargin(px.of(5))
                .setMinWidth(px.of(120));

        for (final SpreadsheetPatternKind kind : SpreadsheetPatternKind.values()) {
            dropdownButton.appendChild(
                    DropdownAction.create(
                            kind,
                            historyToken.setPatternKind(kind)
                                    .link(
                                            spreadsheetPatternKindId(kind)
                                    ).setTabIndex(0)
                                    .addPushHistoryToken(
                                            context
                                    ).setTextContent(
                                            context.patternKindButtonText(kind)
                                    ).element()
                    )
            );
        }

        return dropdownButton;
    }

    /**
     * Closes or hides the {@link ModalDialog}. THis is necessary when the history token changes and editing a pattern
     * is no longer true.
     */
    public void close() {
        this.modalDialog.close();
    }

    private final ModalDialog modalDialog;

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
        final SpreadsheetPatternEditorWidgetContext context = this.context;
        context.debug("SpreadsheetPatternEditorWidget.onCloseButtonClick");
        context.close();
        this.modalDialog.close();
    }

    /**
     * When clicked the SAVE button invokes {@link SpreadsheetPatternEditorWidgetContext#save(String)}.
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
        final SpreadsheetPatternEditorWidgetContext context = this.context;

        try {
            context.patternKind().parse(patternText);
            context.debug("SpreadsheetPatternEditorWidget.onSaveButtonClick " + CharSequences.quoteAndEscape(patternText));
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
        final SpreadsheetPatternEditorWidgetContext context = this.context;

        final String patternText = context.loaded();
        context.debug("SpreadsheetPatternEditorWidget.onUndoButtonClick " + CharSequences.quoteAndEscape(patternText));

        this.setPatternText(patternText);
    }

    /**
     * When clicked the REMOVE button invokes {@link SpreadsheetPatternEditorWidgetContext#remove()}.
     */
    private Button removeButton() {
        return this.button(
                "Remove",
                StyleType.DANGER,
                this::onRemoveButtonClick
        );
    }

    private void onRemoveButtonClick(final Event event) {
        final SpreadsheetPatternEditorWidgetContext context = this.context;

        context.debug("SpreadsheetPatternEditorWidget.onRemoveButtonClick");
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
        button.setButtonType(type);
        button.elevate(Elevation.LEVEL_1);

        button.addEventListener(
                EventType.click,
                listener
        );

        return button;
    }

    /**
     * Refreshes the widget, typically done when the {@link SpreadsheetPatternKind} changes etc.
     */
    public void refresh() {
        final SpreadsheetPatternEditorWidgetContext context = this.context;

        context.debug("SpreadsheetPatternEditorWidget.refresh");

        this.modalDialog.setTitle(context.title());
        this.patternAppendLinksRebuild();
        this.setPatternText(context.loaded());
    }

    private final SpreadsheetPatternEditorWidgetContext context;

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
