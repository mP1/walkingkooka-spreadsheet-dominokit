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

import walkingkooka.spreadsheet.dominokit.anchor.ValueHistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.anchor.ValueHistoryTokenAnchorComponentDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellLabelSelectHistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

/**
 * Creates a {@link SpreadsheetCellCreateLabelSelectAnchorComponent} which will create a label for the given {@link SpreadsheetExpressionReference}.
 */
public final class SpreadsheetCellCreateLabelSelectAnchorComponent implements ValueHistoryTokenAnchorComponentDelegator<SpreadsheetCellCreateLabelSelectAnchorComponent, SpreadsheetExpressionReference> {

    public static SpreadsheetCellCreateLabelSelectAnchorComponent with(final String id,
                                                                       final HistoryContext context) {
        return new SpreadsheetCellCreateLabelSelectAnchorComponent(
            id,
            context
        );
    }

    private SpreadsheetCellCreateLabelSelectAnchorComponent(final String id,
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
            .flatMap(t -> t.selection().map(SpreadsheetSelection::toExpressionReference));
    }

    private void setter(final Optional<? extends SpreadsheetExpressionReference> value,
                        final HistoryTokenAnchorComponent anchor) {
        String text;
        HistoryToken historyToken = null;

        if (value.isPresent()) {
            historyToken = this.context.historyToken()
                .setSelection(value
                ).createLabel();
            if (false == historyToken instanceof SpreadsheetCellLabelSelectHistoryToken) {
                historyToken = null;
            }
            text = value.get()
                .text();
        } else {
            text = "Create";
        }

        anchor.setHistoryToken(
            Optional.ofNullable(historyToken)
        ).setTextContent(text);
    }

    @Override
    public Optional<SpreadsheetExpressionReference> value() {
        return this.component.value();
    }

    @Override
    public SpreadsheetCellCreateLabelSelectAnchorComponent setValue(final Optional<SpreadsheetExpressionReference> value) {
        this.component.setValue(value);
        return this;
    }

    // ValueHistoryTokenAnchorComponentDelegator........................................................................

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
