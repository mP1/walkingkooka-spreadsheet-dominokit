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

import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.Context;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
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
        LoadedSpreadsheetMetadataRequired,
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

        this.save = this.anchor("Save");
        this.undo = this.anchor("Undo");
        this.delete = this.anchor("Delete");

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
        context.addLabelMappingWatcher(this);

        this.undoLabel = Optional.empty();
        this.undoTarget = Optional.empty();

        this.loadLabel = false;
    }

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, which includes a few text boxes to edit the label and the target.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetLabelMappingComponentContext context = this.context;

        final SpreadsheetDialogComponent dialog = SpreadsheetDialogComponent.create(context);
        dialog.setTitle("Label");
        dialog.id(ID);

        dialog.appendChild(this.label);
        dialog.appendChild(this.target);

        dialog.appendChild(
                ElementsFactory.elements.div()
                        .appendChild(this.save)
                        .appendChild(this.undo)
                        .appendChild(this.delete)
                        .appendChild(
                                this.closeAnchor(
                                        context.historyToken()
                                )
                        )
        );

        return dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetLabelMappingComponentContext context;

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    // SpreadsheetLabelComponent........................................................................................

    private SpreadsheetLabelComponent label(final Context context) {
        return SpreadsheetLabelComponent.with(context)
                .setId(ID_PREFIX + "label-TextBox")
                .setLabel("Label")
                .required()
                .addChangeListener(
                        (oldValue, newValue) -> this.refresh()
                );
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
                        (oldValue, newValue) -> this.refresh()
                ).addKeyupListener(
                        (e) -> this.refresh()
                );
    }

    private final SpreadsheetExpressionReferenceComponent target;

    private void refreshSave() {
        final Optional<SpreadsheetLabelName> label = this.label.value();
        final Optional<SpreadsheetExpressionReference> target = this.target.value();

        this.save.setHistoryToken(
                Optional.ofNullable(
                        label.isPresent() && target.isPresent() ?
                                this.context.historyToken()
                                        .setLabelName(label)
                                        .setSave(target) :
                                null
                )
        );
    }

    private final HistoryTokenAnchorComponent save;

    /**
     * Refreshes the UNDO link with the undo label + target.
     */
    private void refreshUndo() {
        this.undo.setHistoryToken(
                Optional.of(
                        this.context.historyToken()
                                .setLabelName(
                                        this.undoLabel
                                ).setSave(
                                        this.undoTarget
                                )
                )
        );
    }

    private final HistoryTokenAnchorComponent undo;

    private Optional<SpreadsheetLabelName> undoLabel;

    private Optional<SpreadsheetExpressionReference> undoTarget;

    private void refreshDelete() {
        final Optional<SpreadsheetLabelName> label = this.label.value();

        this.delete.setHistoryToken(
                Optional.ofNullable(
                        label.isPresent() ?
                                this.context.historyToken()
                                        .setLabelName(label)
                                        .setDelete() :
                                null
                )
        );
    }

    private final HistoryTokenAnchorComponent delete;

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
        this.refresh();
    }

    private void refresh() {
        this.refreshSave();
        this.refreshUndo();
        this.refreshDelete();

        loadLabelIfNecessary();
    }

    private void loadLabelIfNecessary() {
        if (this.loadLabel) {
            this.loadLabel = false;

            final SpreadsheetLabelMappingComponentContext context = this.context;
            SpreadsheetLabelName labelName = null;
            try {
                final SpreadsheetLabelMappingSelectHistoryToken token = context.historyToken()
                        .cast(SpreadsheetLabelMappingSelectHistoryToken.class);
                final Optional<SpreadsheetLabelName> maybeLabelName = token.labelName();
                if (maybeLabelName.isPresent()) {
                    labelName = maybeLabelName.get();

                    context.loadLabel(labelName);
                }

            } catch (final RuntimeException ignore) {
                context.error("Unable to load label " + labelName);
            }
        }
    }

    private boolean loadLabel;

    // SpreadsheetLabelMappingFetcherWatcher...................................................................................

    @Override
    public void onSpreadsheetLabelMapping(final Optional<SpreadsheetLabelMapping> mapping,
                                          final AppContext context) {
        this.undoLabel = mapping.map(SpreadsheetLabelMapping::label);
        this.undoTarget = mapping.map(SpreadsheetLabelMapping::target);
        this.refresh();
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
