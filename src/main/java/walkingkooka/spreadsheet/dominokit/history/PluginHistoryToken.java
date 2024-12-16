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

/**
 * Base class for all plugin {@link HistoryToken tokens}.
 */
public abstract class PluginHistoryToken extends HistoryToken {

    PluginHistoryToken() {
        super();
    }

    @Override //
    final HistoryToken replaceIdAndName(final SpreadsheetId id,
                                        final SpreadsheetName name) {
        return spreadsheetSelect(
                id,
                name
        );
    }

    // HasUrlFragment...................................................................................................

    @Override
    public final UrlFragment urlFragment() {
        return UrlFragment.SLASH.append(
                HistoryToken.PLUGIN
        ).appendSlashThen(this.pluginUrlFragment());
    }

    abstract UrlFragment pluginUrlFragment();
}
