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

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;

import java.util.Objects;
import java.util.Optional;

/**
 * Saves or updates a new or existing {@link SpreadsheetLabelMapping}.
 * <pre>
 * /123/SpreadsheetName456/label/Label123/save/A1
 * /spreadsheet-id/spreadsheet-name/label/selected-label-name/save/target-cell-or-cell-range-or-another-label
 * </pre>
 */
public final class SpreadsheetLabelMappingSaveHistoryToken extends SpreadsheetLabelMappingHistoryToken {

    static SpreadsheetLabelMappingSaveHistoryToken with(final SpreadsheetId id,
                                                        final SpreadsheetName name,
                                                        final SpreadsheetLabelMapping mapping) {
        return new SpreadsheetLabelMappingSaveHistoryToken(
                id,
                name,
                mapping
        );
    }

    private SpreadsheetLabelMappingSaveHistoryToken(final SpreadsheetId id,
                                                    final SpreadsheetName name,
                                                    final SpreadsheetLabelMapping mapping) {
        super(
                id,
                name
        );
        this.mapping = Objects.requireNonNull(mapping, "mapping");
    }

    @Override
    public Optional<SpreadsheetLabelName> labelName() {
        return Optional.of(
                this.mapping.label()
        );
    }

    private final SpreadsheetLabelMapping mapping;

    // /Label123/save/B2
    @Override
    UrlFragment labelUrlFragment() {
        final SpreadsheetLabelMapping mapping = this.mapping;

        return UrlFragment.with(
                        mapping.label()
                                .value()
        ).appendSlashThen(
                this.saveUrlFragment(
                        mapping.target().toString())
                );
    }

    @Override
    public HistoryToken clearAction() {
        return this.labelMappingSelect();
    }

    // new id/name same labelName
    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return with(
                id,
                name,
                this.mapping
        );
    }

    @Override
    HistoryToken save0(final String value) {
        return this;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        final SpreadsheetId id = this.id();
        final SpreadsheetLabelMapping mapping = this.mapping;
        context.debug(this.getClass().getSimpleName() + ".onHistoryTokenChange0 save " + id + " " + mapping);

        context.spreadsheetDeltaFetcher()
                .saveLabelMapping(
                        id,
                        mapping
                );
    }
}
