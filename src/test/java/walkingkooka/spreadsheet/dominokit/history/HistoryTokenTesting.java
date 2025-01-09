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

import walkingkooka.test.Testing;
import walkingkooka.text.CharSequences;

import java.util.Optional;

public interface HistoryTokenTesting extends Testing {

    default void closeAndCheck(final HistoryToken opened,
                               final HistoryToken closed) {
        this.checkEquals(
            closed,
            opened.close(),
            () -> opened + " close"
        );

        this.checkNotEquals(
            opened,
            closed,
            () -> "opened " + opened + " must be different from closed " + closed
        );
    }

    default void setSaveValueAndCheck(final HistoryToken selected,
                                      final Optional<?> saveValue,
                                      final HistoryToken saved) {
        this.checkEquals(
            saved,
            selected.setSaveValue(saveValue),
            () -> selected + " save " + CharSequences.quoteIfChars(saveValue.orElse(null))
        );

        this.checkNotEquals(
            selected,
            saved,
            () -> "selected " + selected + " must be different from saved " + saved
        );
    }
}
