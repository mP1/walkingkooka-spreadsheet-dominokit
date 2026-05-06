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
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellStyleSelectHistoryTokenTest extends SpreadsheetCellStyleHistoryTokenTestCase<SpreadsheetCellStyleSelectHistoryToken<Color>> {

    private final static Optional<String> NO_FILTER = Optional.empty();

    private final static Optional<String> FILTER = Optional.of(
        "Filter 123"
    );

    // with.............................................................................................................

    @Test
    public void testWithNullFilter() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SELECTION,
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
    public void testSetSaveStringValueNotEmptyString() {
        final AnchoredSpreadsheetSelection selection = CELL.setDefaultAnchor();
        final TextStylePropertyName<Color> propertyName = TextStylePropertyName.BACKGROUND_COLOR;
        final String value = "#123456";
        final HistoryToken historyToken = HistoryToken.cellStyle(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            selection,
            Optional.of(propertyName),
            NO_FILTER
        );

        this.setSaveStringValueAndCheck(
            historyToken,
            value,
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                selection,
                propertyName,
                Optional.of(Color.parse(value))
            )
        );
    }

    @Test
    public void testSetSaveStringValueWithEmptyString() {
        final AnchoredSpreadsheetSelection selection = CELL.setDefaultAnchor();
        final TextStylePropertyName<Color> propertyName = TextStylePropertyName.BACKGROUND_COLOR;
        final String value = "";
        final HistoryToken historyToken = HistoryToken.cellStyle(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            selection,
            Optional.of(propertyName),
            NO_FILTER
        );

        this.setSaveStringValueAndCheck(
            historyToken,
            value,
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                selection,
                propertyName,
                Optional.empty()
            )
        );
    }

    @Test
    public void testSetSaveStringValueWithoutTextStylePropertyNameAndNotEmptyString() {
        final AnchoredSpreadsheetSelection selection = CELL.setDefaultAnchor();

        final TextStyle value = TextStyle.parse("text-align: LEFT; vertical-align: TOP");

        final HistoryToken historyToken = HistoryToken.cellStyle(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            selection,
            Optional.empty(),
            NO_FILTER
        );

        this.setSaveStringValueAndCheck(
            historyToken,
            value.text(),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                selection,
                TextStylePropertyName.ALL,
                Optional.of(value)
            )
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public void testSetSaveValueNotEmptyValue() {
        final AnchoredSpreadsheetSelection selection = CELL.setDefaultAnchor();
        final TextStylePropertyName<Color> propertyName = TextStylePropertyName.BACKGROUND_COLOR;
        final Color value = Color.BLACK;

        final HistoryToken historyToken = HistoryToken.cellStyle(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            selection,
            Optional.of(propertyName),
            NO_FILTER
        );

        this.setSaveValueAndCheck(
            historyToken,
            value,
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                selection,
                propertyName,
                Optional.of(value)
            )
        );
    }

    @Test
    public void testSetSaveValueWithTextStylePropertyValueAllAndNotEmptyTextStyleValue() {
        final AnchoredSpreadsheetSelection selection = CELL.setDefaultAnchor();

        final TextStylePropertyName<TextStyle> propertyName = TextStylePropertyName.ALL;
        final TextStyle value = TextStyle.parse("text-align: LEFT; vertical-align: TOP");

        final HistoryToken historyToken = HistoryToken.cellStyle(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            selection,
            Optional.of(propertyName),
            NO_FILTER
        );

        this.setSaveValueAndCheck(
            historyToken,
            value,
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                selection,
                propertyName,
                Optional.of(value)
            )
        );
    }

    @Test
    public void testSetSaveValueWithoutTextStylePropertyValueAllAndNotEmptyTextStyleValue() {
        final AnchoredSpreadsheetSelection selection = CELL.setDefaultAnchor();

        final TextStyle value = TextStyle.parse("text-align: LEFT; vertical-align: TOP");

        final HistoryToken historyToken = HistoryToken.cellStyle(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            selection,
            Optional.empty(),
            NO_FILTER
        );

        this.setSaveValueAndCheck(
            historyToken,
            value,
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                selection,
                TextStylePropertyName.ALL,
                Optional.of(value)
            )
        );
    }
    
    // urlFragment.....................................................................................................

    @Test
    public void testUrlFragmentCellWithoutTextStylePropertyName() {
        this.urlFragmentAndCheck(
            SpreadsheetCellStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                Optional.empty(),
                NO_FILTER
            ),
            "/123/SpreadsheetName456/cell/A1/style"
        );
    }

    @Test
    public void testUrlFragmentCellWithoutTextStylePropertyNameAndFilter() {
        this.urlFragmentAndCheck(
            SpreadsheetCellStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                Optional.empty(),
                FILTER
            ),
            "/123/SpreadsheetName456/cell/A1/style/*/filter/Filter%20123"
        );
    }

    @Test
    public void testUrlFragmentCellAll() {
        this.urlFragmentAndCheck(
            SpreadsheetCellStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                Optional.of(TextStylePropertyName.ALL),
                NO_FILTER
            ),
            "/123/SpreadsheetName456/cell/A1/style/*"
        );
    }

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/style/color");
    }

    @Test
    public void testUrlFragmentCellAndFilter() {
        this.urlFragmentAndCheck(
            this.createHistoryToken(FILTER),
            "/123/SpreadsheetName456/cell/A1/style/color/filter/Filter%20123");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
            CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/B2:C3/top-left/style/color"
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck(
            SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/*/top-left/style/color"
        );
    }

    @Test
    public void testUrlFragmentCellRangeAndFilter() {
        this.urlFragmentAndCheck(
            SpreadsheetCellStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                Optional.of(STYLE_PROPERTY_NAME),
                FILTER
            ),
            "/123/SpreadsheetName456/cell/B2:C3/top-left/style/color/filter/Filter%20123"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
            LABEL,
            "/123/SpreadsheetName456/cell/Label123/style/color"
        );
    }

    @Test
    public void testUrlFragmentAllTextStylePropertyNames() {
        for (final TextStylePropertyName<?> propertyName : TextStylePropertyName.VALUES) {
            final SpreadsheetCellStyleSelectHistoryToken<?> token = SpreadsheetCellStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                Optional.of(propertyName),
                NO_FILTER
            );
            this.urlFragmentAndCheck(
                token,
                "/123/SpreadsheetName456/cell/A1/style/" + propertyName.value()
            );
        }
    }

    @Test
    public void testUrlFragmentAll() {
        final SpreadsheetCellStyleSelectHistoryToken<?> token = SpreadsheetCellStyleSelectHistoryToken.with(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            CELL.setDefaultAnchor(),
            Optional.of(TextStylePropertyName.ALL),
            NO_FILTER
        );
        this.urlFragmentAndCheck(
            token,
            token.urlFragment()
        );
    }

    @Test
    public void testUrlFragmentAllAndFilter() {
        final SpreadsheetCellStyleSelectHistoryToken<?> token = SpreadsheetCellStyleSelectHistoryToken.with(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            CELL.setDefaultAnchor(),
            Optional.of(TextStylePropertyName.ALL),
            FILTER
        );
        this.urlFragmentAndCheck(
            token,
            "/123/SpreadsheetName456/cell/A1/style/*/filter/Filter%20123"
        );
    }

    @Test
    public void testParseUntilEmptyTextStylePropertyNames() {
        for (final TextStylePropertyName<?> propertyName : TextStylePropertyName.VALUES) {
            final SpreadsheetCellStyleSelectHistoryToken<?> token = SpreadsheetCellStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                Optional.of(propertyName),
                NO_FILTER
            );
            this.parseAndCheck(
                token.urlFragment(),
                token
            );
        }
    }

    @Test
    public void testParseUntilEmptyTextStylePropertyNamesWithFilter() {
        for (final TextStylePropertyName<?> propertyName : TextStylePropertyName.VALUES) {
            final SpreadsheetCellStyleSelectHistoryToken<?> token = SpreadsheetCellStyleSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                Optional.of(propertyName),
                FILTER
            );
            this.parseAndCheck(
                token.urlFragment(),
                token
            );
        }
    }

    @Test
    public void testParseAllWildcard() {
        final SpreadsheetCellStyleSelectHistoryToken<?> token = SpreadsheetCellStyleSelectHistoryToken.with(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            CELL.setDefaultAnchor(),
            Optional.of(TextStylePropertyName.ALL),
            NO_FILTER
        );
        this.parseAndCheck(
            token.urlFragment(),
            token
        );
    }

    @Test
    public void testParseAllWildcardAndFilter() {
        final SpreadsheetCellStyleSelectHistoryToken<?> token = SpreadsheetCellStyleSelectHistoryToken.with(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            CELL.setDefaultAnchor(),
            Optional.of(TextStylePropertyName.ALL),
            FILTER
        );
        this.parseAndCheck(
            token.urlFragment(),
            token
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
    }

    private SpreadsheetCellStyleSelectHistoryToken<Color> createHistoryToken(final String filter) {
        return this.createHistoryToken(
            Optional.of(filter)
        );
    }

    private SpreadsheetCellStyleSelectHistoryToken<Color> createHistoryToken(final Optional<String> filter) {
        return SpreadsheetCellStyleSelectHistoryToken.with(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SELECTION,
            Optional.of(STYLE_PROPERTY_NAME),
            filter
        );
    }

    @Override
    SpreadsheetCellStyleSelectHistoryToken<Color> createHistoryToken(final SpreadsheetId id,
                                                                     final SpreadsheetName name,
                                                                     final AnchoredSpreadsheetSelection selection,
                                                                     final TextStylePropertyName<Color> propertyName) {
        return SpreadsheetCellStyleSelectHistoryToken.with(
            id,
            name,
            selection,
            Optional.of(STYLE_PROPERTY_NAME),
            NO_FILTER
        );
    }

    @Override
    public Class<SpreadsheetCellStyleSelectHistoryToken<Color>> type() {
        return Cast.to(SpreadsheetCellStyleSelectHistoryToken.class);
    }
}
