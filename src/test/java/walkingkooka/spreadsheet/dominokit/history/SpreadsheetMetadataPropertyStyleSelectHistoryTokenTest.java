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
import walkingkooka.Cast;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetMetadataPropertyStyleSelectHistoryTokenTest extends SpreadsheetMetadataPropertyStyleHistoryTokenTestCase<SpreadsheetMetadataPropertyStyleSelectHistoryToken<Color>, Color> {

    private final static Optional<String> NO_FILTER = Optional.empty();

    private final static Optional<String> FILTER = Optional.of(
        "Filter 123"
    );

    // with.............................................................................................................

    @Test
    public void testWithNullFilter() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.of(STYLE_PROPERTY_NAME),
                null
            )
        );
    }

    // filter...........................................................................................................

    @Test
    public void testFilter() {
        this.filterAndCheck(
            this.createHistoryToken()
        );
    }

    // setFilter........................................................................................................

    @Test
    public void testSetFilterWithSameNonEmptyFilter() {
        final String filter = "Filter 123";

        this.setFilterAndCheck(
            this.createHistoryToken(filter),
            filter
        );
    }

    @Test
    public void testSetFilterWithDifferentFilter() {
        final String filter = "Different Filter 222";

        this.setFilterAndCheck(
            this.createHistoryToken("Filter 111"),
            filter,
            this.createHistoryToken(filter)
        );
    }

    // setSaveStringValue...............................................................................................

    @Test
    public void testSetSaveStringValueWithString() {
        final SpreadsheetMetadataPropertyStyleSelectHistoryToken<Color> historyToken = this.createHistoryToken();
        final String value = "#123456";

        this.setSaveStringValueAndCheck(
            historyToken,
            value,
            HistoryToken.metadataPropertyStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                STYLE_PROPERTY_NAME,
                Optional.of(Color.parse(value))
            )
        );
    }

    @Test
    public void testSetSaveStringValueWithEmptyText() {
        final SpreadsheetMetadataPropertyStyleSelectHistoryToken<Color> historyToken = this.createHistoryToken();
        final String value = "";

        this.setSaveStringValueAndCheck(
            historyToken,
            value,
            HistoryToken.metadataPropertyStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                STYLE_PROPERTY_NAME,
                Optional.empty()
            )
        );
    }

    @Test
    public void testSetSaveStringValueWithoutTextStylePropertyName() {
        final TextStyle value = TextStyle.parse("text-align: LEFT; vertical-align: TOP;");

        this.setSaveStringValueAndCheck(
            SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.empty(),
                FILTER
            ),
            value.text(),
            HistoryToken.metadataPropertyStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                TextStylePropertyName.ALL,
                Optional.of(value)
            )
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public void testSetSaveValueWithoutTextStylePropertyName() {
        final Optional<TextStyle> value = Optional.of(
            TextStyle.parse("text-align: left;")
        );

        this.setSaveValueAndCheck(
            SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.empty(),
                FILTER
            ),
            value,
            HistoryToken.metadataPropertyStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                TextStylePropertyName.ALL,
                value
            )
        );
    }

    @Test
    public void testSetSaveValueWithEmptyValue() {
        final Optional<Color> value = Optional.empty();

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            value,
            HistoryToken.metadataPropertyStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                STYLE_PROPERTY_NAME,
                value
            )
        );
    }

    @Test
    public void testSetSaveValueWithValue() {
        final Optional<Color> value = Optional.of(
            Color.parse("#999")
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            value,
            HistoryToken.metadataPropertyStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                STYLE_PROPERTY_NAME,
                value
            )
        );
    }

    // stylePropertyName................................................................................................

    @Test
    public void testStylePropertyName() {
        this.stylePropertyNameAndCheck(
            this.createHistoryToken(),
            STYLE_PROPERTY_NAME
        );
    }

    // setStylePropertyName................................................................................................

    @Test
    public void testSetStylePropertyName() {
        this.setStylePropertyNameAndCheck(
            this.createHistoryToken(),
            STYLE_PROPERTY_NAME
        );
    }

    @Test
    public void testSetStylePropertyNameDifferent() {
        final TextStylePropertyName<Color> textStylePropertyName = TextStylePropertyName.BACKGROUND_COLOR;

        this.setStylePropertyNameAndCheck(
            this.createHistoryToken(),
            textStylePropertyName,
            HistoryToken.metadataPropertyStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.of(textStylePropertyName),
                NO_FILTER
            )
        );
    }

    @Test
    public void testSetStylePropertyNameDifferentWithFilter() {
        final TextStylePropertyName<Color> textStylePropertyName = TextStylePropertyName.BACKGROUND_COLOR;

        this.setStylePropertyNameAndCheck(
            HistoryToken.metadataPropertyStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.of(STYLE_PROPERTY_NAME),
                FILTER
            ),
            textStylePropertyName,
            HistoryToken.metadataPropertyStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.of(textStylePropertyName),
                FILTER
            )
        );
    }

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragmentWithoutTextStylePropertyName() {
        this.urlFragmentAndCheck(
            SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.empty(),
                NO_FILTER
            ),
            "/123/SpreadsheetName456/spreadsheet/style"
        );
    }

    @Test
    public void testUrlFragmentWithoutTextStylePropertyNameAndFilter() {
        this.urlFragmentAndCheck(
            SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.empty(),
                FILTER
            ),
            "/123/SpreadsheetName456/spreadsheet/style/*/filter/Filter%20123"
        );
    }

    @Test
    public void testUrlFragmentColor() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/spreadsheet/style/color");
    }

    @Test
    public void testUrlFragmentFontFamily() {
        this.urlFragmentAndCheck(
            SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.of(TextStylePropertyName.FONT_FAMILY),
                NO_FILTER
            ),
            "/123/SpreadsheetName456/spreadsheet/style/font-family"
        );
    }

    @Test
    public void testUrlFragmentFontFamilyAndFilter() {
        this.urlFragmentAndCheck(
            SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.of(TextStylePropertyName.FONT_FAMILY),
                FILTER
            ),
            "/123/SpreadsheetName456/spreadsheet/style/font-family/filter/Filter%20123"
        );
    }

    @Test
    public void testUrlFragmentFontStyle() {
        this.urlFragmentAndCheck(
            SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.of(TextStylePropertyName.FONT_STYLE),
                NO_FILTER
            ),
            "/123/SpreadsheetName456/spreadsheet/style/font-style"
        );
    }

    @Test
    public void testUrlFragmentFontStyleAndFilter() {
        this.urlFragmentAndCheck(
            SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.of(TextStylePropertyName.FONT_STYLE),
                FILTER
            ),
            "/123/SpreadsheetName456/spreadsheet/style/font-style/filter/Filter%20123"
        );
    }

    @Test
    public void testParseBackgroundColor() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/style/background-color",
            SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.of(TextStylePropertyName.BACKGROUND_COLOR),
                NO_FILTER
            )
        );
    }

    @Test
    public void testParseBackgroundColorFilterEmptyText() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/style/background-color/filter",
            SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.of(TextStylePropertyName.BACKGROUND_COLOR),
                NO_FILTER
            )
        );
    }

    @Test
    public void testParseBackgroundColorFilter() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/style/background-color/filter/Filter%20123",
            SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.of(TextStylePropertyName.BACKGROUND_COLOR),
                FILTER
            )
        );
    }

    @Test
    public void testParseFontFamily() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/style/font-family",
            SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.of(TextStylePropertyName.FONT_FAMILY),
                NO_FILTER
            )
        );
    }

    @Test
    public void testParseFontFamilyWithFilter() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/style/font-family/filter/Filter%20123",
            SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.of(TextStylePropertyName.FONT_FAMILY),
                FILTER
            )
        );
    }

    @Test
    public void testParseUnknown() {
        this.urlFragmentAndCheck(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            "/123/SpreadsheetName456"
        );
    }

    // clearAction.....................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
    }

    private SpreadsheetMetadataPropertyStyleSelectHistoryToken<Color> createHistoryToken(final String filter) {
        return this.createHistoryToken(
            Optional.of(filter)
        );
    }

    private SpreadsheetMetadataPropertyStyleSelectHistoryToken<Color> createHistoryToken(final Optional<String> filter) {
        return SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            Optional.of(STYLE_PROPERTY_NAME),
            filter
        );
    }

    @Override
    SpreadsheetMetadataPropertyStyleSelectHistoryToken<Color> createHistoryToken(final SpreadsheetId id,
                                                                                 final SpreadsheetName name,
                                                                                 final SpreadsheetMetadataPropertyName<TextStyle> propertyName) {
        return SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
            id,
            name,
            Optional.of(STYLE_PROPERTY_NAME),
            NO_FILTER
        );
    }

    @Override
    public Class<SpreadsheetMetadataPropertyStyleSelectHistoryToken<Color>> type() {
        return Cast.to(SpreadsheetMetadataPropertyStyleSelectHistoryToken.class);
    }
}
