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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;
import walkingkooka.tree.text.BorderStyle;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellStyleSaveHistoryTokenTest extends SpreadsheetCellStyleHistoryTokenTestCase<SpreadsheetCellStyleSaveHistoryToken<Color>> {

    @Test
    public void testWithNullPropertyValueFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetCellStyleSaveHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        TextStylePropertyName.COLOR,
                        null
                )
        );
    }

    @Test
    public void testUrlFragmentCellAll() {
        this.urlFragmentAndCheck(
                SpreadsheetCellStyleSaveHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        TextStylePropertyName.ALL,
                        Optional.empty()
                ),
                "/123/SpreadsheetName456/cell/A1/style/*/save/"
        );
    }

    @Test
    public void testUrlFragmentCellColor() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/style/color/save/#123456");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/B2:C3/top-left/style/color/save/#123456"
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck(
                SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/*/top-left/style/color/save/#123456"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/123/SpreadsheetName456/cell/Label123/style/color/save/#123456"
        );
    }

    @Test
    public void testUrlFragmentTextStylePropertyBottomBorderStyle() {
        this.urlFragmentAndCheck(
                TextStylePropertyName.BORDER_BOTTOM_STYLE,
                BorderStyle.DASHED,
                "/123/SpreadsheetName456/cell/A1/style/border-bottom-style/save/DASHED"
        );
    }

    @Test
    public void testParseTextStylePropertyBottomBorderStyle() {
        this.parseAndCheck(
                TextStylePropertyName.BORDER_BOTTOM_STYLE,
                BorderStyle.DASHED,
                "/123/SpreadsheetName456/cell/A1/style/border-bottom-style/save/DASHED"
        );
    }

    @Test
    public void testUrlFragmentTextStylePropertyBottomBorderWidth() {
        this.urlFragmentAndCheck(
                TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                Length.pixel(123.5),
                "/123/SpreadsheetName456/cell/A1/style/border-bottom-width/save/123.5px"
        );
    }

    @Test
    public void testParseTextStylePropertyBottomBorderWidth() {
        this.parseAndCheck(
                TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                Length.pixel(123.5),
                "/123/SpreadsheetName456/cell/A1/style/border-bottom-width/save/123.5px"
        );
    }

    @Test
    public void testParseTextStylePropertyBottomBorderWidthNull() {
        this.parseAndCheck(
                TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                null,
                "/123/SpreadsheetName456/cell/A1/style/border-bottom-width/save/"
        );
    }

    private <TT> void urlFragmentAndCheck(final TextStylePropertyName<TT> propertyName,
                                          final TT propertyValue,
                                          final String urlFragment) {
        this.urlFragmentAndCheck(
                SpreadsheetCellStyleSaveHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        propertyName,
                        Optional.ofNullable(propertyValue)
                ),
                urlFragment
        );
    }

    private <TT> void parseAndCheck(final TextStylePropertyName<TT> propertyName,
                                    final TT propertyValue,
                                    final String urlFragment) {
        this.parseAndCheck(
                urlFragment,
                SpreadsheetCellStyleSaveHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        propertyName,
                        Optional.ofNullable(propertyValue)
                )
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
                this.createHistoryToken(),
                HistoryToken.cellStyle(
                        ID,
                        NAME,
                        SELECTION,
                        PROPERTY_NAME
                )
        );
    }

    @Override
    SpreadsheetCellStyleSaveHistoryToken<Color> createHistoryToken(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final AnchoredSpreadsheetSelection selection,
                                                                   final TextStylePropertyName<Color> propertyName) {
        return SpreadsheetCellStyleSaveHistoryToken.with(
                id,
                name,
                selection,
                propertyName,
                Optional.of(PROPERTY_VALUE)
        );
    }

    @Override
    public Class<SpreadsheetCellStyleSaveHistoryToken<Color>> type() {
        return Cast.to(SpreadsheetCellStyleSaveHistoryToken.class);
    }
}
