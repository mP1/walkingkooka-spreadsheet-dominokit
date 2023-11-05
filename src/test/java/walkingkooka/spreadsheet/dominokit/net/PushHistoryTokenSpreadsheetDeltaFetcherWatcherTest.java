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

import org.junit.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PushHistoryTokenSpreadsheetDeltaFetcherWatcherTest implements ToStringTesting<PushHistoryTokenSpreadsheetDeltaFetcherWatcher>,
        ClassTesting<PushHistoryTokenSpreadsheetDeltaFetcherWatcher> {

    @Test
    public void testWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> PushHistoryTokenSpreadsheetDeltaFetcherWatcher.with(null)
        );
    }

    @Test
    public void testOnSpreadsheetDelta() {
        final List<HistoryToken> pushed = Lists.array();

        final HistoryToken historyToken = HistoryToken.column(
                SpreadsheetId.with(222),
                SpreadsheetName.with("Hello222"),
                SpreadsheetSelection.parseColumn("ABC").setDefaultAnchor()
        );

        PushHistoryTokenSpreadsheetDeltaFetcherWatcher.with(historyToken)
                .onSpreadsheetDelta(
                        SpreadsheetDelta.EMPTY,
                        new FakeAppContext() {

                            @Override
                            public HistoryToken historyToken() {
                                return historyToken.setDelete();
                            }

                            @Override
                            public void pushHistoryToken(final HistoryToken token) {
                                pushed.add(token);
                            }
                        }
                );

        this.checkEquals(
                Lists.of(historyToken),
                pushed
        );
    }

    @Test
    public void testToString() {
        final HistoryToken historyToken = HistoryToken.metadataSelect(
                SpreadsheetId.with(111),
                SpreadsheetName.with("Name222")
        );

        this.toStringAndCheck(
                PushHistoryTokenSpreadsheetDeltaFetcherWatcher.with(historyToken),
                historyToken + ""
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<PushHistoryTokenSpreadsheetDeltaFetcherWatcher> type() {
        return PushHistoryTokenSpreadsheetDeltaFetcherWatcher.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
