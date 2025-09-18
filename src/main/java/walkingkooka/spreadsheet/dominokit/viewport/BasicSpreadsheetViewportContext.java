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

import walkingkooka.tree.text.TextStyle;

import java.util.Objects;
import java.util.function.Function;

final class BasicSpreadsheetViewportContext implements SpreadsheetViewportContext {

    static BasicSpreadsheetViewportContext with(final TextStyle allCellsStyle,
                                                final TextStyle selectedAllCellsStyle,
                                                final TextStyle cellStyle,
                                                final Function<TextStyle,TextStyle> selectedCellStyle,
                                                final TextStyle columnStyle,
                                                final TextStyle selectedColumnStyle,
                                                final TextStyle rowStyle,
                                                final TextStyle selectedRowStyle,
                                                final Function<TextStyle, TextStyle> hideZeroStyle,
                                                final Function<TextStyle, TextStyle> showFormulasStyle) {
        return new BasicSpreadsheetViewportContext(
            Objects.requireNonNull(allCellsStyle, "allCellsStyle"),
            Objects.requireNonNull(selectedAllCellsStyle, "selectedAllCellsStyle"),
            Objects.requireNonNull(cellStyle, "cellStyle"),
            Objects.requireNonNull(selectedCellStyle, "selectedCellStyle"),
            Objects.requireNonNull(columnStyle, "columnStyle"),
            Objects.requireNonNull(selectedColumnStyle, "selectedColumnStyle"),
            Objects.requireNonNull(rowStyle, "rowStyle"),
            Objects.requireNonNull(selectedRowStyle, "selectedRowStyle"),
            Objects.requireNonNull(hideZeroStyle, "hideZeroStyle"),
            Objects.requireNonNull(showFormulasStyle, "showFormulasStyle")
        );
    }

    private BasicSpreadsheetViewportContext(final TextStyle allCellsStyle,
                                            final TextStyle selectedAllCellsStyle,
                                            final TextStyle cellStyle,
                                            final Function<TextStyle, TextStyle> selectedCellStyle,
                                            final TextStyle columnStyle,
                                            final TextStyle selectedColumnStyle,
                                            final TextStyle rowStyle,
                                            final TextStyle selectedRowStyle,
                                            final Function<TextStyle, TextStyle> hideZeroStyle,
                                            final Function<TextStyle, TextStyle> showFormulasStyle) {
        this.allCellsStyle = allCellsStyle;
        this.selectedAllCellsStyle = selectedAllCellsStyle;

        this.cellStyle = cellStyle;
        this.selectedCellStyle = selectedCellStyle;

        this.columnStyle = columnStyle;
        this.selectedColumnStyle = selectedColumnStyle;

        this.rowStyle = rowStyle;
        this.selectedRowStyle = selectedRowStyle;

        this.hideZeroStyle = hideZeroStyle;

        this.showFormulasStyle = showFormulasStyle;
    }

    // SpreadsheetViewportContext.......................................................................................

    @Override
    public TextStyle allCellsStyle() {
        return this.allCellsStyle;
    }

    private final TextStyle allCellsStyle;

    @Override
    public TextStyle selectedAllCellsStyle() {
        return this.selectedAllCellsStyle;
    }

    private final TextStyle selectedAllCellsStyle;

    @Override
    public TextStyle cellStyle() {
        return this.cellStyle;
    }

    private final TextStyle cellStyle;

    @Override
    public TextStyle selectedCellStyle(final TextStyle cellStyle) {
        return this.selectedCellStyle.apply(cellStyle);
    }

    private final Function<TextStyle, TextStyle> selectedCellStyle;

    @Override
    public TextStyle columnStyle() {
        return this.columnStyle;
    }

    private final TextStyle columnStyle;

    @Override
    public TextStyle selectedColumnStyle() {
        return this.selectedColumnStyle;
    }

    private final TextStyle selectedColumnStyle;

    @Override
    public TextStyle rowStyle() {
        return this.rowStyle;
    }

    private final TextStyle rowStyle;

    @Override
    public TextStyle selectedRowStyle() {
        return this.selectedRowStyle;
    }

    private final TextStyle selectedRowStyle;


    @Override
    public TextStyle hideZeroStyle(final TextStyle style) {
        return this.hideZeroStyle.apply(style);
    }

    private final Function<TextStyle, TextStyle> hideZeroStyle;

    @Override
    public TextStyle showFormulasStyle(final TextStyle style) {
        return this.showFormulasStyle.apply(style);
    }

    private final Function<TextStyle, TextStyle> showFormulasStyle;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return "allCellsStyle: " + this.allCellsStyle +
            ", selectedAllCellsStyle: " + this.selectedAllCellsStyle +
            ", cellStyle: " + this.cellStyle +
            ", selectedCellStyle: " + this.selectedCellStyle +
            ", columnStyle: " + this.columnStyle +
            ", selectedColumnStyle: " + this.selectedColumnStyle +
            ", rowStyle: " + this.rowStyle +
            ", selectedRowStyle: " + this.selectedRowStyle +
            ", hideZeroStyle: " + this.hideZeroStyle +
            ", showFormulasStyle: " + this.showFormulasStyle;
    }
}
