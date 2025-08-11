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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStyle;

public final class SpreadsheetViewportContextTest implements ClassTesting<SpreadsheetViewportContext> {

    // selectionStyle...................................................................................................

    private final static TextStyle TEXT_STYLE = TextStyle.parse("color: black;");

    private final static TextStyle CELL_STYLE = TextStyle.parse("background-color: white;");

    private final static TextStyle SELECTED_CELL_STYLE = TEXT_STYLE.merge(CELL_STYLE);

    @Test
    public void testSelectionStyleWithCell() {
        this.selectionStyleAndCheck(
            new FakeSpreadsheetViewportContext() {

                @Override
                public TextStyle cellStyle() {
                    return TEXT_STYLE;
                }
            },
            SpreadsheetSelection.A1,
            TEXT_STYLE
        );
    }

    @Test
    public void testSelectionStyleWithCellRange() {
        this.selectionStyleAndCheck(
            new FakeSpreadsheetViewportContext() {

                @Override
                public TextStyle cellStyle() {
                    return TEXT_STYLE;
                }
            },
            SpreadsheetSelection.parseCellRange("B2:C3"),
            TEXT_STYLE
        );
    }

    @Test
    public void testSelectionStyleWithLabel() {
        this.selectionStyleAndCheck(
            new FakeSpreadsheetViewportContext() {

                @Override
                public TextStyle cellStyle() {
                    return TEXT_STYLE;
                }
            },
            SpreadsheetSelection.labelName("Label123"),
            TEXT_STYLE
        );
    }

    @Test
    public void testSelectionStyleWithColumn() {
        this.selectionStyleAndCheck(
            new FakeSpreadsheetViewportContext() {

                @Override
                public TextStyle columnStyle() {
                    return TEXT_STYLE;
                }
            },
            SpreadsheetSelection.parseColumn("F"),
            TEXT_STYLE
        );
    }

    @Test
    public void testSelectionStyleWithColumnRange() {
        this.selectionStyleAndCheck(
            new FakeSpreadsheetViewportContext() {

                @Override
                public TextStyle columnStyle() {
                    return TEXT_STYLE;
                }
            },
            SpreadsheetSelection.parseColumnRange("G:H"),
            TEXT_STYLE
        );
    }

    @Test
    public void testSelectionStyleWithRow() {
        this.selectionStyleAndCheck(
            new FakeSpreadsheetViewportContext() {

                @Override
                public TextStyle rowStyle() {
                    return TEXT_STYLE;
                }
            },
            SpreadsheetSelection.parseRow("6"),
            TEXT_STYLE
        );
    }

    @Test
    public void testSelectionStyleWithRowRange() {
        this.selectionStyleAndCheck(
            new FakeSpreadsheetViewportContext() {

                @Override
                public TextStyle rowStyle() {
                    return TEXT_STYLE;
                }
            },
            SpreadsheetSelection.parseRowRange("7:88"),
            TEXT_STYLE
        );
    }

    private void selectionStyleAndCheck(final SpreadsheetViewportContext context,
                                        final SpreadsheetSelection selection,
                                        final TextStyle expected) {
        this.checkEquals(
            expected,
            context.selectionStyle(selection)
        );
    }

    // selectedSelectionStyle...........................................................................................

    @Test
    public void testSelectedSelectionStyleWithCell() {
        this.selectedSelectionStyleAndCheck(
            new FakeSpreadsheetViewportContext() {

                @Override
                public TextStyle selectedCellStyle(final TextStyle cellStyle) {
                    return TEXT_STYLE.merge(cellStyle);
                }
            },
            SpreadsheetSelection.A1,
            CELL_STYLE,
            SELECTED_CELL_STYLE
        );
    }

    @Test
    public void testSelectedSelectionStyleWithCellRange() {
        this.selectedSelectionStyleAndCheck(
            new FakeSpreadsheetViewportContext() {

                @Override
                public TextStyle selectedCellStyle(final TextStyle cellStyle) {
                    return TEXT_STYLE.merge(cellStyle);
                }
            },
            SpreadsheetSelection.parseCellRange("B2:C3"),
            CELL_STYLE,
            SELECTED_CELL_STYLE
        );
    }

    @Test
    public void testSelectedSelectionStyleWithLabel() {
        this.selectedSelectionStyleAndCheck(
            new FakeSpreadsheetViewportContext() {

                @Override
                public TextStyle selectedCellStyle(final TextStyle cellStyle) {
                    return TEXT_STYLE.merge(cellStyle);
                }
            },
            SpreadsheetSelection.labelName("Label123"),
            CELL_STYLE,
            SELECTED_CELL_STYLE
        );
    }

    @Test
    public void testSelectedSelectionStyleWithColumn() {
        this.selectedSelectionStyleAndCheck(
            new FakeSpreadsheetViewportContext() {

                @Override
                public TextStyle selectedColumnStyle() {
                    return TEXT_STYLE;
                }
            },
            SpreadsheetSelection.parseColumn("F"),
            TEXT_STYLE
        );
    }

    @Test
    public void testSelectedSelectionStyleWithColumnRange() {
        this.selectedSelectionStyleAndCheck(
            new FakeSpreadsheetViewportContext() {

                @Override
                public TextStyle selectedColumnStyle() {
                    return TEXT_STYLE;
                }
            },
            SpreadsheetSelection.parseColumnRange("G:H"),
            TEXT_STYLE
        );
    }

    @Test
    public void testSelectedSelectionStyleWithRow() {
        this.selectedSelectionStyleAndCheck(
            new FakeSpreadsheetViewportContext() {

                @Override
                public TextStyle selectedRowStyle() {
                    return TEXT_STYLE;
                }
            },
            SpreadsheetSelection.parseRow("6"),
            TEXT_STYLE
        );
    }

    @Test
    public void testSelectedSelectionStyleWithRowRange() {
        this.selectedSelectionStyleAndCheck(
            new FakeSpreadsheetViewportContext() {

                @Override
                public TextStyle selectedRowStyle() {
                    return TEXT_STYLE;
                }
            },
            SpreadsheetSelection.parseRowRange("7:88"),
            TEXT_STYLE
        );
    }

    private void selectedSelectionStyleAndCheck(final SpreadsheetViewportContext context,
                                                final SpreadsheetSelection selection,
                                                final TextStyle expected) {
        this.selectedSelectionStyleAndCheck(
            context,
            selection,
            TextStyle.EMPTY,
            expected
        );
    }

    private void selectedSelectionStyleAndCheck(final SpreadsheetViewportContext context,
                                                final SpreadsheetSelection selection,
                                                final TextStyle style,
                                                final TextStyle expected) {
        this.checkEquals(
            expected,
            context.selectedSelectionStyle(
                selection,
                style
            ),
            selection::toString
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetViewportContext> type() {
        return SpreadsheetViewportContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
