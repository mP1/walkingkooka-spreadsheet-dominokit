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
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SpreadsheetViewportHistoryTokenTestCase<T extends SpreadsheetViewportHistoryToken> extends SpreadsheetSelectionHistoryTokenTestCase<T> {

    SpreadsheetViewportHistoryTokenTestCase() {
        super();
    }

    // freezeOrEmpty....................................................................................................

    final void freezeOrEmptyAndCheck(final SpreadsheetViewport viewport) {
        this.freezeOrEmptyAndCheck(
                viewport,
                Optional.empty()
        );
    }

    final void freezeOrEmptyAndCheck(final SpreadsheetViewport viewport,
                                     final HistoryToken expected) {
        this.freezeOrEmptyAndCheck(
                viewport,
                Optional.of(
                        expected
                )
        );
    }

    final void freezeOrEmptyAndCheck(final SpreadsheetViewport viewport,
                                     final Optional<HistoryToken> expected) {
        final SpreadsheetViewportHistoryToken historyToken = HistoryToken.viewport(
                ID,
                NAME,
                viewport
        );

        this.checkEquals(
                expected,
                historyToken.freezeOrEmpty(),
                () -> historyToken + " freezeOrEmpty"
        );
    }

    // setViewport.............................................................................................

    final void setViewportAndCheck(final SpreadsheetViewport viewport) {
        final T token = this.createHistoryToken(viewport);

        this.setViewportAndCheck(
                token,
                viewport,
                token.setViewport(
                        Optional.of(
                                viewport
                        )
                )
        );
    }

    // unfreezeOrEmpty..................................................................................................

    final void unfreezeOrEmptyAndCheck(final SpreadsheetViewport viewport) {
        this.unfreezeOrEmptyAndCheck(
                viewport,
                Optional.empty()
        );
    }

    final void unfreezeOrEmptyAndCheck(final SpreadsheetViewport viewport,
                                       final HistoryToken expected) {
        this.unfreezeOrEmptyAndCheck(
                viewport,
                Optional.of(
                        expected
                )
        );
    }

    final void unfreezeOrEmptyAndCheck(final SpreadsheetViewport viewport,
                                       final Optional<HistoryToken> expected) {
        final SpreadsheetViewportHistoryToken historyToken = HistoryToken.viewport(
                ID,
                NAME,
                viewport
        );

        this.checkEquals(
                expected,
                historyToken.unfreezeOrEmpty(),
                () -> historyToken + " unfreezeOrEmpty"
        );
    }

    // viewportOrEmpty.........................................................................................

    @Test
    public void testViewportOrEmpty() {
        final T token = this.createHistoryToken();
        this.checkEquals(
                Optional.of(token.viewport()),
                token.viewport()
        );
    }

    final void createHistoryTokenFails(final SpreadsheetViewport viewport,
                                       final String expected) {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> this.createHistoryToken(
                        viewport
                )
        );

        this.checkEquals(
                expected,
                thrown.getMessage(),
                () -> "" + viewport
        );
    }

    final void urlFragmentAndCheck(final SpreadsheetViewport viewport,
                                   final String expected) {
        this.urlFragmentAndCheck(
                this.createHistoryToken(
                        ID,
                        NAME,
                        viewport
                ),
                expected
        );
    }

    final T createHistoryToken(final SpreadsheetViewport viewport) {
        return this.createHistoryToken(
                ID,
                NAME,
                viewport
        );
    }

    abstract T createHistoryToken(final SpreadsheetId id,
                                  final SpreadsheetName name,
                                  final SpreadsheetViewport viewport);
}
