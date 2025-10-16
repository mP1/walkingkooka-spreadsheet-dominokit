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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertSame;

public abstract class PluginHistoryTokenTestCase<T extends PluginHistoryToken> extends HistoryTokenTestCase<T> {

    PluginHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testSetIdName() {
        this.setIdAndNameAndCheck(
            ID,
            NAME,
            HistoryToken.spreadsheetSelect(ID, NAME)
        );
    }

    // createLabel......................................................................................................

    @Test
    public final void testCreateLabel() {
        this.createLabelAndCheck(
            this.createHistoryToken()
        );
    }

    // labels...........................................................................................................

    @Test
    public final void testSetLabels() {
        this.setLabelsAndCheck(
            this.createHistoryToken(),
            HistoryTokenOffsetAndCount.EMPTY.setCount(
                OptionalInt.of(123)
            )
        );
    }

    // navigation.......................................................................................................

    @Test
    public final void testNavigation() {
        this.navigationAndCheck(
            this.createHistoryToken()
        );
    }

    // setSelection.....................................................................................................

    @Test
    public final void testSetSelectionWithCell() {
        this.setSelectionAndCheck(
            this.createHistoryToken(),
            SpreadsheetSelection.A1
        );
    }

    // parse............................................................................................................

    @Test
    public final void testParse() {
        final T token = this.createHistoryToken();

        this.parseAndCheck(
            token.urlFragment(),
            token
        );
    }

    final void parseAndCheck(final String fragment,
                             final PluginHistoryToken token) {
        this.parseAndCheck(
            UrlFragment.parse(fragment),
            token
        );
    }

    final void parseAndCheck(final UrlFragment fragment,
                             final PluginHistoryToken token) {
        this.checkEquals(
            token,
            HistoryToken.parse(fragment),
            () -> "parse " + fragment
        );
    }

    // urlFragment......................................................................................................

    @Test
    public final void testUrlFragmentCached() {
        final T token = this.createHistoryToken();
        assertSame(
            token.urlFragment(),
            token.urlFragment()
        );
    }
}

