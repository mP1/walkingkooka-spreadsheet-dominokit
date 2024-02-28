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

import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.UrlFragment;

public enum SpreadsheetCellPropertySelector implements HasUrlFragment {

    ALL("/*"),

    FORMULA("/formula"),

    FORMAT_PATTERN("/format-pattern"),

    PARSE_PATTERN("/parse-pattern"),

    STYLE("/style"),

    VALUE("/value");

    SpreadsheetCellPropertySelector(final String urlFragment) {
        this.urlFragment = UrlFragment.parse(urlFragment);
    }

    @Override
    public UrlFragment urlFragment() {
        return this.urlFragment;
    }

    private final UrlFragment urlFragment;
}
