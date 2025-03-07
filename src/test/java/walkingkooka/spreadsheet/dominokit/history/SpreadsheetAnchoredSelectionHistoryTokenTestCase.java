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

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SpreadsheetAnchoredSelectionHistoryTokenTestCase<T extends SpreadsheetAnchoredSelectionHistoryToken> extends SpreadsheetSelectionHistoryTokenTestCase<T> {

    SpreadsheetAnchoredSelectionHistoryTokenTestCase() {
        super();
    }

    // anchoredSelection................................................................................................

    @Test
    public final void testAnchoredSelection() {
        final T token = this.createHistoryToken();
        this.checkEquals(
            token.anchoredSelection(),
            token.anchoredSelection()
        );
    }

    // anchoredSelectionOrEmpty........................................................................................

    @Test
    public final void testAnchoredSelectionOrEmpty() {
        final T token = this.createHistoryToken();
        this.checkEquals(
            Optional.of(
                token.anchoredSelection()
            ),
            token.anchoredSelectionOrEmpty()
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
        final SpreadsheetAnchoredSelectionHistoryToken historyToken = HistoryToken.selection(
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

    // list.............................................................................................................

    @Test
    public final void testSetListWithEmptyOffsetCount() {
        this.setListAndCheck(
            this.createHistoryToken(),
            HistoryTokenOffsetAndCount.EMPTY,
            HistoryToken.spreadsheetListSelect(HistoryTokenOffsetAndCount.EMPTY)
        );
    }

    @Test
    public final void testSetListWithNonEmptyOffsetCount() {
        final HistoryTokenOffsetAndCount offsetAndCount = HistoryTokenOffsetAndCount.with(
            OptionalInt.of(123),
            OptionalInt.of(456)
        );

        this.setListAndCheck(
            this.createHistoryToken(),
            offsetAndCount,
            HistoryToken.spreadsheetListSelect(offsetAndCount)
        );
    }

    // setSelection.....................................................................................................

    @Test
    public final void testSetSelectionWithoutSelection() {
        this.setSelectionAndCheck(
            this.createHistoryToken()
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
        final SpreadsheetAnchoredSelectionHistoryToken historyToken = HistoryToken.selection(
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
