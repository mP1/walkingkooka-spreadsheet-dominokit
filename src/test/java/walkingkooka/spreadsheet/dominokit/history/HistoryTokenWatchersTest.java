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

import org.junit.jupiter.api.Test;
import walkingkooka.net.UrlFragment;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.AppContexts;

public final class HistoryTokenWatchersTest implements ClassTesting<HistoryTokenWatchers> {

    @Test
    public void testAddThenFire() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.EMPTY);
        final AppContext appContext = AppContexts.fake();

        final HistoryTokenWatchers watchers = HistoryTokenWatchers.empty();
        watchers.add(
                new HistoryTokenWatcher() {
                    @Override
                    public void onHistoryTokenChange(final HistoryToken previous,
                                                     final AppContext context) {
                        HistoryTokenWatchersTest.this.checkEquals(historyToken, previous);
                        HistoryTokenWatchersTest.this.checkEquals(appContext, context);

                        HistoryTokenWatchersTest.this.fired = true;
                    }
                });
        watchers.onHistoryTokenChange(historyToken, appContext);

        this.checkEquals(true, this.fired);
    }

    private boolean fired = false;

    // ClassTesting....................................................................................................

    @Override
    public Class<HistoryTokenWatchers> type() {
        return HistoryTokenWatchers.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
