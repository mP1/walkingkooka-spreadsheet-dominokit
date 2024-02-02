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

package walkingkooka.spreadsheet.dominokit.ui.integerbox;

import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;

import java.util.Objects;
import java.util.Optional;

final class SpreadsheetIntegerBoxChangeListener implements ChangeListener<Integer> {

    static SpreadsheetIntegerBoxChangeListener with(final ChangeListener<Optional<Integer>> listener) {
        Objects.requireNonNull(listener, "listener");
        return new SpreadsheetIntegerBoxChangeListener(listener);
    }

    private SpreadsheetIntegerBoxChangeListener(final ChangeListener<Optional<Integer>> listener) {
        this.listener = listener;
    }

    @Override
    public void onValueChanged(final Integer oldValue,
                               final Integer newValue) {
        this.listener.onValueChanged(
                Optional.ofNullable(oldValue),
                Optional.ofNullable(newValue)
        );
    }

    private final ChangeListener<Optional<Integer>> listener;

    @Override
    public String toString() {
        return this.listener.toString();
    }
}
