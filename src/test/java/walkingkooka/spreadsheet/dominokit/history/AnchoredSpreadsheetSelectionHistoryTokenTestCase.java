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

import org.junit.Test;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AnchoredSpreadsheetSelectionHistoryTokenTestCase<T extends AnchoredSpreadsheetSelectionHistoryToken> extends SpreadsheetSelectionHistoryTokenTestCase<T> {

    AnchoredSpreadsheetSelectionHistoryTokenTestCase() {
        super();
    }

    // freezeOrEmpty....................................................................................................

    final void freezeOrEmptyAndCheck(final AnchoredSpreadsheetSelection selection) {
        this.freezeOrEmptyAndCheck(
                selection,
                Optional.empty()
        );
    }

    final void freezeOrEmptyAndCheck(final AnchoredSpreadsheetSelection selection,
                                     final HistoryToken expected) {
        this.freezeOrEmptyAndCheck(
                selection,
                Optional.of(
                        expected
                )
        );
    }

    final void freezeOrEmptyAndCheck(final AnchoredSpreadsheetSelection selection,
                                     final Optional<HistoryToken> expected) {
        final AnchoredSpreadsheetSelectionHistoryToken historyToken = HistoryToken.selection(
                ID,
                NAME,
                selection
        );

        this.checkEquals(
                expected,
                historyToken.freezeOrEmpty(),
                () -> historyToken + " freezeOrEmpty"
        );
    }

    // setViewport.............................................................................................

    final void setSelectionAndCheck(final AnchoredSpreadsheetSelection selection) {
        final T token = this.createHistoryToken(selection);

        this.setSelectionAndCheck(
                token,
                selection,
                token.setSelection(
                        Optional.of(
                                selection
                        )
                )
        );
    }

    // unfreezeOrEmpty..................................................................................................

    final void unfreezeOrEmptyAndCheck(final AnchoredSpreadsheetSelection selection) {
        this.unfreezeOrEmptyAndCheck(
                selection,
                Optional.empty()
        );
    }

    final void unfreezeOrEmptyAndCheck(final AnchoredSpreadsheetSelection selection,
                                       final HistoryToken expected) {
        this.unfreezeOrEmptyAndCheck(
                selection,
                Optional.of(
                        expected
                )
        );
    }

    final void unfreezeOrEmptyAndCheck(final AnchoredSpreadsheetSelection selection,
                                       final Optional<HistoryToken> expected) {
        final AnchoredSpreadsheetSelectionHistoryToken historyToken = HistoryToken.selection(
                ID,
                NAME,
                selection
        );

        this.checkEquals(
                expected,
                historyToken.unfreezeOrEmpty(),
                () -> historyToken + " unfreezeOrEmpty"
        );
    }

    // selectionOrEmpty.........................................................................................

    @Test
    public void testViewportOrEmpty() {
        final T token = this.createHistoryToken();
        this.checkEquals(
                Optional.of(token.selection()),
                token.selection()
        );
    }

    final void createHistoryTokenFails(final AnchoredSpreadsheetSelection selection,
                                       final String expected) {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> this.createHistoryToken(
                        selection
                )
        );

        this.checkEquals(
                expected,
                thrown.getMessage(),
                () -> "" + selection
        );
    }

    final void urlFragmentAndCheck(final AnchoredSpreadsheetSelection selection,
                                   final String expected) {
        this.urlFragmentAndCheck(
                this.createHistoryToken(
                        ID,
                        NAME,
                        selection
                ),
                expected
        );
    }

    final T createHistoryToken(final AnchoredSpreadsheetSelection selection) {
        return this.createHistoryToken(
                ID,
                NAME,
                selection
        );
    }

    abstract T createHistoryToken(final SpreadsheetId id,
                                  final SpreadsheetName name,
                                  final AnchoredSpreadsheetSelection selection);
}
