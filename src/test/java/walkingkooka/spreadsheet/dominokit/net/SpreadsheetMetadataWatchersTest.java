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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

public final class SpreadsheetMetadataWatchersTest implements ClassTesting<SpreadsheetMetadataWatchers> {

    @Test
    public void testAddThenFire() {
        final SpreadsheetMetadata spreadsheetMetadata = SpreadsheetMetadata.EMPTY;
        final AppContext appContext = new FakeAppContext();

        final SpreadsheetMetadataWatchers watchers = SpreadsheetMetadataWatchers.empty();
        watchers.add(
                new SpreadsheetMetadataWatcher() {
                    @Override
                    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                                      final AppContext context) {
                        SpreadsheetMetadataWatchersTest.this.checkEquals(spreadsheetMetadata, metadata);
                        SpreadsheetMetadataWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetMetadataWatchersTest.this.fired = true;
                    }
                });
        watchers.onSpreadsheetMetadata(spreadsheetMetadata, appContext);

        this.checkEquals(true, this.fired);
    }

    private boolean fired = false;

    // ClassTesting....................................................................................................

    @Override
    public Class<SpreadsheetMetadataWatchers> type() {
        return SpreadsheetMetadataWatchers.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
