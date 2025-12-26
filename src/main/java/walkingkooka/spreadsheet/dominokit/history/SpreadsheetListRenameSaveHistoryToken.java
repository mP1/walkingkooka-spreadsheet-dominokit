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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;

/**
 * Represents an action where the user has renamed a spreadsheet from the list spreadsheet dialog.
 * <pre>
 * /rename/123/save/new-spreadsheet-name-here
 * </pre>
 */
public final class SpreadsheetListRenameSaveHistoryToken extends SpreadsheetListRenameHistoryToken implements Value<SpreadsheetName>,
    HistoryTokenWatcher {

    static SpreadsheetListRenameSaveHistoryToken with(final SpreadsheetId id,
                                                      final SpreadsheetName value) {
        return new SpreadsheetListRenameSaveHistoryToken(
            id,
            value
        );
    }

    private SpreadsheetListRenameSaveHistoryToken(final SpreadsheetId id,
                                                  final SpreadsheetName value) {
        super(
            id
        );
        this.value = Objects.requireNonNull(value, "value");
    }

    @Override
    public SpreadsheetName value() {
        return this.value;
    }

    public SpreadsheetListRenameSaveHistoryToken setValue(final SpreadsheetName name) {
        Objects.requireNonNull(name, "name");

        return this.value.equals(name) ?
            this :
            new SpreadsheetListRenameSaveHistoryToken(
                this.id,
                name
            );
    }

    private final SpreadsheetName value;

    @Override
    public UrlFragment renameUrlFragment() {
        return saveUrlFragment(this.value);
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.spreadsheetListRenameSelect(
            this.id
        );
    }

    @Override
    HistoryToken parseNext(final String component,
                           final TextCursor cursor) {
        return this; // ignore tokens after /save/SpreadsheetName
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        context.pushHistoryToken(previous);
        context.spreadsheetMetadataFetcher()
            .patchMetadata(
                this.id,
                SpreadsheetMetadataPropertyName.SPREADSHEET_NAME.patch(
                    this.value()
                )
            );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitSpreadsheetListRenameSave(
            this.id,
            this.value
        );
    }
}
