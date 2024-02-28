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
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class SpreadsheetCellStyleSelectHistoryTokenTest extends SpreadsheetCellStyleHistoryTokenTestCase<SpreadsheetCellStyleSelectHistoryToken<Color>> {

    // setSave.........................................................................................................

    @Test
    public void testSetSaveStyle() {
        final AnchoredSpreadsheetSelection selection = CELL.setDefaultAnchor();
        final TextStylePropertyName<Color> propertyName = TextStylePropertyName.BACKGROUND_COLOR;
        final String value = "#123456";
        final HistoryToken historyToken = HistoryToken.cellStyle(ID, NAME, selection, propertyName);

        this.setSaveAndCheck(
                historyToken,
                value,
                HistoryToken.cellStyleSave(
                        ID,
                        NAME,
                        selection,
                        propertyName,
                        Optional.of(Color.parse(value))
                )
        );
    }

    @Test
    public void testSetSaveStyleWithEmptyText() {
        final AnchoredSpreadsheetSelection selection = CELL.setDefaultAnchor();
        final TextStylePropertyName<Color> propertyName = TextStylePropertyName.BACKGROUND_COLOR;
        final String value = "";
        final HistoryToken historyToken = HistoryToken.cellStyle(ID, NAME, selection, propertyName);

        this.setSaveAndCheck(
                historyToken,
                value,
                HistoryToken.cellStyleSave(
                        ID,
                        NAME,
                        selection,
                        propertyName,
                        Optional.empty()
                )
        );
    }

    // urlFragment.....................................................................................................

    @Test
    public void testUrlFragmentCellAll() {
        this.urlFragmentAndCheck(
                SpreadsheetCellStyleSelectHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        TextStylePropertyName.ALL
                ),
                "/123/SpreadsheetName456/cell/A1/style/*"
        );
    }

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/style/color");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
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
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/123/SpreadsheetName456/cell/Label123/style/color"
        );
    }

    @Test
    public void testUrlFragmentAllTextStylePropertyNames() {
        for (final TextStylePropertyName<?> propertyName : TextStylePropertyName.values()) {
            final SpreadsheetCellStyleSelectHistoryToken<?> token = SpreadsheetCellStyleSelectHistoryToken.with(
                    ID,
                    NAME,
                    CELL.setDefaultAnchor(),
                    propertyName
            );
            this.urlFragmentAndCheck(
                    token,
                    token.urlFragment()
            );
        }
    }

    @Test
    public void testParseAllTextStylePropertyNames() {
        for (final TextStylePropertyName<?> propertyName : TextStylePropertyName.values()) {
            final SpreadsheetCellStyleSelectHistoryToken<?> token = SpreadsheetCellStyleSelectHistoryToken.with(
                    ID,
                    NAME,
                    CELL.setDefaultAnchor(),
                    propertyName
            );
            this.parseAndCheck(
                    token.urlFragment(),
                    token
            );
        }
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
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
                PROPERTY_NAME
        );
    }

    @Override
    public Class<SpreadsheetCellStyleSelectHistoryToken<Color>> type() {
        return Cast.to(SpreadsheetCellStyleSelectHistoryToken.class);
    }
}
