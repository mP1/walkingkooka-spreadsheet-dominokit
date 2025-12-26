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

package walkingkooka.spreadsheet.dominokit.navigate;

import org.junit.jupiter.api.Test;
import walkingkooka.net.UrlFragment;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentTesting;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public final class SpreadsheetNavigateLinkComponentTest implements ComponentTesting<SpreadsheetNavigateLinkComponent> {

    private final static SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1);
    private final static SpreadsheetName SPREADSHEET_NAME = SpreadsheetName.with("SpreadsheetName111");

    @Test
    public void testTreePrintSpreadsheetListSelectHistoryToken() {
        this.treePrintAndCheck2(
            HistoryToken.spreadsheetListSelect(
                HistoryTokenOffsetAndCount.EMPTY
            ),
            "SpreadsheetNavigateLinkComponent\n" +
                "  \"Navigate\" DISABLED"
        );
    }

    @Test
    public void testTreePrintSpreadsheetSelectHistoryToken() {
        this.treePrintAndCheck2(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            "SpreadsheetNavigateLinkComponent\n" +
                "  \"Navigate\" [#/1/SpreadsheetName111/navigate]"
        );
    }

    @Test
    public void testTreePrintSpreadsheetCellSelectHistoryToken() {
        this.treePrintAndCheck2(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            "SpreadsheetNavigateLinkComponent\n" +
                "  \"Navigate\" [#/1/SpreadsheetName111/cell/A1/navigate]"
        );
    }

    @Test
    public void testTreePrintSpreadsheetColumnSelectHistoryToken() {
        this.treePrintAndCheck2(
            HistoryToken.columnSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseColumn("B")
                    .setDefaultAnchor()
            ),
            "SpreadsheetNavigateLinkComponent\n" +
                "  \"Navigate\" [#/1/SpreadsheetName111/column/B/navigate]"
        );
    }

    private void treePrintAndCheck2(final HistoryToken historyToken,
                                    final String expected) {
        final AppContext context = new FakeAppContext() {
            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }
        };

        final SpreadsheetNavigateLinkComponent component = SpreadsheetNavigateLinkComponent.with(context);
        component.onHistoryTokenChange(
            HistoryToken.unknown(
                UrlFragment.EMPTY
            ),
            context
        );

        this.treePrintAndCheck(
            component,
            expected
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetNavigateLinkComponent> type() {
        return SpreadsheetNavigateLinkComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
