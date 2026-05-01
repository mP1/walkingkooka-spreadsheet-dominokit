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
            )
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
                "  background-color=green\n" +
                "  font-size=11\n" +
                "  font-style=NORMAL\n" +
                "  font-variant=NORMAL\n" +
                "  font-weight=NORMAL\n" +
                "  hyphens=NONE\n" +
                "  margin-bottom=none\n" +
                "  margin-left=none\n" +
                "  margin-right=none\n" +
                "  margin-top=none\n" +
                "  padding-bottom=none\n" +
                "  padding-left=none\n" +
                "  padding-right=none\n" +
                "  padding-top=none\n" +
                "  text-align=LEFT\n" +
                "  vertical-align=TOP\n" +
                "  word-break=NORMAL\n"
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
            )
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
                "  background-color=green\n" +
                "  border-bottom-color=#888\n" +
                "  border-bottom-style=SOLID\n" +
                "  border-bottom-width=1px\n" +
                "  border-left-color=#888\n" +
                "  border-left-style=SOLID\n" +
                "  border-left-width=1px\n" +
                "  border-right-color=#888\n" +
                "  border-right-style=SOLID\n" +
                "  border-right-width=1px\n" +
                "  border-top-color=#888\n" +
                "  border-top-style=SOLID\n" +
                "  border-top-width=1px\n" +
                "  font-size=11\n" +
                "  font-style=NORMAL\n" +
                "  font-variant=NORMAL\n" +
                "  font-weight=NORMAL\n" +
                "  hyphens=NONE\n" +
                "  margin-bottom=none\n" +
                "  margin-left=none\n" +
                "  margin-right=none\n" +
                "  margin-top=none\n" +
                "  padding-bottom=none\n" +
                "  padding-left=none\n" +
                "  padding-right=none\n" +
                "  padding-top=none\n" +
                "  text-align=LEFT\n" +
                "  vertical-align=TOP\n" +
                "  word-break=NORMAL\n"
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
            metadata
        );

        final TextStyle with = context.cellStyle();

        context.onSpreadsheetMetadata(
            metadata.set(
                SpreadsheetMetadataPropertyName.SHOW_GRID_LINES,
                false
            )
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
            )
        );

        final TextStyle before = context.cellStyle();

        context.onSpreadsheetMetadata(
            SpreadsheetMetadata.EMPTY.set(
                SpreadsheetMetadataPropertyName.STYLE,
                TextStyle.EMPTY.set(
                    TextStylePropertyName.BACKGROUND_COLOR,
                    WebColorName.BLUE.color()
                )
            )
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
            )
        );


        this.treePrintAndCheck(
            context.selectedCellStyle(
                TextStyle.EMPTY.set(
                    TextStylePropertyName.COLOR,
                    WebColorName.PURPLE.color()
                )
            ),
            "TextStyle\n" +
                "  background-color=#2f8f2f\n" +
                "  color=purple\n" +
                "  font-size=11\n" +
                "  font-style=NORMAL\n" +
                "  font-variant=NORMAL\n" +
                "  font-weight=NORMAL\n" +
                "  hyphens=NONE\n" +
                "  margin-bottom=none\n" +
                "  margin-left=none\n" +
                "  margin-right=none\n" +
                "  margin-top=none\n" +
                "  padding-bottom=none\n" +
                "  padding-left=none\n" +
                "  padding-right=none\n" +
                "  padding-top=none\n" +
                "  text-align=LEFT\n" +
                "  vertical-align=TOP\n" +
                "  word-break=NORMAL\n"
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
            )
        );


        this.treePrintAndCheck(
            context.selectedCellStyle(
                TextStyle.EMPTY.set(
                    TextStylePropertyName.COLOR,
                    WebColorName.PURPLE.color()
                )
            ),
            "TextStyle\n" +
                "  background-color=#2f8f2f\n" +
                "  border-bottom-color=#888\n" +
                "  border-bottom-style=SOLID\n" +
                "  border-bottom-width=1px\n" +
                "  border-left-color=#888\n" +
                "  border-left-style=SOLID\n" +
                "  border-left-width=1px\n" +
                "  border-right-color=#888\n" +
                "  border-right-style=SOLID\n" +
                "  border-right-width=1px\n" +
                "  border-top-color=#888\n" +
                "  border-top-style=SOLID\n" +
                "  border-top-width=1px\n" +
                "  color=purple\n" +
                "  font-size=11\n" +
                "  font-style=NORMAL\n" +
                "  font-variant=NORMAL\n" +
                "  font-weight=NORMAL\n" +
                "  hyphens=NONE\n" +
                "  margin-bottom=none\n" +
                "  margin-left=none\n" +
                "  margin-right=none\n" +
                "  margin-top=none\n" +
                "  padding-bottom=none\n" +
                "  padding-left=none\n" +
                "  padding-right=none\n" +
                "  padding-top=none\n" +
                "  text-align=LEFT\n" +
                "  vertical-align=TOP\n" +
                "  word-break=NORMAL\n"
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
            metadata
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
            )
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
