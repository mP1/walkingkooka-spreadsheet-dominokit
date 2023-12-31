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

package walkingkooka.spreadsheet.dominokit.ui.textbox;

import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;

import java.util.Objects;
import java.util.Optional;

final class SpreadsheetTextBoxChangeListener implements ChangeListener<String> {

    static SpreadsheetTextBoxChangeListener with(final ChangeListener<Optional<String>> listener) {
        Objects.requireNonNull(listener, "listener");
        return new SpreadsheetTextBoxChangeListener(listener);
    }

    private SpreadsheetTextBoxChangeListener(final ChangeListener<Optional<String>> listener) {
        this.listener = listener;
    }

    @Override
    public void onValueChanged(final String oldValue,
                               final String newValue) {
        this.listener.onValueChanged(
                emptyToNull(oldValue),
                emptyToNull(newValue)
        );
    }

    private static Optional<String> emptyToNull(final String text) {
        return Optional.ofNullable(
                "".equals(text) ? null : text
        );
    }

    private final ChangeListener<Optional<String>> listener;

    @Override
    public String toString() {
        return this.listener.toString();
    }
}
