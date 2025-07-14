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

package walkingkooka.spreadsheet.dominokit.convert;

import org.junit.jupiter.api.Test;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AppContextConverterSelectorDialogComponentContextTest implements ConverterSelectorDialogComponentContextTesting<AppContextConverterSelectorDialogComponentContext> {

    private final static SpreadsheetMetadataPropertyName<ConverterSelector> PROPERTY_NAME = SpreadsheetMetadataPropertyName.FIND_CONVERTER;

    @Test
    public void testWithNullPropertyNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppContextConverterSelectorDialogComponentContext.with(
                null,
                AppContexts.fake()
            )
        );
    }

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppContextConverterSelectorDialogComponentContext.with(
                PROPERTY_NAME,
                null
            )
        );
    }

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertySelectHistoryTokenFindConverter() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.FIND_CONVERTER
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertySelectHistoryTokenFormattingConverter() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.FORMATTING_CONVERTER
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertySelectHistoryTokenConverters() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.CONVERTERS
            ),
            false
        );
    }

    @Override
    public AppContextConverterSelectorDialogComponentContext createContext() {
        return AppContextConverterSelectorDialogComponentContext.with(
            PROPERTY_NAME,
            AppContexts.fake()
        );
    }

    @Override
    public String typeNameSuffix() {
        return ConverterSelectorDialogComponentContext.class.getSimpleName();
    }

    // class............................................................................................................

    @Override
    public Class<AppContextConverterSelectorDialogComponentContext> type() {
        return AppContextConverterSelectorDialogComponentContext.class;
    }
}
