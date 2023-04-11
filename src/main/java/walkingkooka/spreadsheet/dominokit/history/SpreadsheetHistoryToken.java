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
import walkingkooka.spreadsheet.dominokit.SpreadsheetMetadataWatcher;

import java.util.Optional;

/**
 * Instances represent a token within a history hash.
 */
public abstract class SpreadsheetHistoryToken extends HistoryToken implements SpreadsheetMetadataWatcher {

    final static UrlFragment CLEAR = UrlFragment.SLASH.append(UrlFragment.with("clear"));

    final static UrlFragment DELETE = UrlFragment.SLASH.append(UrlFragment.with("delete"));

    final static UrlFragment FREEZE = UrlFragment.SLASH.append(UrlFragment.with("freeze"));

    final static UrlFragment LABEL = UrlFragment.SLASH.append(UrlFragment.with("label"))
            .append(UrlFragment.SLASH);

    final static UrlFragment MENU = UrlFragment.SLASH.append(UrlFragment.with("menu"));

    final static UrlFragment SELECT = UrlFragment.EMPTY;

    final static UrlFragment SAVE = UrlFragment.SLASH
            .append(UrlFragment.with("save"))
            .append(UrlFragment.SLASH);

    final static UrlFragment STYLE = UrlFragment.SLASH
            .append(UrlFragment.with("style"))
            .append(UrlFragment.SLASH);

    final static UrlFragment UNFREEZE = UrlFragment.SLASH
            .append(UrlFragment.with("unfreeze"));


    SpreadsheetHistoryToken() {
        super();
    }

    /**
     * Creates a {@link UrlFragment} with a save and value.
     */
    final UrlFragment saveUrlFragment(final Object value) {
        return value instanceof Optional ?
                this.saveUrlFragmentOptional((Optional<?>) value) :
                this.saveUrlFragment0(value);
    }

    private UrlFragment saveUrlFragmentOptional(final Optional<?> value) {
        return this.saveUrlFragment0(value.orElse(null));
    }

    private UrlFragment saveUrlFragment0(final Object value) {
        final UrlFragment urlFragment;

        if (null == value) {
            urlFragment = UrlFragment.EMPTY;
        } else {
            if (value instanceof HasUrlFragment) {
                final HasUrlFragment has = (HasUrlFragment) value;
                urlFragment = has.urlFragment();
            } else {
                urlFragment = UrlFragment.with(
                        String.valueOf(
                                String.valueOf(value)
                        )
                );
            }
        }

        return SAVE.append(urlFragment);
    }
}
