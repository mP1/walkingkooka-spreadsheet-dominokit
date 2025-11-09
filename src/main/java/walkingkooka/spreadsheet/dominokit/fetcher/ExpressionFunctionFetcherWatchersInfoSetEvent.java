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

package walkingkooka.spreadsheet.dominokit.fetcher;

import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;

/**
 * The event payload used by {@link ExpressionFunctionFetcherWatchers}.
 */
final class ExpressionFunctionFetcherWatchersInfoSetEvent extends FetcherWatchersEvent<ExpressionFunctionFetcherWatcher> {

    static ExpressionFunctionFetcherWatchersInfoSetEvent with(final ExpressionFunctionInfoSet infos) {
        return new ExpressionFunctionFetcherWatchersInfoSetEvent(infos);
    }

    private ExpressionFunctionFetcherWatchersInfoSetEvent(final ExpressionFunctionInfoSet infos) {
        super();
        this.infos = infos;
    }

    @Override
    void fire(final ExpressionFunctionFetcherWatcher watcher) {
        watcher.onExpressionFunctionInfoSet(this.infos);
    }

    private final ExpressionFunctionInfoSet infos;

    @Override
    public String toString() {
        return this.infos.toString();
    }
}
