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
 * Lists all the references for a given {@link SpreadsheetLabelName}.
 * <pre>
 * /123/SpreadsheetName456/label/Label123/references
 * /spreadsheet-id/spreadsheet-name/label/selected-label/references
 * </pre>
 * The reference portion of a {@link walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping} is not present.
 */
public final class SpreadsheetLabelMappingReferencesHistoryToken extends SpreadsheetLabelMappingHistoryToken {

    static SpreadsheetLabelMappingReferencesHistoryToken with(final SpreadsheetId id,
                                                              final SpreadsheetName name,
                                                              final SpreadsheetLabelName labelName,
                                                              final HistoryTokenOffsetAndCount offsetAndCount) {
        return new SpreadsheetLabelMappingReferencesHistoryToken(
            id,
            name,
            labelName,
            offsetAndCount
        );
    }

    private SpreadsheetLabelMappingReferencesHistoryToken(final SpreadsheetId id,
                                                          final SpreadsheetName name,
                                                          final SpreadsheetLabelName labelName,
                                                          final HistoryTokenOffsetAndCount offsetAndCount) {
        super(
            id,
            name
        );
        this.labelName = Objects.requireNonNull(labelName, "labelName");
        this.offsetAndCount = Objects.requireNonNull(offsetAndCount, "offsetAndCount");
    }

    // @see HistoryToken.labelName
    final SpreadsheetLabelName labelName;

    final HistoryTokenOffsetAndCount offsetAndCount;

    // /Label123/references
    // /Label123/references/offset/1/count/2
    @Override
    UrlFragment labelUrlFragment() {
        final UrlFragment urlFragment = UrlFragment.with(
            this.labelName.value()
        ).appendSlashThen(REFERENCES);

        final HistoryTokenOffsetAndCount offsetAndCount = this.offsetAndCount;

        return offsetAndCount.isEmpty() ?
            urlFragment :
            urlFragment.append(offsetAndCount.urlFragment());
    }

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        return this;
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.labelMappingSelect(
            this.id(),
            this.name(),
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
            this.labelName,
            this.offsetAndCount
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // NOP
    }
}
