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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;

/**
 * This action saves the given spreadsheet name.
 */
public final class SpreadsheetRenameSaveHistoryToken extends SpreadsheetRenameHistoryToken implements Value<SpreadsheetName> {

    static SpreadsheetRenameSaveHistoryToken with(final SpreadsheetId spreadsheetId,
                                                  final SpreadsheetName spreadsheetName,
                                                  final SpreadsheetName value) {
        return new SpreadsheetRenameSaveHistoryToken(
            spreadsheetId,
            spreadsheetName,
            value
        );
    }

    private SpreadsheetRenameSaveHistoryToken(final SpreadsheetId spreadsheetId,
                                              final SpreadsheetName spreadsheetName,
                                              final SpreadsheetName value) {
        super(
            spreadsheetId,
            spreadsheetName
        );
        this.value = Objects.requireNonNull(value, "value");
    }

    @Override
    public SpreadsheetName value() {
        return this.value;
    }

    private final SpreadsheetName value;

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.spreadsheetRenameSelect(
            this.spreadsheetId,
            this.spreadsheetName
        );
    }

    @Override //
    HistoryToken replaceSpreadsheetIdAndSpreadsheetName(final SpreadsheetId spreadsheetId,
                                                        final SpreadsheetName spreadsheetName) {
        return new SpreadsheetRenameSaveHistoryToken(
            spreadsheetId,
            spreadsheetName,
            this.value
        );
    }

    @Override
    UrlFragment spreadsheetRenameUrlFragment() {
        return saveUrlFragment(this.value.urlFragment());
    }

    @Override
    HistoryToken parseNext(final String component,
                           final TextCursor cursor) {
        return this;
    }

    @Override
    public void onHistoryTokenChange0(final HistoryToken previous,
                                      final AppContext context) {
        context.pushHistoryToken(previous);
        context.spreadsheetMetadataFetcher()
            .patchMetadata(
                this.spreadsheetId,
                SpreadsheetMetadataPropertyName.SPREADSHEET_NAME.patch(
                    this.value()
                )
            );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitSpreadsheetRenameSave(
            this.spreadsheetId,
            this.spreadsheetName,
            this.value
        );
    }
}
