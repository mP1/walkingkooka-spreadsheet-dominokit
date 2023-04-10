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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.text.cursor.TextCursor;

import java.util.Optional;

/**
 * A token that represents a spreadsheet create action.
 */
public final class SpreadsheetCreateHistoryToken extends SpreadsheetHistoryToken {

    static SpreadsheetCreateHistoryToken with() {
        return new SpreadsheetCreateHistoryToken();
    }

    private SpreadsheetCreateHistoryToken() {
        super();
    }

    @Override
    public UrlFragment urlFragment() {
        return UrlFragment.SLASH;
    }

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        return component.length() > 0 ?
                spreadsheetLoad(
                        SpreadsheetId.parse(component)
                ).parse(cursor) :
                this;
    }

    @Override
    SpreadsheetHistoryToken setIdNameViewportSelection0(final SpreadsheetId id,
                                                        final SpreadsheetName name,
                                                        final SpreadsheetViewportSelection viewportSelection) {
        // shouldnt happen...
        return spreadsheetLoad(id);
    }

    @Override
    public void onHashChange(final HistoryToken previous,
                             final AppContext context) {
        context.spreadsheetMetadataFetcher()
                .createSpreadsheetMetadata();
    }

    /**
     * When the spreadsheet is created and a new {@link SpreadsheetMetadata} is returned update the history token.
     */
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        final Optional<SpreadsheetId> id = metadata.id();
        final Optional<SpreadsheetName> name = metadata.name();

        if (id.isPresent() && name.isPresent()) {
            context.pushHistoryToken(
                    spreadsheetSelect(
                            id.get(),
                            name.get()
                    )
            );
        }
    }
}
