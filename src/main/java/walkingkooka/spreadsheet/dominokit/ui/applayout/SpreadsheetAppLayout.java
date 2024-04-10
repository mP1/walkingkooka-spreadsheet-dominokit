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

package walkingkooka.spreadsheet.dominokit.ui.applayout;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.layout.AppLayout;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetNameHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.VisibleComponentLifecycle;

public final class SpreadsheetAppLayout extends AppLayout implements VisibleComponentLifecycle<HTMLDivElement, SpreadsheetAppLayout> {

    public static SpreadsheetAppLayout empty() {
        return new SpreadsheetAppLayout();
    }

    private SpreadsheetAppLayout() {
        super();
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return false;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetNameHistoryToken;
    }

    @Override
    public void refresh(final AppContext context) {
        // do nothing only want AppLayout to be hidden when not SpreadsheetNameHistoryToken
    }

    @Override
    public void openGiveFocus(final AppContext context) {
        // do nothing
    }

    @Override
    public boolean shouldLogLifecycleChanges() {
        return false;
    }
}
