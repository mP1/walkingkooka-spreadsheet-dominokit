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
import walkingkooka.ToStringTesting;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HistoryContextBasicTest implements HistoryContextTesting<HistoryContextBasic>,
    SpreadsheetMetadataTesting,
    ToStringTesting<HistoryContextBasic> {

    private final static HistoryToken HISTORY_TOKEN = HistoryToken.cellSelect(
        SPREADSHEET_ID,
        SPREADSHEET_NAME,
        SpreadsheetSelection.A1.setDefaultAnchor()
    );

    private final static AppContext APP_CONTEXT = AppContexts.fake();

    @Test
    public void testWithNullHistoryTokenFails() {
        assertThrows(
            NullPointerException.class,
            () -> HistoryContextBasic.with(
                null,
                APP_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> HistoryContextBasic.with(
                HISTORY_TOKEN,
                null
            )
        );
    }

    @Test
    public void testPushHistoryToken() {
        this.fired = false;

        final HistoryContextBasic context = HistoryContextBasic.with(
            HISTORY_TOKEN,
            APP_CONTEXT
        );

        final HistoryToken pushed = HistoryToken.cellSelect(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SpreadsheetSelection.labelName("pushed")
                .setDefaultAnchor()
        );

        context.addHistoryWatcher(
            new HistoryWatcher() {
                @Override
                public void onHistoryTokenChange(final HistoryToken previous,
                                                 final AppContext context) {
                    checkEquals(HISTORY_TOKEN, previous, "previous");
                    checkEquals(APP_CONTEXT, context, "context");

                    HistoryContextBasicTest.this.fired = true;
                }
            }
        );

        context.pushHistoryToken(pushed);

        this.historyTokenAndCheck(
            context,
            pushed
        );

        this.checkEquals(
            true,
            this.fired,
            "fired"
        );
    }

    private boolean fired;

    @Override
    public HistoryContextBasic createContext() {
        return HistoryContextBasic.with(
            HISTORY_TOKEN,
            APP_CONTEXT
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createContext(),
            HISTORY_TOKEN.toString()
        );
    }

    // class............................................................................................................

    @Override
    public Class<HistoryContextBasic> type() {
        return HistoryContextBasic.class;
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
