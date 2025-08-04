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

package walkingkooka.spreadsheet.dominokit.locale;

import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.dominokit.AppContext;

public final class SpreadsheetLocaleDialogComponentContexts implements PublicStaticHelper {

    /**
     * {@see AppContextLocaleDialogComponentContextCellLocale}
     */
    public static SpreadsheetLocaleDialogComponentContext appContextCellLocale(final AppContext context) {
        return AppContextLocaleDialogComponentContextCellLocale.with(
            context
        );
    }

    /**
     * {@see AppContextSpreadsheetLocaleDialogComponentContextMetadataLocale}
     */
    public static SpreadsheetLocaleDialogComponentContext appContextSpreadsheetMetadataLocale(final AppContext context) {
        return AppContextSpreadsheetLocaleDialogComponentContextMetadataLocale.with(
            context
        );
    }

    /**
     * {@see FakeSpreadsheetLocaleDialogComponentContext}
     */
    public static FakeSpreadsheetLocaleDialogComponentContext fake() {
        throw new UnsupportedOperationException();
    }

    /**
     * Stop creation
     */
    private SpreadsheetLocaleDialogComponentContexts() {
        throw new UnsupportedOperationException();
    }
}
