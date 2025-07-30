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

package walkingkooka.spreadsheet.dominokit.dialog;

import org.junit.jupiter.api.Test;
import walkingkooka.net.UrlFragment;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycleTestingTest.TestSpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.text.SpreadsheetTextBox;
import walkingkooka.text.printer.TreePrintable;

import java.util.Optional;

public final class SpreadsheetDialogComponentLifecycleTestingTest implements SpreadsheetDialogComponentLifecycleTesting<TestSpreadsheetDialogComponentLifecycle> {

    @Test
    public void testAnchor() {
        this.treePrintAndCheck(
            new TestSpreadsheetDialogComponentLifecycle().anchor("Hello"),
            "\"Hello\" DISABLED id=id123-hello-Link"
        );
    }

    @Test
    public void testAnchorWithMultiWordText() {
        this.treePrintAndCheck(
            new TestSpreadsheetDialogComponentLifecycle().anchor("Hello goodbye"),
            "\"Hello goodbye\" DISABLED id=id123-hello-goodbye-Link"
        );
    }

    private final static HistoryToken HISTORY_TOKEN = HistoryToken.spreadsheetCreate();

    @Test
    public void testNoMatchedOnHistoryTokenChangeAndCheck() {
        final TestSpreadsheetDialogComponentLifecycle table = new TestSpreadsheetDialogComponentLifecycle();
        this.onHistoryTokenChangeAndCheck(
            table,
            new FakeAppContext() {
                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.unknown(
                        UrlFragment.parse("/unknown!")
                    );
                }
            },
            "TestSpreadsheetDialogComponentLifecycle\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Title456\n" +
                "    id=id123 includeClose=true CLOSED\n" +
                "      SpreadsheetTextBox\n" +
                "        [NOT onGiveFocus]\n" +
                "      SpreadsheetTextBox\n" +
                "        [NOT refreshed]\n"
        );

        // dialog never opened or closed
        this.checkEquals(
            0,
            table.dialogReset
        );
    }

    @Test
    public void testMatchedOnHistoryTokenChangeAndCheck() {
        final TestSpreadsheetDialogComponentLifecycle table = new TestSpreadsheetDialogComponentLifecycle();
        this.onHistoryTokenChangeAndCheck(
            table,
            new FakeAppContext() {
                @Override
                public HistoryToken historyToken() {
                    return HISTORY_TOKEN;
                }
            },
            "TestSpreadsheetDialogComponentLifecycle\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Title456\n" +
                "    id=id123 includeClose=true\n" +
                "      SpreadsheetTextBox\n" +
                "        [onGiveFocus]\n" +
                "      SpreadsheetTextBox\n" +
                "        [refreshed]\n"
        );

        // dialog should be opened but never closed.
        this.checkEquals(
            1,
            table.dialogReset
        );
    }

    @Test
    public void testMatchedOnHistoryTokenChangeAndCheckOpenAndClose() {
        final TestSpreadsheetDialogComponentLifecycle table = new TestSpreadsheetDialogComponentLifecycle();

        this.onHistoryTokenChangeAndCheck(
            table,
            new FakeAppContext() {
                @Override
                public HistoryToken historyToken() {
                    return HISTORY_TOKEN;
                }
            },
            "TestSpreadsheetDialogComponentLifecycle\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Title456\n" +
                "    id=id123 includeClose=true\n" +
                "      SpreadsheetTextBox\n" +
                "        [onGiveFocus]\n" +
                "      SpreadsheetTextBox\n" +
                "        [refreshed]\n"
        );

        // dialog should be opened but never closed.
        this.checkEquals(
            1,
            table.dialogReset
        );

        this.onHistoryTokenChangeAndCheck(
            table,
            new FakeAppContext() {
                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.parseString("/unknown");
                }
            },
            "TestSpreadsheetDialogComponentLifecycle\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Title456\n" +
                "    id=id123 includeClose=true CLOSED\n" +
                "      SpreadsheetTextBox\n" +
                "        [onGiveFocus]\n" +
                "      SpreadsheetTextBox\n" +
                "        [refreshed]\n"
        );

        // dialog should now be closed
        this.checkEquals(
            2,
            table.dialogReset
        );
    }

    @Override
    public TestSpreadsheetDialogComponentLifecycle createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return new TestSpreadsheetDialogComponentLifecycle();
    }

    final static class TestSpreadsheetDialogComponentLifecycle implements SpreadsheetDialogComponentLifecycle,
        TreePrintable {

        TestSpreadsheetDialogComponentLifecycle() {
            this.onGiveFocus = SpreadsheetTextBox.empty()
                .setValue(
                    Optional.of("NOT onGiveFocus")
                );
            this.refreshed = SpreadsheetTextBox.empty()
                .setValue(
                    Optional.of("NOT refreshed")
                );
            this.dialog = SpreadsheetDialogComponent.with(
                    null,
                    null,
                    "id123",
                    "Title456",
                    SpreadsheetDialogComponent.INCLUDE_CLOSE,
                    SpreadsheetDialogComponentContexts.fake()
                ).appendChild(this.onGiveFocus)
                .appendChild(this.refreshed);
        }

        @Override
        public void dialogReset() {
            this.dialogReset++;
        }

        int dialogReset;

        @Override
        public void openGiveFocus(final RefreshContext context) {
            this.onGiveFocus.setValue(
                Optional.of("onGiveFocus")
            );
        }

        private final SpreadsheetTextBox onGiveFocus;

        @Override
        public boolean shouldIgnore(final HistoryToken token) {
            return false;
        }

        @Override
        public boolean isMatch(final HistoryToken token) {
            return token.equals(HISTORY_TOKEN);
        }

        @Override
        public void refresh(final RefreshContext context) {
            this.refreshed.setValue(
                Optional.of("refreshed")
            );
        }

        private final SpreadsheetTextBox refreshed;

        @Override
        public SpreadsheetDialogComponent dialog() {
            return this.dialog;
        }

        private final SpreadsheetDialogComponent dialog;

        @Override
        public String idPrefix() {
            return "id123-";
        }
    }

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<TestSpreadsheetDialogComponentLifecycle> type() {
        return TestSpreadsheetDialogComponentLifecycle.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
