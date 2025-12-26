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
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;

import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.Assert.assertThrows;

public final class SpreadsheetFormListHistoryTokenTest extends SpreadsheetFormHistoryTokenTestCase<SpreadsheetFormListHistoryToken> {

    private final static HistoryTokenOffsetAndCount OFFSET_AND_COUNT = HistoryTokenOffsetAndCount.EMPTY
        .setOffset(OptionalInt.of(1))
        .setCount(OptionalInt.of(2));

    // with.............................................................................................................

    @Test
    public void testWithNullFormNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetFormListHistoryToken.with(
                ID,
                NAME,
                null
            )
        );
    }

    // formName.........................................................................................................

    @Test
    public void testFormName() {
        this.formNameAndCheck(
            this.createHistoryToken(),
            Optional.empty()
        );
    }

    // offset...........................................................................................................

    @Test
    public void testOffset() {
        this.offsetAndCheck(
            this.createHistoryToken(),
            OFFSET_AND_COUNT.offset()
        );
    }

    // count............................................................................................................

    @Test
    public void testCount() {
        this.countAndCheck(
            this.createHistoryToken(),
            OFFSET_AND_COUNT.count()
        );
    }

    // clear...........................................................................................................

    @Test
    public void testClear() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            SpreadsheetFormListHistoryToken.with(
                ID,
                NAME,
                OFFSET_AND_COUNT
            )
        );
    }

    // UrlFragment......................................................................................................

    @Test
    public void testUrlFragmentWithZeroOffsetAndZeroCount() {
        this.urlFragmentAndCheck(
            SpreadsheetFormListHistoryToken.with(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.EMPTY
            ),
            "/123/SpreadsheetName456/form"
        );
    }

    @Test
    public void testUrlFragmentWithOffsetAndCount() {
        this.urlFragmentAndCheck(
            "/123/SpreadsheetName456/form/*/offset/1/count/2"
        );
    }

    @Override
    SpreadsheetFormListHistoryToken createHistoryToken(final SpreadsheetId id,
                                                       final SpreadsheetName name) {
        return SpreadsheetFormListHistoryToken.with(
            id,
            name,
            OFFSET_AND_COUNT
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFormListHistoryToken> type() {
        return SpreadsheetFormListHistoryToken.class;
    }
}
