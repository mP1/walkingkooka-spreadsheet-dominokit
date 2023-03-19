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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;
import walkingkooka.tree.text.TextStylePropertyName;

public final class SpreadsheetCellStyleSelectHistoryTokenTest extends SpreadsheetCellStyleHistoryTokenTestCase<SpreadsheetCellStyleSelectHistoryToken<Color>> {

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/style/color");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/B2:C3/top-left/style/color"
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
                    CELL.setAnchor(SpreadsheetViewportSelectionAnchor.NONE),
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
                    CELL.setAnchor(SpreadsheetViewportSelectionAnchor.NONE),
                    propertyName
            );
            this.parseAndCheck(
                    token.urlFragment(),
                    token
            );
        }
    }

    @Override
    SpreadsheetCellStyleSelectHistoryToken<Color> createHistoryToken(final SpreadsheetId id,
                                                                     final SpreadsheetName name,
                                                                     final SpreadsheetViewportSelection viewportSelection,
                                                                     final TextStylePropertyName<Color> propertyName) {
        return SpreadsheetCellStyleSelectHistoryToken.with(
                id,
                name,
                viewportSelection,
                PROPERTY_NAME
        );
    }

    @Override
    public Class<SpreadsheetCellStyleSelectHistoryToken<Color>> type() {
        return Cast.to(SpreadsheetCellStyleSelectHistoryToken.class);
    }
}
