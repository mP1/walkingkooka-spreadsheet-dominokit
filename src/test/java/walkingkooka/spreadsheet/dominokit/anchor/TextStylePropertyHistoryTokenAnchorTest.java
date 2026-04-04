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

package walkingkooka.spreadsheet.dominokit.anchor;

import elemental2.dom.HTMLAnchorElement;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class TextStylePropertyHistoryTokenAnchorTest implements ValueComponentTesting<HTMLAnchorElement, TextAlign, TextStylePropertyHistoryTokenAnchor<TextAlign>>,
    ComponentLifecycleMatcherTesting {

    @Test
    public void testSetValueWhenCellSelectHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.A1.setDefaultAnchor()
                )
            ),
            "\"Left!\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/LEFT]"
        );
    }

    @Test
    public void testSetValueWhenColumnSelectHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.columnSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.A1.column()
                        .setDefaultAnchor()
                )
            ),
            "\"Left!\" DISABLED"
        );
    }

    @Test
    public void testSetValueWhenSpreadsheetSelectHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            ),
            "\"Left!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/LEFT]"
        );
    }

    @Test
    public void testSetValueWhenCellSelectHistoryTokenChange() {
        final TextStylePropertyHistoryTokenAnchorContext context = this.createContext(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        final TextStylePropertyHistoryTokenAnchor<TextAlign> anchor = this.createComponent(context);

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCell("B2")
                    .setDefaultAnchor()
            )
        );

        this.treePrintAndCheck(
            anchor,
            "\"Left!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/LEFT]"
        );
    }

    @Override
    public TextStylePropertyHistoryTokenAnchor<TextAlign> createComponent() {
        return this.createComponent(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            )
        );
    }

    private TextStylePropertyHistoryTokenAnchor<TextAlign> createComponent(final HistoryToken historyToken) {
        return this.createComponent(
            this.createContext(historyToken)
        );
    }

    private TextStylePropertyHistoryTokenAnchor<TextAlign> createComponent(final TextStylePropertyHistoryTokenAnchorContext context) {
        return TextStylePropertyHistoryTokenAnchor.with(
            TextStylePropertyName.TEXT_ALIGN,
            context
        ).setValue(
            Optional.of(TextAlign.LEFT)
        ).setTextContent("Left!");
    }

    private TextStylePropertyHistoryTokenAnchorContext createContext(final HistoryToken historyToken) {
        return new FakeTextStylePropertyHistoryTokenAnchorContext() {

            @Override
            public HistoryToken historyToken() {
                return this.current;
            }

            private HistoryToken current = historyToken;

            @Override
            public void pushHistoryToken(final HistoryToken token) {
                final HistoryToken previous = this.current;
                this.current = token;
                this.watchers.onHistoryTokenChange(
                    previous,
                    AppContexts.fake()
                );
            }

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return this.watchers.add(watcher);
            }

            private final HistoryTokenWatchers watchers = HistoryTokenWatchers.empty();
        };
    }

    @Override
    public Class<TextStylePropertyHistoryTokenAnchor<TextAlign>> type() {
        return Cast.to(TextStylePropertyHistoryTokenAnchor.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
