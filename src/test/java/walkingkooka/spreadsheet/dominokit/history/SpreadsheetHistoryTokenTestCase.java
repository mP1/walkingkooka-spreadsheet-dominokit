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

    // idNameViewportSelection.......................................................................................

    @Test
    public final void testIdNameViewportSelectionNullIdFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createHistoryToken().idNameViewportSelection(
                        null,
                        NAME,
                        Optional.of(
                                SpreadsheetSelection.A1.setDefaultAnchor()
                        )
                )
        );
    }

    @Test
    public final void testIdNameViewportSelectionNullNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createHistoryToken().idNameViewportSelection(
                        ID,
                        null,
                        Optional.of(
                                SpreadsheetSelection.A1.setDefaultAnchor()
                        )
                )
        );
    }

    @Test
    public final void testIdNameViewportSelectionNullViewportSelectionFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createHistoryToken().idNameViewportSelection(
                        ID,
                        NAME,
                        null
                )
        );
    }

    final void idNameViewportSelectionAndCheck(final SpreadsheetId id,
                                               final SpreadsheetName name,
                                               final SpreadsheetViewportSelection viewportSelection,
                                               final SpreadsheetHistoryToken expected) {
        this.idNameViewportSelectionAndCheck(
                id,
                name,
                Optional.of(viewportSelection),
                expected
        );
    }

    final void idNameViewportSelectionAndCheck(final SpreadsheetId id,
                                               final SpreadsheetName name,
                                               final Optional<SpreadsheetViewportSelection> viewportSelection,
                                               final SpreadsheetHistoryToken expected) {
        this.idNameViewportSelectionAndCheck(
                this.createHistoryToken(),
                id,
                name,
                viewportSelection,
                expected
        );
    }

    final void idNameViewportSelectionAndCheck(final SpreadsheetHistoryToken token,
                                               final SpreadsheetId id,
                                               final SpreadsheetName name,
                                               final SpreadsheetViewportSelection viewportSelection,
                                               final SpreadsheetHistoryToken expected) {
        this.idNameViewportSelectionAndCheck(
                token,
                id,
                name,
                Optional.of(
                        viewportSelection
                ),
                expected
        );
    }

    final void idNameViewportSelectionAndCheck(final SpreadsheetHistoryToken token,
                                               final SpreadsheetId id,
                                               final SpreadsheetName name,
                                               final Optional<SpreadsheetViewportSelection> viewportSelection,
                                               final SpreadsheetHistoryToken expected) {
        this.checkEquals(
                expected,
                token.idNameViewportSelection(
                        id,
                        name,
                        viewportSelection
                ),
                () -> token + " idAndNameViewportSelection " + id + ", " + name + ", " + viewportSelection
        );
    }
}
