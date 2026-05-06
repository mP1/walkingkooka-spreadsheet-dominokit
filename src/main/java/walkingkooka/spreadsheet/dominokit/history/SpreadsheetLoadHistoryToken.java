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

import java.util.Objects;
import java.util.Optional;

/**
 * Captures loading a spreadsheet using its spreadsheet-id
 * <pre>
 * /123
 * /spreadsheet-id
 * </pre>
 */
public final class SpreadsheetLoadHistoryToken extends SpreadsheetIdHistoryToken implements HistoryTokenWatcher {

    static SpreadsheetLoadHistoryToken with(final SpreadsheetId spreadsheetId) {
        return new SpreadsheetLoadHistoryToken(
            spreadsheetId
        );
    }

    private SpreadsheetLoadHistoryToken(final SpreadsheetId spreadsheetId) {
        super(spreadsheetId);
    }

    @Override
    UrlFragment spreadsheetUrlFragment() {
        return this.spreadsheetId.urlFragment();
    }

    @Override
    HistoryToken parseNext(final String component,
                           final TextCursor cursor) {
        return spreadsheetSelect(
            this.spreadsheetId,
            SpreadsheetName.with(component)
        ).parse(cursor);
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.spreadsheetListSelect(HistoryTokenOffsetAndCount.EMPTY);
    }

    @Override //
    HistoryToken replaceSpreadsheetIdAndSpreadsheetName(final SpreadsheetId spreadsheetId,
                                                        final SpreadsheetName spreadsheetName) {
        return spreadsheetSelect(
            spreadsheetId,
            spreadsheetName
        );
    }

    @Override
    public HistoryToken setSaveValue(final Optional<?> value) {
        Objects.requireNonNull(value, "value");

        return this;
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        context.loadSpreadsheetMetadataAndPushPreviousIfFails(
            this.spreadsheetId,
            previous
        );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitSpreadsheetLoad();
    }
}
