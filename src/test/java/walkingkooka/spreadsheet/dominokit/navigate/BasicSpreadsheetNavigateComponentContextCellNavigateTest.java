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
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class BasicSpreadsheetNavigateComponentContextCellNavigateTest extends BasicSpreadsheetNavigateComponentContextTestCase<BasicSpreadsheetNavigateComponentContextCellNavigate> {

    @Test
    public void testIsMatchNavigate() {
        final HistoryToken historyToken = HistoryToken.navigate(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            Optional.empty()
        );
        this.isMatchAndCheck(
            this.createContext(historyToken),
            historyToken,
            false
        );
    }

    @Test
    public void testIsMatchCellNavigate() {
        final HistoryToken historyToken = HistoryToken.cellNavigate(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SpreadsheetSelection.A1.setDefaultAnchor(),
            Optional.empty()
        );
        this.isMatchAndCheck(
            this.createContext(historyToken),
            historyToken,
            true
        );
    }

    @Test
    public void testIsMatchColumnNavigate() {
        final HistoryToken historyToken = HistoryToken.columnNavigate(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SpreadsheetSelection.A1.column()
                .setDefaultAnchor(),
            Optional.empty()
        );
        this.isMatchAndCheck(
            this.createContext(historyToken),
            historyToken,
            false
        );
    }

    @Test
    public void testIsMatchRowNavigate() {
        final HistoryToken historyToken = HistoryToken.rowNavigate(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SpreadsheetSelection.A1.row()
                .setDefaultAnchor(),
            Optional.empty()
        );
        this.isMatchAndCheck(
            this.createContext(historyToken),
            historyToken,
            false
        );
    }

    @Override
    BasicSpreadsheetNavigateComponentContextCellNavigate createContext(final HistoryContext historyContext,
                                                                       final LoggingContext loggingContext) {
        return BasicSpreadsheetNavigateComponentContextCellNavigate.with(
            historyContext,
            loggingContext
        );
    }

    @Override
    public Class<BasicSpreadsheetNavigateComponentContextCellNavigate> type() {
        return BasicSpreadsheetNavigateComponentContextCellNavigate.class;
    }
}
