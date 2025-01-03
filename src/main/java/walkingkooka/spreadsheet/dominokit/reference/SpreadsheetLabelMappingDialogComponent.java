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

package walkingkooka.spreadsheet.dominokit.reference;

import walkingkooka.Cast;
import walkingkooka.Context;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingSelectHistoryToken;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A model dialog with several textboxes that allow creation, editing, saving and deletion of a {@link SpreadsheetLabelMapping}.
 */
public final class SpreadsheetLabelMappingDialogComponent implements SpreadsheetDialogComponentLifecycle,
        LoadedSpreadsheetMetadataRequired,
        NopFetcherWatcher,
        NopEmptyResponseFetcherWatcher,
        SpreadsheetDeltaFetcherWatcher {

    /**
     * Creates a new {@link SpreadsheetLabelMappingDialogComponent}.
     */
    public static SpreadsheetLabelMappingDialogComponent with(final SpreadsheetLabelMappingDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetLabelMappingDialogComponent(context);
    }

    private SpreadsheetLabelMappingDialogComponent(final SpreadsheetLabelMappingDialogComponentContext context) {
        this.context = context;

        this.label = label(context);
        this.target = this.target();

        this.save = this.anchor("Save");
        this.undo = this.anchor("Undo");
        this.delete = this.anchor("Delete");
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetDeltaFetcherWatcher(this);

        this.undoLabel = Optional.empty();
        this.undoTarget = Optional.empty();

        this.loadLabel = false;
    }

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, which includes a few text boxes to edit the label and the target.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetLabelMappingDialogComponentContext context = this.context;

        return SpreadsheetDialogComponent.with(
                        ID + SpreadsheetElementIds.DIALOG,
                        "Label",
                        true, // includeClose
                        context
                ).appendChild(this.label)
                .appendChild(this.target)
                .appendChild(
                        SpreadsheetFlexLayout.row()
                                .appendChild(this.save)
                                .appendChild(this.undo)
                                .appendChild(this.delete)
                                .appendChild(this.close)
                );
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetLabelMappingDialogComponentContext context;

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    private final static String ID = "labelMapping";

    private final static String ID_PREFIX = ID + "-";

    // SpreadsheetLabelComponent........................................................................................

    private SpreadsheetLabelComponent label(final Context context) {
        return SpreadsheetLabelComponent.with(context)
                .setId(ID_PREFIX + "label-TextBox")
                .setLabel("Label")
                .required()
                .addChangeListener(
                        (oldValue, newValue) -> this.refreshLinksAndLoadLabels()
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
                        (oldValue, newValue) -> this.refreshLinksAndLoadLabels()
                ).addKeyupListener(
                        (e) -> this.refreshLinksAndLoadLabels()
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
                                        .save(target) :
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
                                ).save(
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
                                        .delete() :
                                null
                )
        );
    }

    private final HistoryTokenAnchorComponent delete;

    // close............................................................................................................

    private void refreshClose() {
        this.close.setHistoryToken(
                Optional.ofNullable(
                        this.context.historyToken()
                                .close()
                )
        );
    }

    private final HistoryTokenAnchorComponent close;

    // HistoryTokenAwareComponentLifecycle..............................................................................

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
    public void dialogReset() {
        // NOP
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        boolean loadLabel = false;

        try {
            final SpreadsheetLabelMappingSelectHistoryToken token = context.historyToken()
                    .cast(SpreadsheetLabelMappingSelectHistoryToken.class);
            this.label.setValue(
                    token.labelName()
            );
            this.target.setValue(
                    Cast.to(
                            token.anchoredSelectionOrEmpty()
                                    .map(AnchoredSpreadsheetSelection::selection)
                    )
            );
            loadLabel = token.labelName()
                    .isPresent();
        } catch (final RuntimeException ignore) {
            this.label.clearValue();
            this.target.clearValue();

            loadLabel = false;
        }

        this.loadLabel = loadLabel;
        this.loaded = null;

        context.giveFocus(
                this.label::focus
        );
    }

    /**
     * Refreshes the widget, typically done when the label within the history token changes.
     */
    @Override
    public void refresh(final RefreshContext context) {
        this.refreshLinksAndLoadLabels();
    }

    private void refreshLinksAndLoadLabels() {
        this.refreshLabelAndTarget();
        this.refreshSave();
        this.refreshUndo();
        this.refreshDelete();
        this.refreshClose();

        this.loadLabelIfNecessary();
    }

    private void loadLabelIfNecessary() {
        if (this.loadLabel) {
            this.loadLabel = false;

            final SpreadsheetLabelMappingDialogComponentContext context = this.context;
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

    private void refreshLabelAndTarget() {
        final SpreadsheetLabelMapping loaded = this.loaded;

        if (null != loaded) {
            this.loaded = null;

            this.label.setValue(
                    Optional.of(loaded.label())
            );
            this.target.setValue(
                    Optional.of(loaded.target())
            );
        }
    }

    private SpreadsheetLabelMapping loaded;

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        if (this.isOpen()) {
            final Optional<SpreadsheetLabelName> undoLabel;
            final Optional<SpreadsheetExpressionReference> undoTarget;

            if (HttpMethod.GET.equals(method) || HttpMethod.POST.equals(method)) {
                final Set<SpreadsheetLabelMapping> mappings = delta.labels();
                switch (mappings.size()) {
                    case 1:
                        final SpreadsheetLabelMapping mapping = mappings.iterator()
                                .next();

                        undoLabel = Optional.of(
                                mapping.label()
                        );
                        undoTarget = Optional.of(
                                mapping.target()
                        );

                        this.loaded = mapping;
                        break;
                    default:
                        undoLabel = Optional.empty();
                        undoTarget = Optional.empty();
                        break;
                }
            } else {
                undoLabel = Optional.empty();
                undoTarget = Optional.empty();
            }

            this.undoLabel = undoLabel;
            this.undoTarget = undoTarget;

            this.refreshLinksAndLoadLabels();
        }
    }

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
