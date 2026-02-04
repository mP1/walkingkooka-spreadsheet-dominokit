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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.OptionalInt;

public abstract class SpreadsheetLabelMappingHistoryTokenTestCase<T extends SpreadsheetLabelMappingHistoryToken> extends SpreadsheetSelectionHistoryTokenTestCase<T> {

    SpreadsheetLabelMappingHistoryTokenTestCase() {
        super();
    }

    // close............................................................................................................

    @Test
    public final void testClose() {
        this.closeAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetSelect(
                ID,
                NAME
            )
        );
    }

    // dateTimeSymbols..................................................................................................

    @Test
    public final void testDateTimeSymbols() {
        this.dateTimeSymbolsAndCheck(
            this.createHistoryToken()
        );
    }

    // createLabel......................................................................................................

    @Test
    public final void testCreateLabel() {
        this.createLabelAndCheck(
            this.createHistoryToken(),
            HistoryToken.labelMappingCreate(
                ID,
                NAME
            )
        );
    }

    // labels...........................................................................................................

    @Test
    public final void testSetLabels() {
        final HistoryTokenOffsetAndCount offsetAndCount = HistoryTokenOffsetAndCount.EMPTY.setCount(
            OptionalInt.of(123)
        );

        this.setLabelsAndCheck(
            this.createHistoryToken(),
            offsetAndCount,
            HistoryToken.labelMappingList(
                ID,
                NAME,
                offsetAndCount
            )
        );
    }

    // locale...........................................................................................................

    @Test
    public final void testLocale() {
        this.localeAndCheck(
            this.createHistoryToken()
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
    public final void testSetSelectionWithoutSelection() {
        this.setSelectionAndCheck(
            this.createHistoryToken()
        );
    }

    @Test
    public final void testSetSelectionWithColumn() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseColumn("A");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.columnSelect(
                ID,
                NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testSetSelectionWithColumnRange() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseColumnRange("B:C");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.columnSelect(
                ID,
                NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testSetSelectionWithCell() {
        final SpreadsheetSelection selection = SpreadsheetSelection.A1;

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.cellSelect(
                ID,
                NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testSetSelectionWithCellRange() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseCellRange("B2:C3");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.cellSelect(
                ID,
                NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testSetSelectionWithLabel() {
        final SpreadsheetSelection selection = SpreadsheetSelection.labelName("Hello");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.cellSelect(
                ID,
                NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testSetSelectionWithRow() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseRow("1");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.rowSelect(
                ID,
                NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testSetSelectionWithRowRange() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseRowRange("2:3");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.rowSelect(
                ID,
                NAME,
                selection.setDefaultAnchor()
            )
        );
    }
}
