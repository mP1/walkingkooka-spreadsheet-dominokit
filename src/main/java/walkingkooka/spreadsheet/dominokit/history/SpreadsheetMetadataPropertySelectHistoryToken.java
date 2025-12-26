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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetName;

public final class SpreadsheetMetadataPropertySelectHistoryToken<T> extends SpreadsheetMetadataPropertyHistoryToken<T> {

    static <T> SpreadsheetMetadataPropertySelectHistoryToken<T> with(final SpreadsheetId id,
                                                                     final SpreadsheetName name,
                                                                     final SpreadsheetMetadataPropertyName<T> propertyName) {
        return new SpreadsheetMetadataPropertySelectHistoryToken<>(
            id,
            name,
            propertyName
        );
    }

    private SpreadsheetMetadataPropertySelectHistoryToken(final SpreadsheetId id,
                                                          final SpreadsheetName name,
                                                          final SpreadsheetMetadataPropertyName<T> propertyName) {
        super(
            id,
            name,
            propertyName
        );
    }

    @Override
    UrlFragment metadataPropertyUrlFragment() {
        return SELECT;
    }

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    // new id/name but still metadata+property select
    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return with(
            id,
            name,
            this.propertyName
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // show metadata edit UI
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitMetadataPropertySelect(
            this.id,
            this.name,
            this.propertyName
        );
    }
}
