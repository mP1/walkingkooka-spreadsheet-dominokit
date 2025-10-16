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
 * /123/SpreadsheetName456/label/Label123
 * /spreadsheet-id/spreadsheet-name/label/STAR
 * /spreadsheet-id/spreadsheet-name/label/STAR/offset/0
 * /spreadsheet-id/spreadsheet-name/label/STAR/offset/0/count/1
 * /spreadsheet-id/spreadsheet-name/label/STAR/count/0
 * </pre>
 * The reference portion of a {@link walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping} is not present.
 */
public final class SpreadsheetLabelMappingListHistoryToken extends SpreadsheetLabelMappingHistoryToken {

    static SpreadsheetLabelMappingListHistoryToken with(final SpreadsheetId id,
                                                        final SpreadsheetName name,
                                                        final HistoryTokenOffsetAndCount offsetAndCount) {
        return new SpreadsheetLabelMappingListHistoryToken(
            id,
            name,
            offsetAndCount
        );
    }

    private SpreadsheetLabelMappingListHistoryToken(final SpreadsheetId id,
                                                    final SpreadsheetName name,
                                                    final HistoryTokenOffsetAndCount offsetAndCount) {
        super(
            id,
            name
        );
        this.offsetAndCount = Objects.requireNonNull(offsetAndCount, "offsetAndCount");
    }

    final HistoryTokenOffsetAndCount offsetAndCount;

    // /
    // /*/offset/123/count/456
    @Override
    UrlFragment labelUrlFragment() {
        return countAndOffsetUrlFragment(
            this.offsetAndCount,
            UrlFragment.EMPTY
        );
    }

    @Override
    HistoryToken parseNext(final String component,
                           final TextCursor cursor) {
        return this;
    }

    @Override
    public HistoryToken clearAction() {
        return spreadsheetSelect(
            this.id,
            this.name
        );
    }

    // new id/name same labelName
    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return with(
            id,
            name,
            this.offsetAndCount
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // NOP
    }
}
