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

package walkingkooka.spreadsheet.dominokit.toolbar;

import elemental2.dom.HTMLElement;
import elemental2.dom.Node;
import walkingkooka.CanBeEmpty;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.HistoryTokenAwareComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.VisibleHtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextDecorationLine;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.TextTransform;
import walkingkooka.tree.text.VerticalAlign;

import java.util.Optional;

/**
 * A ui such as an icon within a {@link SpreadsheetToolbarComponent}.
 */
abstract class SpreadsheetToolbarComponentItem<C extends SpreadsheetToolbarComponentItem<C>> implements HtmlElementComponent<HTMLElement, C>,
        HistoryTokenAwareComponentLifecycle,
        LoadedSpreadsheetMetadataRequired,
        VisibleHtmlElementComponent<HTMLElement, C>,
        CanBeEmpty {

    static SpreadsheetToolbarComponentItem<?> bold(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorTextStyleProperty.with(
                TextStylePropertyName.FONT_WEIGHT,
                FontWeight.BOLD,
                Optional.of(
                        SpreadsheetIcons.bold()
                ),
                "Bold",
                "Bold",
                context
        );
    }

    static SpreadsheetToolbarComponentItem<?> clearStyle(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorTextStyleClear.with(
                context
        );
    }

    /**
     * {@see SpreadsheetToolbarComponentItemLinkCellFind}
     */
    static SpreadsheetToolbarComponentItem<?> findCells(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorCellFind.with(
                context
        );
    }

    /**
     * {@link SpreadsheetToolbarComponentItemAnchorFormatter}
     */
    static SpreadsheetToolbarComponentItem<?> formatPattern(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorFormatter.with(context);
    }

    /**
     * {@see SpreadsheetToolbarComponentItemLinkMetadataHideZeroValues}
     */
    static SpreadsheetToolbarComponentItem<?> hideZeroValues(final AppContext context) {
        return SpreadsheetToolbarComponentItemAnchorMetadataHideZeroValues.with(
                context
        );
    }

    /**
     * {@see SpreadsheetToolbarComponentItemAnchorMetadataFindHighlighting}
     */
    static SpreadsheetToolbarComponentItem<?> highlightCells(final AppContext context) {
        return SpreadsheetToolbarComponentItemAnchorMetadataFindHighlighting.with(
                context
        );
    }

    static SpreadsheetToolbarComponentItem<?> italics(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorTextStyleProperty.with(
                TextStylePropertyName.FONT_STYLE,
                FontStyle.ITALIC,
                Optional.of(
                        SpreadsheetIcons.italics()
                ),
                "Italics",
                "Italics",
                context
        );
    }

    /**
     * {@see SpreadsheetToolbarComponentItemLinkLabelCreate}
     */
    static SpreadsheetToolbarComponentItem<?> labelCreate(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorLabelCreate.with(
                context
        );
    }

    /**
     * {@link SpreadsheetToolbarComponentItemAnchorParser}
     */
    static SpreadsheetToolbarComponentItem<?> parsePattern(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorParser.with(context);
    }

    /**
     * {@link SpreadsheetToolbarComponentItemAnchorReload}
     */
    static SpreadsheetToolbarComponentItem<?> reload(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorReload.with(context);
    }


    /**
     * {@link SpreadsheetToolbarComponentItemAnchorSort}
     */
    static SpreadsheetToolbarComponentItem<?> sort(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorSort.with(context);
    }

    static SpreadsheetToolbarComponentItem<?> strikeThru(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorTextStyleProperty.with(
                TextStylePropertyName.TEXT_DECORATION_LINE,
                TextDecorationLine.LINE_THROUGH,
                Optional.of(
                        SpreadsheetIcons.strikethrough()
                ),
                "Strike",
                "Strike-thru",
                context
        );
    }

    /**
     * {@link SpreadsheetToolbarComponentItemAnchorSwagger}
     */
    static SpreadsheetToolbarComponentItem<?> swagger(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorSwagger.with(context);
    }

    static SpreadsheetToolbarComponentItem<?> textAlignLeft(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorTextStyleProperty.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT,
                Optional.of(
                        SpreadsheetIcons.alignLeft()
                ),
                "Left",
                "Left align",
                context
        );
    }

    static SpreadsheetToolbarComponentItem<?> textAlignCenter(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorTextStyleProperty.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.CENTER,
                Optional.of(
                        SpreadsheetIcons.alignCenter()
                ),
                "Center",
                "Center align",
                context
        );
    }

    static SpreadsheetToolbarComponentItem<?> textAlignRight(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorTextStyleProperty.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.RIGHT,
                Optional.of(
                        SpreadsheetIcons.alignRight()
                ),
                "Right",
                "Right align",
                context
        );
    }

    static SpreadsheetToolbarComponentItem<?> textAlignJustify(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorTextStyleProperty.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.JUSTIFY,
                Optional.of(
                        SpreadsheetIcons.alignJustify()
                ),
                "Justify",
                "Justify",
                context
        );
    }

    static SpreadsheetToolbarComponentItem<?> textCaseCapitalize(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorTextStyleProperty.with(
                TextStylePropertyName.TEXT_TRANSFORM,
                TextTransform.CAPITALIZE,
                Optional.of(
                        SpreadsheetIcons.textCaseCapitalize()
                ),
                "Capitalize",
                "Capitalize text",
                context
        );
    }

    static SpreadsheetToolbarComponentItem<?> textCaseLowercase(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorTextStyleProperty.with(
                TextStylePropertyName.TEXT_TRANSFORM,
                TextTransform.LOWERCASE,
                Optional.of(
                        SpreadsheetIcons.textCaseLower()
                ),
                "Lower-case",
                "Lower-case text",
                context
        );
    }

    static SpreadsheetToolbarComponentItem<?> textCaseUppercase(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorTextStyleProperty.with(
                TextStylePropertyName.TEXT_TRANSFORM,
                TextTransform.UPPERCASE,
                Optional.of(
                        SpreadsheetIcons.textCaseLower()
                ),
                "Upper-case",
                "Upper-case text",
                context
        );
    }

    static SpreadsheetToolbarComponentItem<?> underline(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorTextStyleProperty.with(
                TextStylePropertyName.TEXT_DECORATION_LINE,
                TextDecorationLine.UNDERLINE,
                Optional.of(
                        SpreadsheetIcons.underline()
                ),
                "Underline",
                "Underline",
                context
        );
    }

    static SpreadsheetToolbarComponentItem<?> verticalAlignTop(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorTextStyleProperty.with(
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.TOP,
                Optional.of(
                        SpreadsheetIcons.verticalAlignTop()
                ),
                "Top",
                "Align top",
                context
        );
    }

    static SpreadsheetToolbarComponentItem<?> verticalAlignMiddle(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorTextStyleProperty.with(
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.MIDDLE,
                Optional.of(
                        SpreadsheetIcons.verticalAlignMiddle()
                ),
                "Middle",
                "Align middle",
                context
        );
    }

    static SpreadsheetToolbarComponentItem<?> verticalAlignBottom(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemAnchorTextStyleProperty.with(
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.BOTTOM,
                Optional.of(
                        SpreadsheetIcons.verticalAlignBottom()
                ),
                "Bottom",
                "Align bottom",
                context
        );
    }

    // ctor.............................................................................................................

    SpreadsheetToolbarComponentItem() {
        this.open = false;
    }

    // CanBeEmpty.......................................................................................................

    @Override
    public final boolean isEmpty() {
        return false == this.isOpen();
    }

    // node.............................................................................................................

    @Override
    public final Node node() {
        return this.element();
    }

    /**
     * The root {@link HTMLElement}
     */
    public abstract HTMLElement element();

    // HistoryTokenAwareComponentLifecycle..............................................................................
    @Override
    public final boolean isOpen() {
        return this.open;
    }

    @Override
    public final void open(final AppContext context) {
        this.setVisibility(true);
        this.open = true;
    }

    @Override
    public final void close(final AppContext context) {
        this.setVisibility(false);
        this.open = false;
    }

    private boolean open;

    @Override
    public final boolean shouldLogLifecycleChanges() {
        return false;
    }
}
