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

import walkingkooka.color.Color;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetDominoKitColor;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.text.BorderStyle;
import walkingkooka.tree.text.FontSize;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontVariant;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.Hyphens;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.VerticalAlign;
import walkingkooka.tree.text.WordBreak;

import java.util.Objects;
import java.util.Set;

/**
 * A {@link SpreadsheetViewportContext} that contains many default {@link TextStyle} and also honours the {@link SpreadsheetMetadataPropertyName#SHOW_GRID_LINES}.
 */
final class SpreadsheetMetadataSpreadsheetViewportContext implements SpreadsheetViewportContext,
    SpreadsheetMetadataFetcherWatcher,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher {

    final static Color BORDER_COLOR = SpreadsheetDominoKitColor.VIEWPORT_LINES_COLOR;
    final static BorderStyle BORDER_STYLE = BorderStyle.SOLID;
    final static Length<?> BORDER_LENGTH = Length.pixel(1.0);

    private final static TextStyle DEFAULT_STYLE = TextStyle.EMPTY
        .set(
            TextStylePropertyName.MARGIN,
            Length.none()
        ).set(
            TextStylePropertyName.PADDING,
            Length.none()
        ).set(
            TextStylePropertyName.FONT_SIZE,
            FontSize.with(11)
        ).set(
            TextStylePropertyName.FONT_STYLE,
            FontStyle.NORMAL
        ).set(
            TextStylePropertyName.FONT_WEIGHT,
            FontWeight.NORMAL
        ).set(
            TextStylePropertyName.FONT_VARIANT,
            FontVariant.NORMAL
        ).set(
            TextStylePropertyName.HYPHENS,
            Hyphens.NONE
        ).set(
            TextStylePropertyName.WORD_BREAK,
            WordBreak.NORMAL
        );

    private final static TextStyle STYLE_WITH_BORDERS = DEFAULT_STYLE.setBorder(
        BORDER_COLOR,
        BORDER_STYLE,
        BORDER_LENGTH
    );

    private final static TextStyle CELL_STYLE = setCellTextAlignVerticalAlign(DEFAULT_STYLE);

    private final static TextStyle CELL_STYLE_WITH_BORDERS = setCellTextAlignVerticalAlign(STYLE_WITH_BORDERS);

    private static TextStyle setCellTextAlignVerticalAlign(final TextStyle style) {
        return setTextAlignVerticalAlign(
            style,
            TextAlign.LEFT,
            VerticalAlign.TOP
        );
    }

    // headers always have borders (grid lines)
    final static TextStyle HEADER_STYLE = setTextAlignVerticalAlign(
        STYLE_WITH_BORDERS,
        TextAlign.CENTER,
        VerticalAlign.MIDDLE
    );

    private static TextStyle setTextAlignVerticalAlign(final TextStyle style,
                                                       final TextAlign textAlign,
                                                       final VerticalAlign verticalAlign) {
        return style.set(
            TextStylePropertyName.TEXT_ALIGN,
            textAlign
        ).set(
            TextStylePropertyName.VERTICAL_ALIGN,
            verticalAlign
        );
    }

    static SpreadsheetMetadataSpreadsheetViewportContext with(final SpreadsheetMetadataFetcherWatchers watchers) {
        return new SpreadsheetMetadataSpreadsheetViewportContext(watchers);
    }

    private SpreadsheetMetadataSpreadsheetViewportContext(final SpreadsheetMetadataFetcherWatchers watchers) {
        watchers.addSpreadsheetMetadataFetcherWatcher(this);
    }

    @Override
    public TextStyle allCellsStyle() {
        return HEADER_STYLE;
    }

    @Override
    public TextStyle selectedAllCellsStyle() {
        return HEADER_STYLE;
    }

    @Override
    public TextStyle cellStyle() {
        final TextStyle cellStyle = this.cellStyle;
        if (null == cellStyle) {
            throw new IllegalStateException("Missing " + SpreadsheetMetadata.class.getSimpleName() + " and its default cell style");
        }
        return cellStyle;
    }

    /**
     * Contains the effective default cell style by merging the current {@link SpreadsheetMetadata} and enabling/disabling grid lines.
     */
    private TextStyle cellStyle;

    @Override
    public TextStyle selectedCellStyle(final TextStyle cellStyle) {
        Objects.requireNonNull(cellStyle, "cellStyle");

        return mixBackgroundColor(
            this.cellStyle()
                .merge(
                    cellStyle
                ),
            SpreadsheetDominoKitColor.VIEWPORT_CELL_SELECTED_BACKGROUND_COLOR
        );
    }

    @Override
    public TextStyle columnStyle() {
        return HEADER_STYLE;
    }

    @Override
    public TextStyle selectedColumnStyle() {
        return SELECTED_HEADER_STYLE;
    }

    @Override
    public TextStyle rowStyle() {
        return HEADER_STYLE;
    }

    @Override
    public TextStyle selectedRowStyle() {
        return SELECTED_HEADER_STYLE;
    }

    private final static TextStyle SELECTED_HEADER_STYLE = setBackgroundColor(
        HEADER_STYLE,
        SpreadsheetDominoKitColor.VIEWPORT_HEADER_SELECTED_BACKGROUND_COLOR
    );

    @Override
    public TextStyle hideZeroStyle(final TextStyle style) {
        Objects.requireNonNull(style, "style");

        return setBackgroundColor(
            style,
            SpreadsheetDominoKitColor.HIGHLIGHT_COLOR
        );
    }

    private static TextStyle mixBackgroundColor(final TextStyle style,
                                                final Color color) {
        return setBackgroundColor(
            style,
            style.getOrFail(TextStylePropertyName.BACKGROUND_COLOR)
                .mix(
                    color,
                    0.25f
                )
        );
    }

    private static TextStyle setBackgroundColor(final TextStyle style,
                                                final Color color) {
        return style.set(
            TextStylePropertyName.BACKGROUND_COLOR,
            color
        );
    }

    // SpreadsheetMetadataFetcherWatcher................................................................................

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        final boolean showGridLines = metadata.get(SpreadsheetMetadataPropertyName.SHOW_GRID_LINES)
            .orElse(true);

        this.cellStyle = metadata.effectiveStyle()
            .merge(
                showGridLines ?
                    CELL_STYLE_WITH_BORDERS :
                    CELL_STYLE
            );
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas,
                                         final AppContext context) {
        // ignore
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
