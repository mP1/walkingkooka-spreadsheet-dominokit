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

import walkingkooka.Value;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;

/**
 * Saves or updates a new or existing {@link SpreadsheetLabelMapping}.
 * <pre>
 * /123/SpreadsheetName456/label/Label123/save/A1
 * /spreadsheet-id/spreadsheet-name/label/selected-label-name/save/target-cell-or-cell-range-or-another-label
 * </pre>
 */
public final class SpreadsheetLabelMappingSaveHistoryToken extends SpreadsheetLabelMappingHistoryToken
    implements Value<SpreadsheetLabelName> {

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
    public SpreadsheetLabelName value() {
        return this.mapping.label();
    }

    // HistoryToken#labelMappingReference
    final SpreadsheetLabelMapping mapping;

    // /Label123/save/B2
    @Override
    UrlFragment labelUrlFragment() {
        final SpreadsheetLabelMapping mapping = this.mapping;

        return UrlFragment.with(
            mapping.label()
                .value()
        ).appendSlashThen(
            saveUrlFragment(
                mapping.reference()
                    .toString())
        );
    }

    @Override
    HistoryToken parseNext(final String component,
                           final TextCursor cursor) {
        return this;
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.labelMappingSelect(
            this.id,
            this.name,
            this.mapping.label()
        );
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
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        context.spreadsheetDeltaFetcher()
            .postLabelMapping(
                this.id,
                this.mapping
            );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitSpreadsheetLabelMappingSave(
            this.id,
            this.name,
            this.mapping
        );
    }
}
