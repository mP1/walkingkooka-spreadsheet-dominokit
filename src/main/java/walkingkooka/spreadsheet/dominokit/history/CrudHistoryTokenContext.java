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

/**
 * A {@link CloseableHistoryTokenContext} that contains a basic operations required by an editing dialog ui with basic default implementations.
 */
public interface CrudHistoryTokenContext<T> extends CloseableHistoryTokenContext {

    /**
     * Deletes the current value.
     */
    default void delete() {
        this.pushHistoryToken(
                this.historyToken()
                        .setDelete()
        );
    }

    /**
     * Saves the given value.
     */
    void save(final T value);
}
