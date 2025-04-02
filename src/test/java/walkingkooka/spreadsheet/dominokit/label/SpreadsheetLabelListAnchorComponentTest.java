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

package walkingkooka.spreadsheet.dominokit.label;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetLabelListAnchorComponentTest implements AnchorComponentTesting<SpreadsheetLabelListAnchorComponent, Void> {

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetLabelListAnchorComponent.with(
                HistoryTokenAnchorComponent.empty(),
                "id123",
                null
            )
        );
    }

    // value............................................................................................................

    @Test
    public void testSetValueFails() {
        assertThrows(
            UnsupportedOperationException.class,
            () -> this.createComponent().setValue(
                Optional.empty()
            )
        );
    }

    // onHistoryChange..................................................................................................

    @Test
    public void testOnHistoryChangeWithSpreadsheetNameHistoryToken() {
        final TestHistoryContext historyContext = new TestHistoryContext("/2/SpreadsheetName222");

        final SpreadsheetLabelListAnchorComponent component = SpreadsheetLabelListAnchorComponent.with(
            HistoryTokenAnchorComponent.empty(),
            "label-list-anchor-id",
            historyContext
        );

        this.treePrintAndCheck(
            component,
            "\"Labels\" DISABLED id=label-list-anchor-id"
        );

        historyContext.pushHistoryToken(
            HistoryToken.parseString("/3/SpreadsheetName333")
        );

        this.treePrintAndCheck(
            component,
            "\"Labels\" [#/3/SpreadsheetName333/label] id=label-list-anchor-id"
        );
    }

    @Test
    public void testOnHistoryChangeWithSpreadsheetCellSelectHistoryToken() {
        final TestHistoryContext historyContext = new TestHistoryContext("/2/SpreadsheetName222");

        final SpreadsheetLabelListAnchorComponent component = SpreadsheetLabelListAnchorComponent.with(
            HistoryTokenAnchorComponent.empty(),
            "label-list-anchor-id",
            historyContext
        );

        component.setTextContent("List Labels 123");

        historyContext.pushHistoryToken(
            HistoryToken.parseString("/3/SpreadsheetName333/cell/A1")
        );

        this.treePrintAndCheck(
            component,
            "\"List Labels 123\" [#/3/SpreadsheetName333/cell/A1/labels] id=label-list-anchor-id"
        );
    }

    @Override
    public SpreadsheetLabelListAnchorComponent createComponent() {
        return this.createComponent("/1/SpreadsheetName222/label");
    }

    private SpreadsheetLabelListAnchorComponent createComponent(final String historyToken) {
        return SpreadsheetLabelListAnchorComponent.with(
            HistoryTokenAnchorComponent.empty(),
            "label-list-anchor-id",
            new TestHistoryContext(historyToken)
        );
    }

    static class TestHistoryContext extends FakeHistoryContext {

        private TestHistoryContext(final String historyToken) {
            this.historyToken = HistoryToken.parseString(historyToken);
        }

        @Override
        public HistoryToken historyToken() {
            return this.historyToken;
        }

        private HistoryToken historyToken;

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return this.historyTokenWatchers.add(watcher);
        }

        @Override
        public void pushHistoryToken(final HistoryToken token) {
            final HistoryToken previous = this.historyToken;
            this.historyToken = token;
            this.historyTokenWatchers.onHistoryTokenChange(
                previous,
                new FakeAppContext() {

                    @Override
                    public HistoryToken historyToken() {
                        return token;
                    }
                }
            );
        }

        private final HistoryTokenWatchers historyTokenWatchers = HistoryTokenWatchers.empty();

        @Override
        public String toString() {
            return this.historyToken.toString();
        }


    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetLabelListAnchorComponent> type() {
        return SpreadsheetLabelListAnchorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
