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

package walkingkooka.spreadsheet.dominokit.ui.viewport;

import elemental2.dom.HTMLTableCellElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.elements.TDElement;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.popover.Tooltip;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetDominoKitColor;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionNumberSign;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * A TD which includes a single {@link SpreadsheetCell}.
 */
final class SpreadsheetViewportComponentTableCellSpreadsheetCell extends SpreadsheetViewportComponentTableCell
        implements IsElement<HTMLTableCellElement> {

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

        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();

        this.element = ElementsFactory.elements.td()
                .id(
                        SpreadsheetViewportComponent.id(cellReference)
                ).setTabIndex(0)
                .style(
                        css(
                                SpreadsheetViewportComponentTableCell.CELL_STYLE,
                                cache.columnWidth(cellReference.column()),
                                cache.rowHeight(cellReference.row())
                        )
                );
        this.cellReference = cellReference;
        this.tooltipMessage = "";
    }

    void setIdAndName(final SpreadsheetId id,
                      final SpreadsheetName name) {
        // nop
    }

    @Override
    void refresh(final Predicate<SpreadsheetSelection> selected,
                 final SpreadsheetViewportComponentTableContext context) {

        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();
        final SpreadsheetCellReference cellReference = this.cellReference;

        final Optional<SpreadsheetCell> maybeCell = cache.cell(cellReference);

        final boolean isSelected = selected.test(cellReference);
        Color mixBackgroundColor = isSelected ?
                SpreadsheetDominoKitColor.VIEWPORT_CELL_SELECTED_BACKGROUND_COLOR :
                null;

        boolean hideZeroValues = false;

        if (maybeCell.isPresent()) {
            final SpreadsheetCell cell = maybeCell.get();

            if (context.hideZeroValues()) {
                final Object value = cell.formula()
                        .value()
                        .orElse(null);

                if (ExpressionNumber.is(value) &&
                        ExpressionNumberSign.ZERO == ExpressionNumberKind.DEFAULT.create((Number) value).sign()) {
                    mixBackgroundColor = mixBackgroundColor.mix(
                            SpreadsheetDominoKitColor.HIGHLIGHT_COLOR,
                            0.5f
                    );

                    hideZeroValues = true;
                }
            }
        }

        if (context.mustRefresh() ||
                false == maybeCell.equals(this.cell) ||
                this.selected != isSelected ||
                this.hideZeroValues != hideZeroValues
        ) {
            this.cell = maybeCell;
            this.selected = isSelected;
            this.hideZeroValues = hideZeroValues;

            final TDElement td = this.element;
            td.clearElement();

            TextStyle style = context.defaultCellStyle();
            Optional<SpreadsheetError> maybeError = Optional.empty();

            if (maybeCell.isPresent()) {
                final SpreadsheetCell cell = maybeCell.get();

                if (false == hideZeroValues) {
                    final Optional<TextNode> maybeFormatted = cell.formattedValue();
                    if (maybeFormatted.isPresent()) {
                        td.appendChild(
                                Doms.node(maybeFormatted.get())
                        );
                    }
                }
                style = cell.style()
                        .merge(style);
                maybeError = cell.formula()
                        .error();
            }

            if (null != mixBackgroundColor) {
                Color color = style.getOrFail(TextStylePropertyName.BACKGROUND_COLOR);

                style = style.set(
                        TextStylePropertyName.BACKGROUND_COLOR,
                        color.mix(
                                mixBackgroundColor,
                                0.25f
                        )
                );
            }

            // copy width/height to MIN to prevent table squashing cells to fit.
            final Length<?> width = cache.columnWidth(cellReference.column());
            final Length<?> height = cache.rowHeight(cellReference.row());

            style = style.setValues(
                    Maps.of(
                            TextStylePropertyName.WIDTH,
                            width,
                            TextStylePropertyName.HEIGHT,
                            height,
                            TextStylePropertyName.MIN_WIDTH,
                            width,
                            TextStylePropertyName.MIN_HEIGHT,
                            height
                    )
            );

            td.style(
                    style.css() + "box-sizing: border-box;"
            );


            this.tooltipRefresh(maybeError);
        }
    }

    private final SpreadsheetCellReference cellReference;

    private Optional<SpreadsheetCell> cell;

    private boolean selected;

    private boolean hideZeroValues;

    private void tooltipRefresh(final Optional<SpreadsheetError> error) {
        final String newTooltipMessage = error.map(
                e -> {
                    final String errorMessage = e.message();
                    return errorMessage.isEmpty() ?
                            e.toString() :
                            errorMessage;

                }
        ).orElse("");

        final String oldTooltipMessage = this.tooltipMessage;

        if (false == newTooltipMessage.equals(oldTooltipMessage)) {
            if (null != this.tooltip) {
                this.tooltip.detach();
                this.tooltip = null;
            }

            if (false == newTooltipMessage.isEmpty()) {
                this.tooltip = Tooltip.create(
                        this.element,
                        newTooltipMessage
                ).setPosition(DropDirection.BOTTOM_MIDDLE);
            }

            this.tooltipMessage = newTooltipMessage;
        }
    }

    private String tooltipMessage;

    private Tooltip tooltip;

    // IsElement........................................................................................................

    @Override
    public HTMLTableCellElement element() {
        return this.element.element();
    }

    private final TDElement element;
}
