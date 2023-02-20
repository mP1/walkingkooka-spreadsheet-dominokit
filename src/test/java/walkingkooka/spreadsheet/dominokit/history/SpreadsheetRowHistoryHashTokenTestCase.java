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

import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReferenceRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public abstract class SpreadsheetRowHistoryHashTokenTestCase<T extends SpreadsheetRowHistoryHashToken> extends SpreadsheetViewportSelectionHistoryHashTokenTestCase<T> {

    final static SpreadsheetRowReference ROW = SpreadsheetSelection.parseRow("1");

    final static SpreadsheetRowReferenceRange ROW_RANGE = SpreadsheetSelection.parseRowRange("2:3");

    SpreadsheetRowHistoryHashTokenTestCase() {
        super();
    }

    final void urlFragmentAndCheck(final SpreadsheetRowReference reference,
                                   final String expected) {
        this.urlFragmentAndCheck(
                this.createHistoryHashToken(
                        reference.setAnchor(SpreadsheetViewportSelectionAnchor.NONE)
                ),
                expected
        );
    }

    final void urlFragmentAndCheck(final SpreadsheetRowReferenceRange reference,
                                   final String expected) {
        this.urlFragmentAndCheck(
                this.createHistoryHashToken(
                        reference.setAnchor(SpreadsheetViewportSelectionAnchor.NONE)
                ),
                expected
        );
    }

    final T createHistoryHashToken() {
        return this.createHistoryHashToken(
                ROW.setAnchor(SpreadsheetViewportSelectionAnchor.NONE)
        );
    }
}
