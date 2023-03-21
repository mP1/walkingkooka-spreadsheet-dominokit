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

import java.util.Objects;

/**
 * Instances represent a token within a history hash including the {@link SpreadsheetId}
 */
public abstract class SpreadsheetIdHistoryToken extends SpreadsheetHistoryToken {

    SpreadsheetIdHistoryToken(final SpreadsheetId id) {
        super();

        this.id = Objects.requireNonNull(id, "id");
    }

    public final SpreadsheetId id() {
        return this.id;
    }

    private final SpreadsheetId id;

    @Override
    public final UrlFragment urlFragment() {
        return this.id.urlFragment()
                .append(this.spreadsheetIdUrlFragment());
    }

    /**
     * Sub-classes should append additional components to the final {@link UrlFragment}.
     */
    abstract UrlFragment spreadsheetIdUrlFragment();

    @Override
    public final SpreadsheetHistoryToken setIdAndName(final SpreadsheetId id,
                                                      final SpreadsheetName name) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");

        return this.setIdAndName0(
                id,
                name
        );
    }

    abstract SpreadsheetHistoryToken setIdAndName0(final SpreadsheetId id,
                                                   final SpreadsheetName name);
}
