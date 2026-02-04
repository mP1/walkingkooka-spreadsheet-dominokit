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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.OptionalInt;

public abstract class SpreadsheetListHistoryTokenTestCase<T extends SpreadsheetListHistoryToken> extends SpreadsheetHistoryTokenTestCase<T> {

    final static OptionalInt OFFSET = OptionalInt.of(1);

    final static OptionalInt COUNT = OptionalInt.of(23);

    final static HistoryTokenOffsetAndCount OFFSET_AND_COUNT = HistoryTokenOffsetAndCount.with(
        OFFSET,
        COUNT
    );

    SpreadsheetListHistoryTokenTestCase() {
        super();
    }

    // dateTimeSymbols..................................................................................................

    @Test
    public final void testDateTimeSymbols() {
        this.dateTimeSymbolsAndCheck(
            this.createHistoryToken()
        );
    }

    // setId............................................................................................................

    @Test
    public final void testSetIdName() {
        this.setIdAndNameAndCheck(
            ID,
            NAME,
            HistoryToken.spreadsheetSelect(ID, NAME)
        );
    }

    @Test
    public final void testSetIdNameDifferentId() {
        final SpreadsheetId differentId = SpreadsheetId.with(9999);

        this.setIdAndNameAndCheck(
            differentId,
            NAME,
            HistoryToken.spreadsheetSelect(differentId, NAME)
        );
    }

    @Test
    public final void testSetIdNameDifferentName() {
        final SpreadsheetName differentName = SpreadsheetName.with("Different");

        this.setIdAndNameAndCheck(
            ID,
            differentName,
            HistoryToken.spreadsheetSelect(ID, differentName)
        );
    }

    // label............................................................................................................

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

    // list.............................................................................................................

    @Test
    public final void testListDifferentOffsetAndCount() {
        final HistoryTokenOffsetAndCount differentCount = HistoryTokenOffsetAndCount.with(
            OptionalInt.of(111),
            OptionalInt.of(222)
        );

        this.listAndCheck(
            this.createHistoryToken(HistoryTokenOffsetAndCount.EMPTY),
            differentCount,
            this.createHistoryToken(differentCount)
        );
    }

    @Test
    public final void testListDifferentOffsetAndCount2() {
        final HistoryTokenOffsetAndCount differentCount = HistoryTokenOffsetAndCount.EMPTY.setCount(
            OptionalInt.of(222)
        );

        this.listAndCheck(
            this.createHistoryToken(HistoryTokenOffsetAndCount.EMPTY.setOffset(OptionalInt.of(111))),
            differentCount,
            this.createHistoryToken(differentCount)
        );
    }

    @Test
    public final void testSetMetadataPropertyName() {
        this.setMetadataPropertyNameAndCheck(
            SpreadsheetMetadataPropertyName.LOCALE
        );
    }

    // navigation.......................................................................................................

    @Test
    public final void testNavigation() {
        this.navigationAndCheck(
            this.createHistoryToken()
        );
    }

    @Test
    public final void testSetSelectionWithCell() {
        this.setSelectionAndCheck(
            this.createHistoryToken(),
            SpreadsheetSelection.A1
        );
    }

    @Override final T createHistoryToken() {
        return this.createHistoryToken(
            HistoryTokenOffsetAndCount.with(
                OFFSET,
                COUNT
            )
        );
    }

    abstract T createHistoryToken(final HistoryTokenOffsetAndCount offsetAndCount);
}
