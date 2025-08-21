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

package walkingkooka.spreadsheet.dominokit.viewport;

import org.junit.jupiter.api.Test;
import walkingkooka.color.WebColorName;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetMetadataSpreadsheetViewportContextTest implements SpreadsheetViewportContextTesting<SpreadsheetMetadataSpreadsheetViewportContext> {

    @Test
    public void testCellStyleMissingSpreadsheetMetadataFails() {
        final IllegalStateException thrown = assertThrows(
            IllegalStateException.class,
            () -> this.createContext().cellStyle()
        );

        this.checkEquals(
            "Missing SpreadsheetMetadata and its default cell style",
            thrown.getMessage()
        );
    }

    @Test
    public void testCellWithShowGridLinesFalse() {
        final SpreadsheetMetadataSpreadsheetViewportContext context = this.createContext();

        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.BACKGROUND_COLOR,
            WebColorName.GREEN.color()
        );

        context.onSpreadsheetMetadata(
            SpreadsheetMetadata.EMPTY.set(
                SpreadsheetMetadataPropertyName.STYLE,
                style
            ).set(
                SpreadsheetMetadataPropertyName.SHOW_GRID_LINES,
                false
            ),
            AppContexts.fake()
        );

        final TextStyle cellStyle = context.cellStyle();

        this.checkEquals(
            false,
            cellStyle.toString()
                .contains("border"),
            () -> "CellStyle should not have any border* properties\n" + cellStyle
        );

        this.treePrintAndCheck(
            cellStyle,
            "TextStyle\n" +
                "  background-color=green (walkingkooka.color.OpaqueRgbColor)\n" +
                "  font-size=11 (walkingkooka.tree.text.FontSize)\n" +
                "  font-style=NORMAL (walkingkooka.tree.text.FontStyle)\n" +
                "  font-variant=NORMAL (walkingkooka.tree.text.FontVariant)\n" +
                "  font-weight=normal (walkingkooka.tree.text.FontWeight)\n" +
                "  hyphens=NONE (walkingkooka.tree.text.Hyphens)\n" +
                "  margin-bottom=none (walkingkooka.tree.text.NoneLength)\n" +
                "  margin-left=none (walkingkooka.tree.text.NoneLength)\n" +
                "  margin-right=none (walkingkooka.tree.text.NoneLength)\n" +
                "  margin-top=none (walkingkooka.tree.text.NoneLength)\n" +
                "  padding-bottom=none (walkingkooka.tree.text.NoneLength)\n" +
                "  padding-left=none (walkingkooka.tree.text.NoneLength)\n" +
                "  padding-right=none (walkingkooka.tree.text.NoneLength)\n" +
                "  padding-top=none (walkingkooka.tree.text.NoneLength)\n" +
                "  text-align=LEFT (walkingkooka.tree.text.TextAlign)\n" +
                "  vertical-align=TOP (walkingkooka.tree.text.VerticalAlign)\n" +
                "  word-break=NORMAL (walkingkooka.tree.text.WordBreak)\n"
        );
    }

    @Test
    public void testCellWithShowGridLinesTrue() {
        final SpreadsheetMetadataSpreadsheetViewportContext context = this.createContext();

        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.BACKGROUND_COLOR,
            WebColorName.GREEN.color()
        );

        context.onSpreadsheetMetadata(
            SpreadsheetMetadata.EMPTY.set(
                SpreadsheetMetadataPropertyName.STYLE,
                style
            ).set(
                SpreadsheetMetadataPropertyName.SHOW_GRID_LINES,
                true
            ),
            AppContexts.fake()
        );

        final TextStyle cellStyle = context.cellStyle();

        this.checkEquals(
            true,
            cellStyle.toString()
                .contains("border"),
            () -> "CellStyle should not have any border* properties\n" + cellStyle
        );

        this.treePrintAndCheck(
            cellStyle,
            "TextStyle\n" +
                "  background-color=green (walkingkooka.color.OpaqueRgbColor)\n" +
                "  border-bottom-color=#888 (walkingkooka.color.OpaqueRgbColor)\n" +
                "  border-bottom-style=SOLID (walkingkooka.tree.text.BorderStyle)\n" +
                "  border-bottom-width=1px (walkingkooka.tree.text.PixelLength)\n" +
                "  border-left-color=#888 (walkingkooka.color.OpaqueRgbColor)\n" +
                "  border-left-style=SOLID (walkingkooka.tree.text.BorderStyle)\n" +
                "  border-left-width=1px (walkingkooka.tree.text.PixelLength)\n" +
                "  border-right-color=#888 (walkingkooka.color.OpaqueRgbColor)\n" +
                "  border-right-style=SOLID (walkingkooka.tree.text.BorderStyle)\n" +
                "  border-right-width=1px (walkingkooka.tree.text.PixelLength)\n" +
                "  border-top-color=#888 (walkingkooka.color.OpaqueRgbColor)\n" +
                "  border-top-style=SOLID (walkingkooka.tree.text.BorderStyle)\n" +
                "  border-top-width=1px (walkingkooka.tree.text.PixelLength)\n" +
                "  font-size=11 (walkingkooka.tree.text.FontSize)\n" +
                "  font-style=NORMAL (walkingkooka.tree.text.FontStyle)\n" +
                "  font-variant=NORMAL (walkingkooka.tree.text.FontVariant)\n" +
                "  font-weight=normal (walkingkooka.tree.text.FontWeight)\n" +
                "  hyphens=NONE (walkingkooka.tree.text.Hyphens)\n" +
                "  margin-bottom=none (walkingkooka.tree.text.NoneLength)\n" +
                "  margin-left=none (walkingkooka.tree.text.NoneLength)\n" +
                "  margin-right=none (walkingkooka.tree.text.NoneLength)\n" +
                "  margin-top=none (walkingkooka.tree.text.NoneLength)\n" +
                "  padding-bottom=none (walkingkooka.tree.text.NoneLength)\n" +
                "  padding-left=none (walkingkooka.tree.text.NoneLength)\n" +
                "  padding-right=none (walkingkooka.tree.text.NoneLength)\n" +
                "  padding-top=none (walkingkooka.tree.text.NoneLength)\n" +
                "  text-align=LEFT (walkingkooka.tree.text.TextAlign)\n" +
                "  vertical-align=TOP (walkingkooka.tree.text.VerticalAlign)\n" +
                "  word-break=NORMAL (walkingkooka.tree.text.WordBreak)\n"
        );
    }

    @Test
    public void testCellWithAndWithoutGridLinesDifferent() {
        final SpreadsheetMetadataSpreadsheetViewportContext context = this.createContext();

        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.BACKGROUND_COLOR,
            WebColorName.GREEN.color()
        );

        final SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY.set(
            SpreadsheetMetadataPropertyName.STYLE,
            style
        ).set(
            SpreadsheetMetadataPropertyName.SHOW_GRID_LINES,
            true
        );

        context.onSpreadsheetMetadata(
            metadata,
            AppContexts.fake()
        );

        final TextStyle with = context.cellStyle();

        context.onSpreadsheetMetadata(
            metadata.set(
                SpreadsheetMetadataPropertyName.SHOW_GRID_LINES,
                false
            ),
            AppContexts.fake()
        );

        final TextStyle without = context.cellStyle();

        this.checkNotEquals(
            with,
            without
        );
    }

    @Test
    public void testCellAfterDifferentSpreadsheetMetadataStyle() {
        final SpreadsheetMetadataSpreadsheetViewportContext context = this.createContext();

        context.onSpreadsheetMetadata(
            SpreadsheetMetadata.EMPTY.set(
                SpreadsheetMetadataPropertyName.STYLE,
                TextStyle.EMPTY.set(
                    TextStylePropertyName.BACKGROUND_COLOR,
                    WebColorName.GREEN.color()
                )
            ),
            AppContexts.fake()
        );

        final TextStyle before = context.cellStyle();

        context.onSpreadsheetMetadata(
            SpreadsheetMetadata.EMPTY.set(
                SpreadsheetMetadataPropertyName.STYLE,
                TextStyle.EMPTY.set(
                    TextStylePropertyName.BACKGROUND_COLOR,
                    WebColorName.BLUE.color()
                )
            ),
            AppContexts.fake()
        );

        final TextStyle after = context.cellStyle();

        this.checkNotEquals(
            before,
            after
        );
    }

    @Test
    public void testSelectedCellStyleMissingSpreadsheetMetadataFails() {
        final IllegalStateException thrown = assertThrows(
            IllegalStateException.class,
            () -> this.createContext().selectedCellStyle(TextStyle.EMPTY)
        );

        this.checkEquals(
            "Missing SpreadsheetMetadata and its default cell style",
            thrown.getMessage()
        );
    }

    @Test
    public void testSelectedCellWithoutGridLines() {
        final SpreadsheetMetadataSpreadsheetViewportContext context = this.createContext();

        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.BACKGROUND_COLOR,
            WebColorName.GREEN.color()
        );

        context.onSpreadsheetMetadata(
            SpreadsheetMetadata.EMPTY.set(
                SpreadsheetMetadataPropertyName.STYLE,
                style
            ).set(
                SpreadsheetMetadataPropertyName.SHOW_GRID_LINES,
                false
            ),
            AppContexts.fake()
        );


        this.treePrintAndCheck(
            context.selectedCellStyle(
                TextStyle.EMPTY.set(
                    TextStylePropertyName.COLOR,
                    WebColorName.PURPLE.color()
                )
            ),
            "TextStyle\n" +
                "  background-color=#2f8f2f (walkingkooka.color.OpaqueRgbColor)\n" +
                "  color=purple (walkingkooka.color.OpaqueRgbColor)\n" +
                "  font-size=11 (walkingkooka.tree.text.FontSize)\n" +
                "  font-style=NORMAL (walkingkooka.tree.text.FontStyle)\n" +
                "  font-variant=NORMAL (walkingkooka.tree.text.FontVariant)\n" +
                "  font-weight=normal (walkingkooka.tree.text.FontWeight)\n" +
                "  hyphens=NONE (walkingkooka.tree.text.Hyphens)\n" +
                "  margin-bottom=none (walkingkooka.tree.text.NoneLength)\n" +
                "  margin-left=none (walkingkooka.tree.text.NoneLength)\n" +
                "  margin-right=none (walkingkooka.tree.text.NoneLength)\n" +
                "  margin-top=none (walkingkooka.tree.text.NoneLength)\n" +
                "  padding-bottom=none (walkingkooka.tree.text.NoneLength)\n" +
                "  padding-left=none (walkingkooka.tree.text.NoneLength)\n" +
                "  padding-right=none (walkingkooka.tree.text.NoneLength)\n" +
                "  padding-top=none (walkingkooka.tree.text.NoneLength)\n" +
                "  text-align=LEFT (walkingkooka.tree.text.TextAlign)\n" +
                "  vertical-align=TOP (walkingkooka.tree.text.VerticalAlign)\n" +
                "  word-break=NORMAL (walkingkooka.tree.text.WordBreak)\n"
        );
    }

    @Test
    public void testSelectedCellWithGridLines() {
        final SpreadsheetMetadataSpreadsheetViewportContext context = this.createContext();

        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.BACKGROUND_COLOR,
            WebColorName.GREEN.color()
        );

        context.onSpreadsheetMetadata(
            SpreadsheetMetadata.EMPTY.set(
                SpreadsheetMetadataPropertyName.STYLE,
                style
            ).set(
                SpreadsheetMetadataPropertyName.SHOW_GRID_LINES,
                true
            ),
            AppContexts.fake()
        );


        this.treePrintAndCheck(
            context.selectedCellStyle(
                TextStyle.EMPTY.set(
                    TextStylePropertyName.COLOR,
                    WebColorName.PURPLE.color()
                )
            ),
            "TextStyle\n" +
                "  background-color=#2f8f2f (walkingkooka.color.OpaqueRgbColor)\n" +
                "  border-bottom-color=#888 (walkingkooka.color.OpaqueRgbColor)\n" +
                "  border-bottom-style=SOLID (walkingkooka.tree.text.BorderStyle)\n" +
                "  border-bottom-width=1px (walkingkooka.tree.text.PixelLength)\n" +
                "  border-left-color=#888 (walkingkooka.color.OpaqueRgbColor)\n" +
                "  border-left-style=SOLID (walkingkooka.tree.text.BorderStyle)\n" +
                "  border-left-width=1px (walkingkooka.tree.text.PixelLength)\n" +
                "  border-right-color=#888 (walkingkooka.color.OpaqueRgbColor)\n" +
                "  border-right-style=SOLID (walkingkooka.tree.text.BorderStyle)\n" +
                "  border-right-width=1px (walkingkooka.tree.text.PixelLength)\n" +
                "  border-top-color=#888 (walkingkooka.color.OpaqueRgbColor)\n" +
                "  border-top-style=SOLID (walkingkooka.tree.text.BorderStyle)\n" +
                "  border-top-width=1px (walkingkooka.tree.text.PixelLength)\n" +
                "  color=purple (walkingkooka.color.OpaqueRgbColor)\n" +
                "  font-size=11 (walkingkooka.tree.text.FontSize)\n" +
                "  font-style=NORMAL (walkingkooka.tree.text.FontStyle)\n" +
                "  font-variant=NORMAL (walkingkooka.tree.text.FontVariant)\n" +
                "  font-weight=normal (walkingkooka.tree.text.FontWeight)\n" +
                "  hyphens=NONE (walkingkooka.tree.text.Hyphens)\n" +
                "  margin-bottom=none (walkingkooka.tree.text.NoneLength)\n" +
                "  margin-left=none (walkingkooka.tree.text.NoneLength)\n" +
                "  margin-right=none (walkingkooka.tree.text.NoneLength)\n" +
                "  margin-top=none (walkingkooka.tree.text.NoneLength)\n" +
                "  padding-bottom=none (walkingkooka.tree.text.NoneLength)\n" +
                "  padding-left=none (walkingkooka.tree.text.NoneLength)\n" +
                "  padding-right=none (walkingkooka.tree.text.NoneLength)\n" +
                "  padding-top=none (walkingkooka.tree.text.NoneLength)\n" +
                "  text-align=LEFT (walkingkooka.tree.text.TextAlign)\n" +
                "  vertical-align=TOP (walkingkooka.tree.text.VerticalAlign)\n" +
                "  word-break=NORMAL (walkingkooka.tree.text.WordBreak)\n"
        );
    }

    @Test
    public void testSelectedCellWithAndWithoutGridLinesDifferent() {
        final SpreadsheetMetadataSpreadsheetViewportContext context = this.createContext();

        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.BACKGROUND_COLOR,
            WebColorName.GREEN.color()
        );

        final SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY.set(
            SpreadsheetMetadataPropertyName.STYLE,
            style
        ).set(
            SpreadsheetMetadataPropertyName.SHOW_GRID_LINES,
            true
        );

        context.onSpreadsheetMetadata(
            metadata,
            AppContexts.fake()
        );

        final TextStyle cellStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.COLOR,
            WebColorName.PURPLE.color()
        );

        final TextStyle with = context.selectedCellStyle(cellStyle);

        context.onSpreadsheetMetadata(
            metadata.set(
                SpreadsheetMetadataPropertyName.SHOW_GRID_LINES,
                false
            ),
            AppContexts.fake()
        );

        final TextStyle without = context.selectedCellStyle(cellStyle);

        this.checkNotEquals(
            with,
            without
        );
    }

    @Override
    public SpreadsheetMetadataSpreadsheetViewportContext createContext() {
        return SpreadsheetMetadataSpreadsheetViewportContext.with(
            SpreadsheetMetadataFetcherWatchers.empty()
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetMetadataSpreadsheetViewportContext> type() {
        return SpreadsheetMetadataSpreadsheetViewportContext.class;
    }
}
