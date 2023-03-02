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
import walkingkooka.tree.text.FontFamily;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

public final class SpreadsheetMetadataPropertyStyleSaveHistoryHashTokenTest extends SpreadsheetMetadataPropertyStyleHistoryHashTokenTestCase<SpreadsheetMetadataPropertyStyleSaveHistoryHashToken<Color>, Color> {

    @Test
    public void testUrlFragmentColor() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/metadata/style/color/save/#123456");
    }

    @Test
    public void testUrlFragmentFontFamily() {
        this.urlFragmentAndCheck(
                SpreadsheetMetadataPropertyStyleSaveHistoryHashToken.with(
                        ID,
                        NAME,
                        TextStylePropertyName.FONT_FAMILY,
                        FontFamily.with("TimesNewRoman")
                ),
                "/123/SpreadsheetName456/metadata/style/font-family/save/TimesNewRoman"
        );
    }

    @Test
    public void testUrlFragmentFontStyle() {
        this.urlFragmentAndCheck(
                SpreadsheetMetadataPropertyStyleSaveHistoryHashToken.with(
                        ID,
                        NAME,
                        TextStylePropertyName.FONT_STYLE,
                        FontStyle.ITALIC
                ),
                "/123/SpreadsheetName456/metadata/style/font-style/save/ITALIC"
        );
    }

    @Test
    public void testParseColor() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/metadata/style/color/save/#123456",
                SpreadsheetMetadataPropertyStyleSaveHistoryHashToken.with(
                        ID,
                        NAME,
                        TextStylePropertyName.COLOR,
                        Color.parse("#123456")
                )
        );
    }

    @Test
    public void testParseFontFamily() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/metadata/style/font-family/save/TimesNewRoman2",
                SpreadsheetMetadataPropertyStyleSaveHistoryHashToken.with(
                        ID,
                        NAME,
                        TextStylePropertyName.FONT_FAMILY,
                        FontFamily.with("TimesNewRoman2")
                )
        );
    }

    @Override
    SpreadsheetMetadataPropertyStyleSaveHistoryHashToken<Color> createHistoryHashToken(final SpreadsheetId id,
                                                                                       final SpreadsheetName name,
                                                                                       final SpreadsheetMetadataPropertyName<TextStyle> propertyName) {
        return SpreadsheetMetadataPropertyStyleSaveHistoryHashToken.with(
                id,
                name,
                STYLE_PROPERTY_NAME,
                PROPERTY_VALUE
        );
    }

    @Override
    public Class<SpreadsheetMetadataPropertyStyleSaveHistoryHashToken<Color>> type() {
        return Cast.to(SpreadsheetMetadataPropertyStyleSaveHistoryHashToken.class);
    }
}
