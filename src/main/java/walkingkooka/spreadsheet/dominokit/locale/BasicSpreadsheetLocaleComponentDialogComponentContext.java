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

import walkingkooka.locale.LocaleContext;
import walkingkooka.locale.LocaleContextDelegator;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContextDelegator;

import java.util.Objects;

final class BasicSpreadsheetLocaleComponentDialogComponentContext implements SpreadsheetLocaleComponentDialogComponentContext,
    SpreadsheetDialogComponentContextDelegator,
    LocaleContextDelegator {

    static BasicSpreadsheetLocaleComponentDialogComponentContext with(final SpreadsheetDialogComponentContext spreadsheetDialogComponentContext,
                                                                      final LocaleContext localeContext) {
       return new BasicSpreadsheetLocaleComponentDialogComponentContext(
           Objects.requireNonNull(spreadsheetDialogComponentContext, "spreadsheetDialogComponentContext"),
           Objects.requireNonNull(localeContext, "localeContext")
       );
    }

    private BasicSpreadsheetLocaleComponentDialogComponentContext(final SpreadsheetDialogComponentContext spreadsheetDialogComponentContext,
                                                                  final LocaleContext localeContext) {
        this.spreadsheetDialogComponentContext = spreadsheetDialogComponentContext;
        this.localeContext = localeContext;
    }

    // SpreadsheetDialogComponentContextDelegator..........................................................................................

    @Override
    public SpreadsheetDialogComponentContext spreadsheetDialogComponentContext() {
        return this.spreadsheetDialogComponentContext;
    }

    private final SpreadsheetDialogComponentContext spreadsheetDialogComponentContext;

    // LocaleContextDelegator...........................................................................................

    @Override
    public LocaleContext localeContext() {
        return this.localeContext;
    }

    private final LocaleContext localeContext;
}
