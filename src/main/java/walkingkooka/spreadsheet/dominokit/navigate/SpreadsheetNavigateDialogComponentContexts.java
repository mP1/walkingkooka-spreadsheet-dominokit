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

public final class SpreadsheetNavigateDialogComponentContexts implements PublicStaticHelper {

    /**
     * {@see AppContextSpreadsheetNavigateDialogComponentContextCellNavigate}
     */
    public static SpreadsheetNavigateDialogComponentContext cellNavigate(AppContext context) {
        return AppContextSpreadsheetNavigateDialogComponentContextCellNavigate.with(context);
    }

    /**
     * {@see AppContextSpreadsheetNavigateDialogComponentContextColumnNavigate}
     */
    public static SpreadsheetNavigateDialogComponentContext columnNavigate(final AppContext context) {
        return AppContextSpreadsheetNavigateDialogComponentContextColumnNavigate.with(context);
    }

    /**
     * {@see FakeSpreadsheetNavigateDialogComponentContext}
     */
    public static SpreadsheetNavigateDialogComponentContext fake() {
        return new FakeSpreadsheetNavigateDialogComponentContext();
    }

    /**
     * {@see AppContextSpreadsheetNavigateDialogComponentContextNavigate}
     */
    public static SpreadsheetNavigateDialogComponentContext navigate(final AppContext context) {
        return AppContextSpreadsheetNavigateDialogComponentContextNavigate.with(context);
    }

    /**
     * {@see AppContextSpreadsheetNavigateDialogComponentContextRowNavigate}
     */
    public static SpreadsheetNavigateDialogComponentContext rowNavigate(final AppContext context) {
        return AppContextSpreadsheetNavigateDialogComponentContextRowNavigate.with(context);
    }

    /**
     * Stop creation
     */
    private SpreadsheetNavigateDialogComponentContexts() {
        throw new UnsupportedOperationException();
    }
}
