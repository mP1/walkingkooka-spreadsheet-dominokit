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

package walkingkooka.spreadsheet.dominokit.ui.labelmapping;

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.style.Elevation;
import org.dominokit.domino.ui.style.StyleType;
import org.dominokit.domino.ui.utils.ElementsFactory;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import walkingkooka.Context;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetLabelComponent;
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
public final class SpreadsheetLabelMappingComponent implements SpreadsheetDialogComponentLifecycle,
        NopFetcherWatcher,
        SpreadsheetLabelMappingFetcherWatcher {

    /**
     * Creates a new {@link SpreadsheetLabelMappingComponent}.
     */
    public static SpreadsheetLabelMappingComponent with(final SpreadsheetLabelMappingComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetLabelMappingComponent(context);
    }

    private SpreadsheetLabelMappingComponent(final SpreadsheetLabelMappingComponentContext context) {
        this.context = context;

        this.label = label(context);
        this.targetTextBox = this.targetTextBox();

        this.saveButton = this.saveButton();
        this.deleteButton = this.deleteButton();

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
        context.addLabelMappingWatcher(this);
    }

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, which includes a few text boxes to edit the label and the target.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetDialogComponent dialog = SpreadsheetDialogComponent.create(
                () -> this.context.close()
        );
        dialog.setTitle("Label");
        dialog.id(ID);

        dialog.appendChild(this.label);
        dialog.appendChild(this.targetTextBox);

        dialog.appendChild(
                ElementsFactory.elements.div()
                        .appendChild(this.saveButton())
                        .appendChild(this.undoButton())
                        .appendChild(this.deleteButton())
                        .appendChild(this.closeButton())
        );

        return dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetLabelMappingComponentContext context;

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    // SpreadsheetLabelComponent........................................................................................

    private SpreadsheetLabelComponent label(final Context context) {
        return SpreadsheetLabelComponent.with(
                "label",
                context

        ).addChangeListener(
                (oldValue, newValue) ->
                        this.onLabelNameTextBox(newValue)
        ).setId(ID_PREFIX + "label-TextBox");
    }

    private void onLabelNameTextBox(final Optional<SpreadsheetLabelName> label) {
        this.refreshSaveAndDeleteButtons(
                label,
                this.targetTextBox.getValue()
        );
    }

    private void setLabelName(final Optional<SpreadsheetLabelName> label) {
        this.label.setValue(label);
        this.onLabelNameTextBox(label);
    }

    private final SpreadsheetLabelComponent label;

    // targetTextBox...................................................................................................

    /**
     * Creates the {@link walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference} text box and installs a value change listener.
     */
    private TextBox targetTextBox() {
        final TextBox textBox = TextBox.create("Cell, cell range or Label");

        textBox.id(ID_PREFIX + "target-TextBox");
        textBox.element().spellcheck = false;
        textBox.element().type = "text";
        textBox.apply(
                self -> self.appendChild(
                        PostfixAddOn.of(
                                SpreadsheetIcons.close()
                                        .clickable()
                                        .addClickListener(event -> this.setTarget(""))
                        )
                )
        );

        textBox.addEventListener(
                EventType.input,
                (e) -> this.onTargetTextBox(this.targetTextBox.getValue())
        );
        return textBox;
    }

    private void onTargetTextBox(final String text) {
        this.refreshSaveAndDeleteButtons(
                this.label.value(),
                text
        );
    }

    private void setTarget(final String text) {
        this.targetTextBox.setValue(text);
        this.onTargetTextBox(text);
    }

    /**
     * The {@link TextBox} that holds the target of the label.
     */
    private final TextBox targetTextBox;

    // buttons..........................................................................................................

    /**
     * When clicked the CLOSE button closes/hides this dialog.
     */
    private Button closeButton() {
        return this.button(
                "Close",
                StyleType.DANGER,
                this::onCloseButtonClick
        );
    }

    private void onCloseButtonClick(final Event event) {
        final SpreadsheetLabelMappingComponentContext context = this.context;
        context.debug("SpreadsheetLabelMappingComponent.onCloseButtonClick");
        context.close();
    }

    /**
     * When clicked the SAVE button pushes a {@link HistoryToken} which saves the {@link SpreadsheetLabelMapping}.
     */
    private Button saveButton() {
        return this.button(
                "Save",
                StyleType.DEFAULT,
                this::onSaveButtonClick
        );
    }

    private void onSaveButtonClick(final Event event) {
        final SpreadsheetLabelMappingComponentContext context = this.context;
        final Optional<SpreadsheetLabelName> labelName = this.label.value();
        final String target = this.targetTextBox.getValue();
        context.debug("SpreadsheetLabelMappingComponent.onSaveButtonClick labelName: " + labelName + " target: " + CharSequences.quoteAndEscape(target));

        if (labelName.isPresent()) {
            try {
                context.save(
                        labelName.get()
                                .mapping(SpreadsheetExpressionReference.parseCellOrCellRange(target))
                );
            } catch (final Exception cause) {
                context.error(cause.getMessage());
            }
        }
    }

    private final Button saveButton;

    /**
     * When clicked the undo button reloads the last loaded/saved label and target textboxes.
     */
    private Button undoButton() {
        return this.button(
                "undo",
                StyleType.PRIMARY,
                this::onUndoButtonClick
        );
    }

    /**
     * Reloads both the label and target text boxes.
     */
    private void onUndoButtonClick(final Event event) {
        final SpreadsheetLabelMappingComponentContext context = this.context;

        final Optional<SpreadsheetLabelName> label = context.label();
        final String targetText = this.loadedTargetText;
        context.debug("SpreadsheetLabelMappingComponent.onUndoButtonClick " + label + " " + CharSequences.quoteAndEscape(targetText));

        this.setLabelName(label);
        this.setTarget(targetText);
    }

    private String loadedTargetText;

    /**
     * When clicked the REMOVE button invokes {@link SpreadsheetLabelMappingComponentContext#delete()} ()}.
     */
    private Button deleteButton() {
        return this.button(
                "Delete",
                StyleType.DANGER,
                this::onDeleteButtonClick
        );
    }

    private void onDeleteButtonClick(final Event event) {
        final SpreadsheetLabelMappingComponentContext context = this.context;

        context.debug("SpreadsheetLabelMappingComponent.onDeleteButtonClick");
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
    private void refreshSaveAndDeleteButtons(final Optional<SpreadsheetLabelName> labelName,
                                             final String target) {
        boolean deleteDisabled = true;
        boolean saveDisabled = true;

        String targetError = "";

        try {
            SpreadsheetSelection.parseCellRangeOrLabel(target);

            if(false == deleteDisabled) {
                saveDisabled = false;
            }
        } catch (final RuntimeException badTarget) {
            saveDisabled = true;
            targetError = badTarget.getMessage();
        }

        this.targetTextBox.setHelperText(targetError);

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

    @Override
    public void openGiveFocus(final AppContext context) {
        context.giveFocus(
                this.label::focus
        );
    }

    /**
     * Refreshes the widget, typically done when the label within the history token changes.
     */
    @Override
    public void refresh(final AppContext context) {
        final SpreadsheetLabelMappingSelectHistoryToken token = context.historyToken()
                .cast(SpreadsheetLabelMappingSelectHistoryToken.class);
        final Optional<SpreadsheetLabelName> maybeLabelName = token.labelName();
        if (maybeLabelName.isPresent()) {
            this.setLabelName(maybeLabelName);
            try {
                this.context.loadLabel(
                        maybeLabelName.get()
                );
            } catch (final RuntimeException ignore) {
                this.context.error("Unable to load label " + maybeLabelName);
            }
        }
    }

    // SpreadsheetLabelMappingFetcherWatcher...................................................................................

    @Override
    public void onSpreadsheetLabelMapping(final Optional<SpreadsheetLabelMapping> maybeMapping,
                                          final AppContext context) {
        if(maybeMapping.isPresent()) {
            final SpreadsheetLabelMapping mapping = maybeMapping.get();

            // same label update target
            if (this.label.value()
                    .equals(mapping.label())
            ) {
                final String targetText = mapping.target()
                        .text();
                this.setTarget(targetText);
                this.loadedTargetText = targetText;
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
