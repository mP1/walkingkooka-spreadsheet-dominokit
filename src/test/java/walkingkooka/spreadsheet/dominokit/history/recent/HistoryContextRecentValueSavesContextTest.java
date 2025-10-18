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

package walkingkooka.spreadsheet.dominokit.history.recent;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HistoryContextRecentValueSavesContextTest implements RecentValueSavesContextTesting<HistoryContextRecentValueSavesContext> {

    @Test
    public void testWithNullHistoryContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> HistoryContextRecentValueSavesContext.with(null)
        );
    }

    private final static SpreadsheetId ID = SpreadsheetId.parse("1");
    private final static SpreadsheetName NAME = SpreadsheetName.with("SpreadsheetName222");
    private final static AnchoredSpreadsheetSelection CELL = SpreadsheetSelection.A1.setDefaultAnchor();

    // recentValueSaves.................................................................................................

    @Test
    public void testSpreadsheetCellFormatterSaveHistoryToken() {
        final SpreadsheetFormatterSelector formatter1 = SpreadsheetFormatterSelector.parse("formatter-1 111");
        final SpreadsheetFormatterSelector formatter2 = SpreadsheetFormatterSelector.parse("formatter-2 222");
        final SpreadsheetFormatterSelector formatter3 = SpreadsheetFormatterSelector.parse("formatter-3 333");
        final SpreadsheetFormatterSelector formatter4 = SpreadsheetFormatterSelector.parse("formatter-4 444");

        this.recentValueSavesAndCheck2(
            Lists.of(
                formatter1,
                formatter2,
                formatter3,
                formatter4
            ),
            (v) -> HistoryToken.cellFormatterSave(
                ID,
                NAME,
                CELL,
                Optional.of(v)
            ),
            SpreadsheetFormatterSelector.class,
            formatter4,
            formatter3,
            formatter2
        );
    }

    private <T> void recentValueSavesAndCheck2(final List<T> values,
                                               final Function<T, HistoryToken> historyTokenFactory,
                                               final Class<T> valueType,
                                               final T... expected) {
        this.recentValueSavesAndCheck2(
            values,
            historyTokenFactory,
            valueType,
            Lists.of(expected)
        );
    }

    private <T> void recentValueSavesAndCheck2(final List<T> values,
                                               final Function<T, HistoryToken> historyTokenFactory,
                                               final Class<T> valueType,
                                               final List<T> expected) {
        final HistoryContext historyContext = new FakeHistoryContext() {
            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                this.watcher = watcher;
                return null;
            }

            @Override
            public void pushHistoryToken(final HistoryToken token) {
                final HistoryToken previous = this.previous;
                this.previous = token;

                this.watcher.onHistoryTokenChange(
                    previous,
                    new FakeAppContext() {
                        @Override
                        public HistoryToken historyToken() {
                            return token;
                        }
                    }
                );
            }

            private HistoryToken previous;

            private HistoryTokenWatcher watcher;
        };

        final HistoryContextRecentValueSavesContext context = HistoryContextRecentValueSavesContext.with(historyContext);

        for (final T value : values) {
            historyContext.pushHistoryToken(
                historyTokenFactory.apply(value)
            );
        }

        this.recentValueSavesAndCheck(
            context,
            valueType,
            expected
        );
    }

    @Override
    public HistoryContextRecentValueSavesContext createContext() {
        return HistoryContextRecentValueSavesContext.with(
            new FakeHistoryContext() {
                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return null;
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<HistoryContextRecentValueSavesContext> type() {
        return HistoryContextRecentValueSavesContext.class;
    }
}
