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
import walkingkooka.tree.text.TextStylePropertyName;

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

    // selectionTextStylePropertyDialogTitle............................................................................

    @Test
    public void testSelectionTextStylePropertyDialogTitleWithNullSelectionFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetDialogComponentContext.selectionTextStylePropertyDialogTitle(
                null,
                TextStylePropertyName.TEXT_ALIGN
            )
        );
    }

    @Test
    public void testSelectionTextStylePropertyDialogTitleWithNullActionFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetDialogComponentContext.selectionTextStylePropertyDialogTitle(
                SpreadsheetSelection.A1,
                null
            )
        );
    }

    @Test
    public void testSelectionTextStylePropertyDialogTitleWithTextAlign() {
        this.selectionStylePropertyDialogTitleAndCheck(
            SpreadsheetSelection.A1,
            TextStylePropertyName.TEXT_ALIGN,
            "A1: Text Align"
        );
    }

    @Test
    public void testSelectionTextStylePropertyDialogTitleWithBackgroundColor() {
        this.selectionStylePropertyDialogTitleAndCheck(
            SpreadsheetSelection.A1,
            TextStylePropertyName.BACKGROUND_COLOR,
            "A1: Background Color"
        );
    }

    @Test
    public void testSelectionTextStylePropertyDialogTitleWithCellRange() {
        this.selectionStylePropertyDialogTitleAndCheck(
            SpreadsheetSelection.parseCellRange("B2:C3"),
            TextStylePropertyName.TEXT_ALIGN,
            "B2:C3: Text Align"
        );
    }

    @Test
    public void testSelectionTextStylePropertyDialogTitleWithLabel() {
        this.selectionStylePropertyDialogTitleAndCheck(
            SpreadsheetSelection.labelName("Label123"),
            TextStylePropertyName.TEXT_ALIGN,
            "Label123: Text Align"
        );
    }

    private void selectionStylePropertyDialogTitleAndCheck(final SpreadsheetSelection selection,
                                                           final TextStylePropertyName<?> propertyName,
                                                           final String expected) {
        this.checkEquals(
            expected,
            SpreadsheetDialogComponentContext.selectionTextStylePropertyDialogTitle(
                selection,
                propertyName
            ),
            () -> "selection: " + selection + " propertyName: " + propertyName
        );
    }

    // spreadsheetMetadataPropertyNameDialogTitle.......................................................................

    @Test
    public void testSpreadsheetMetadataPropertyNameDialogTitleWithConverters() {
        this.spreadsheetMetadataPropertyNameDialogTitleAndCheck(
            SpreadsheetMetadataPropertyName.CONVERTERS,
            "Spreadsheet: Converters(converters)"
        );
    }

    @Test
    public void testSpreadsheetMetadataPropertyNameDialogTitleWithFindConverter() {
        this.spreadsheetMetadataPropertyNameDialogTitleAndCheck(
            SpreadsheetMetadataPropertyName.FIND_CONVERTER,
            "Spreadsheet: Find Converter(findConverter)"
        );
    }

    @Test
    public void testSpreadsheetMetadataPropertyNameDialogTitleWithRoundingMode() {
        this.spreadsheetMetadataPropertyNameDialogTitleAndCheck(
            SpreadsheetMetadataPropertyName.ROUNDING_MODE,
            "Spreadsheet: Rounding Mode(roundingMode)"
        );
    }

    @Test
    public void testSpreadsheetMetadataPropertyNameDialogTitleWithHideZeroValues() {
        this.spreadsheetMetadataPropertyNameDialogTitleAndCheck(
            SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES,
            "Spreadsheet: Hide Zero Values(hideZeroValues)"
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
