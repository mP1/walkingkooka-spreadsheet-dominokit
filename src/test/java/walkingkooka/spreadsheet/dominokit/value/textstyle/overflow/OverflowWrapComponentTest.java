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
import walkingkooka.tree.text.OverflowWrap;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Optional;

public final class OverflowWrapComponentTest implements TextStylePropertyEnumComponentTesting<OverflowWrap, OverflowWrapComponent> {

    @Test
    public void testSetValue() {
        final OverflowWrapComponent component = this.createComponent();

        component.setValue(
            Optional.of(
                OverflowWrap.ANYWHERE
            )
        );

        this.treePrintAndCheck(
            component,
            "OverflowWrapComponent\n" +
                "  TextStylePropertyEnumComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/overflow-wrap/save/] id=Test123-overflowWrap-Link\n" +
                "          \"Normal\" [#/1/SpreadsheetName111/cell/A1/style/overflow-wrap/save/NORMAL] id=Test123-overflowWrap-NORMAL-Link\n" +
                "          \"Anywhere\" [#/1/SpreadsheetName111/cell/A1/style/overflow-wrap/save/ANYWHERE] CHECKED id=Test123-overflowWrap-ANYWHERE-Link\n" +
                "          \"Break Word\" [#/1/SpreadsheetName111/cell/A1/style/overflow-wrap/save/BREAK_WORD] id=Test123-overflowWrap-BREAK_WORD-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final OverflowWrapComponent component = this.createComponent();

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.OVERFLOW_WRAP,
                        OverflowWrap.BREAK_WORD
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "OverflowWrapComponent\n" +
                "  TextStylePropertyEnumComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/overflow-wrap/save/] id=Test123-overflowWrap-Link\n" +
                "          \"Normal\" [#/1/SpreadsheetName111/cell/A1/style/overflow-wrap/save/NORMAL] id=Test123-overflowWrap-NORMAL-Link\n" +
                "          \"Anywhere\" [#/1/SpreadsheetName111/cell/A1/style/overflow-wrap/save/ANYWHERE] id=Test123-overflowWrap-ANYWHERE-Link\n" +
                "          \"Break Word\" [#/1/SpreadsheetName111/cell/A1/style/overflow-wrap/save/BREAK_WORD] CHECKED id=Test123-overflowWrap-BREAK_WORD-Link\n"
        );
    }

    @Override
    public OverflowWrapComponent createComponent() {
        return OverflowWrapComponent.with(
            "Test123-",
            new FakeOverflowWrapComponentContext() {
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
                            TextStylePropertyName.OVERFLOW_WRAP
                        )
                    );
                }
            }
        );
    }

    @Override
    public List<OverflowWrap> enumValues() {
        return Lists.of(
            OverflowWrap.values()
        );
    }

    // class............................................................................................................

    @Override
    public Class<OverflowWrapComponent> type() {
        return OverflowWrapComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
