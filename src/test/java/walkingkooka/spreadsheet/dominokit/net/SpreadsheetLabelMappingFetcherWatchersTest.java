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
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetLabelMappingFetcherWatchersTest implements ClassTesting<SpreadsheetLabelMappingFetcherWatchers> {

    @Test
    public void testAddThenFire() {
        this.fired = 0;

        final Optional<SpreadsheetLabelMapping> spreadsheetLabelMapping = Optional.of(
                SpreadsheetSelection.labelName("Label123")
                        .mapping(SpreadsheetSelection.A1)
        );
        final AppContext appContext = new FakeAppContext();

        final SpreadsheetLabelMappingFetcherWatchers watchers = SpreadsheetLabelMappingFetcherWatchers.empty();
        watchers.add(
                new SpreadsheetLabelMappingFetcherWatcher() {
                    @Override
                    public void onSpreadsheetLabelMapping(final Optional<SpreadsheetLabelMapping> mapping,
                                                          final AppContext context) {
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(spreadsheetLabelMapping, mapping);
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetLabelMappingFetcherWatchersTest.this.fired++;
                    }
                });
        watchers.onSpreadsheetLabelMapping(spreadsheetLabelMapping, appContext);

        this.checkEquals(1, this.fired);
    }

    @Test
    public void testAddOnce() {
        this.fired = 0;

        final Optional<SpreadsheetLabelMapping> spreadsheetLabelMapping = Optional.of(
                SpreadsheetSelection.labelName("Label123")
                        .mapping(SpreadsheetSelection.A1)
        );
        final AppContext appContext = new FakeAppContext();

        final SpreadsheetLabelMappingFetcherWatchers watchers = SpreadsheetLabelMappingFetcherWatchers.empty();
        watchers.addOnce(
                new SpreadsheetLabelMappingFetcherWatcher() {
                    @Override
                    public void onSpreadsheetLabelMapping(final Optional<SpreadsheetLabelMapping> mapping,
                                                          final AppContext context) {
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(spreadsheetLabelMapping, mapping);
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetLabelMappingFetcherWatchersTest.this.fired++;
                    }
                });
        watchers.onSpreadsheetLabelMapping(spreadsheetLabelMapping, appContext);
        this.checkEquals(1, this.fired);

        watchers.onSpreadsheetLabelMapping(spreadsheetLabelMapping, appContext);
        this.checkEquals(1, this.fired);

        watchers.onSpreadsheetLabelMapping(spreadsheetLabelMapping, appContext);
        this.checkEquals(1, this.fired);
    }

    private int fired = 0;

    // ClassTesting....................................................................................................

    @Override
    public Class<SpreadsheetLabelMappingFetcherWatchers> type() {
        return SpreadsheetLabelMappingFetcherWatchers.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
