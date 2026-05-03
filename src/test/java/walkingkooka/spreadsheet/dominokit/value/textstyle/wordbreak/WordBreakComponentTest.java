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

package walkingkooka.spreadsheet.dominokit.value.textstyle.wordbreak;

import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.WordBreak;

import java.util.Optional;

public final class WordBreakComponentTest implements TextStylePropertyEnumComponentTesting<WordBreak, WordBreakComponent> {

    @Test
    public void testSetValue() {
        final WordBreakComponent component = this.createComponent();

        component.setValue(
            Optional.of(
                WordBreak.BREAK_WORD
            )
        );

        this.treePrintAndCheck(
            component,
            "WordBreakComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/word-break/save/] id=Test123-wordBreak-Link\n" +
                "          \"Normal\" [#/1/SpreadsheetName111/cell/A1/style/word-break/save/NORMAL] id=Test123-wordBreak-NORMAL-Link\n" +
                "          \"Break All\" [#/1/SpreadsheetName111/cell/A1/style/word-break/save/BREAK_ALL] id=Test123-wordBreak-BREAK_ALL-Link\n" +
                "          \"Keep All\" [#/1/SpreadsheetName111/cell/A1/style/word-break/save/KEEP_ALL] id=Test123-wordBreak-KEEP_ALL-Link\n" +
                "          \"Break Word\" [#/1/SpreadsheetName111/cell/A1/style/word-break/save/BREAK_WORD] CHECKED id=Test123-wordBreak-BREAK_WORD-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final WordBreakComponent component = this.createComponent();

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.WORD_BREAK,
                        WordBreak.NORMAL
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "WordBreakComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/word-break/save/] id=Test123-wordBreak-Link\n" +
                "          \"Normal\" [#/1/SpreadsheetName111/cell/A1/style/word-break/save/NORMAL] CHECKED id=Test123-wordBreak-NORMAL-Link\n" +
                "          \"Break All\" [#/1/SpreadsheetName111/cell/A1/style/word-break/save/BREAK_ALL] id=Test123-wordBreak-BREAK_ALL-Link\n" +
                "          \"Keep All\" [#/1/SpreadsheetName111/cell/A1/style/word-break/save/KEEP_ALL] id=Test123-wordBreak-KEEP_ALL-Link\n" +
                "          \"Break Word\" [#/1/SpreadsheetName111/cell/A1/style/word-break/save/BREAK_WORD] id=Test123-wordBreak-BREAK_WORD-Link\n"
        );
    }

    @Override
    public WordBreakComponent createComponent() {
        return WordBreakComponent.with(
            "Test123-",
            new FakeWordBreakComponentContext() {
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
                            TextStylePropertyName.WORD_BREAK
                        )
                    );
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<WordBreakComponent> type() {
        return WordBreakComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
