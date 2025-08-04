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

package walkingkooka.spreadsheet.dominokit.locale;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AppContextSpreadsheetLocaleDialogComponentContextSpreadsheetMetadataLocaleTest extends AppContextSpreadsheetLocaleDialogComponentContextTestCase<AppContextSpreadsheetLocaleDialogComponentContextMetadataLocale> {

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppContextSpreadsheetLocaleDialogComponentContextMetadataLocale.with(null)
        );
    }

    // DialogTitle......................................................................................................

    @Test
    public void testDialogTitle() {
        this.dialogTitleAndCheck(
            this.createContext(),
            "Spreadsheet: Locale (locale)"
        );
    }

    // undoLocale.......................................................................................................

    private final static Locale LOCALE = Locale.forLanguageTag("en-AU");

    @Test
    public void testUndoLocale() {
        this.undoLocaleAndCheck(
            this.createContext(),
            LOCALE
        );
    }

    // IsMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetMetadataSelectNotLocale() {
        for (final SpreadsheetMetadataPropertyName<?> propertyName : SpreadsheetMetadataPropertyName.ALL) {
            if (SpreadsheetMetadataPropertyName.LOCALE.equals(propertyName)) {
                continue;
            }

            this.isMatchAndCheck(
                this.createContext(),
                HistoryToken.metadataPropertySelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    propertyName
                ),
                false
            );
        }
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataSelectWithLocale() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.LOCALE
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataSaveWithLocale() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.LOCALE,
                Optional.of(Locale.ENGLISH)
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataSelectWithFormulaConverter() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.FORMULA_CONVERTER
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithCellLocaleSelect() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellLocaleSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            false
        );
    }

    // shouldIgnore.....................................................................................................

    @Test
    public void testShouldIgnoreSpreadsheetMetadataPropertyNameWithNotLocale() {
        for (final SpreadsheetMetadataPropertyName<?> propertyName : SpreadsheetMetadataPropertyName.ALL) {
            if (SpreadsheetMetadataPropertyName.LOCALE.equals(propertyName)) {
                continue;
            }

            this.shouldIgnoreAndCheck(
                this.createContext(),
                HistoryToken.metadataPropertySelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    propertyName
                ),
                true
            );
        }
    }

    @Test
    public void testShouldIgnoreSpreadsheetMetadataPropertyNameWithLocale() {
        this.shouldIgnoreAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.LOCALE
            ),
            false
        );
    }

    @Override
    public AppContextSpreadsheetLocaleDialogComponentContextMetadataLocale createContext() {
        return AppContextSpreadsheetLocaleDialogComponentContextMetadataLocale.with(
            new FakeAppContext() {

                @Override
                public SpreadsheetMetadata spreadsheetMetadata() {
                    return SpreadsheetMetadata.EMPTY.set(
                        SpreadsheetMetadataPropertyName.LOCALE,
                        LOCALE
                    );
                }
            }
        );
    }

    @Override
    public Class<AppContextSpreadsheetLocaleDialogComponentContextMetadataLocale> type() {
        return AppContextSpreadsheetLocaleDialogComponentContextMetadataLocale.class;
    }
}
