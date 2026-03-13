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

package walkingkooka.spreadsheet.dominokit.form;

import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponent;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.validation.SpreadsheetValidationReference;

import java.util.Objects;
import java.util.Optional;

public final class SpreadsheetFormSelectHistoryTokenAnchorComponent implements AnchorComponentDelegator<SpreadsheetFormSelectHistoryTokenAnchorComponent, SpreadsheetValidationReference> {

    public static SpreadsheetFormSelectHistoryTokenAnchorComponent with(final HistoryTokenAnchorComponent anchor,
                                                                        final String id,
                                                                        final HistoryContext context) {
        return new SpreadsheetFormSelectHistoryTokenAnchorComponent(
            anchor,
            id,
            context
        );
    }

    private SpreadsheetFormSelectHistoryTokenAnchorComponent(final HistoryTokenAnchorComponent anchor,
                                                             final String id,
                                                             final HistoryContext context) {
        this.component = anchor.clear();
        this.setId(id);
        this.context = Objects.requireNonNull(context, "context");
    }

    @Override
    public Optional<SpreadsheetValidationReference> value() {
        return this.component.historyToken()
            .flatMap(HistoryToken::field);
    }

    @Override
    public SpreadsheetFormSelectHistoryTokenAnchorComponent setValue(final Optional<SpreadsheetValidationReference> value) {
        HistoryToken historyToken = this.context.historyToken()
            .setField(value);
        if (historyToken.field().isEmpty()) {
            historyToken = null;
        }

        this.component.setHistoryToken(
            Optional.ofNullable(historyToken)
        );
        return this;
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
