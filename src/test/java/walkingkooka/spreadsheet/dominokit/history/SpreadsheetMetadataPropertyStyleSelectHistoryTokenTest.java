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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

public final class SpreadsheetMetadataPropertyStyleSelectHistoryTokenTest extends SpreadsheetMetadataPropertyStyleHistoryTokenTestCase<SpreadsheetMetadataPropertyStyleSelectHistoryToken<Color>, Color> {

    @Test
    public void testUrlFragmentColor() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/metadata/style/color");
    }

    @Test
    public void testUrlFragmentFontFamily() {
        this.urlFragmentAndCheck(
                SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                        ID,
                        NAME,
                        TextStylePropertyName.FONT_FAMILY
                ),
                "/123/SpreadsheetName456/metadata/style/font-family"
        );
    }

    @Test
    public void testUrlFragmentFontStyle() {
        this.urlFragmentAndCheck(
                SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                        ID,
                        NAME,
                        TextStylePropertyName.FONT_STYLE
                ),
                "/123/SpreadsheetName456/metadata/style/font-style"
        );
    }

    @Test
    public void testParseBackgroundColor() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/metadata/style/background-color",
                SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                        ID,
                        NAME,
                        TextStylePropertyName.BACKGROUND_COLOR
                )
        );
    }

    @Test
    public void testParseFontFamily() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/metadata/style/font-family",
                SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                        ID,
                        NAME,
                        TextStylePropertyName.FONT_FAMILY
                )
        );
    }

    @Test
    public void testParseUnknown() {
        this.urlFragmentAndCheck(
                SpreadsheetHistoryToken.spreadsheetSelect(
                        ID,
                        NAME
                ),
                "/123/SpreadsheetName456"
        );
    }

    @Override
    SpreadsheetMetadataPropertyStyleSelectHistoryToken<Color> createHistoryHashToken(final SpreadsheetId id,
                                                                                     final SpreadsheetName name,
                                                                                     final SpreadsheetMetadataPropertyName<TextStyle> propertyName) {
        return SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                id,
                name,
                STYLE_PROPERTY_NAME
        );
    }

    @Override
    public Class<SpreadsheetMetadataPropertyStyleSelectHistoryToken<Color>> type() {
        return Cast.to(SpreadsheetMetadataPropertyStyleSelectHistoryToken.class);
    }
}
