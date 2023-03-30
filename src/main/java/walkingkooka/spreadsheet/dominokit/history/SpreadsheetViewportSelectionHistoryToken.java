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

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Objects;

public abstract class SpreadsheetViewportSelectionHistoryToken extends SpreadsheetSelectionHistoryToken {

    SpreadsheetViewportSelectionHistoryToken(final SpreadsheetId id,
                                             final SpreadsheetName name,
                                             final SpreadsheetViewportSelection viewportSelection) {
        super(
                id,
                name
        );
        this.viewportSelection = Objects.requireNonNull(viewportSelection, "viewportSelection");
    }

    public final SpreadsheetViewportSelection viewportSelection() {
        return this.viewportSelection;
    }

    private final SpreadsheetViewportSelection viewportSelection;

    @Override
    final UrlFragment selectionUrlFragment() {
        return this.viewportSelection.urlFragment()
                .append(this.selectionViewportUrlFragment());
    }

    abstract UrlFragment selectionViewportUrlFragment();

    /**
     * Factory that returns a {@link SpreadsheetViewportSelectionHistoryToken} without any action and just the
     * {@link SpreadsheetViewportSelection}
     */
    public final SpreadsheetViewportSelectionHistoryToken selection() {
        final SpreadsheetViewportSelectionHistoryToken token = this.selection0();
        return token.equals(this) ?
                this :
                token;
    }

    abstract SpreadsheetViewportSelectionHistoryToken selection0();
}
