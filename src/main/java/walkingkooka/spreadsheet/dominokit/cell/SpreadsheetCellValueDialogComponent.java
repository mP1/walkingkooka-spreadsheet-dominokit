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

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.anchor.HistoryTokenSaveValueAnchorComponent;
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
import walkingkooka.spreadsheet.dominokit.link.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.text.CaseKind;
import walkingkooka.validation.ValidationValueTypeName;

import java.util.List;
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
     * A magic text value that will be replaced by the current date/time or time.
     */
    public final static String NOW_TEXT = "now";

    /**
     * A magic text value that will be replaced by the current date.
     */
    public final static String TODAY_TEXT = "today";

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
                        context.toHistoryTokenSaveStringValue(newValue)
                    )
            )
        );

        this.save = this.<String>saveValueAnchor(context)
            .autoDisableWhenMissingValue();

        String nowOrToday;
        ValidationValueTypeName valueTypeName = context.valueType();
        if (ValidationValueTypeName.DATE.equals(valueTypeName)) {
            nowOrToday = TODAY_TEXT;
        } else {
            if (ValidationValueTypeName.DATE_TIME.equals(valueTypeName)) {
                nowOrToday = NOW_TEXT;
            } else {
                if (ValidationValueTypeName.TIME.equals(valueTypeName)) {
                    nowOrToday = NOW_TEXT;
                } else {
                    nowOrToday = "";
                }
            }
        }

        this.nowOrToday = HistoryTokenSaveValueAnchorComponent.<String>with(
            this.idPrefix() +
                nowOrToday +
                SpreadsheetElementIds.LINK,
            context
        ).setTextContent(
            CaseKind.CAMEL.change(
                nowOrToday,
                CaseKind.TITLE
            )
        );

        this.clear = this.clearValueAnchor(context);
        this.undo = this.undoAnchor(context);
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

        AnchorListComponent links = AnchorListComponent.empty()
            .setCssProperty("margin-top", "5px")
            .setCssProperty("margin-left", "-5px")
            .appendChild(this.save)
            .appendChild(this.clear);

        final ValidationValueTypeName valueType = context.valueType();
        if (ValidationValueTypeName.DATE.equals(valueType) ||
            ValidationValueTypeName.DATE_TIME.equals(valueType) ||
            ValidationValueTypeName.TIME.equals(valueType)) {
            links.appendChild(this.nowOrToday);
        }

        links = links.appendChild(this.undo)
            .appendChild(this.close);

        return SpreadsheetDialogComponent.smallEdit(
                context.id() + SpreadsheetElementIds.DIALOG,
                SpreadsheetDialogComponent.INCLUDE_CLOSE,
                context
            ).appendChild(this.value)
            .appendChild(links);
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetCellValueDialogComponentContext<T> context;

    private void refreshValueAndErrors() {
        final Optional<SpreadsheetFormula> formula = this.context.cell()
            .map(SpreadsheetCell::formula);

        this.value.setValue(
            Cast.to(
                formula.flatMap(SpreadsheetFormula::value)
            )
        );

        final List<String> errors = Lists.array();

        if (formula.isPresent()) {
            final String errorText = formula.get()
                .error()
                .map(SpreadsheetError::text)
                .orElse(null);

            if (null != errorText) {
                errors.add(errorText);
            }
        }

        this.value.setErrors(errors);
    }

    private final FormValueComponent<?, T, ?> value;

    // links............................................................................................................

    private void refreshSave() {
        this.save.setValue(
            Optional.of(
                this.context.toHistoryTokenSaveStringValue(
                    this.context.cell()
                        .flatMap((SpreadsheetCell cell) -> Cast.to(
                            cell.formula()
                                .value())
                        )
                )
            )
        );
    }

    private final HistoryTokenSaveValueAnchorComponent<String> save;

    private void refreshNowOrToday() {
        final SpreadsheetCellValueDialogComponentContext<T> context = this.context;

        // DATE today
        // DATE_TIME now
        // TIME now

        this.nowOrToday.setValue(
            Optional.of(
                ValidationValueTypeName.DATE == context.valueType() ?
                    "today" :
                    "now"
            )
        );
    }

    private final HistoryTokenSaveValueAnchorComponent<String> nowOrToday;

    private void refreshClear() {
        this.clear.setValue(
            Optional.of(
                this.context.toHistoryTokenSaveStringValue(
                    Optional.empty()
                )
            )
        );
    }

    private final HistoryTokenSaveValueAnchorComponent<String> clear;

    private void refreshUndo() {
        this.undo.setValue(
            Optional.of(
                this.context.toHistoryTokenSaveStringValue(
                    this.context.cell()
                        .flatMap((SpreadsheetCell cell) -> Cast.to(
                            cell.formula()
                                .value())
                        )
                )
            )
        );
    }

    private final HistoryTokenSaveValueAnchorComponent<String> undo;

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
            this.context.valueType().equals(valueType);
    }

    @Override
    public void dialogReset() {
        this.save.clear();
        this.nowOrToday.clear();
        this.value.clearValue();
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        // NOP
    }

    @Override
    public void refresh(final RefreshContext context) {
        this.context.refreshDialogTitle(this);

        this.refreshValueAndErrors();

        this.refreshSave();
        this.refreshNowOrToday();
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
