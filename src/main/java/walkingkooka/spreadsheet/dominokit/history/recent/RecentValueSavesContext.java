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

package walkingkooka.spreadsheet.dominokit.history.recent;

import walkingkooka.Context;

import java.util.List;

/**
 * A {@link Context} that may be used to query recent value saves.
 */
public interface RecentValueSavesContext extends Context {

    /**
     * Retrieves the most recent saves for the given {@link Class}
     */
    <T> List<T> recentSaves(final Class<T> type);
}
