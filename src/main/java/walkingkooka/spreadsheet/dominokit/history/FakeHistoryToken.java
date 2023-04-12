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

import javaemul.internal.annotations.GwtIncompatible;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.test.Fake;
import walkingkooka.text.cursor.TextCursor;

@GwtIncompatible
public class FakeHistoryToken extends HistoryToken implements Fake {

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UrlFragment urlFragment() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onHashChange(final HistoryToken previous,
                             final AppContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HistoryToken idName(final SpreadsheetId id,
                               final SpreadsheetName name) {
        throw new UnsupportedOperationException();
    }

    @Override
    HistoryToken idNameViewportSelection0(final SpreadsheetId id,
                                          final SpreadsheetName name,
                                          final SpreadsheetViewportSelection viewportSelection) {
        throw new UnsupportedOperationException();
    }
}
