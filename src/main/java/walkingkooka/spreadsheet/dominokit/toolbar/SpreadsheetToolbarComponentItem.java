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
import walkingkooka.CanBeEmpty;
import walkingkooka.spreadsheet.dominokit.HistoryTokenAwareComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
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
abstract class SpreadsheetToolbarComponentItem<C extends SpreadsheetToolbarComponentItem<C>> implements HtmlComponent<HTMLElement, C>,
    HistoryTokenAwareComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    CanBeEmpty {

    static SpreadsheetToolbarComponentItem<?> bold(final SpreadsheetToolbarComponentContext context) {
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

    static SpreadsheetToolbarComponentItem<?> clearStyle(final SpreadsheetToolbarComponentContext context) {
        return SpreadsheetToolbarComponentItemAnchorTextStyleClear.with(
            context
        );
    }

    /**
     * {@see SpreadsheetToolbarComponentItemAnchorDateTimeSymbols}
     */
    static SpreadsheetToolbarComponentItem<?> dateTimeSymbols(final SpreadsheetToolbarComponentContext context) {
        return SpreadsheetToolbarComponentItemAnchorDateTimeSymbols.with(
            context
        );
    }

    /**
     * {@see SpreadsheetToolbarComponentItemAnchorDecimalNumberSymbols}
     */
    static SpreadsheetToolbarComponentItem<?> decimalNumberSymbols(final SpreadsheetToolbarComponentContext context) {
        return SpreadsheetToolbarComponentItemAnchorDecimalNumberSymbols.with(
            context
        );
    }

    /**
     * {@see SpreadsheetToolbarComponentItemLinkCellFind}
     */
    static SpreadsheetToolbarComponentItem<?> findCells(final SpreadsheetToolbarComponentContext context) {
        return SpreadsheetToolbarComponentItemAnchorCellFind.with(
            context
        );
    }

    /**
     * {@link SpreadsheetToolbarComponentItemAnchorFormatter}
     */
    static SpreadsheetToolbarComponentItem<?> formatter(final SpreadsheetToolbarComponentContext context) {
        return SpreadsheetToolbarComponentItemAnchorFormatter.with(context);
    }

    /**
     * {@see SpreadsheetToolbarComponentItemAnchorMetadataBooleanHideZeroValues}
     */
    static SpreadsheetToolbarComponentItem<?> hideZeroValues(final SpreadsheetToolbarComponentContext context) {
        return SpreadsheetToolbarComponentItemAnchorMetadataBooleanHideZeroValues.with(
            context
        );
    }

    /**
     * {@see SpreadsheetToolbarComponentItemAnchorMetadataBooleanFindHighlighting}
     */
    static SpreadsheetToolbarComponentItem<?> highlightCells(final SpreadsheetToolbarComponentContext context) {
        return SpreadsheetToolbarComponentItemAnchorMetadataBooleanFindHighlighting.with(
            context
        );
    }

    static SpreadsheetToolbarComponentItem<?> italics(final SpreadsheetToolbarComponentContext context) {
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
    static SpreadsheetToolbarComponentItem<?> labelCreate(final SpreadsheetToolbarComponentContext context) {
        return SpreadsheetToolbarComponentItemAnchorLabelCreate.with(
            context
        );
    }

    /**
     * {@see SpreadsheetToolbarComponentItemLinkLabelList}
     */
    static SpreadsheetToolbarComponentItem<?> labelList(final SpreadsheetToolbarComponentContext context) {
        return SpreadsheetToolbarComponentItemAnchorLabelList.with(
            context
        );
    }

    /**
     * {@link SpreadsheetToolbarComponentItemAnchorParser}
     */
    static SpreadsheetToolbarComponentItem<?> parser(final SpreadsheetToolbarComponentContext context) {
        return SpreadsheetToolbarComponentItemAnchorParser.with(context);
    }

    /**
     * {@link SpreadsheetToolbarComponentItemAnchorPlugin}
     */
    static SpreadsheetToolbarComponentItem<?> plugin(final SpreadsheetToolbarComponentContext context) {
        return SpreadsheetToolbarComponentItemAnchorPlugin.with(context);
    }

    /**
     * {@link SpreadsheetToolbarComponentItemAnchorReload}
     */
    static SpreadsheetToolbarComponentItem<?> reload(final SpreadsheetToolbarComponentContext context) {
        return SpreadsheetToolbarComponentItemAnchorReload.with(context);
    }


    /**
     * {@see SpreadsheetToolbarComponentItemAnchorMetadataBooleanShowFormulas}
     */
    static SpreadsheetToolbarComponentItem<?> showFormulas(final SpreadsheetToolbarComponentContext context) {
        return SpreadsheetToolbarComponentItemAnchorMetadataBooleanShowFormulas.with(context);
    }

    /**
     * {@see SpreadsheetToolbarComponentItemAnchorMetadataBooleanShowGridLines}
     */
    static SpreadsheetToolbarComponentItem<?> showGridLines(final SpreadsheetToolbarComponentContext context) {
        return SpreadsheetToolbarComponentItemAnchorMetadataBooleanShowGridLines.with(context);
    }

    /**
     * {@link SpreadsheetToolbarComponentItemAnchorSort}
     */
    static SpreadsheetToolbarComponentItem<?> sort(final SpreadsheetToolbarComponentContext context) {
        return SpreadsheetToolbarComponentItemAnchorSort.with(context);
    }

    static SpreadsheetToolbarComponentItem<?> strikeThru(final SpreadsheetToolbarComponentContext context) {
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
    static SpreadsheetToolbarComponentItem<?> swagger(final SpreadsheetToolbarComponentContext context) {
        return SpreadsheetToolbarComponentItemAnchorSwagger.with(context);
    }

    static SpreadsheetToolbarComponentItem<?> textAlignLeft(final SpreadsheetToolbarComponentContext context) {
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

    static SpreadsheetToolbarComponentItem<?> textAlignCenter(final SpreadsheetToolbarComponentContext context) {
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

    static SpreadsheetToolbarComponentItem<?> textAlignRight(final SpreadsheetToolbarComponentContext context) {
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

    static SpreadsheetToolbarComponentItem<?> textAlignJustify(final SpreadsheetToolbarComponentContext context) {
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

    static SpreadsheetToolbarComponentItem<?> textCaseCapitalize(final SpreadsheetToolbarComponentContext context) {
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

    static SpreadsheetToolbarComponentItem<?> textCaseLowercase(final SpreadsheetToolbarComponentContext context) {
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

    static SpreadsheetToolbarComponentItem<?> textCaseUppercase(final SpreadsheetToolbarComponentContext context) {
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

    static SpreadsheetToolbarComponentItem<?> underline(final SpreadsheetToolbarComponentContext context) {
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

    /**
     * {@link SpreadsheetToolbarComponentItemAnchorValidator}
     */
    static SpreadsheetToolbarComponentItem<?> validator(final SpreadsheetToolbarComponentContext context) {
        return SpreadsheetToolbarComponentItemAnchorValidator.with(context);
    }

    static SpreadsheetToolbarComponentItem<?> verticalAlignTop(final SpreadsheetToolbarComponentContext context) {
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

    static SpreadsheetToolbarComponentItem<?> verticalAlignMiddle(final SpreadsheetToolbarComponentContext context) {
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

    static SpreadsheetToolbarComponentItem<?> verticalAlignBottom(final SpreadsheetToolbarComponentContext context) {
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

    // IsElement........................................................................................................

    /**
     * The root {@link HTMLElement}
     */
    @Override
    public abstract HTMLElement element();

    // HistoryTokenAwareComponentLifecycle..............................................................................
    @Override
    public final boolean isOpen() {
        return this.open;
    }

    @Override
    public final void open(final RefreshContext context) {
        this.setVisibility(true);
        this.open = true;
    }

    @Override
    public final void close(final RefreshContext context) {
        this.setVisibility(false);
        this.open = false;
    }

    private boolean open;

    @Override
    public final boolean shouldLogLifecycleChanges() {
        return false;
    }
}
