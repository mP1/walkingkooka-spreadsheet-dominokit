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

    final static UrlFragment CLEAR = UrlFragment.parse("/clear");

    final static UrlFragment COPY = UrlFragment.parse("/copy");

    final static UrlFragment CUT = UrlFragment.parse("/cut");

    final static UrlFragment DELETE = UrlFragment.parse("/delete");

    final static UrlFragment FIND = UrlFragment.parse("/find");

    final static UrlFragment FREEZE = UrlFragment.parse("/freeze");

    final static UrlFragment INSERT_AFTER = UrlFragment.parse("/insertAfter");

    final static UrlFragment INSERT_BEFORE = UrlFragment.parse("/insertBefore");

    final static UrlFragment LABEL = UrlFragment.parse("/label");

    final static UrlFragment MENU = UrlFragment.parse("/menu");

    final static UrlFragment PASTE = UrlFragment.parse("/paste");

    final static UrlFragment SELECT = UrlFragment.EMPTY;

    final static UrlFragment SAVE = UrlFragment.parse("/save/");

    final static UrlFragment STYLE = UrlFragment.parse("/style/");

    final static UrlFragment UNFREEZE = UrlFragment.parse("/unfreeze");


    SpreadsheetHistoryToken() {
        super();
    }

    /**
     * Creates a {@link UrlFragment} with a save and value.
     */
    final UrlFragment saveUrlFragment(final Object value) {
        return null == value ?
                this.saveUrlFragment(UrlFragment.EMPTY) :
                value instanceof HasUrlFragment ?
                        this.saveUrlFragmentUrlFragment(
                                ((HasUrlFragment) value)
                                        .urlFragment()
                        ) :
                        value instanceof Optional ?
                                this.saveUrlFragmentOptional((Optional<?>) value) :
                                this.saveUrlFragmentNonNull(value);
    }

    private UrlFragment saveUrlFragmentOptional(final Optional<?> value) {
        return this.saveUrlFragment(
                value.orElse(null)
        );
    }

    private UrlFragment saveUrlFragmentNonNull(final Object value) {
        return this.saveUrlFragmentUrlFragment(
                UrlFragment.with(
                        String.valueOf(value)
                )
        );
    }

    private UrlFragment saveUrlFragmentUrlFragment(final UrlFragment urlFragment) {
        return SAVE.append(urlFragment);
    }
}
