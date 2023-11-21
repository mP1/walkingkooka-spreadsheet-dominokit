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

package walkingkooka.spreadsheet.dominokit.label;

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.dialogs.Dialog;
import org.dominokit.domino.ui.dialogs.DialogSize;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.layout.NavBar;
import org.dominokit.domino.ui.style.Elevation;
import org.dominokit.domino.ui.style.StyleType;
import org.dominokit.domino.ui.utils.ElementsFactory;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.component.ComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcherWatcher;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * A model dialog with several textboxes that allow creation, editing, saving and deletion of a {@link SpreadsheetLabelMapping}.
 */
public final class SpreadsheetLabelMappingEditorComponent implements ComponentLifecycle,
        NopFetcherWatcher,
        SpreadsheetLabelMappingFetcherWatcher {

    /**
     * Creates a new {@link SpreadsheetLabelMappingEditorComponent}.
     */
    public static SpreadsheetLabelMappingEditorComponent with(final SpreadsheetLabelMappingEditorComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetLabelMappingEditorComponent(context);
    }

    private SpreadsheetLabelMappingEditorComponent(final SpreadsheetLabelMappingEditorComponentContext context) {
        this.context = context;

        this.labelNameTextBox = this.labelNameTextBox();
        this.referenceTextBox = this.referenceTextBox();

        this.saveButton = this.saveButton();
        this.deleteButton = this.deleteButton();

        this.dialogNavBar = this.dialogNavBar();
        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
        context.addLabelMappingWatcher(this);
    }

    // dialogNavBar.....................................................................................................

    private NavBar dialogNavBar() {
        return NavBar.create() //
                .appendChild(
                        PostfixAddOn.of(
                                SpreadsheetIcons.close()
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
                //.setType(DialogType.DEFAULT) // large
                .setAutoClose(true)
                .setModal(true)
                .setStretchWidth(DialogSize.LARGE)
                .setStretchHeight(DialogSize.LARGE)
                .withHeader(
                        (d, header) ->
                                header.appendChild(this.dialogNavBar)
                );
        dialog.id(ID);

        dialog.appendChild(this.labelNameTextBox);
        dialog.appendChild(this.referenceTextBox);

        dialog.appendChild(
                ElementsFactory.elements.div()
                        .appendChild(this.saveButton())
                        .appendChild(this.undoButton())
                        .appendChild(this.deleteButton())
                        .appendChild(this.closeButton())
        );

        return dialog;
    }

    private final Dialog dialog;

    private final SpreadsheetLabelMappingEditorComponentContext context;

    // labelNameTextBox...................................................................................................

    /**
     * Creates the label text box and installs a value change listener.
     */
    private TextBox labelNameTextBox() {
        final TextBox textBox = TextBox.create("Label");

        textBox.id(ID_PREFIX + "label-TextBox");
        textBox.element().spellcheck = false;
        textBox.element().type = "text";
        textBox.apply(
                self -> self.appendChild(
                        PostfixAddOn.of(
                                SpreadsheetIcons.close()
                                        .clickable()
                                        .addClickListener(event -> this.setLabelName(""))
                        )
                )
        );

        textBox.addEventListener(
                EventType.input,
                (e) -> this.onLabelNameTextBox(this.labelNameTextBox.getValue())
        );
        return textBox;
    }

    private void onLabelNameTextBox(final String text) {
        this.refreshSaveAndDeleteButtons(
                text,
                this.referenceTextBox.getValue()
        );
    }

    private void setLabelName(final String text) {
        this.labelNameTextBox.setValue(text);
        this.onLabelNameTextBox(text);
    }

    /**
     * The {@link TextBox} that holds the edited {@link SpreadsheetLabelName}.
     */
    private final TextBox labelNameTextBox;

    // referenceTextBox...................................................................................................

    /**
     * Creates the {@link walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference} text box and installs a value change listener.
     */
    private TextBox referenceTextBox() {
        final TextBox textBox = TextBox.create("Cell, cell range or Label");

        textBox.id(ID_PREFIX + "label-TextBox");
        textBox.element().spellcheck = false;
        textBox.element().type = "text";
        textBox.apply(
                self -> self.appendChild(
                        PostfixAddOn.of(
                                SpreadsheetIcons.close()
                                        .clickable()
                                        .addClickListener(event -> this.setReference(""))
                        )
                )
        );

        textBox.addEventListener(
                EventType.input,
                (e) -> this.onReferenceTextBox(this.referenceTextBox.getValue())
        );
        return textBox;
    }

    private void onReferenceTextBox(final String text) {
        this.refreshSaveAndDeleteButtons(
                this.labelNameTextBox.getValue(),
                text
        );
    }

    private void setReference(final String text) {
        this.referenceTextBox.setValue(text);
        this.onReferenceTextBox(text);
    }

    /**
     * The {@link TextBox} that holds the edited {@link SpreadsheetExpressionReference}.
     */
    private final TextBox referenceTextBox;

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
        final SpreadsheetLabelMappingEditorComponentContext context = this.context;
        context.debug("SpreadsheetLabelMappingEditorComponent.onCloseButtonClick");
        context.close();
    }

    /**
     * When clicked the SAVE button invokes {@link SpreadsheetLabelMappingEditorComponentContext#save(Object)}}.
     */
    private Button saveButton() {
        return this.button(
                "Save",
                StyleType.DEFAULT,
                this::onSaveButtonClick
        );
    }

    private void onSaveButtonClick(final Event event) {
        final SpreadsheetLabelMappingEditorComponentContext context = this.context;
        final String labelName = this.labelNameTextBox.getValue();
        final String reference = this.referenceTextBox.getValue();
        context.debug("SpreadsheetLabelMappingEditorComponent.onSaveButtonClick labelName: " + CharSequences.quoteAndEscape(labelName) + " reference: " + CharSequences.quoteAndEscape(reference));

        try {
            final SpreadsheetLabelMapping mapping = SpreadsheetSelection.labelName(labelName)
                    .mapping(SpreadsheetExpressionReference.parseCellOrCellRange(reference));
            context.save(mapping);
        } catch (final Exception cause) {
            context.error(cause.getMessage());
        }
    }

    private final Button saveButton;

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
        final SpreadsheetLabelMappingEditorComponentContext context = this.context;

        final String labelName = context.label()
                .map(SpreadsheetLabelName::value)
                .orElse("");
        final String referenceText = this.loadedReferenceText;
        context.debug("SpreadsheetLabelMappingEditorComponent.onUndoButtonClick " + CharSequences.quoteAndEscape(labelName) + " " + CharSequences.quoteAndEscape(referenceText));

        this.setLabelName(labelName);
        this.setReference(referenceText);
    }

    private String loadedReferenceText;

    /**
     * When clicked the REMOVE button invokes {@link SpreadsheetLabelMappingEditorComponentContext#delete()} ()}.
     */
    private Button deleteButton() {
        return this.button(
                "Delete",
                StyleType.DANGER,
                this::onDeleteButtonClick
        );
    }

    private void onDeleteButtonClick(final Event event) {
        final SpreadsheetLabelMappingEditorComponentContext context = this.context;

        context.debug("SpreadsheetLabelMappingEditorComponent.onDeleteButtonClick");
        context.delete();
    }

    private final Button deleteButton;

    /**
     * Creates one of the modal action buttons that appear at the bottom of the modal dialog.
     */
    private Button button(final String text,
                          final StyleType type,
                          final EventListener listener) {
        final Button button = new Button(text);

        button.id(ID_PREFIX + text.toLowerCase() + SpreadsheetIds.BUTTON);
        button.addCss("dui-" + type.getStyle());
        button.elevate(Elevation.LEVEL_1);

        button.addEventListener(
                EventType.click,
                listener
        );

        return button;
    }

    /**
     * Refreshes or enable/disables the SAVE and DELETE buttons.
     */
    private void refreshSaveAndDeleteButtons(final String labelName,
                                             final String reference) {
        boolean deleteDisabled = true;
        boolean saveDisabled = true;

        String labelError = "";
        String referenceError = "";

        try {
            SpreadsheetSelection.labelName(labelName);
            deleteDisabled = false;
        } catch (final RuntimeException fail) {
            labelError = fail.getMessage();
        }

        try {
            SpreadsheetSelection.parseCellRangeOrLabel(reference);

            if(false == deleteDisabled) {
                saveDisabled = false;
            }
        } catch (final RuntimeException badReference) {
            saveDisabled = true;
            referenceError = badReference.getMessage();
        }

        this.labelNameTextBox.setHelperText(labelError);
        this.referenceTextBox.setHelperText(referenceError);

        this.deleteButton.setDisabled(deleteDisabled);
        this.saveButton.setDisabled(saveDisabled);
    }

    // ComponentLifecycle...............................................................................................

    // save should not open or close the dialog.
    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return false == this.isMatch(token) && token instanceof SpreadsheetLabelMappingHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetLabelMappingSelectHistoryToken;
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
        this.dialog.open();

        final SpreadsheetLabelMappingSelectHistoryToken token = context.historyToken()
                .cast(SpreadsheetLabelMappingSelectHistoryToken.class);
        final Optional<SpreadsheetLabelName> maybeLabelName = token.labelName();
        if (maybeLabelName.isPresent()) {
            final SpreadsheetLabelName labelName = maybeLabelName.get();
            final String text = labelName.text();
            this.setLabelName(text);
            try {
                this.context.loadLabel(labelName);
            } catch (final RuntimeException ignore) {
                this.context.error("Unable to load label " + CharSequences.quoteAndEscape(text));
            }
        }


        context.giveFocus(
                this.labelNameTextBox::focus
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
     * Refreshes the widget, typically done when the label within the history token changes.
     */
    @Override
    public void refresh(final AppContext context) {
        this.dialogNavBar.setTitle("Label");

        final SpreadsheetLabelMappingSelectHistoryToken token = context.historyToken()
                .cast(SpreadsheetLabelMappingSelectHistoryToken.class);
        this.setLabelName(
                token.labelName()
                        .map(SpreadsheetLabelName::value)
                        .orElse("")
        );
    }

    // SpreadsheetLabelMappingFetcherWatcher...................................................................................

    @Override
    public void onSpreadsheetLabelMapping(final Optional<SpreadsheetLabelMapping> maybeMapping,
                                          final AppContext context) {
        if(maybeMapping.isPresent()) {
            final SpreadsheetLabelMapping mapping = maybeMapping.get();

            // same label update reference
            if (this.labelNameTextBox.getValue().equals(mapping.label().value())) {
                final String referenceText = mapping.reference()
                        .text();
                this.setReference(referenceText);
                this.loadedReferenceText = referenceText;
            }
        }
    }

    // UI...............................................................................................................

    private final static String ID = "labelMapping";

    private final static String ID_PREFIX = ID + "-";

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
