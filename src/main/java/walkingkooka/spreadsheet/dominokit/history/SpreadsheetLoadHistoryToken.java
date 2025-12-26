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
import walkingkooka.spreadsheet.dominokit.AppContext;
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

    static SpreadsheetLoadHistoryToken with(final SpreadsheetId id) {
        return new SpreadsheetLoadHistoryToken(
            id
        );
    }

    private SpreadsheetLoadHistoryToken(final SpreadsheetId id) {
        super(id);
    }

    @Override
    UrlFragment spreadsheetUrlFragment() {
        return this.id.urlFragment();
    }

    @Override
    HistoryToken parseNext(final String component,
                           final TextCursor cursor) {
        return spreadsheetSelect(
            this.id,
            SpreadsheetName.with(component)
        ).parse(cursor);
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.spreadsheetListSelect(HistoryTokenOffsetAndCount.EMPTY);
    }

    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return spreadsheetSelect(
            id,
            name
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
            this.id,
            previous
        );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitSpreadsheetLoad();
    }
}
