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
import org.dominokit.domino.ui.style.Elevation;
import org.dominokit.domino.ui.style.StyleType;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.Context;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.label.SpreadsheetLabelComponent;
import walkingkooka.spreadsheet.dominokit.ui.spreadsheetexpressionreference.SpreadsheetExpressionReferenceComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;

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
        this.target = this.target();

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
        final SpreadsheetDialogComponent dialog = SpreadsheetDialogComponent.create(this.context);
        dialog.setTitle("Label");
        dialog.id(ID);

        dialog.appendChild(this.label);
        dialog.appendChild(this.target);

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
        return SpreadsheetLabelComponent.with(context)
                .setId(ID_PREFIX + "label-TextBox")
                .setLabel("Label")
                .required()
                .addChangeListener(
                        (oldValue, newValue) ->
                                this.onLabel(newValue)
                );
    }

    private void onLabel(final Optional<SpreadsheetLabelName> label) {
        this.refreshSaveAndDeleteButtons(
                label,
                this.target.value()
        );
    }

    private void setLabel(final Optional<SpreadsheetLabelName> label) {
        this.label.setValue(label);
        this.onLabel(label);
    }

    private final SpreadsheetLabelComponent label;

    // target...................................................................................................

    /**
     * Creates the {@link walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference} text box and installs a value change listener.
     */
    private SpreadsheetExpressionReferenceComponent target() {
        return SpreadsheetExpressionReferenceComponent.empty()
                .setId(ID_PREFIX + "target-TextBox")
                .setLabel("Cell, cell range or Label")
                .addChangeListener(
                        (oldValue, newValue) -> this.onTarget(newValue)
                );
    }

    private void onTarget(final Optional<SpreadsheetExpressionReference> expressionReference) {
        this.refreshSaveAndDeleteButtons(
                this.label.value(),
                expressionReference
        );
    }

    private void setTarget(final Optional<SpreadsheetExpressionReference> expressionReference) {
        this.target.setValue(expressionReference);
        this.onTarget(expressionReference);
    }

    private final SpreadsheetExpressionReferenceComponent target;

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
        final Optional<SpreadsheetExpressionReference> target = this.target.value();

        context.debug("SpreadsheetLabelMappingComponent.onSaveButtonClick labelName: " +
                labelName.map(Object::toString)
                        .orElse("") +
                " target: " +
                target.map(Object::toString)
                        .orElse("")
        );

        if (labelName.isPresent() && target.isPresent()) {
            try {
                context.save(
                        labelName.get()
                                .mapping(target.get())
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
        final Optional<SpreadsheetExpressionReference> target = this.loadedTarget;
        context.debug("SpreadsheetLabelMappingComponent.onUndoButtonClick " + label + " " + target);

        this.setLabel(label);
        this.setTarget(target);
    }

    private Optional<SpreadsheetExpressionReference> loadedTarget;

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
                                             final Optional<SpreadsheetExpressionReference> target) {
        this.deleteButton.setDisabled(labelName.isPresent());
        this.saveButton.setDisabled(labelName.isPresent() && target.isPresent());
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
            this.setLabel(maybeLabelName);
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
                final Optional<SpreadsheetExpressionReference> target = Optional.of(mapping.target());
                this.setTarget(target);
                this.loadedTarget = target;
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
