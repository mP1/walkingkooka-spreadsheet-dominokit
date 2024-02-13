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

    // anchoredSelection.........................................................................................

    @Test
    public void testAnchoredSelection() {
        final T token = this.createHistoryToken();
        this.checkEquals(
                Optional.of(token.anchoredSelection()),
                token.anchoredSelection()
        );
    }

    // setAnchoredSelection.............................................................................................

    final void setAnchoredSelectionAndCheck(final AnchoredSpreadsheetSelection anchoredSelection) {
        final T token = this.createHistoryToken(anchoredSelection);

        this.setAnchoredSelectionAndCheck(
                token,
                anchoredSelection,
                token.setAnchoredSelection(
                        Optional.of(
                                anchoredSelection
                        )
                )
        );
    }

    // freezeOrEmpty....................................................................................................

    final void freezeOrEmptyAndCheck(final AnchoredSpreadsheetSelection anchoredSelection) {
        this.freezeOrEmptyAndCheck(
                anchoredSelection,
                Optional.empty()
        );
    }

    final void freezeOrEmptyAndCheck(final AnchoredSpreadsheetSelection anchoredSelection,
                                     final HistoryToken expected) {
        this.freezeOrEmptyAndCheck(
                anchoredSelection,
                Optional.of(
                        expected
                )
        );
    }

    final void freezeOrEmptyAndCheck(final AnchoredSpreadsheetSelection anchoredSelection,
                                     final Optional<HistoryToken> expected) {
        final AnchoredSpreadsheetSelectionHistoryToken historyToken = HistoryToken.selection(
                ID,
                NAME,
                anchoredSelection
        );

        this.checkEquals(
                expected,
                historyToken.freezeOrEmpty(),
                () -> historyToken + " freezeOrEmpty"
        );
    }

    // unfreezeOrEmpty..................................................................................................

    final void unfreezeOrEmptyAndCheck(final AnchoredSpreadsheetSelection anchoredSelection) {
        this.unfreezeOrEmptyAndCheck(
                anchoredSelection,
                Optional.empty()
        );
    }

    final void unfreezeOrEmptyAndCheck(final AnchoredSpreadsheetSelection anchoredSelection,
                                       final HistoryToken expected) {
        this.unfreezeOrEmptyAndCheck(
                anchoredSelection,
                Optional.of(
                        expected
                )
        );
    }

    final void unfreezeOrEmptyAndCheck(final AnchoredSpreadsheetSelection anchoredSelection,
                                       final Optional<HistoryToken> expected) {
        final AnchoredSpreadsheetSelectionHistoryToken historyToken = HistoryToken.selection(
                ID,
                NAME,
                anchoredSelection
        );

        this.checkEquals(
                expected,
                historyToken.unfreezeOrEmpty(),
                () -> historyToken + " unfreezeOrEmpty"
        );
    }

    // helpers..........................................................................................................

    final void createHistoryTokenFails(final AnchoredSpreadsheetSelection anchoredSelection,
                                       final String expected) {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> this.createHistoryToken(
                        anchoredSelection
                )
        );

        this.checkEquals(
                expected,
                thrown.getMessage(),
                () -> "" + anchoredSelection
        );
    }

    final void urlFragmentAndCheck(final AnchoredSpreadsheetSelection anchoredSelection,
                                   final String expected) {
        this.urlFragmentAndCheck(
                this.createHistoryToken(
                        ID,
                        NAME,
                        anchoredSelection
                ),
                expected
        );
    }

    final T createHistoryToken(final AnchoredSpreadsheetSelection anchoredSelection) {
        return this.createHistoryToken(
                ID,
                NAME,
                anchoredSelection
        );
    }

    abstract T createHistoryToken(final SpreadsheetId id,
                                  final SpreadsheetName name,
                                  final AnchoredSpreadsheetSelection anchoredSelection);
}
