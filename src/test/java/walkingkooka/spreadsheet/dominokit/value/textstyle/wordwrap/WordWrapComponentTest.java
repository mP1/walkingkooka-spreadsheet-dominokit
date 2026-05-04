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

package walkingkooka.spreadsheet.dominokit.value.textstyle.wordwrap;

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
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.WordWrap;

import java.util.List;
import java.util.Optional;

public final class WordWrapComponentTest implements TextStylePropertyEnumComponentTesting<WordWrap, WordWrapComponent> {

    @Test
    public void testSetValue() {
        final WordWrapComponent component = this.createComponent();

        component.setValue(
            Optional.of(
                WordWrap.BREAK_WORD
            )
        );

        this.treePrintAndCheck(
            component,
            "WordWrapComponent\n" +
                "  TextStylePropertyEnumComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/word-wrap/save/] id=Test123-wordWrap-Link\n" +
                "          \"Normal\" [#/1/SpreadsheetName111/cell/A1/style/word-wrap/save/NORMAL] id=Test123-wordWrap-NORMAL-Link\n" +
                "          \"Break Word\" [#/1/SpreadsheetName111/cell/A1/style/word-wrap/save/BREAK_WORD] CHECKED id=Test123-wordWrap-BREAK_WORD-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final WordWrapComponent component = this.createComponent();

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.WORD_WRAP,
                        WordWrap.NORMAL
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "WordWrapComponent\n" +
                "  TextStylePropertyEnumComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/word-wrap/save/] id=Test123-wordWrap-Link\n" +
                "          \"Normal\" [#/1/SpreadsheetName111/cell/A1/style/word-wrap/save/NORMAL] CHECKED id=Test123-wordWrap-NORMAL-Link\n" +
                "          \"Break Word\" [#/1/SpreadsheetName111/cell/A1/style/word-wrap/save/BREAK_WORD] id=Test123-wordWrap-BREAK_WORD-Link\n"
        );
    }

    @Override
    public WordWrapComponent createComponent() {
        return WordWrapComponent.with(
            "Test123-",
            new FakeWordWrapComponentContext() {
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
                            TextStylePropertyName.WORD_WRAP
                        )
                    );
                }
            }
        );
    }

    @Override
    public List<WordWrap> enumValues() {
        return Lists.of(
            WordWrap.values()
        );
    }

    // class............................................................................................................

    @Override
    public Class<WordWrapComponent> type() {
        return WordWrapComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
