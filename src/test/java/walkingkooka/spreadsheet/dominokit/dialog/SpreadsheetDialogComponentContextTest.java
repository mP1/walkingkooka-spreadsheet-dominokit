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

package walkingkooka.spreadsheet.dominokit.dialog;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetDialogComponentContextTest implements ClassTesting<SpreadsheetDialogComponentContext> {

    @Test
    public void testSelectionDialogTitleWithNullSelectionFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetDialogComponentContext.selectionDialogTitle(
                null,
                "Action123"
            )
        );
    }

    @Test
    public void testSelectionDialogTitleWithNullActionFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetDialogComponentContext.selectionDialogTitle(
                SpreadsheetSelection.A1,
                null
            )
        );
    }

    @Test
    public void testSelectionDialogTitleWithEmptyActionFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetDialogComponentContext.selectionDialogTitle(
                SpreadsheetSelection.A1,
                ""
            )
        );
    }

    @Test
    public void testSelectionDialogTitleWithCell() {
        this.selectionDialogTitleAndCheck(
            SpreadsheetSelection.A1,
            "Action123",
            "A1: Action123"
        );
    }

    @Test
    public void testSelectionDialogTitleWithCellRange() {
        this.selectionDialogTitleAndCheck(
            SpreadsheetSelection.parseCellRange("B2:C3"),
            "Action456",
            "B2:C3: Action456"
        );
    }

    @Test
    public void testSelectionDialogTitleWithLabel() {
        this.selectionDialogTitleAndCheck(
            SpreadsheetSelection.labelName("Label123"),
            "Action789",
            "Label123: Action789"
        );
    }

    private void selectionDialogTitleAndCheck(final SpreadsheetSelection selection,
                                              final String action,
                                              final String expected) {
        this.checkEquals(
            expected,
            SpreadsheetDialogComponentContext.selectionDialogTitle(
                selection,
                action
            ),
            () -> "selection: " + selection + " action: " + action
        );
    }

    // spreadsheetMetadataPropertyNameDialogTitle.......................................................................

    @Test
    public void testSpreadsheetMetadataPropertyNameDialogTitleWithConverters() {
        this.spreadsheetMetadataPropertyNameDialogTitleAndCheck(
            SpreadsheetMetadataPropertyName.CONVERTERS,
            "Spreadsheet metadata: Converters(converters)"
        );
    }

    @Test
    public void testSpreadsheetMetadataPropertyNameDialogTitleWithFindConverter() {
        this.spreadsheetMetadataPropertyNameDialogTitleAndCheck(
            SpreadsheetMetadataPropertyName.FIND_CONVERTER,
            "Spreadsheet metadata: Find Converter(findConverter)"
        );
    }

    @Test
    public void testSpreadsheetMetadataPropertyNameDialogTitleWithRoundingMode() {
        this.spreadsheetMetadataPropertyNameDialogTitleAndCheck(
            SpreadsheetMetadataPropertyName.ROUNDING_MODE,
            "Spreadsheet metadata: Rounding Mode(roundingMode)"
        );
    }

    @Test
    public void testSpreadsheetMetadataPropertyNameDialogTitleWithHideZeroValues() {
        this.spreadsheetMetadataPropertyNameDialogTitleAndCheck(
            SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES,
            "Spreadsheet metadata: Hide Zero Values(hideZeroValues)"
        );
    }

    private void spreadsheetMetadataPropertyNameDialogTitleAndCheck(final SpreadsheetMetadataPropertyName<?> propertyName,
                                                                    final String expected) {
        this.checkEquals(
            expected,
            SpreadsheetDialogComponentContext.spreadsheetMetadataPropertyNameDialogTitle(propertyName)
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetDialogComponentContext> type() {
        return SpreadsheetDialogComponentContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
