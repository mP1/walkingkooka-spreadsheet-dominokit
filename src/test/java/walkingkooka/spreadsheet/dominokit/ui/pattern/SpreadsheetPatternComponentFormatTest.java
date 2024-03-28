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

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetPatternComponentFormatTest implements SpreadsheetDialogComponentLifecycleTesting<SpreadsheetPatternComponentFormat> {

    private final static SpreadsheetId ID = SpreadsheetId.with(1);

    private final static SpreadsheetName NAME = SpreadsheetName.with("Spreadsheet123");

    private final static AnchoredSpreadsheetSelection CELL = SpreadsheetSelection.A1.setDefaultAnchor();

    @Test
    public void testMetadataFormatPatternDateDialogClose() {
        final SpreadsheetMetadataPropertyName<SpreadsheetDateFormatPattern> property = SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN;

        this.historyTokenCloseDialogAndCheck(
                HistoryToken.metadataPropertySelect(
                        ID,
                        NAME,
                        property
                ),
                HistoryToken.metadataSelect(
                        ID,
                        NAME
                )
        );
    }

    @Test
    public void testMetadataFormatPatternDateDialogSave() {
        final SpreadsheetMetadataPropertyName<SpreadsheetDateFormatPattern> property = SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN;
        final SpreadsheetDateFormatPattern value = SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy");

        this.historyTokenSaveValueAndCheck(
                HistoryToken.metadataPropertySelect(
                        ID,
                        NAME,
                        property
                ),
                Optional.of(value),
                HistoryToken.metadataPropertySave(
                        ID,
                        NAME,
                        property,
                        Optional.of(value)
                )
        );
    }

    @Test
    public void testMetadataFormatPatternDateDialogRemove() {
        final SpreadsheetMetadataPropertyName<SpreadsheetDateFormatPattern> property = SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN;

        this.historyTokenRemoveValueAndCheck(
                HistoryToken.metadataPropertySelect(
                        ID,
                        NAME,
                        property
                ),
                Optional.empty(),
                HistoryToken.metadataPropertySave(
                        ID,
                        NAME,
                        property,
                        Optional.empty()
                )
        );
    }

    @Test
    public void testCellFormatPatternDateDialogClose() {
        this.historyTokenCloseDialogAndCheck(
                HistoryToken.cellFormatPattern(
                        ID,
                        NAME,
                        CELL
                ),
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL
                )
        );
    }

    @Test
    public void testCellFormatPatternDateDialogSave() {
        final SpreadsheetDateFormatPattern value = SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy");
        final SpreadsheetPatternKind kind = SpreadsheetPatternKind.DATE_FORMAT_PATTERN;

        this.historyTokenSaveValueAndCheck(
                HistoryToken.cellPattern(
                        ID,
                        NAME,
                        CELL,
                        kind
                ),
                Optional.of(value),
                HistoryToken.cellPatternSave(
                        ID,
                        NAME,
                        CELL,
                        kind,
                        Optional.of(value)
                )
        );
    }

    @Test
    public void testCellFormatPatternDateDialogRemove() {
        final SpreadsheetPatternKind kind = SpreadsheetPatternKind.DATE_FORMAT_PATTERN;

        this.historyTokenRemoveValueAndCheck(
                HistoryToken.cellPattern(
                        ID,
                        NAME,
                        CELL,
                        kind
                ),
                Optional.empty(),
                HistoryToken.cellPatternSave(
                        ID,
                        NAME,
                        CELL,
                        kind,
                        Optional.empty()
                )
        );
    }
}
