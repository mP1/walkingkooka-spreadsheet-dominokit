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
 * A ui such as an icon within a {@link ToolbarComponent}.
 */
abstract class ToolbarComponentItem<C extends ToolbarComponentItem<C>> implements HtmlComponent<HTMLElement, C>,
    HistoryTokenAwareComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    CanBeEmpty {

    /**
     * {@see ToolbarComponentItemAnchorMetadataBooleanAutoHideScrollbars}
     */
    static ToolbarComponentItem<?> autoHideScrollbars(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorMetadataBooleanAutoHideScrollbars.with(context);
    }

    static ToolbarComponentItem<?> bold(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorTextStyleProperty.with(
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

    static ToolbarComponentItem<?> clearStyle(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorTextStyleClear.with(
            context
        );
    }

    /**
     * {@see ToolbarComponentItemAnchorDateTimeSymbols}
     */
    static ToolbarComponentItem<?> dateTimeSymbols(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorDateTimeSymbols.with(
            context
        );
    }

    /**
     * {@see ToolbarComponentItemAnchorDecimalNumberSymbols}
     */
    static ToolbarComponentItem<?> decimalNumberSymbols(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorDecimalNumberSymbols.with(
            context
        );
    }

    /**
     * {@see SpreadsheetToolbarComponentItemLinkCellFind}
     */
    static ToolbarComponentItem<?> findCells(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorCellFind.with(
            context
        );
    }

    /**
     * {@link ToolbarComponentItemAnchorFormatter}
     */
    static ToolbarComponentItem<?> formatter(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorFormatter.with(context);
    }

    /**
     * {@see ToolbarComponentItemAnchorMetadataBooleanHideZeroValues}
     */
    static ToolbarComponentItem<?> hideZeroValues(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorMetadataBooleanHideZeroValues.with(
            context
        );
    }

    /**
     * {@see ToolbarComponentItemAnchorMetadataBooleanFindHighlighting}
     */
    static ToolbarComponentItem<?> highlightCells(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorMetadataBooleanFindHighlighting.with(
            context
        );
    }

    static ToolbarComponentItem<?> italics(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorTextStyleProperty.with(
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
    static ToolbarComponentItem<?> labelCreate(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorLabelCreate.with(
            context
        );
    }

    /**
     * {@see SpreadsheetToolbarComponentItemLinkLabelList}
     */
    static ToolbarComponentItem<?> labelList(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorLabelList.with(
            context
        );
    }

    /**
     * {@link ToolbarComponentItemAnchorLocale}
     */
    static ToolbarComponentItem<?> locale(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorLocale.with(context);
    }
    
    /**
     * {@link ToolbarComponentItemAnchorParser}
     */
    static ToolbarComponentItem<?> parser(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorParser.with(context);
    }

    /**
     * {@link ToolbarComponentItemAnchorPlugin}
     */
    static ToolbarComponentItem<?> plugin(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorPlugin.with(context);
    }

    /**
     * {@link ToolbarComponentItemAnchorReload}
     */
    static ToolbarComponentItem<?> reload(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorReload.with(context);
    }

    /**
     * {@see ToolbarComponentItemAnchorMetadataBooleanShowFormulas}
     */
    static ToolbarComponentItem<?> showFormulaEditor(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorMetadataBooleanShowFormulaEditor.with(context);
    }

    /**
     * {@see ToolbarComponentItemAnchorMetadataBooleanShowFormulas}
     */
    static ToolbarComponentItem<?> showFormulas(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorMetadataBooleanShowFormulas.with(context);
    }

    /**
     * {@see ToolbarComponentItemAnchorMetadataBooleanShowGridLines}
     */
    static ToolbarComponentItem<?> showGridLines(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorMetadataBooleanShowGridLines.with(context);
    }

    /**
     * {@see ToolbarComponentItemAnchorMetadataBooleanShowHeadings}
     */
    static ToolbarComponentItem<?> showHeadings(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorMetadataBooleanShowHeadings.with(context);
    }

    /**
     * {@link ToolbarComponentItemAnchorSort}
     */
    static ToolbarComponentItem<?> sort(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorSort.with(context);
    }

    static ToolbarComponentItem<?> strikeThru(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorTextStyleProperty.with(
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
     * {@link ToolbarComponentItemAnchorSwagger}
     */
    static ToolbarComponentItem<?> swagger(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorSwagger.with(context);
    }

    static ToolbarComponentItem<?> textAlignLeft(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorTextStyleProperty.with(
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

    static ToolbarComponentItem<?> textAlignCenter(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorTextStyleProperty.with(
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

    static ToolbarComponentItem<?> textAlignRight(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorTextStyleProperty.with(
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

    static ToolbarComponentItem<?> textAlignJustify(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorTextStyleProperty.with(
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

    static ToolbarComponentItem<?> textCaseCapitalize(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorTextStyleProperty.with(
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

    static ToolbarComponentItem<?> textCaseLowercase(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorTextStyleProperty.with(
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

    static ToolbarComponentItem<?> textCaseUppercase(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorTextStyleProperty.with(
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

    static ToolbarComponentItem<?> underline(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorTextStyleProperty.with(
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
     * {@link ToolbarComponentItemAnchorValidator}
     */
    static ToolbarComponentItem<?> validator(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorValidator.with(context);
    }

    static ToolbarComponentItem<?> verticalAlignTop(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorTextStyleProperty.with(
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

    static ToolbarComponentItem<?> verticalAlignMiddle(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorTextStyleProperty.with(
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

    static ToolbarComponentItem<?> verticalAlignBottom(final ToolbarComponentContext context) {
        return ToolbarComponentItemAnchorTextStyleProperty.with(
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

    ToolbarComponentItem() {
        this.open = false;
    }

    // CanBeEmpty.......................................................................................................

    @Override
    public final boolean isEmpty() {
        return false == this.isOpen();
    }

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
        return SPREADSHEET_TOOLBAR_COMPONENT_ITEM;
    }
}
