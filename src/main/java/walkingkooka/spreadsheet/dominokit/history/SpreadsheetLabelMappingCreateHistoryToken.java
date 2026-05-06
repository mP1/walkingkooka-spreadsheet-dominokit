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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.text.cursor.TextCursor;

/**
 * Displays a modal dialog with a form that allows editing of a {@link walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping}
 * <pre>
 * /123/SpreadsheetName456/label-create
 * </pre>
 */
public final class SpreadsheetLabelMappingCreateHistoryToken extends SpreadsheetLabelMappingHistoryToken {

    static SpreadsheetLabelMappingCreateHistoryToken with(final SpreadsheetId spreadsheetId,
                                                          final SpreadsheetName spreadsheetName) {
        return new SpreadsheetLabelMappingCreateHistoryToken(
            spreadsheetId,
            spreadsheetName
        );
    }

    private SpreadsheetLabelMappingCreateHistoryToken(final SpreadsheetId spreadsheetId,
                                                      final SpreadsheetName spreadsheetName) {
        super(
            spreadsheetId,
            spreadsheetName
        );
    }

    //
    // Label123
    @Override
    UrlFragment labelUrlFragment() {
        return LABEL_CREATE;
    }

    @Override
    HistoryToken parseNext(final String component,
                           final TextCursor cursor) {
        return this;
    }

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    // new id/name same labelName
    @Override //
    HistoryToken replaceSpreadsheetIdAndSpreadsheetName(final SpreadsheetId spreadsheetId,
                                                        final SpreadsheetName spreadsheetName) {
        return with(
            spreadsheetId,
            spreadsheetName
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // show label mapping UI
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitSpreadsheetLabelMappingCreate(
            this.spreadsheetId,
            this.spreadsheetName
        );
    }
}
