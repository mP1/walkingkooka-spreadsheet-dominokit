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

package walkingkooka.spreadsheet.dominokit;

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.test.Testing;

public interface ComponentLifecycleMatcherTesting extends Testing {

    SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1);

    SpreadsheetName SPREADSHEET_NAME = SpreadsheetName.with("SpreadsheetName1");

    default void shouldIgnoreAndCheck(final ComponentLifecycleMatcher matcher,
                                      final HistoryToken token,
                                      final boolean expected) {
        this.checkEquals(
            expected,
            matcher.shouldIgnore(token),
            () -> matcher + " should ignore " + token
        );
    }

    default void isMatchAndCheck(final ComponentLifecycleMatcher matcher,
                                 final HistoryToken token,
                                 final boolean expected) {
        this.checkEquals(
            expected,
            matcher.isMatch(token),
            token::toString
        );

        if(expected) {
            this.checkEquals(
                false,
                matcher.shouldIgnore(token),
                () -> matcher + " isMatch " + token
            );
        }
    }
}
