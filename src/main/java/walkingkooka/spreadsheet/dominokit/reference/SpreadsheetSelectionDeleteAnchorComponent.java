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
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellDeleteHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnDeleteHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowDeleteHistoryToken;
import walkingkooka.spreadsheet.dominokit.value.ValueHistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

/**
 * Creates a {@link SpreadsheetSelectionDeleteAnchorComponent} which will delete the given {@link SpreadsheetSelection}.
 * Ideally a type parameter would be used but some combinations of {@link SpreadsheetSelection} such as column and column-range
 * do not share a good parent class.
 */
public final class SpreadsheetSelectionDeleteAnchorComponent implements AnchorComponentDelegator<SpreadsheetSelectionDeleteAnchorComponent, SpreadsheetSelection> {

    public static SpreadsheetSelectionDeleteAnchorComponent with(final String id,
                                                                 final HistoryContext context) {
        return new SpreadsheetSelectionDeleteAnchorComponent(
            id,
            context
        );
    }

    private SpreadsheetSelectionDeleteAnchorComponent(final String id,
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
     * Getter that returns the {@link SpreadsheetSelection} if one is present.
     */
    private Optional<SpreadsheetSelection> getter(final HistoryTokenAnchorComponent anchor) {
        return anchor.historyToken()
            .flatMap(HistoryToken::selection);
    }

    private void setter(final Optional<? extends SpreadsheetSelection> value,
                        final HistoryTokenAnchorComponent anchor) {
        String text;
        HistoryToken historyToken = null;

        if (value.isPresent()) {
            historyToken = this.context.historyToken()
                .setSelection(value
                ).delete();
            if (false == historyToken instanceof SpreadsheetCellDeleteHistoryToken &&
                false == historyToken instanceof SpreadsheetColumnDeleteHistoryToken &&
                false == historyToken instanceof SpreadsheetRowDeleteHistoryToken) {
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

    @Override
    public Optional<SpreadsheetSelection> value() {
        return this.component.value();
    }

    @Override
    public SpreadsheetSelectionDeleteAnchorComponent setValue(final Optional<SpreadsheetSelection> value) {
        this.component.setValue(value);
        return this;
    }

    // AnchorComponentDelegator......................................................................................

    @Override
    public AnchorComponent<?, SpreadsheetSelection> anchorComponent() {
        return this.component;
    }

    // @VisibleForTesting
    final ValueHistoryTokenAnchorComponent<SpreadsheetSelection> component;

    final HistoryContext context;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
