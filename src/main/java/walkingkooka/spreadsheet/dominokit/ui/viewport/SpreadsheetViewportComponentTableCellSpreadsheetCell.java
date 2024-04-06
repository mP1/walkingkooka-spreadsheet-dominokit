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
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetError;
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

        final SpreadsheetViewportCache cache = context.viewportCache();

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
    }

    @Override
    void refresh(final Predicate<SpreadsheetSelection> selected,
                 final SpreadsheetViewportComponentTableContext context) {

        final SpreadsheetViewportCache cache = context.viewportCache();
        final SpreadsheetCellReference cellReference = this.cellReference;
        final TDElement td = this.element;
        td.clearElement();

        final Optional<SpreadsheetCell> maybeCell = cache.cell(cellReference);
        TextStyle style = context.defaultCellStyle();
        Optional<SpreadsheetError> maybeError = Optional.empty();

        if (maybeCell.isPresent()) {
            final SpreadsheetCell cell = maybeCell.get();

            boolean skipFormattedValue = false;
            if (context.hideZeroValues()) {
                final Object value = cell.formula()
                        .value()
                        .orElse(null);

                if (ExpressionNumber.is(value) &&
                        ExpressionNumberSign.ZERO == ExpressionNumberKind.DEFAULT.create((Number) value).sign()) {
                    skipFormattedValue = true;
                }
            }


            if (false == skipFormattedValue) {
                final Optional<TextNode> maybeFormatted = cell.formattedValue();
                if (maybeFormatted.isPresent()) {
                    td.appendChild(
                            Doms.node(maybeFormatted.get())
                    );
                }
            }
            style = cell.style()
                    .merge(style);

            if (skipFormattedValue) {
                style = hideZeroValues(style);
            }

            maybeError = cell.formula()
                    .error();
        }

        if (selected.test(cellReference)) {
            style = style.set(
                    TextStylePropertyName.BACKGROUND_COLOR,
                    style.getOrFail(TextStylePropertyName.BACKGROUND_COLOR)
                            .mix(
                                    SpreadsheetDominoKitColor.VIEWPORT_CELL_SELECTED_BACKGROUND_COLOR,
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

    private final SpreadsheetCellReference cellReference;

    private SpreadsheetCell cell;


    private void tooltipRefresh(final Optional<SpreadsheetError> error) {
        if (null != this.tooltip) {
            this.tooltip.detach();
            this.tooltip = null;
        }

        if (error.isPresent()) {
            final String message = error.get()
                    .message();

            // if theres no message show SpreadsheetError#toString
            this.tooltip = Tooltip.create(
                    this.element,
                    message.isEmpty() ?
                            error.toString() :
                            message
            ).setPosition(DropDirection.BOTTOM_MIDDLE);
        }
    }

    private Tooltip tooltip;

    private TextStyle hideZeroValues(final TextStyle style) {
        return style.set(
                TextStylePropertyName.BACKGROUND_COLOR,
                style.getOrFail(TextStylePropertyName.BACKGROUND_COLOR)
                        .mix(
                                SpreadsheetDominoKitColor.HIDE_ZERO_VALUES_COLOR,
                                0.5f
                        )
        );
    }

    // IsElement........................................................................................................

    @Override
    public HTMLTableCellElement element() {
        return this.element.element();
    }

    private final TDElement element;
}
