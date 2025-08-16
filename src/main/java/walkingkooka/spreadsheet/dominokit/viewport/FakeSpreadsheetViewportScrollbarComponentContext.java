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

import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

public class FakeSpreadsheetViewportScrollbarComponentContext extends FakeHistoryContext implements SpreadsheetViewportScrollbarComponentContext {

    public FakeSpreadsheetViewportScrollbarComponentContext() {
        super();
    }

    @Override
    public int viewportGridWidth() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int viewportGridHeight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean autoHideScrollbars() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetViewportCache spreadsheetViewportCache() {
        throw new UnsupportedOperationException();
    }
}
