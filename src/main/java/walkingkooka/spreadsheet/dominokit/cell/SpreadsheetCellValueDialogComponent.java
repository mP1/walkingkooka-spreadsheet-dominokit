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

package walkingkooka.spreadsheet.dominokit.cell;

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
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellValueSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellValueSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.link.SpreadsheetLinkListComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.HistoryTokenSaveValueAnchorComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.validation.ValidationValueTypeName;

import java.util.Objects;
import java.util.Optional;

/**
 * A modal dialog that displays a value with a few links such as CLOSE.
 */
public final class SpreadsheetCellValueDialogComponent<T> implements SpreadsheetDialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    SpreadsheetDeltaFetcherWatcher,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher {

    /**
     * Creates a new {@link SpreadsheetCellValueDialogComponent}.
     */
    public static <T> SpreadsheetCellValueDialogComponent<T> with(final FormValueComponent<?, T, ?> valueComponent,
                                                                  final SpreadsheetCellValueDialogComponentContext<T> context) {
        return new SpreadsheetCellValueDialogComponent<>(
            Objects.requireNonNull(valueComponent, "valueComponent"),
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetCellValueDialogComponent(final FormValueComponent<?, T, ?> valueComponent,
                                                final SpreadsheetCellValueDialogComponentContext<T> context) {
        this.context = context;

        this.value = valueComponent.addChangeListener(
                (final Optional<T> oldValue,
                 final Optional<T> newValue) -> context.pushHistoryToken(
                    context.historyToken()
                        .setSaveStringValue(
                            context.prepareSaveValue(newValue)
                        )
                )
            );
        this.undo = this.undoAnchor(context);
        this.clear = this.clearValueAnchor(context);
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetDeltaFetcherWatcher(this);
    }

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, with the value editing component and a few links to SAVE, UNDO and CLOSE
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetCellValueDialogComponentContext<T> context = this.context;

        return SpreadsheetDialogComponent.smallEdit(
                context.id() + SpreadsheetElementIds.DIALOG,
                context.dialogTitle(),
                SpreadsheetDialogComponent.INCLUDE_CLOSE,
                context
            ).appendChild(this.value)
            .appendChild(
                SpreadsheetLinkListComponent.empty()
                    .setCssProperty("margin-top", "5px")
                    .setCssProperty("margin-left", "-5px")
                    .appendChild(this.clear)
                    .appendChild(this.undo)
                    .appendChild(this.close)
            );
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetCellValueDialogComponentContext<T> context;

    private void refreshValue() {
        this.value.setValue(
            this.context.value()
        );
    }

    private final FormValueComponent<?, T, ?> value;

    // links............................................................................................................

    private void refreshUndo() {
        this.undo.setValue(
            Optional.of(
                this.context.prepareSaveValue(
                    this.context.value()
                )
            )
        );
    }

    private final HistoryTokenSaveValueAnchorComponent<String> undo;

    private void refreshClear() {
        this.clear.setValue(
            Optional.of(
                this.context.prepareSaveValue(
                    Optional.empty()
                )
            )
        );
    }

    private final HistoryTokenSaveValueAnchorComponent<String> clear;

    private void refreshClose() {
        this.close.setHistoryToken(
            Optional.of(
                this.context.historyToken()
                    .close()
            )
        );
    }

    private final HistoryTokenAnchorComponent close;

    // SpreadsheetDialogComponentLifecycle..............................................................................

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    @Override
    public String idPrefix() {
        return this.context.id() + "-";
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetCellValueSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        final ValidationValueTypeName valueType = token.valueType()
            .orElse(null);
        return null != valueType &&
            token instanceof SpreadsheetCellValueSelectHistoryToken &&
            this.context.isMatch(valueType);
    }

    @Override
    public void dialogReset() {
        this.value.clearValue();
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        // NOP
    }

    @Override
    public void refresh(final RefreshContext context) {
        this.refreshValue();
        this.refreshClear();
        this.refreshUndo();
        this.refreshClose();
    }

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        this.refreshIfOpen(context);
    }

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
