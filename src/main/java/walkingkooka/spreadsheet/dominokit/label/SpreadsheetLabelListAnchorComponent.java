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

package walkingkooka.spreadsheet.dominokit.label;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponent;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;

import java.util.Objects;
import java.util.Optional;

/**
 * Creates a {@link SpreadsheetLabelListAnchorComponent} that automatically updates itself when the {@link walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher#onHistoryTokenChange(HistoryToken, AppContext)} happen.
 * Attempts to setValue will throw a {@link UnsupportedOperationException}.
 */
public final class SpreadsheetLabelListAnchorComponent implements AnchorComponentDelegator<SpreadsheetLabelListAnchorComponent, Void> {

    public static SpreadsheetLabelListAnchorComponent with(final HistoryTokenAnchorComponent anchor,
                                                           final String id,
                                                           final HistoryContext context) {
        return new SpreadsheetLabelListAnchorComponent(
            anchor,
            id,
            context
        );
    }

    private SpreadsheetLabelListAnchorComponent(final HistoryTokenAnchorComponent anchor,
                                                final String id,
                                                final HistoryContext context) {
        this.component = anchor.setTextContent("Labels");
        this.setId(id);

        this.context = Objects.requireNonNull(context, "context");
        this.context.addHistoryTokenWatcher(
            (final HistoryToken previous, final AppContext appContext) ->
                this.component.setHistoryToken(
                    Optional.of(
                        context.historyToken()
                            .setLabels(HistoryTokenOffsetAndCount.EMPTY)
                    )
                )
        );
    }

    @Override
    public SpreadsheetLabelListAnchorComponent setValue(final Optional<Void> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Void> value() {
        return Optional.empty();
    }

    // AnchorComponentDelegator.........................................................................................

    @Override
    public AnchorComponent<?, ?> anchorComponent() {
        return this.component;
    }

    // @VisibleForTesting
    final HistoryTokenAnchorComponent component;

    final HistoryContext context;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
