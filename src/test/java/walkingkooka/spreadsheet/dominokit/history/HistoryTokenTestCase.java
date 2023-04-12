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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.UrlFragment;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class HistoryTokenTestCase<T extends HistoryToken> implements ClassTesting<T>, HashCodeEqualsDefinedTesting2<T>, ToStringTesting<T> {

    final static SpreadsheetId ID = SpreadsheetId.with(0x123);

    final static SpreadsheetName NAME = SpreadsheetName.with("SpreadsheetName456");

    HistoryTokenTestCase() {
        super();
    }


    // idName............................................................................................................

    @Test
    public final void testIdNameNullIdFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createHistoryToken().idName(
                        null,
                        NAME
                )
        );
    }

    @Test
    public final void testIdNameNullNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createHistoryToken().idName(
                        ID,
                        null
                )
        );
    }

    final void idNameAndCheck(final SpreadsheetId id,
                              final SpreadsheetName name,
                              final HistoryToken expected) {
        this.idNameAndCheck(
                this.createHistoryToken(),
                id,
                name,
                expected
        );
    }

    final void idNameAndCheck(final HistoryToken token,
                              final SpreadsheetId id,
                              final SpreadsheetName name,
                              final HistoryToken expected) {
        this.checkEquals(
                expected,
                token.idName(
                        id,
                        name
                ),
                () -> token + " id=" + id + " name=" + name
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
                                               final HistoryToken expected) {
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
                                               final HistoryToken expected) {
        this.idNameViewportSelectionAndCheck(
                this.createHistoryToken(),
                id,
                name,
                viewportSelection,
                expected
        );
    }

    final void idNameViewportSelectionAndCheck(final HistoryToken token,
                                               final SpreadsheetId id,
                                               final SpreadsheetName name,
                                               final SpreadsheetViewportSelection viewportSelection,
                                               final HistoryToken expected) {
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

    final void idNameViewportSelectionAndCheck(final HistoryToken token,
                                               final SpreadsheetId id,
                                               final SpreadsheetName name,
                                               final Optional<SpreadsheetViewportSelection> viewportSelection,
                                               final HistoryToken expected) {
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

    // equals...........................................................................................................

    @Test
    public final void testEqualsDifferentType() {
        this.checkNotEquals(
                new FakeHistoryToken() {
                    @Override
                    public UrlFragment urlFragment() {
                        return UrlFragment.SLASH;
                    }
                }
        );
    }

    @Test
    public final void testEqualsDifferentTypeSameUrgent() {
        final T token = this.createHistoryToken();

        this.checkNotEquals(
                token,
                new FakeHistoryToken() {
                    @Override
                    public UrlFragment urlFragment() {
                        return token.urlFragment();
                    }
                }
        );
    }

    final void urlFragmentAndCheck(final String expected) {
        this.urlFragmentAndCheck(
                this.createHistoryToken(),
                expected
        );
    }

    final void urlFragmentAndCheck(final HasUrlFragment fragment,
                                   final String expected) {
        this.urlFragmentAndCheck(
                fragment,
                UrlFragment.with(expected)
        );
    }

    final void urlFragmentAndCheck(final HasUrlFragment fragment,
                                   final UrlFragment expected) {
        this.checkEquals(
                expected,
                fragment.urlFragment(),
                () -> "urlFragment of " + fragment
        );
    }

    abstract T createHistoryToken();

    @Override
    public final T createObject() {
        return this.createHistoryToken();
    }

    // ClassTesting.....................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
