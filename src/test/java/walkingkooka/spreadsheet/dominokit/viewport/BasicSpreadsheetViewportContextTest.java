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
import walkingkooka.ToStringTesting;
import walkingkooka.color.Color;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicSpreadsheetViewportContextTest implements SpreadsheetViewportContextTesting<BasicSpreadsheetViewportContext>,
    ToStringTesting<BasicSpreadsheetViewportContext> {

    private final static TextStyle ALL_CELLS_STYLE = TextStyle.EMPTY.set(
        TextStylePropertyName.COLOR,
        Color.BLACK
    );
    private final static  TextStyle SELECTED_ALL_CELLS_STYLE = TextStyle.EMPTY.set(
        TextStylePropertyName.COLOR,
        Color.parse("#111")
    );
    private final static TextStyle CELL_STYLE = TextStyle.EMPTY.set(
        TextStylePropertyName.COLOR,
        Color.parse("#222")
    );
    private final static  TextStyle SELECTED_CELL_STYLE = TextStyle.EMPTY.set(
        TextStylePropertyName.COLOR,
        Color.parse("#333")
    );
    private final static TextStyle COLUMN_STYLE = TextStyle.EMPTY.set(
        TextStylePropertyName.COLOR,
        Color.parse("#444")
    );
    private final static  TextStyle SELECTED_COLUMN_STYLE = TextStyle.EMPTY.set(
        TextStylePropertyName.COLOR,
        Color.parse("#555")
    );
    private final static TextStyle ROW_STYLE = TextStyle.EMPTY.set(
        TextStylePropertyName.COLOR,
        Color.parse("#666")
    );
    private final static  TextStyle SELECTED_ROW_STYLE = TextStyle.EMPTY.set(
        TextStylePropertyName.COLOR,
        Color.parse("#777")
    );
    private final static  Function<TextStyle, TextStyle> HIDE_ZERO_STYLE = (s) -> {
        Objects.requireNonNull(s, "textStyle");
        return s;
    };

    // with.............................................................................................................

    @Test
    public void testWithNullAllCellsStyleFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetViewportContext.with(
                null,
                SELECTED_ALL_CELLS_STYLE,
                CELL_STYLE,
                SELECTED_CELL_STYLE,
                COLUMN_STYLE,
                SELECTED_COLUMN_STYLE,
                ROW_STYLE,
                SELECTED_ROW_STYLE,
                HIDE_ZERO_STYLE
            )
        );
    }

    @Test
    public void testWithNullSelectedAllCellsStyleFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetViewportContext.with(
                ALL_CELLS_STYLE,
                null,
                CELL_STYLE,
                SELECTED_CELL_STYLE,
                COLUMN_STYLE,
                SELECTED_COLUMN_STYLE,
                ROW_STYLE,
                SELECTED_ROW_STYLE,
                HIDE_ZERO_STYLE
            )
        );
    }

    @Test
    public void testWithNullCellStyleFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetViewportContext.with(
                ALL_CELLS_STYLE,
                SELECTED_ALL_CELLS_STYLE,
                null,
                SELECTED_CELL_STYLE,
                COLUMN_STYLE,
                SELECTED_COLUMN_STYLE,
                ROW_STYLE,
                SELECTED_ROW_STYLE,
                HIDE_ZERO_STYLE
            )
        );
    }

    @Test
    public void testWithNullSelectedCellStyleFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetViewportContext.with(
                ALL_CELLS_STYLE,
                SELECTED_ALL_CELLS_STYLE,
                CELL_STYLE,
                null,
                COLUMN_STYLE,
                SELECTED_COLUMN_STYLE,
                ROW_STYLE,
                SELECTED_ROW_STYLE,
                HIDE_ZERO_STYLE
            )
        );
    }

    @Test
    public void testWithNullColumnStyleFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetViewportContext.with(
                ALL_CELLS_STYLE,
                SELECTED_ALL_CELLS_STYLE,
                CELL_STYLE,
                SELECTED_CELL_STYLE,
                null,
                SELECTED_COLUMN_STYLE,
                ROW_STYLE,
                SELECTED_ROW_STYLE,
                HIDE_ZERO_STYLE
            )
        );
    }

    @Test
    public void testWithNullSelectedColumnStyleFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetViewportContext.with(
                ALL_CELLS_STYLE,
                SELECTED_ALL_CELLS_STYLE,
                CELL_STYLE,
                SELECTED_CELL_STYLE,
                COLUMN_STYLE,
                null,
                ROW_STYLE,
                SELECTED_ROW_STYLE,
                HIDE_ZERO_STYLE
            )
        );
    }

    @Test
    public void testWithNullRowStyleFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetViewportContext.with(
                ALL_CELLS_STYLE,
                SELECTED_ALL_CELLS_STYLE,
                CELL_STYLE,
                SELECTED_CELL_STYLE,
                COLUMN_STYLE,
                SELECTED_COLUMN_STYLE,
                null,
                SELECTED_ROW_STYLE,
                HIDE_ZERO_STYLE
            )
        );
    }

    @Test
    public void testWithNullSelectedRowStyleFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetViewportContext.with(
                ALL_CELLS_STYLE,
                SELECTED_ALL_CELLS_STYLE,
                CELL_STYLE,
                SELECTED_CELL_STYLE,
                COLUMN_STYLE,
                SELECTED_COLUMN_STYLE,
                ROW_STYLE,
                null,
                HIDE_ZERO_STYLE
            )
        );
    }

    @Test
    public void testWithNullHideZeroStyleFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetViewportContext.with(
                ALL_CELLS_STYLE,
                SELECTED_ALL_CELLS_STYLE,
                CELL_STYLE,
                SELECTED_CELL_STYLE,
                COLUMN_STYLE,
                SELECTED_COLUMN_STYLE,
                ROW_STYLE,
                SELECTED_ROW_STYLE,
                null
            )
        );
    }

    @Override
    public BasicSpreadsheetViewportContext createContext() {
        return BasicSpreadsheetViewportContext.with(
            ALL_CELLS_STYLE,
            SELECTED_ALL_CELLS_STYLE,
            CELL_STYLE,
            SELECTED_CELL_STYLE,
            COLUMN_STYLE,
            SELECTED_COLUMN_STYLE,
            ROW_STYLE,
            SELECTED_ROW_STYLE,
            HIDE_ZERO_STYLE
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createContext(),
            "allCellsStyle: {color=black}, selectedAllCellsStyle: {color=#111111}, cellStyle: {color=#222222}, selectedCellStyle: {color=#333333}, columnStyle: {color=#444444}, selectedColumnStyle: {color=#555555}, rowStyle: {color=#666666}, selectedRowStyle: {color=#777777}, hideZeroStyle: " + HIDE_ZERO_STYLE
        );
    }

    // class............................................................................................................

    @Override
    public String typeNameSuffix() {
        return SpreadsheetViewportContext.class.getSimpleName();
    }

    @Override 
    public Class<BasicSpreadsheetViewportContext> type() {
        return BasicSpreadsheetViewportContext.class;
    }
}
