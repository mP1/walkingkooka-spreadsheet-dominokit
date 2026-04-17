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

package walkingkooka.spreadsheet.dominokit.value.textstyle.hangingpunctuation;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.HangingPunctuation;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class HangingPunctuationComponentTest implements TextStylePropertyComponentTesting<HTMLFieldSetElement, HangingPunctuation, HangingPunctuationComponent> {

    @Test
    public void testSetValue() {
        final HangingPunctuationComponent component = this.createComponent();

        component.setValue(
            Optional.of(
                HangingPunctuation.ALLOW_END
            )
        );

        this.treePrintAndCheck(
            component,
            "HangingPunctuationComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    Hanging Punctuation\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/hanging-punctuation/save/] id=Test123-hangingPunctuation-Link\n" +
                "            \"None\" [#/1/SpreadsheetName111/cell/A1/style/hanging-punctuation/save/NONE] id=Test123-hangingPunctuation-NONE-Link\n" +
                "            \"First\" [#/1/SpreadsheetName111/cell/A1/style/hanging-punctuation/save/FIRST] id=Test123-hangingPunctuation-FIRST-Link\n" +
                "            \"Last\" [#/1/SpreadsheetName111/cell/A1/style/hanging-punctuation/save/LAST] id=Test123-hangingPunctuation-LAST-Link\n" +
                "            \"Allow End\" [#/1/SpreadsheetName111/cell/A1/style/hanging-punctuation/save/ALLOW_END] CHECKED id=Test123-hangingPunctuation-ALLOW_END-Link\n" +
                "            \"Force End\" [#/1/SpreadsheetName111/cell/A1/style/hanging-punctuation/save/FORCE_END] id=Test123-hangingPunctuation-FORCE_END-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final HangingPunctuationComponent component = this.createComponent();

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.HANGING_PUNCTUATION,
                        HangingPunctuation.FIRST
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "HangingPunctuationComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    Hanging Punctuation\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/hanging-punctuation/save/] id=Test123-hangingPunctuation-Link\n" +
                "            \"None\" [#/1/SpreadsheetName111/cell/A1/style/hanging-punctuation/save/NONE] id=Test123-hangingPunctuation-NONE-Link\n" +
                "            \"First\" [#/1/SpreadsheetName111/cell/A1/style/hanging-punctuation/save/FIRST] CHECKED id=Test123-hangingPunctuation-FIRST-Link\n" +
                "            \"Last\" [#/1/SpreadsheetName111/cell/A1/style/hanging-punctuation/save/LAST] id=Test123-hangingPunctuation-LAST-Link\n" +
                "            \"Allow End\" [#/1/SpreadsheetName111/cell/A1/style/hanging-punctuation/save/ALLOW_END] id=Test123-hangingPunctuation-ALLOW_END-Link\n" +
                "            \"Force End\" [#/1/SpreadsheetName111/cell/A1/style/hanging-punctuation/save/FORCE_END] id=Test123-hangingPunctuation-FORCE_END-Link\n"
        );
    }

    @Override
    public HangingPunctuationComponent createComponent() {
        return HangingPunctuationComponent.with(
            "Test123-",
            new FakeHangingPunctuationComponentContext() {
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
                            TextStylePropertyName.HANGING_PUNCTUATION
                        )
                    );
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<HangingPunctuationComponent> type() {
        return HangingPunctuationComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
