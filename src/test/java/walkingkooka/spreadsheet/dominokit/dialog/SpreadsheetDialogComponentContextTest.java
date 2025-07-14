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

public final class SpreadsheetDialogComponentContextTest implements ClassTesting<SpreadsheetDialogComponentContext> {

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
