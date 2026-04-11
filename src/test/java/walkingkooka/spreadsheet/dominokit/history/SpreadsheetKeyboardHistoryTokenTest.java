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
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.OptionalInt;

public final class SpreadsheetKeyboardHistoryTokenTest extends SpreadsheetNameHistoryTokenTestCase<SpreadsheetKeyboardHistoryToken> {

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/keyboard");
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            )
        );
    }

    // close............................................................................................................

    @Test
    public void testClose() {
        this.closeAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            )
        );
    }

    // label............................................................................................................

    @Test
    public void testCreateLabel() {
        this.createLabelAndCheck(
            this.createHistoryToken(),
            HistoryToken.labelMappingCreate(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            )
        );
    }

    // labels...........................................................................................................

    @Test
    public void testSetLabels() {
        final HistoryTokenOffsetAndCount offsetAndCount = HistoryTokenOffsetAndCount.EMPTY.setCount(
            OptionalInt.of(123)
        );

        this.setLabelsAndCheck(
            this.createHistoryToken(),
            offsetAndCount,
            HistoryToken.labelMappingList(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                offsetAndCount
            )
        );
    }

    // setMetadataPropertyName..........................................................................................

    @Test
    public void testSetMetadataPropertyName() {
        final SpreadsheetMetadataPropertyName<?> propertyName = SpreadsheetMetadataPropertyName.LOCALE;

        this.setMetadataPropertyNameAndCheck(
            propertyName,
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                propertyName
            )
        );
    }

    // navigation.......................................................................................................

    @Test
    public void testNavigation() {
        this.navigationAndCheck(
            this.createHistoryToken()
        );
    }

    // rename...........................................................................................................

    @Test
    public void testRename() {
        this.renameAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetRenameSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            )
        );
    }

    // setSelection.....................................................................................................

    @Test
    public void testSetSelectionWithoutSelection() {
        this.setSelectionAndCheck(
            this.createHistoryToken()
        );
    }

    @Test
    public void testSetSelectionWithColumn() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseColumn("A");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.columnSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testSetSelectionWithColumnRange() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseColumnRange("B:C");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.columnSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testSetSelectionWithCell() {
        final SpreadsheetSelection selection = SpreadsheetSelection.A1;

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testSetSelectionWithCellRange() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseCellRange("B2:C3");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testSetSelectionWithLabel() {
        final SpreadsheetSelection selection = SpreadsheetSelection.labelName("Hello");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                selection.setDefaultAnchor()
            )
        );
    }


    @Test
    public void testSetSelectionWithRow() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseRow("1");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.rowSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testSetSelectionWithRowRange() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseRowRange("2:3");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.rowSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    // stylePropertyName................................................................................................

    @Test
    public void testStylePropertyName() {
        this.stylePropertyNameAndCheck(
            this.createHistoryToken()
        );
    }

    // setStyleProperty.................................................................................................

    @Test
    public void testSetStyleProperty() {
        this.setStylePropertyAndCheck(
            this.createHistoryToken(),
            TextStylePropertyName.TEXT_ALIGN.setValue(TextAlign.LEFT)
        );
    }

    @Override
    SpreadsheetKeyboardHistoryToken createHistoryToken(final SpreadsheetId id,
                                                       final SpreadsheetName name) {
        return SpreadsheetKeyboardHistoryToken.with(
            id,
            name
        );
    }

    @Override
    public Class<SpreadsheetKeyboardHistoryToken> type() {
        return SpreadsheetKeyboardHistoryToken.class;
    }
}
