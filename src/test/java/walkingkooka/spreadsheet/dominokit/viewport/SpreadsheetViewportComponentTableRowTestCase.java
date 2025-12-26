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

import elemental2.dom.HTMLTableRowElement;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetName;

abstract class SpreadsheetViewportComponentTableRowTestCase<T extends SpreadsheetViewportComponentTableRow<T>> implements HtmlComponentTesting<T, HTMLTableRowElement> {

    SpreadsheetViewportComponentTableRowTestCase() {
        super();
    }

    final static SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1);

    final static SpreadsheetName SPREADSHEET_NAME = SpreadsheetName.with("SpreadsheetName222");

    @Override
    public final void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
