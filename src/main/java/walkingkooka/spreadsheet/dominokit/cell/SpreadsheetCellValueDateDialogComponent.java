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

import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellValueHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellValueSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellValueSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.link.SpreadsheetLinkListComponent;
import walkingkooka.spreadsheet.dominokit.value.HistoryTokenSaveValueAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.SpreadsheetDateComponent;
import walkingkooka.validation.ValidationValueTypeName;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

/**
 * A modal dialog that displays a date picker with a few links such as CLOSE.
 */
public final class SpreadsheetCellValueDateDialogComponent implements SpreadsheetDialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired {

    /**
     * Creates a new {@link SpreadsheetCellValueDateDialogComponent}.
     */
    public static SpreadsheetCellValueDateDialogComponent with(final SpreadsheetCellValueDateDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetCellValueDateDialogComponent(context);
    }

    private SpreadsheetCellValueDateDialogComponent(final SpreadsheetCellValueDateDialogComponentContext context) {
        this.context = context;

        this.date = SpreadsheetDateComponent.empty(ID_PREFIX + "date" + SpreadsheetElementIds.DATE)
            .addChangeListener(
                (final Optional<LocalDate> oldDate,
                 final Optional<LocalDate> newDate) -> context.pushHistoryToken(
                    context.historyToken()
                        .setSaveStringValue(
                            context.prepareSaveValue(newDate)
                        )
                )
            );
        this.undo = this.undoAnchor(context);
        this.clear = this.clearValueAnchor(context);
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
    }

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, with the date picker and a few links to SAVE, UNDO and CLOSE
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final HistoryContext context = this.context;

        return SpreadsheetDialogComponent.smallEdit(
                ID + SpreadsheetElementIds.DIALOG,
                "Date",
                SpreadsheetDialogComponent.INCLUDE_CLOSE,
                context
            ).appendChild(this.date)
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

    private final SpreadsheetCellValueDateDialogComponentContext context;

    private void refreshDate() {
        final SpreadsheetCellValueDateDialogComponentContext context = this.context;

        final Optional<LocalDate> value = context.value();

        this.date.setValue(
            value.isPresent() ?
                value :
                Optional.of(
                    context.now()
                        .toLocalDate()
                )
        );
    }

    private final SpreadsheetDateComponent date;

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
        return ID_PREFIX;
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetCellValueSaveHistoryToken;
    }

    /**
     * Only matches {@link SpreadsheetCellValueHistoryToken} with {@link ValidationValueTypeName#DATE}.
     */
    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellValueSelectHistoryToken && ValidationValueTypeName.DATE.equals(
            token.valueType()
                .orElse(null)
        );
    }

    @Override
    public void dialogReset() {
        this.date.resetView()
            .setValue(
                Optional.of(
                    this.context.now()
                        .toLocalDate()
                )
            );
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        // NOP
    }

    @Override
    public void refresh(final RefreshContext context) {
        this.refreshDate();
        this.refreshClear();
        this.refreshUndo();
        this.refreshClose();
    }

    // UI...............................................................................................................

    private final static String ID = "cellValueDate";

    private final static String ID_PREFIX = ID + "-";

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
