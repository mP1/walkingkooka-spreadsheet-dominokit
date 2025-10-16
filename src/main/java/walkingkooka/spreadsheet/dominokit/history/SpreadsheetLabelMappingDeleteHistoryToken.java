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
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;

/**
 * Deletes the given spreadsheet mapping.
 * <pre>
 * /123/SpreadsheetName456/label/Label123/delete
 * /spreadsheet-id/spreadsheet-name/label/selected-label/delete
 * </pre>
 */
public final class SpreadsheetLabelMappingDeleteHistoryToken extends SpreadsheetLabelMappingHistoryToken {

    static SpreadsheetLabelMappingDeleteHistoryToken with(final SpreadsheetId id,
                                                          final SpreadsheetName name,
                                                          final SpreadsheetLabelName labelName) {
        return new SpreadsheetLabelMappingDeleteHistoryToken(
            id,
            name,
            labelName
        );
    }

    private SpreadsheetLabelMappingDeleteHistoryToken(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final SpreadsheetLabelName labelName) {
        super(
            id,
            name
        );
        this.labelName = Objects.requireNonNull(labelName, "labelName");
    }

    // @see HistoryToken.labelName
    final SpreadsheetLabelName labelName;

    // /Label123/delete
    @Override
    UrlFragment labelUrlFragment() {
        return UrlFragment.with(
            this.labelName.value()
        ).appendSlashThen(DELETE);
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
            this.labelName
        );
    }

    // new id/name same labelName
    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return with(
            id,
            name,
            this.labelName
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        context.spreadsheetDeltaFetcher()
            .deleteLabelMapping(
                this.id,
                this.labelName // getter returns Optional label
            );
    }
}
