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

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;

public final class SpreadsheetDeltaWatchersTest implements ClassTesting<SpreadsheetDeltaWatchers> {

    @Test
    public void testAddThenFire() {
        final SpreadsheetDelta spreadsheetDelta = SpreadsheetDelta.EMPTY;
        final AppContext appContext = new FakeAppContext();

        final SpreadsheetDeltaWatchers watchers = SpreadsheetDeltaWatchers.empty();
        watchers.add(
                new SpreadsheetDeltaWatcher() {
                    @Override
                    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                                   final AppContext context) {
                        SpreadsheetDeltaWatchersTest.this.checkEquals(spreadsheetDelta, delta);
                        SpreadsheetDeltaWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetDeltaWatchersTest.this.fired = true;
                    }
                });
        watchers.onSpreadsheetDelta(spreadsheetDelta, appContext);

        this.checkEquals(true, this.fired);
    }

    private boolean fired = false;

    // ClassTesting....................................................................................................

    @Override
    public Class<SpreadsheetDeltaWatchers> type() {
        return SpreadsheetDeltaWatchers.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
