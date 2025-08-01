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
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.plugin.PluginName;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetDialogComponentContextTest implements ClassTesting<SpreadsheetDialogComponentContext> {

    private final static SpreadsheetId ID = SpreadsheetId.with(1);

    private final static SpreadsheetName NAME = SpreadsheetName.with("SpreadsheetName123");

    // pluginDialogTitle................................................................................................

    @Test
    public void testPluginDialogTitleWithoutPluginName() {
        this.pluginDialogTitleAndCheck(
            Optional.empty(),
            "Action123",
            "Plugin: Action123"
        );
    }

    @Test
    public void testPluginDialogTitleWithPluginName() {
        this.pluginDialogTitleAndCheck(
            Optional.of(
                PluginName.with("HelloPlugin")
            ),
            "Action456",
            "Plugin HelloPlugin: Action456"
        );
    }

    private void pluginDialogTitleAndCheck(final Optional<PluginName> plugin,
                                           final String action,
                                           final String expected) {
        this.checkEquals(
            expected,
            new FakeSpreadsheetDialogComponentContext() {

                @Override
                public HistoryToken historyToken() {
                    return plugin.isPresent() ?
                        HistoryToken.pluginSelect(plugin.get()) :
                        HistoryToken.pluginUploadSelect();
                }
            }.pluginDialogTitle(action),
            () -> "plugin: " + plugin + " action: " + action
        );
    }

    // selectionDialogTitle.............................................................................................

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
            new FakeSpreadsheetDialogComponentContext() {

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.cellSelect(
                        ID,
                        NAME,
                        selection.setDefaultAnchor()
                    );
                }
            }.selectionDialogTitle(action),
            () -> "selection: " + selection + " action: " + action
        );
    }

    // selectionTextStylePropertyDialogTitle............................................................................

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
            new FakeSpreadsheetDialogComponentContext() {
                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.cellStyle(
                        ID,
                        NAME,
                        selection.setDefaultAnchor(),
                        propertyName
                    );
                }
            }.selectionTextStylePropertyDialogTitle(propertyName),
            () -> "selection: " + selection + " propertyName: " + propertyName
        );
    }

    // selectionValueDialogTitle........................................................................................

    @Test
    public void testSelectionValueDialogTitleWithDateTimeSymbols() {
        this.selectionValueAndCheck(
            SpreadsheetSelection.A1,
            DateTimeSymbols.class,
            "A1: Date Time Symbols"
        );
    }

    @Test
    public void testSelectionValueDialogTitleWithLocale() {
        this.selectionValueAndCheck(
            SpreadsheetSelection.A1,
            Locale.class,
            "A1: Locale"
        );
    }

    @Test
    public void testSelectionValueDialogTitleWithSpreadsheetFormatterSelector() {
        this.selectionValueAndCheck(
            SpreadsheetSelection.A1,
            SpreadsheetFormatterSelector.class,
            "A1: Formatter"
        );
    }

    @Test
    public void testSelectionValueDialogTitleWithSpreadsheetParserSelector() {
        this.selectionValueAndCheck(
            SpreadsheetSelection.A1,
            SpreadsheetParserSelector.class,
            "A1: Parser"
        );
    }

    @Test
    public void testSelectionValueDialogTitleWithValidator() {
        this.selectionValueAndCheck(
            SpreadsheetSelection.A1,
            ValidatorSelector.class,
            "A1: Validator"
        );
    }

    @Test
    public void testSelectionValueDialogTitleWithCellRange() {
        this.selectionValueAndCheck(
            SpreadsheetSelection.parseCellRange("B2:C3"),
            Locale.class,
            "B2:C3: Locale"
        );
    }

    @Test
    public void testSelectionValueDialogTitleWithLabel() {
        this.selectionValueAndCheck(
            SpreadsheetSelection.labelName("Label123"),
            Locale.class,
            "Label123: Locale"
        );
    }

    private void selectionValueAndCheck(final SpreadsheetSelection selection,
                                        final Class<?> type,
                                        final String expected) {
        this.checkEquals(
            expected,
            new FakeSpreadsheetDialogComponentContext() {
                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.selection(
                        ID,
                        NAME,
                        selection.setDefaultAnchor()
                    );
                }
            }.selectionValueDialogTitle(type),
            () -> "selection: " + selection + " type: " + type.getSimpleName()
        );
    }

    // spreadsheetMetadataPropertyNameDialogTitle.......................................................................

    @Test
    public void testSpreadsheetMetadataPropertyNameDialogTitleWithConverters() {
        this.spreadsheetMetadataPropertyNameDialogTitleAndCheck(
            SpreadsheetMetadataPropertyName.CONVERTERS,
            "Spreadsheet: Converters (converters)"
        );
    }

    @Test
    public void testSpreadsheetMetadataPropertyNameDialogTitleWithFindConverter() {
        this.spreadsheetMetadataPropertyNameDialogTitleAndCheck(
            SpreadsheetMetadataPropertyName.FIND_CONVERTER,
            "Spreadsheet: Find Converter (findConverter)"
        );
    }

    @Test
    public void testSpreadsheetMetadataPropertyNameDialogTitleWithRoundingMode() {
        this.spreadsheetMetadataPropertyNameDialogTitleAndCheck(
            SpreadsheetMetadataPropertyName.ROUNDING_MODE,
            "Spreadsheet: Rounding Mode (roundingMode)"
        );
    }

    @Test
    public void testSpreadsheetMetadataPropertyNameDialogTitleWithHideZeroValues() {
        this.spreadsheetMetadataPropertyNameDialogTitleAndCheck(
            SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES,
            "Spreadsheet: Hide Zero Values (hideZeroValues)"
        );
    }

    @Test
    public void testSpreadsheetMetadataPropertyNameDialogTitleWithDateFormatter() {
        this.spreadsheetMetadataPropertyNameDialogTitleAndCheck(
            SpreadsheetMetadataPropertyName.DATE_FORMATTER,
            "Spreadsheet: Date Formatter (dateFormatter)"
        );
    }

    @Test
    public void testSpreadsheetMetadataPropertyNameDialogTitleWithDateTimeFormatter() {
        this.spreadsheetMetadataPropertyNameDialogTitleAndCheck(
            SpreadsheetMetadataPropertyName.DATE_TIME_FORMATTER,
            "Spreadsheet: Date Time Formatter (dateTimeFormatter)"
        );
    }

    private void spreadsheetMetadataPropertyNameDialogTitleAndCheck(final SpreadsheetMetadataPropertyName<?> propertyName,
                                                                    final String expected) {
        this.checkEquals(
            expected,
            new FakeSpreadsheetDialogComponentContext().spreadsheetMetadataPropertyNameDialogTitle(propertyName)
        );
    }

    // spreadsheetDialogTitle...........................................................................................

    @Test
    public void testSpreadsheetDialogTitleWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> new FakeSpreadsheetDialogComponentContext()
                .spreadsheetDialogTitle(null)
        );
    }

    @Test
    public void testSpreadsheetDialogTitleWithEmptyFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new FakeSpreadsheetDialogComponentContext()
                .spreadsheetDialogTitle("")
        );
    }

    @Test
    public void testSpreadsheetDialogTitleWithText() {
        this.spreadsheetDialogTitleAndCheck(
            "Text123",
            "Spreadsheet: Text123"
        );
    }

    private void spreadsheetDialogTitleAndCheck(final String title,
                                                final String expected) {
        this.checkEquals(
            expected,
            new FakeSpreadsheetDialogComponentContext()
                .spreadsheetDialogTitle(title)
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
