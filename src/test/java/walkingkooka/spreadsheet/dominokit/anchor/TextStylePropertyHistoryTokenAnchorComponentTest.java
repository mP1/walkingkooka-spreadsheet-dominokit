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
import walkingkooka.EmptyTextException;
import walkingkooka.color.Color;
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

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStylePropertyHistoryTokenAnchorComponentTest implements ValueComponentTesting<HTMLAnchorElement, TextAlign, TextStylePropertyHistoryTokenAnchorComponent<TextAlign>>,
    ComponentLifecycleMatcherTesting {

    // with.............................................................................................................

    @Test
    public void testWithNullIdPrefixFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStylePropertyHistoryTokenAnchorComponent.with(
                null,
                TextStylePropertyName.COLOR,
                Optional.of(Color.BLACK),
                new FakeTextStylePropertyHistoryTokenAnchorComponentContext()
            )
        );
    }

    @Test
    public void testWithNullEmptyPrefixFails() {
        assertThrows(
            EmptyTextException.class,
            () -> TextStylePropertyHistoryTokenAnchorComponent.with(
                "",
                TextStylePropertyName.COLOR,
                Optional.of(Color.BLACK),
                new FakeTextStylePropertyHistoryTokenAnchorComponentContext()
            )
        );
    }

    @Test
    public void testWithNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStylePropertyHistoryTokenAnchorComponent.with(
                null,
                TextStylePropertyName.COLOR,
                null,
                new FakeTextStylePropertyHistoryTokenAnchorComponentContext()
            )
        );
    }

    // setValue.........................................................................................................

    @Test
    public void testTreePrintWhenCellSelectHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.A1.setDefaultAnchor()
                )
            ),
            "\"Left!\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/LEFT] id=TestID123-textAlign-LEFT-Link"
        );
    }

    @Test
    public void testTreePrintWhenCellSelectHistoryTokenAndEmptyValue() {
        this.treePrintAndCheck(
            this.createComponent(
                Optional.empty(),
                this.createContext(
                    HistoryToken.cellSelect(
                        SPREADSHEET_ID,
                        SPREADSHEET_NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor()
                    )
                )
            ),
            "\"Left!\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/] id=TestID123-textAlign-Link"
        );
    }

    @Test
    public void testTreePrintWhenColumnSelectHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.columnSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.A1.column()
                        .setDefaultAnchor()
                )
            ),
            "\"Left!\" DISABLED id=TestID123-textAlign-LEFT-Link"
        );
    }

    @Test
    public void testTreePrintWhenSpreadsheetSelectHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            ),
            "\"Left!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/LEFT] id=TestID123-textAlign-LEFT-Link"
        );
    }

    @Test
    public void testTreePrintWhenCellSelectHistoryTokenChange() {
        final TextStylePropertyHistoryTokenAnchorComponentContext context = this.createContext(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        final TextStylePropertyHistoryTokenAnchorComponent<TextAlign> anchor = this.createComponent(context);

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
            "\"Left!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/LEFT] id=TestID123-textAlign-LEFT-Link"
        );
    }

    @Override
    public TextStylePropertyHistoryTokenAnchorComponent<TextAlign> createComponent() {
        return this.createComponent(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            )
        );
    }

    private TextStylePropertyHistoryTokenAnchorComponent<TextAlign> createComponent(final HistoryToken historyToken) {
        return this.createComponent(
            this.createContext(historyToken)
        );
    }

    private TextStylePropertyHistoryTokenAnchorComponent<TextAlign> createComponent(final TextStylePropertyHistoryTokenAnchorComponentContext context) {
        return this.createComponent(
            Optional.of(TextAlign.LEFT),
            context
        );
    }

    private TextStylePropertyHistoryTokenAnchorComponent<TextAlign> createComponent(final Optional<TextAlign> value,
                                                                                    final TextStylePropertyHistoryTokenAnchorComponentContext context) {
        return TextStylePropertyHistoryTokenAnchorComponent.with(
            "TestID123-",
            TextStylePropertyName.TEXT_ALIGN,
            value,
            context
        ).setTextContent("Left!");
    }

    private TextStylePropertyHistoryTokenAnchorComponentContext createContext(final HistoryToken historyToken) {
        return new FakeTextStylePropertyHistoryTokenAnchorComponentContext() {

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
    public Class<TextStylePropertyHistoryTokenAnchorComponent<TextAlign>> type() {
        return Cast.to(TextStylePropertyHistoryTokenAnchorComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
