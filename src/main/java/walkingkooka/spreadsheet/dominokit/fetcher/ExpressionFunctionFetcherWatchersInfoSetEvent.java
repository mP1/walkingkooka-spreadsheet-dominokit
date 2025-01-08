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

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;

/**
 * The event payload used by {@link ExpressionFunctionFetcherWatchers}.
 */
final class ExpressionFunctionFetcherWatchersInfoSetEvent extends FetcherWatchersEvent<ExpressionFunctionFetcherWatcher> {

    private final SpreadsheetId id;
    private final ExpressionFunctionInfoSet infos;

    private ExpressionFunctionFetcherWatchersInfoSetEvent(final SpreadsheetId id,
                                                          final ExpressionFunctionInfoSet infos,
                                                          final AppContext context) {
        super(context);
        this.id = id;
        this.infos = infos;
    }

    static ExpressionFunctionFetcherWatchersInfoSetEvent with(final SpreadsheetId id,
                                                              final ExpressionFunctionInfoSet infos,
                                                              final AppContext context) {
        return new ExpressionFunctionFetcherWatchersInfoSetEvent(
            id,
            infos,
            context
        );
    }

    @Override
    void fire(final ExpressionFunctionFetcherWatcher watcher) {
        watcher.onExpressionFunctionInfoSet(
            this.id,
            this.infos,
            this.context
        );
    }

    @Override
    public String toString() {
        return this.id + " " + this.infos;
    }
}
