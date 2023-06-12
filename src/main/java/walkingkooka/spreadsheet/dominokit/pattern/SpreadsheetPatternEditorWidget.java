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

import elemental2.dom.DomGlobal;
import elemental2.dom.Event;
import elemental2.dom.EventListener;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.button.DropdownButton;
import org.dominokit.domino.ui.chips.Chip;
import org.dominokit.domino.ui.dropdown.DropDownPosition;
import org.dominokit.domino.ui.dropdown.DropdownAction;
import org.dominokit.domino.ui.forms.FieldStyle;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.modals.ModalDialog;
import org.dominokit.domino.ui.notifications.Notification;
import org.dominokit.domino.ui.style.ColorScheme;
import org.dominokit.domino.ui.style.Elevation;
import org.dominokit.domino.ui.style.StyleType;
import org.dominokit.domino.ui.utils.HasRemoveHandler.RemoveHandler;
import org.jboss.elemento.EventType;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.spreadsheet.dominokit.dom.Anchor;
import walkingkooka.spreadsheet.dominokit.dom.Span;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellPatternHistoryToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatParserTokenKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Map;
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
        this.patternAppendToAnchor = Maps.ordered();

        this.modalDialog = this.modalDialogCreate(context.title());

        this.setPatternText(context.loaded());
        this.refresh();
    }

    /**
     * The {@link TextBox} that holds the pattern in text form.
     */
    private final TextBox patternTextBox;

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
                this::onPatternTextBox
        );
        return textBox;
    }

    /**
     * Tries to parse the text box text into a {@link SpreadsheetPattern}.
     * If that fails an error message will be displayed and the SAVE button disabled.
     */
    private void onPatternTextBox(final Event event) {
        // update UI here...
        this.context.debug("SpreadsheetPatternEditorWidget.onPatternTextBox " + this.patternText());
        this.patternAppendLinksHrefRefresh();
        this.patternComponentChipsRebuild();
    }

    /**
     * Retrieves the current pattern.
     */
    private String patternText() {
        return this.patternTextBox.getValue();
    }

    private void setPatternText(final String pattern) {
        this.patternTextBox.setValue(pattern);

        this.patternComponentChipsRebuild();
    }

    /**
     * Creates the modal dialog, loaded with the pattern textbox and some buttons.
     */
    private ModalDialog modalDialogCreate(final String title) {
        final ModalDialog modal = ModalDialog.create(title)
                .large()
                .setAutoClose(true);
        modal.id(ID);

        modal.appendChild(this.patternComponentParent);
        modal.appendChild(this.patternAppendParent);

        modal.appendChild(this.patternTextBox);

        modal.appendFooterChild(this.spreadsheetPatternKindDropDownCreate());

        modal.appendFooterChild(DomGlobal.document.createTextNode(" "));

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
    private void patternComponentChipsRebuild() {
        final Span parent = this.patternComponentParent.removeAllChildren();

        final SpreadsheetPatternEditorWidgetContext context = this.context;
        final SpreadsheetPatternKind patternKind = context.patternKind();
        final List<String> componentChipPatternTexts = this.patternComponentChipPatternTexts;
        componentChipPatternTexts.clear();

        final String patternText = this.patternText();
        final int patternTextLength = patternText.length();
        int last = patternTextLength;

        while (last > 0) {
            final String tryingPatternText = patternText.substring(0, last);
            context.debug("SpreadsheetPatternEditorWidget.patternComponentChipsRebuild trying to parse " + CharSequences.quoteAndEscape(tryingPatternText));

            // try parsing...
            try {
                final SpreadsheetPattern pattern = patternKind.parse(tryingPatternText);

                pattern.forEachComponent(
                        (kind, tokenPatternText) -> {
                            componentChipPatternTexts.add(tokenPatternText);
                        }
                );

                if (last < patternTextLength) {
                    componentChipPatternTexts.add(
                            patternText.substring(last)
                    );
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
                break; // best attempt stop

            } catch (final Exception failed) {
                last--;
                context.debug("SpreadsheetPatternEditorWidget.patternComponentChipsRebuild parsing failed " + CharSequences.quoteAndEscape(tryingPatternText), failed);
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
        final Span parent = this.patternAppendParent.removeAllChildren();
        final Map<String, Anchor> appendPatternToAnchor = this.patternAppendToAnchor;
        appendPatternToAnchor.clear();

        final SpreadsheetPatternEditorWidgetContext context = this.context;

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
                                .setTextContent(pattern)
                                .addClick(
                                        (e) -> {
                                            e.preventDefault();
                                            this.setPatternText(this.patternText() + pattern);
                                        }
                                );
                        appendPatternToAnchor.put(pattern, anchor);
                        parent.append(anchor);
                    }
                    break;
            }
        }

        this.patternAppendLinksHrefRefresh();
    }

    /**
     * This should be invoked each time the pattern text is updated, and will update the link for each append link.
     * The updated href is not strictly needed and is merely cosmetic.
     */
    private void patternAppendLinksHrefRefresh() {
        final SpreadsheetCellPatternHistoryToken historyToken = this.context.historyToken();
        final String patternText = this.patternText();

        this.patternAppendToAnchor.forEach(
                (p, a) -> {
                    HistoryToken save;
                    try {
                        save = historyToken.setSave(
                                patternText + p
                        );
                    } catch (final RuntimeException invalidPattern) {
                        save = null;
                    }
                    a.setHistoryToken(save);
                }
        );
    }

    /**
     * THe parent holding all the append-pattern links.
     */
    private final Span patternAppendParent;

    /**
     * A cache of a single pattern from a {@link SpreadsheetFormatParserTokenKind} to its matching ANCHOR.
     * This is kept to support updates o the ANCHOR link as the {@link #patternTextBox} changes.
     */
    private final Map<String, Anchor> patternAppendToAnchor;

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
            this.error(cause.getMessage());
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
     * Display an error message.
     */
    private void error(final String errorMessage) {
        Notification.create(errorMessage)
                .setPosition(Notification.TOP_CENTER)
                .show();
    }

    /**
     * Refreshes the widget, typically done when the {@link SpreadsheetPatternKind} changes etc.
     */
    public void refresh() {
        final SpreadsheetPatternEditorWidgetContext context = this.context;

        context.debug("SpreadsheetPatternEditorWidget.refresh");

        this.modalDialog.setTitle(context.title());
        this.setPatternText(context.loaded());
        this.patternAppendLinksRebuild();
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
