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
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellLabelHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellLabelSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingSaveHistoryToken;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
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

        this.labelName = labelName(context);
        this.labelMappingTarget = this.labelMappingTarget();

        this.save = this.anchor("Save");
        this.undo = this.anchor("Undo");
        this.delete = this.anchor("Delete");
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetDeltaFetcherWatcher(this);

        this.undoLabelName = Optional.empty();
        this.undoLabelMappingTarget = Optional.empty();
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
            ).appendChild(this.labelName)
            .appendChild(this.labelMappingTarget)
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

    private SpreadsheetLabelComponent labelName(final HistoryTokenContext context) {
        return SpreadsheetLabelComponent.with(context)
            .setId(ID_PREFIX + "label" + SpreadsheetElementIds.TEXT_BOX)
            .setLabel("Label")
            .required()
            .addChangeListener(
                (oldValue, newValue) -> {
                    // history change will trigger a load label if necessary
                    this.loaded = null;

                    final HistoryToken historyToken = context.historyToken();
                    if(historyToken instanceof SpreadsheetLabelMappingHistoryToken) {
                        context.pushHistoryToken(
                            historyToken.setLabelName(newValue)
                        );
                    } else {
                        this.labelName.setValue(newValue);
                        this.refreshLinks();
                    }
                }
            );
    }

    private final SpreadsheetLabelComponent labelName;

    // labelMappingTarget...............................................................................................

    /**
     * Creates the {@link walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference} text box and installs a value change listener.
     */
    private SpreadsheetExpressionReferenceComponent labelMappingTarget() {
        return SpreadsheetExpressionReferenceComponent.empty()
            .setId(ID_PREFIX + "target" + SpreadsheetElementIds.TEXT_BOX)
            .setLabel("Cell, cell range or Label")
            .addChangeListener(
                (oldValue, newValue) -> {
                    this.onLabelMappingTargetNewValue(newValue);
                }
            ).addKeyupListener(
                (e) -> {
                    this.onLabelMappingTargetNewValue(
                        this.labelMappingTarget.value()
                    );
                }
            );
    }

    private final SpreadsheetExpressionReferenceComponent labelMappingTarget;

    private void onLabelMappingTargetNewValue(final Optional<SpreadsheetExpressionReference> newValue) {
        final HistoryToken historyToken = context.historyToken();
        if(historyToken instanceof SpreadsheetCellLabelHistoryToken) {
            context.pushHistoryToken(
                historyToken.setLabelMappingReference(newValue)
            );
        } else {
            this.labelMappingTarget.setValue(newValue);
            this.refreshLinks();
        }
    }

    // save.............................................................................................................

    private void refreshSave() {
        final Optional<SpreadsheetLabelName> label = this.labelName.value();
        final Optional<SpreadsheetExpressionReference> target = this.labelMappingTarget.value();

        this.save.setHistoryToken(
            Optional.ofNullable(
                label.isPresent() && target.isPresent() ?
                    this.context.historyToken()
                        .setLabelName(label)
                        .setLabelMappingReference(target) :
                    null
            )
        );
    }

    private final HistoryTokenAnchorComponent save;

    // undo.............................................................................................................

    /**
     * Refreshes the UNDO link with the undo label + target.
     */
    private void refreshUndo() {
        this.undo.setHistoryToken(
            Optional.of(
                this.context.historyToken()
                    .setLabelName(this.undoLabelName)
                    .setLabelMappingReference(this.undoLabelMappingTarget)
            )
        );
    }

    private final HistoryTokenAnchorComponent undo;

    private Optional<SpreadsheetLabelName> undoLabelName;

    private Optional<SpreadsheetExpressionReference> undoLabelMappingTarget;

    // delete...........................................................................................................

    private void refreshDelete() {
        final Optional<SpreadsheetLabelName> label = this.labelName.value();

        final HistoryToken historyToken = this.context.historyToken();

        // dont enable when SpreadsheetCellLabelHistoryToken
        this.delete.setHistoryToken(
            Optional.ofNullable(
                label.isPresent() && historyToken instanceof SpreadsheetLabelMappingHistoryToken ?
                    historyToken.setLabelName(label)
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
        return token instanceof SpreadsheetCellLabelSaveHistoryToken ||
            token instanceof SpreadsheetLabelMappingSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellLabelHistoryToken ||
            token instanceof SpreadsheetLabelMappingHistoryToken;
    }

    @Override
    public void dialogReset() {
        this.loaded = null;
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        context.giveFocus(
            this.labelName::focus
        );
    }

    /**
     * Refreshes the widget, typically done when the label within the history token changes.
     */
    @Override
    public void refresh(final RefreshContext context) {
        this.refreshLabelNameAndLabelMappingTarget();
        this.loadLabelMappingIfNecessary();

        this.refreshLinks();
    }

    /**
     * Refreshes all links.
     */
    private void refreshLinks() {
        this.refreshSave();
        this.refreshUndo();
        this.refreshDelete();
        this.refreshClose();
    }

    /**
     * If the current history token is {@link SpreadsheetLabelMappingHistoryToken} with a {@link SpreadsheetLabelName},
     * and it has not been yet loaded, load it.<br>
     * If the history token is {@link SpreadsheetCellLabelHistoryToken} the label is never loaded.
     */
    private void loadLabelMappingIfNecessary() {
        final SpreadsheetLabelMappingDialogComponentContext context = this.context;
        final HistoryToken historyToken = this.context.historyToken();

        if (null == this.loaded) {
            if (historyToken instanceof SpreadsheetLabelMappingHistoryToken) {
                SpreadsheetLabelName labelName = null;
                try {
                    final Optional<SpreadsheetLabelName> maybeLabelName = historyToken.labelName();
                    if (maybeLabelName.isPresent()) {
                        labelName = maybeLabelName.get();

                        context.loadLabel(labelName);
                    }

                } catch (final RuntimeException ignore) {
                    context.error("Unable to load label " + labelName);
                }
            }
        }
    }

    /**
     * Refreshes the label and label mapping target from the loaded {@link SpreadsheetLabelMapping} or
     * {@link HistoryToken}
     */
    private void refreshLabelNameAndLabelMappingTarget() {
        final Optional<SpreadsheetLabelMapping> loaded = this.loaded;

        final SpreadsheetLabelComponent labelNameComponent = this.labelName;
        final SpreadsheetExpressionReferenceComponent labelMappingTargetComponent = this.labelMappingTarget;

        if (null != loaded) {
            if (loaded.isPresent()) {
                labelNameComponent.setValue(
                    loaded.map(SpreadsheetLabelMapping::label)
                );
                labelMappingTargetComponent.setValue(
                    loaded.map(SpreadsheetLabelMapping::reference)
                );
            }

            this.loaded = Optional.empty();
        } else {
            final HistoryToken token = context.historyToken();

            if (token instanceof SpreadsheetCellLabelHistoryToken) {
                final Optional<SpreadsheetExpressionReference> historyLabelMappingTarget = token.labelMappingTarget();
                final Optional<SpreadsheetExpressionReference> componentLabelMappingTarget = labelMappingTargetComponent.value();

                if (false == historyLabelMappingTarget.equals(componentLabelMappingTarget)) {
                    labelMappingTargetComponent.setValue(
                        historyLabelMappingTarget
                    );
                    labelNameComponent.clearValue();
                }
            }

            if (token instanceof SpreadsheetLabelMappingHistoryToken) {
                final Optional<SpreadsheetLabelName> historyLabelName = token.labelName();
                final Optional<SpreadsheetLabelName> componentLabelName = labelNameComponent.value();

                if (false == historyLabelName.equals(componentLabelName)) {
                    labelNameComponent.setValue(
                        historyLabelName
                    );
                    labelMappingTargetComponent.clearValue();
                }
            }
        }
    }

    /**
     * When null indicates that when a label is available it should be loaded.
     */
    private Optional<SpreadsheetLabelMapping> loaded;

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        if (this.isOpen()) {
            if (HttpMethod.GET.equals(method) || HttpMethod.POST.equals(method)) {
                final Set<SpreadsheetLabelMapping> mappings = delta.labels();

                SpreadsheetLabelMapping mapping = null;
                Optional<SpreadsheetLabelName> undoLabelName = null;
                Optional<SpreadsheetExpressionReference> undoLabelMappingTarget = null;

                switch (mappings.size()) {
                    case 1:
                        mapping = mappings.iterator()
                            .next();
                        undoLabelName = Optional.of(
                            mapping.label()
                        );
                        undoLabelMappingTarget = Optional.of(
                            mapping.reference()
                        );
                        break;
                    default:
                        undoLabelName = Optional.empty();
                        undoLabelMappingTarget = Optional.empty();
                        break;
                }
                this.loaded = Optional.ofNullable(mapping); // label does not exist
                this.undoLabelName = undoLabelName;
                this.undoLabelMappingTarget = undoLabelMappingTarget;

                this.refreshIfOpen(context);
            }
        }
    }

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
