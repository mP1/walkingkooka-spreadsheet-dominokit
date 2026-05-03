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

package walkingkooka.spreadsheet.dominokit.value.textstyle.overflow;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.Overflow;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Optional;

public final class OverflowComponentTest implements TextStylePropertyEnumComponentTesting<Overflow, OverflowComponent> {

    @Test
    public void testSetValue() {
        final OverflowComponent component = this.createComponent();

        component.setValue(
            Optional.of(
                Overflow.HIDDEN
            )
        );

        this.treePrintAndCheck(
            component,
            "OverflowComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/overflow-y/save/] id=Test123-overflowY-Link\n" +
                "          \"Visible\" [#/1/SpreadsheetName111/cell/A1/style/overflow-y/save/VISIBLE] id=Test123-overflowY-VISIBLE-Link\n" +
                "          \"Hidden\" [#/1/SpreadsheetName111/cell/A1/style/overflow-y/save/HIDDEN] CHECKED id=Test123-overflowY-HIDDEN-Link\n" +
                "          \"Scroll\" [#/1/SpreadsheetName111/cell/A1/style/overflow-y/save/SCROLL] id=Test123-overflowY-SCROLL-Link\n" +
                "          \"Auto\" [#/1/SpreadsheetName111/cell/A1/style/overflow-y/save/AUTO] id=Test123-overflowY-AUTO-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final OverflowComponent component = this.createComponent();

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.OVERFLOW_X,
                        Overflow.VISIBLE
                    ).set(
                        TextStylePropertyName.OVERFLOW_Y,
                        Overflow.HIDDEN
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "OverflowComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/overflow-y/save/] id=Test123-overflowY-Link\n" +
                "          \"Visible\" [#/1/SpreadsheetName111/cell/A1/style/overflow-y/save/VISIBLE] id=Test123-overflowY-VISIBLE-Link\n" +
                "          \"Hidden\" [#/1/SpreadsheetName111/cell/A1/style/overflow-y/save/HIDDEN] CHECKED id=Test123-overflowY-HIDDEN-Link\n" +
                "          \"Scroll\" [#/1/SpreadsheetName111/cell/A1/style/overflow-y/save/SCROLL] id=Test123-overflowY-SCROLL-Link\n" +
                "          \"Auto\" [#/1/SpreadsheetName111/cell/A1/style/overflow-y/save/AUTO] id=Test123-overflowY-AUTO-Link\n"
        );
    }

    @Override
    public OverflowComponent createComponent() {
        return OverflowComponent.overflowY(
            "Test123-",
            new FakeOverflowComponentContext() {
                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return () -> {
                    };
                }

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.cellStyle(
                        SpreadsheetId.with(1),
                        SpreadsheetName.with("SpreadsheetName111"),
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        Optional.of(
                            TextStylePropertyName.OUTLINE_STYLE
                        )
                    );
                }
            }
        );
    }

    @Override
    public List<Overflow> enumValues() {
        return Lists.of(
            Overflow.values()
        );
    }

    // class............................................................................................................

    @Override
    public Class<OverflowComponent> type() {
        return OverflowComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
