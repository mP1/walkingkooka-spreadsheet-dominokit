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

package walkingkooka.spreadsheet.dominokit.net;

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;

public final class ExpressionFunctionFetcherWatchers extends FetcherWatchers<ExpressionFunctionFetcherWatcher>
        implements ExpressionFunctionFetcherWatcher {

    private ExpressionFunctionFetcherWatchers() {
        super();
    }

    public static ExpressionFunctionFetcherWatchers empty() {
        return new ExpressionFunctionFetcherWatchers();
    }

    @Override
    public void onExpressionFunctionInfoSet(final SpreadsheetId id,
                                            final ExpressionFunctionInfoSet infos,
                                            final AppContext context) {
        this.fire(
                ExpressionFunctionFetcherWatchersInfoSetEvent.with(
                        id,
                        infos,
                        context
                )
        );
    }
}
