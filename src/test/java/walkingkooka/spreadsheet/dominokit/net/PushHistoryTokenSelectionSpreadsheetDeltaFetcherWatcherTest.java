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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PushHistoryTokenSelectionSpreadsheetDeltaFetcherWatcherTest implements ToStringTesting<PushHistoryTokenSelectionSpreadsheetDeltaFetcherWatcher>,
        ClassTesting<PushHistoryTokenSelectionSpreadsheetDeltaFetcherWatcher> {

    @Test
    public void testWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> PushHistoryTokenSelectionSpreadsheetDeltaFetcherWatcher.with(null)
        );
    }

    @Test
    public void testOnSpreadsheetDelta() {
        final List<HistoryToken> pushed = Lists.array();

        final AnchoredSpreadsheetSelection anchored = SpreadsheetSelection.A1.setDefaultAnchor();

        final SpreadsheetId id = SpreadsheetId.with(123);
        final SpreadsheetName name = SpreadsheetName.with("Hello456");

        final HistoryToken historyToken = HistoryToken.column(
                id,
                name,
                SpreadsheetSelection.parseColumn("ABC").setDefaultAnchor()
        );

        PushHistoryTokenSelectionSpreadsheetDeltaFetcherWatcher.with(
                Optional.of(anchored)
        ).onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY,
                new FakeAppContext() {

                    @Override
                    public HistoryToken historyToken() {
                        return historyToken;
                    }

                    @Override
                    public void pushHistoryToken(final HistoryToken token) {
                        pushed.add(token);
                    }
                }
        );

        this.checkEquals(
                HistoryToken.cell(
                        id,
                        name,
                        anchored
                ),
                pushed
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
                PushHistoryTokenSelectionSpreadsheetDeltaFetcherWatcher.with(
                        Optional.of(
                                SpreadsheetSelection.parseCell("AB12")
                                        .setDefaultAnchor()
                        )
                ),
                "AB12"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<PushHistoryTokenSelectionSpreadsheetDeltaFetcherWatcher> type() {
        return PushHistoryTokenSelectionSpreadsheetDeltaFetcherWatcher.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
