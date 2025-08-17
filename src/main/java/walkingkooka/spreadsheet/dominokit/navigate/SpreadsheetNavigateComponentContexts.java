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

import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.dominokit.AppContext;

public final class SpreadsheetNavigateComponentContexts implements PublicStaticHelper {

    /**
     * {@see AppContextSpreadsheetNavigateComponentContextCellNavigate}
     */
    public static SpreadsheetNavigateComponentContext cellNavigate(AppContext context) {
        return AppContextSpreadsheetNavigateComponentContextCellNavigate.with(context);
    }

    /**
     * {@see AppContextSpreadsheetNavigateComponentContextColumnNavigate}
     */
    public static SpreadsheetNavigateComponentContext columnNavigate(final AppContext context) {
        return AppContextSpreadsheetNavigateComponentContextColumnNavigate.with(context);
    }

    /**
     * {@see FakeSpreadsheetNavigateComponentContext}
     */
    public static SpreadsheetNavigateComponentContext fake() {
        return new FakeSpreadsheetNavigateComponentContext();
    }

    /**
     * {@see AppContextSpreadsheetNavigateComponentContextNavigate}
     */
    public static SpreadsheetNavigateComponentContext navigate(final AppContext context) {
        return AppContextSpreadsheetNavigateComponentContextNavigate.with(context);
    }

    /**
     * {@see AppContextSpreadsheetNavigateComponentContextRowNavigate}
     */
    public static SpreadsheetNavigateComponentContext rowNavigate(final AppContext context) {
        return AppContextSpreadsheetNavigateComponentContextRowNavigate.with(context);
    }

    /**
     * Stop creation
     */
    private SpreadsheetNavigateComponentContexts() {
        throw new UnsupportedOperationException();
    }
}
