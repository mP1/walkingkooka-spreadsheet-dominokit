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

import java.util.Optional;

/**
 * Instances represent a token within a history hash.
 */
public abstract class SpreadsheetHistoryToken extends HistoryToken {

    SpreadsheetHistoryToken() {
        super();
    }

    // HasUrlFragment...................................................................................................

    @Override
    public final UrlFragment urlFragment() {
        return UrlFragment.SLASH.append(
                this.spreadsheetUrlFragment()
        );
    }

    abstract UrlFragment spreadsheetUrlFragment();

    // save helpers.....................................................................................................

    /**
     * Creates a {@link UrlFragment} with a save returning a {@link UrlFragment} with its equivalent value.
     */
    final UrlFragment saveUrlFragment(final Object value) {
        // always want slash after SAVE
        return SAVE.append(UrlFragment.SLASH)
                .append(
                        this.saveUrlFragmentValue(value)
                );
    }

    private UrlFragment saveUrlFragmentValue(final Object value) {
        return null == value ?
                UrlFragment.EMPTY :
                value instanceof UrlFragment ?
                        (UrlFragment) value :
                        value instanceof HasUrlFragment ?
                                ((HasUrlFragment) value)
                                        .urlFragment() :
                                value instanceof Optional ?
                                        this.saveUrlFragmentValueOptional((Optional<?>) value) :
                                        UrlFragment.with(
                                                String.valueOf(value)
                                        );
    }

    private UrlFragment saveUrlFragmentValueOptional(final Optional<?> value) {
        return this.saveUrlFragmentValue(
                value.orElse(null)
        );
    }
}
