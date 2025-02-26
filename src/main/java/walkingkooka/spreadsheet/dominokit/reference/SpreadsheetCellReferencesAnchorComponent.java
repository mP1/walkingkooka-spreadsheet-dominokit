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

import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponent;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellReferencesHistoryToken;
import walkingkooka.spreadsheet.dominokit.value.ValueHistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

/**
 * Creates a {@link SpreadsheetCellReferencesAnchorComponent} which will display cell reference for the given {@link SpreadsheetExpressionReference}.
 */
public final class SpreadsheetCellReferencesAnchorComponent implements AnchorComponentDelegator<SpreadsheetCellReferencesAnchorComponent, SpreadsheetExpressionReference> {

    public static SpreadsheetCellReferencesAnchorComponent with(final String id,
                                                                final HistoryTokenContext context) {
        return new SpreadsheetCellReferencesAnchorComponent(
            id,
            context
        );
    }

    private SpreadsheetCellReferencesAnchorComponent(final String id,
                                                     final HistoryTokenContext context) {
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
            .map(t -> t.cast(SpreadsheetCellReferencesHistoryToken.class)
                .anchoredSelection()
                .selection()
                .toExpressionReference()
            );
    }

    private void setter(final Optional<SpreadsheetExpressionReference> value,
                        final HistoryTokenAnchorComponent anchor) {
        String text = null;
        HistoryToken historyToken = null;

        if (value.isPresent()) {
            historyToken = this.context.historyToken()
                .setAnchoredSelection(
                    value.map(SpreadsheetSelection::setDefaultAnchor)
                ).references(HistoryTokenOffsetAndCount.EMPTY);
            if (false == (historyToken instanceof SpreadsheetCellReferencesHistoryToken)) {
                historyToken = null;
            }
            text = value.get()
                .text();
        } else {
            text = "Cell References";
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
    public SpreadsheetCellReferencesAnchorComponent setValue(final Optional<SpreadsheetExpressionReference> value) {
        this.component.setValue(value);
        return this;
    }

    // AnchorComponentDelegator......................................................................................

    @Override
    public AnchorComponent<?, SpreadsheetExpressionReference> anchorComponent() {
        return this.component;
    }

    // @VisibleForTesting
    final ValueHistoryTokenAnchorComponent<SpreadsheetExpressionReference> component;

    final HistoryTokenContext context;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
