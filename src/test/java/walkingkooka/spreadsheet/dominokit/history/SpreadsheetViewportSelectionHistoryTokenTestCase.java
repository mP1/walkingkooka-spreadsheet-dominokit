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
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SpreadsheetViewportSelectionHistoryTokenTestCase<T extends SpreadsheetViewportSelectionHistoryToken> extends SpreadsheetSelectionHistoryTokenTestCase<T> {

    SpreadsheetViewportSelectionHistoryTokenTestCase() {
        super();
    }

    @Test
    public void testViewportSelectionOrEmpty() {
        final T token = this.createHistoryToken();
        this.checkEquals(
                Optional.of(token.viewportSelection()),
                token.viewportSelection()
        );
    }

    final void createHistoryTokenFails(final SpreadsheetViewportSelection viewportSelection,
                                       final String expected) {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> this.createHistoryToken(
                        viewportSelection
                )
        );

        this.checkEquals(
                expected,
                thrown.getMessage(),
                () -> "" + viewportSelection
        );
    }

    final void urlFragmentAndCheck(final SpreadsheetViewportSelection viewportSelection,
                                   final String expected) {
        this.urlFragmentAndCheck(
                this.createHistoryToken(
                        ID,
                        NAME,
                        viewportSelection
                ),
                expected
        );
    }

    final T createHistoryToken(final SpreadsheetViewportSelection viewportSelection) {
        return this.createHistoryToken(
                ID,
                NAME,
                viewportSelection
        );
    }

    abstract T createHistoryToken(final SpreadsheetId id,
                                  final SpreadsheetName name,
                                  final SpreadsheetViewportSelection viewportSelection);
}
