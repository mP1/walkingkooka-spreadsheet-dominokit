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

import elemental2.dom.HTMLTableCellElement;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.dom.TdComponent;
import walkingkooka.spreadsheet.dominokit.tooltip.TooltipComponent;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionNumberSign;
import walkingkooka.tree.text.Badge;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * A TD which includes a single {@link SpreadsheetCell}.
 */
final class SpreadsheetViewportComponentTableCellSpreadsheetCell extends SpreadsheetViewportComponentTableCell<HTMLTableCellElement, SpreadsheetViewportComponentTableCellSpreadsheetCell> {

    static SpreadsheetViewportComponentTableCellSpreadsheetCell empty(final SpreadsheetCellReference cellReference,
                                                                      final SpreadsheetViewportComponentTableContext context) {
        return new SpreadsheetViewportComponentTableCellSpreadsheetCell(
            cellReference,
            context
        );
    }

    private SpreadsheetViewportComponentTableCellSpreadsheetCell(final SpreadsheetCellReference cellReference,
                                                                 final SpreadsheetViewportComponentTableContext context) {
        super();

        this.td = HtmlElementComponent.td()
            .setId(
                SpreadsheetViewportComponent.id(cellReference)
            ).setTabIndex(0);
        this.cellReference = cellReference;
        this.tooltipMessage = "";

        this.refresh(context);
    }

    @Override
    void setIdAndName(final SpreadsheetId id,
                      final SpreadsheetName name) {
        // nop
    }

    @Override
    void refresh(final Predicate<SpreadsheetSelection> selected,
                 final SpreadsheetViewportComponentTableContext context) {

        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();
        final SpreadsheetCellReference cellReference = this.cellReference;

        final SpreadsheetCell cell = cache.cell(cellReference)
            .orElse(null);

        final TdComponent td = this.td;
        td.clear();

        TextStyle style = selected.test(cellReference) ?
            context.selectedCellStyle(
                null != cell ?
                    cell.style() :
                    TextStyle.EMPTY
            ) :
            context.cellStyle();

        String tooltipText = null;
        Optional<SpreadsheetError> maybeError = Optional.empty();
        final boolean shouldHideZeroValues = context.shouldHideZeroValues();
        final boolean showFormulas = context.shouldShowFormulas();

        boolean zeroValue = false;
        if (null != cell) {
            final SpreadsheetFormula formula = cell.formula();
            if (false == showFormulas && shouldHideZeroValues) {
                final Object value = formula.errorOrValue()
                    .orElse(null);

                if (ExpressionNumber.is(value) &&
                    ExpressionNumberSign.ZERO == ExpressionNumberKind.DEFAULT.create((Number) value).sign()) {
                    zeroValue = true;
                }
            }

            final String formulaText = formula.text();
            if (showFormulas) {
                if (false == formulaText.trim().isEmpty()) {
                    td.appendChild(
                        TextNode.text(formulaText)
                    );
                    style = context.showFormulasStyle(style);
                }
            } else {
                style = style.merge(
                    cell.style()
                );

                if (false == zeroValue) {
                    TextNode formatted = cell.formattedValue()
                        .orElse(null);
                    if (null != formatted) {
                        if (formatted.isBadge()) {
                            formatted.firstChild()
                                .orElse(null);
                            tooltipText = ((Badge) formatted)
                                .badgeText();
                        }

                        if (null != formatted) {
                            td.appendChild(formatted);
                        }
                    }
                }
            }

            if (null == tooltipText) {
                tooltipText = formula.error()
                    .map(e -> {
                            final String errorMessage = e.message();
                            return errorMessage.isEmpty() ?
                                e.toString() :
                                errorMessage;

                        }
                    ).orElse("");
            }
        }
        if (zeroValue) {
            style = context.hideZeroStyle(style);
        }

        td.setCssText(
            setWidthAndHeight(
                style,
                context
            )
        );

        this.tooltipRefresh(
            CharSequences.nullToEmpty(tooltipText)
                .toString()
        );
    }

    private final SpreadsheetCellReference cellReference;

    private void tooltipRefresh(final String newTooltipMessage) {
        final String oldTooltipMessage = this.tooltipMessage;

        if (false == newTooltipMessage.equals(oldTooltipMessage)) {
            if (null != this.tooltip) {
                this.tooltip.detach();
                this.tooltip = null;
            }

            if (false == newTooltipMessage.isEmpty()) {
                this.tooltip = TooltipComponent.attach(
                    this.td,
                    newTooltipMessage,
                    DropDirection.BOTTOM_MIDDLE
                );
            }

            this.tooltipMessage = newTooltipMessage;
        }
    }

    private String tooltipMessage;

    private TooltipComponent tooltip;

    @Override //
    Length<?> width(final SpreadsheetViewportComponentTableContext context) {
        return context.spreadsheetViewportCache()
            .columnWidth(this.cellReference.toColumn());
    }

    @Override //
    Length<?> height(final SpreadsheetViewportComponentTableContext context) {
        return context.spreadsheetViewportCache()
            .rowHeight(this.cellReference.toRow());
    }

    @Override//
    TextStyle selectedTextStyle(final SpreadsheetViewportComponentTableContext context) {
        throw new UnsupportedOperationException();
    }

    @Override //
    TextStyle unselectedTextStyle(final SpreadsheetViewportComponentTableContext context) {
        throw new UnsupportedOperationException();
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlElementComponent<HTMLTableCellElement, ?> htmlComponent() {
        return this.td;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLTableCellElement element() {
        return this.td.element();
    }

    private final TdComponent td;
}
