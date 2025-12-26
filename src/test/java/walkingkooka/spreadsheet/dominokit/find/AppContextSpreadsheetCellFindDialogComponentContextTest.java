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

package walkingkooka.spreadsheet.dominokit.find;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AppContextSpreadsheetCellFindDialogComponentContextTest implements SpreadsheetCellFindDialogComponentContextTesting<AppContextSpreadsheetCellFindDialogComponentContext>,
    SpreadsheetMetadataTesting {

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppContextSpreadsheetCellFindDialogComponentContext.with(null)
        );
    }

    // cellLabels.......................................................................................................

    @Test
    public void testCellLabelsWithCell() {
        final SpreadsheetLabelName label = SpreadsheetSelection.labelName("Label111");

        final SpreadsheetExpressionReference spreadsheetExpressionReference = SpreadsheetSelection.A1;

        this.cellLabelsAndCheck(
            AppContextSpreadsheetCellFindDialogComponentContext.with(
                new TestAppContext() {

                    @Override
                    public Optional<SpreadsheetSelection> resolveIfLabel(final SpreadsheetSelection selection) {
                        return Optional.of(selection);
                    }

                    @Override
                    public SpreadsheetViewportCache spreadsheetViewportCache() {
                        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty(this);

                        cache.onSpreadsheetMetadata(
                            METADATA_EN_AU.set(
                                SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                                SpreadsheetId.with(1)
                            )
                        );

                        cache.onSpreadsheetDelta(
                            HttpMethod.GET,
                            Url.parseRelative("/api/spreadsheet/1/SpreadsheetName2/cell/"),
                            SpreadsheetDelta.EMPTY.setLabels(
                                Sets.of(
                                    label.setLabelMappingReference(spreadsheetExpressionReference)
                                )
                            )
                        );
                        return cache;
                    }
                }
            ),
            spreadsheetExpressionReference,
            label
        );
    }

    @Test
    public void testCellLabelsWithLabel() {
        final SpreadsheetLabelName label1 = SpreadsheetSelection.labelName("Label111");
        final SpreadsheetLabelName label2 = SpreadsheetSelection.labelName("Label222");
        final SpreadsheetCellReference label2Target = SpreadsheetSelection.A1;

        this.cellLabelsAndCheck(
            AppContextSpreadsheetCellFindDialogComponentContext.with(
                new TestAppContext() {

                    @Override
                    public Optional<SpreadsheetSelection> resolveIfLabel(final SpreadsheetSelection selection) {
                        checkEquals(
                            label1,
                            selection,
                            "resolveIfLabel.selection"
                        );
                        return Optional.of(selection);
                    }

                    @Override
                    public SpreadsheetViewportCache spreadsheetViewportCache() {
                        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty(this);

                        cache.onSpreadsheetMetadata(
                            METADATA_EN_AU.set(
                                SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                                SpreadsheetId.with(1)
                            )
                        );

                        cache.onSpreadsheetDelta(
                            HttpMethod.GET,
                            Url.parseRelative("/api/spreadsheet/1/SpreadsheetName2/cell/"),
                            SpreadsheetDelta.EMPTY.setLabels(
                                Sets.of(
                                    label1.setLabelMappingReference(label2),
                                    label2.setLabelMappingReference(label2Target)
                                )
                            )
                        );
                        return cache;
                    }
                }
            ),
            label1,
            label1,
            label2
        );
    }

    @Test
    public void testCellLabelsWithUnknownLabel() {
        final SpreadsheetLabelName label1 = SpreadsheetSelection.labelName("Label111");

        this.cellLabelsAndCheck(
            AppContextSpreadsheetCellFindDialogComponentContext.with(
                new TestAppContext() {

                    @Override
                    public Optional<SpreadsheetSelection> resolveIfLabel(final SpreadsheetSelection selection) {
                        throw new IllegalArgumentException("Unknown label=" + selection);
                    }

                    @Override
                    public SpreadsheetViewportCache spreadsheetViewportCache() {
                        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty(this);

                        cache.onSpreadsheetMetadata(
                            METADATA_EN_AU.set(
                                SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                                SpreadsheetId.with(1)
                            )
                        );

                        cache.onSpreadsheetDelta(
                            HttpMethod.GET,
                            Url.parseRelative("/api/spreadsheet/1/SpreadsheetName2/cell/"),
                            SpreadsheetDelta.EMPTY
                        );
                        return cache;
                    }
                }
            ),
            label1
        );
    }

    // cellReferences...................................................................................................

    // context..........................................................................................................

    @Override
    public AppContextSpreadsheetCellFindDialogComponentContext createContext() {
        return AppContextSpreadsheetCellFindDialogComponentContext.with(AppContexts.fake());
    }

    abstract static class TestAppContext extends FakeAppContext {

        TestAppContext() {
            super();
        }

        @Override
        public final Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return null;
        }

        @Override
        public final Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
            return null;
        }

        @Override
        public final Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
            return null;
        }

        @Override
        public final void debug(final Object... values) {
            System.out.println(Arrays.toString(values));
        }
    }

    @Override
    public String typeNameSuffix() {
        return SpreadsheetCellFindDialogComponentContext.class.getSimpleName();
    }

    // class............................................................................................................

    @Override
    public Class<AppContextSpreadsheetCellFindDialogComponentContext> type() {
        return AppContextSpreadsheetCellFindDialogComponentContext.class;
    }
}
