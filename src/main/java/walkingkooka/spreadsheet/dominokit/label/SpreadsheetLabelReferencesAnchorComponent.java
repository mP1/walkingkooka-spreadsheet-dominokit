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

import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponent;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingReferencesHistoryToken;
import walkingkooka.spreadsheet.dominokit.value.ValueHistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

/**
 * Creates a {@link SpreadsheetLabelReferencesAnchorComponent} which will display cell reference for the given {@link SpreadsheetLabelName}.
 */
public final class SpreadsheetLabelReferencesAnchorComponent implements AnchorComponentDelegator<SpreadsheetLabelReferencesAnchorComponent, SpreadsheetLabelName> {

    public static SpreadsheetLabelReferencesAnchorComponent with(final String id,
                                                                 final SpreadsheetLabelReferencesAnchorComponentContext context) {
        return new SpreadsheetLabelReferencesAnchorComponent(
            id,
            context
        );
    }

    private SpreadsheetLabelReferencesAnchorComponent(final String id,
                                                      final SpreadsheetLabelReferencesAnchorComponentContext context) {
        this.component = ValueHistoryTokenAnchorComponent.with(
            HistoryTokenAnchorComponent.empty(),
            this::getter,
            this::setter
        );
        this.setId(id);
        this.context = Objects.requireNonNull(context, "context");
    }

    /**
     * Getter that returns the {@link SpreadsheetLabelName} if one is present.
     */
    private Optional<SpreadsheetLabelName> getter(final HistoryTokenAnchorComponent anchor) {
        return anchor.historyToken()
            .flatMap(t -> t.cast(SpreadsheetLabelMappingReferencesHistoryToken.class)
                .labelName()
            );
    }

    private void setter(final Optional<SpreadsheetLabelName> value,
                        final HistoryTokenAnchorComponent anchor) {
        String text = null;
        HistoryToken historyToken = null;

        if (value.isPresent()) {
            historyToken = this.context.historyToken()
                .setAnchoredSelection(
                    value.map(SpreadsheetSelection::setDefaultAnchor)
                ).references(HistoryTokenOffsetAndCount.EMPTY);
            if (false == (historyToken instanceof SpreadsheetLabelMappingReferencesHistoryToken)) {
                historyToken = null;
            }
            text = value.get()
                .text();
        } else {
            text = "References";
        }

        anchor.setHistoryToken(
            Optional.ofNullable(historyToken)
        ).setTextContent(text);
    }

    @Override
    public Optional<SpreadsheetLabelName> value() {
        return this.component.value();
    }

    @Override
    public SpreadsheetLabelReferencesAnchorComponent setValue(final Optional<SpreadsheetLabelName> value) {
        this.component.setValue(value);
        return this;
    }

    // AnchorComponentDelegator......................................................................................

    @Override
    public AnchorComponent<?, SpreadsheetLabelName> anchorComponent() {
        return this.component;
    }

    // @VisibleForTesting
    final ValueHistoryTokenAnchorComponent<SpreadsheetLabelName> component;

    final SpreadsheetLabelReferencesAnchorComponentContext context;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
