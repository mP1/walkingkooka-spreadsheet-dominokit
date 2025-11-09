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

package walkingkooka.spreadsheet.dominokit.cell.value;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.anchor.HistoryTokenSaveValueAnchorComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.email.EmailAddressComponent;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellValueSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellValueSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.link.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.number.NumberComponent;
import walkingkooka.spreadsheet.dominokit.number.WholeNumberComponent;
import walkingkooka.spreadsheet.dominokit.url.AbsoluteUrlComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.HasValueWatchers;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.text.CaseKind;
import walkingkooka.validation.ValueTypeName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A modal dialog that displays a value with a few links such as CLOSE.
 */
public final class SpreadsheetCellValueDialogComponent<T> implements DialogComponentLifecycle,
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

        if (valueComponent instanceof HasValueWatchers) {
            ((HasValueWatchers<?, T, ?>) valueComponent)
                .addValueWatcher2(
                    (final Optional<T> value) -> context.pushHistoryToken(
                        context.historyToken()
                            .setSaveValue(value)
                    )
                );
            this.value = valueComponent;
        } else {
            this.value = valueComponent.addChangeListener(
                (final Optional<T> oldValue,
                 final Optional<T> newValue) -> context.pushHistoryToken(
                    context.historyToken()
                        .setSaveValue(newValue)
                )
            );
        }

        this.save = this.<String>saveValueAnchor(context);

        String nowOrToday;
        ValueTypeName valueTypeName = context.valueType();
        if (ValueTypeName.DATE.equals(valueTypeName)) {
            nowOrToday = TODAY_TEXT;
        } else {
            if (ValueTypeName.DATE_TIME.equals(valueTypeName)) {
                nowOrToday = NOW_TEXT;
            } else {
                if (ValueTypeName.TIME.equals(valueTypeName)) {
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

        if (valueComponent instanceof AbsoluteUrlComponent) {
            final AbsoluteUrlComponent absoluteUrlComponent = (AbsoluteUrlComponent) valueComponent;
            absoluteUrlComponent.addValueWatcher(
                (v) -> this.save.setValue(
                    Cast.to(v)
                )
            );
        }

        if (valueComponent instanceof EmailAddressComponent) {
            final EmailAddressComponent emailAddressComponent = (EmailAddressComponent) valueComponent;
            emailAddressComponent.addValueWatcher(
                (v) -> this.save.setValue(
                    Cast.to(v)
                )
            );
        }

        if (valueComponent instanceof NumberComponent) {
            final NumberComponent numberComponent = (NumberComponent) valueComponent;
            numberComponent.addValueWatcher(
                (v) -> this.save.setValue(
                    Cast.to(v)
                )
            );
        }

        // refresh SAVE as the WholeNumberComponent is updated
        if (valueComponent instanceof WholeNumberComponent) {
            WholeNumberComponent wholeNumberComponent = (WholeNumberComponent) valueComponent;
            wholeNumberComponent.addValueWatcher(
                (v) -> this.save.setValue(
                    Cast.to(v)
                )
            );
        }

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
    private DialogComponent dialogCreate() {
        final SpreadsheetCellValueDialogComponentContext<T> context = this.context;

        AnchorListComponent links = AnchorListComponent.empty()
            .setCssProperty("margin-top", "5px")
            .setCssProperty("margin-left", "-5px")
            .appendChild(this.save)
            .appendChild(this.clear);

        final ValueTypeName valueType = context.valueType();
        if (ValueTypeName.DATE.equals(valueType) ||
            ValueTypeName.DATE_TIME.equals(valueType) ||
            ValueTypeName.TIME.equals(valueType)) {
            links.appendChild(this.nowOrToday);
        }

        links = links.appendChild(this.undo)
            .appendChild(this.close);

        return DialogComponent.smallEdit(
                context.id() + SpreadsheetElementIds.DIALOG,
                DialogComponent.INCLUDE_CLOSE,
                context
            ).appendChild(this.value)
            .appendChild(links);
    }

    private final DialogComponent dialog;

    private final SpreadsheetCellValueDialogComponentContext<T> context;

    private void refreshValueAndErrors() {
        final Optional<SpreadsheetFormula> formula = this.context.cell()
            .map(SpreadsheetCell::formula);

        final FormValueComponent<?, T, ?> value = this.value;

        // dont want to "clear" textbox components like AbsoluteUrl that are incomplete values (Urls) with "no value" but errors.
        if(false == value.isEditing()) {
            value.setValue(
                Cast.to(
                    formula.flatMap(SpreadsheetFormula::value)
                )
            );
        }

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

        value.setErrors(errors);
    }

    private final FormValueComponent<?, T, ?> value;

    // links............................................................................................................

    private void refreshSave() {
        this.save.setValue(
            Cast.to(
                this.context.cell()
                    .flatMap((SpreadsheetCell cell) -> cell.formula()
                        .value()
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
                ValueTypeName.DATE == context.valueType() ?
                    "today" :
                    "now"
            )
        );
    }

    private final HistoryTokenSaveValueAnchorComponent<String> nowOrToday;

    private void refreshClear() {
        this.clear.setValue(
            Optional.empty()
        );
    }

    private final HistoryTokenSaveValueAnchorComponent<String> clear;

    private void refreshUndo() {
        this.undo.setValue(
            this.context.cell()
                .flatMap((SpreadsheetCell cell) -> Cast.to(
                    cell.formula()
                        .value())
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

    // DialogComponentLifecycle.........................................................................................

    @Override
    public DialogComponent dialog() {
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
        final ValueTypeName valueType = token.valueType()
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
        this.value.setValue(
            this.context.cell()
                .flatMap(c ->
                    Cast.to(
                        c.formula()
                            .value()
                    )
                )
        );
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

    @Override
    public  boolean shouldLogLifecycleChanges() {
        return SPREADSHEET_CELL_VALUE_DIALOG_COMPONENT;
    }

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta) {
        this.refreshIfOpen(this.context);
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
