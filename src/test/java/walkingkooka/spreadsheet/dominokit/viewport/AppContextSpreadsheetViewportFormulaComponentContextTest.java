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

package walkingkooka.spreadsheet.dominokit.viewport;

import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContexts;

public final class AppContextSpreadsheetViewportFormulaComponentContextTest implements SpreadsheetViewportFormulaComponentContextTesting<AppContextSpreadsheetViewportFormulaComponentContext> {

    @Override
    public void testEnvironmentValueWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetEnvironmentValueNameWithNullNameFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetEnvironmentValueNameWithNullValueFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRemoveEnvironmentValueWithNullNameFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSpreadsheetFormatterSelectorWithNullParserSelectorFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSpreadsheetParserNameWithNullContextFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSpreadsheetParserNameWithNullNameFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSpreadsheetParserNameWithNullValuesFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSpreadsheetParserNextTokenWithNullSelectorFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSpreadsheetParserSelectorWithNullContextFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSpreadsheetParserSelectorWithNullSelectorFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testUserNotNull() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AppContextSpreadsheetViewportFormulaComponentContext createContext() {
        return AppContextSpreadsheetViewportFormulaComponentContext.with(AppContexts.fake());
    }

    // class............................................................................................................

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<AppContextSpreadsheetViewportFormulaComponentContext> type() {
        return AppContextSpreadsheetViewportFormulaComponentContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
