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

import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellDeleteHistoryToken;
import walkingkooka.spreadsheet.dominokit.value.ValueHistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueHistoryTokenAnchorComponentDelegator;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;

import java.util.Objects;
import java.util.Optional;

/**
 * Creates a {@link SpreadsheetCellDeleteAnchorComponent}.
 * The value which must be a {@link SpreadsheetExpressionReference} will create a link which pushes a {@link walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellDeleteHistoryToken}.
 */
public final class SpreadsheetCellDeleteAnchorComponent implements ValueHistoryTokenAnchorComponentDelegator<SpreadsheetCellDeleteAnchorComponent, SpreadsheetExpressionReference> {

    public static SpreadsheetCellDeleteAnchorComponent with(final String id,
                                                            final HistoryContext context) {
        return new SpreadsheetCellDeleteAnchorComponent(
            id,
            context
        );
    }

    private SpreadsheetCellDeleteAnchorComponent(final String id,
                                                 final HistoryContext context) {
        this.component = ValueHistoryTokenAnchorComponent.with(
            HistoryTokenAnchorComponent.empty(),
            this::getter,
            this::setter
        );
        this.setId(id);
        this.context = Objects.requireNonNull(context, "context");
    }

    /**
     * Getter that returns the {@link SpreadsheetExpressionReference} if one is present.
     */
    private Optional<SpreadsheetExpressionReference> getter(final HistoryTokenAnchorComponent anchor) {
        return anchor.historyToken()
            .map(t -> t.cast(SpreadsheetCellDeleteHistoryToken.class)
                .anchoredSelection()
                .selection()
                .toExpressionReference()
            );
    }

    private void setter(final Optional<SpreadsheetExpressionReference> value,
                        final HistoryTokenAnchorComponent anchor) {
        String text;
        HistoryToken historyToken = null;

        if (value.isPresent()) {
            historyToken = this.context.historyToken()
                .setSelection(value)
                .delete();
            if (false == (historyToken instanceof SpreadsheetCellDeleteHistoryToken)) {
                historyToken = null;
            }
            text = value.get()
                .text();
        } else {
            text = "Delete";
        }

        anchor.setHistoryToken(
            Optional.ofNullable(historyToken)
        ).setTextContent(text);
    }

    // AnchorComponentDelegator.........................................................................................

    @Override
    public ValueHistoryTokenAnchorComponent<SpreadsheetExpressionReference> valueHistoryTokenAnchorComponent() {
        return this.component;
    }

    // @VisibleForTesting
    final ValueHistoryTokenAnchorComponent<SpreadsheetExpressionReference> component;

    final HistoryContext context;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
