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
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SpreadsheetHistoryTokenTestCase<T extends SpreadsheetHistoryToken> extends HistoryTokenTestCase<T> {

    final static SpreadsheetId ID = SpreadsheetId.with(0x123);

    final static SpreadsheetName NAME = SpreadsheetName.with("SpreadsheetName456");

    SpreadsheetHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testParse() {
        final T token = this.createHistoryToken();

        this.parseAndCheck(
                token.urlFragment(),
                token
        );
    }

    final void parseAndCheck(final String fragment,
                             final SpreadsheetHistoryToken token) {
        this.parseAndCheck(
                UrlFragment.parse(fragment),
                token
        );
    }

    final void parseAndCheck(final UrlFragment fragment,
                             final SpreadsheetHistoryToken token) {
        this.checkEquals(
                token,
                SpreadsheetHistoryToken.parse(fragment)
        );
    }

    // setIdNameViewportSelection.......................................................................................

    @Test
    public final void testSetIdNameViewportSelectionNullIdFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createHistoryToken().setIdNameViewportSelection(
                        null,
                        NAME,
                        Optional.of(
                                SpreadsheetSelection.A1.setDefaultAnchor()
                        )
                )
        );
    }

    @Test
    public final void testSetIdNameViewportSelectionNullNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createHistoryToken().setIdNameViewportSelection(
                        ID,
                        null,
                        Optional.of(
                                SpreadsheetSelection.A1.setDefaultAnchor()
                        )
                )
        );
    }

    @Test
    public final void testSetIdNameViewportSelectionNullViewportSelectionFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createHistoryToken().setIdNameViewportSelection(
                        ID,
                        NAME,
                        null
                )
        );
    }

    final void setIdNameViewportSelectionAndCheck(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final SpreadsheetViewportSelection viewportSelection,
                                                  final SpreadsheetHistoryToken expected) {
        this.setIdNameViewportSelectionAndCheck(
                id,
                name,
                Optional.of(viewportSelection),
                expected
        );
    }

    final void setIdNameViewportSelectionAndCheck(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final Optional<SpreadsheetViewportSelection> viewportSelection,
                                                  final SpreadsheetHistoryToken expected) {
        this.setIdNameViewportSelectionAndCheck(
                this.createHistoryToken(),
                id,
                name,
                viewportSelection,
                expected
        );
    }

    final void setIdNameViewportSelectionAndCheck(final SpreadsheetHistoryToken token,
                                                  final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final SpreadsheetViewportSelection viewportSelection,
                                                  final SpreadsheetHistoryToken expected) {
        this.setIdNameViewportSelectionAndCheck(
                token,
                id,
                name,
                Optional.of(
                        viewportSelection
                ),
                expected
        );
    }

    final void setIdNameViewportSelectionAndCheck(final SpreadsheetHistoryToken token,
                                                  final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final Optional<SpreadsheetViewportSelection> viewportSelection,
                                                  final SpreadsheetHistoryToken expected) {
        this.checkEquals(
                expected,
                token.setIdNameViewportSelection(
                        id,
                        name,
                        viewportSelection
                ),
                () -> token + " setIdAndName " + id + ", " + name
        );
    }
}
