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
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTestingTest.TestDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.text.TextBoxComponent;
import walkingkooka.text.printer.TreePrintable;

import java.util.Optional;

public final class DialogComponentLifecycleTestingTest implements DialogComponentLifecycleTesting<TestDialogComponentLifecycle> {

    @Test
    public void testAnchor() {
        this.treePrintAndCheck(
            new TestDialogComponentLifecycle().anchor("Hello"),
            "\"Hello\" DISABLED id=id123-hello-Link"
        );
    }

    @Test
    public void testAnchorWithMultiWordText() {
        this.treePrintAndCheck(
            new TestDialogComponentLifecycle().anchor("Hello goodbye"),
            "\"Hello goodbye\" DISABLED id=id123-hello-goodbye-Link"
        );
    }

    private final static HistoryToken HISTORY_TOKEN = HistoryToken.spreadsheetCreate();

    @Test
    public void testNoMatchedOnHistoryTokenChangeAndCheck() {
        final TestDialogComponentLifecycle table = new TestDialogComponentLifecycle();
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
            "TestDialogComponentLifecycle\n" +
                "  DialogComponent\n" +
                "    id=id123 includeClose=true CLOSED\n" +
                "      TextBoxComponent\n" +
                "        [NOT onGiveFocus]\n" +
                "      TextBoxComponent\n" +
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
        final TestDialogComponentLifecycle table = new TestDialogComponentLifecycle();
        this.onHistoryTokenChangeAndCheck(
            table,
            new FakeAppContext() {
                @Override
                public HistoryToken historyToken() {
                    return HISTORY_TOKEN;
                }
            },
            "TestDialogComponentLifecycle\n" +
                "  DialogComponent\n" +
                "    Title456\n" +
                "    id=id123 includeClose=true\n" +
                "      TextBoxComponent\n" +
                "        [onGiveFocus]\n" +
                "      TextBoxComponent\n" +
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
        final TestDialogComponentLifecycle table = new TestDialogComponentLifecycle();

        this.onHistoryTokenChangeAndCheck(
            table,
            new FakeAppContext() {
                @Override
                public HistoryToken historyToken() {
                    return HISTORY_TOKEN;
                }
            },
            "TestDialogComponentLifecycle\n" +
                "  DialogComponent\n" +
                "    Title456\n" +
                "    id=id123 includeClose=true\n" +
                "      TextBoxComponent\n" +
                "        [onGiveFocus]\n" +
                "      TextBoxComponent\n" +
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
            "TestDialogComponentLifecycle\n" +
                "  DialogComponent\n" +
                "    id=id123 includeClose=true CLOSED\n" +
                "      TextBoxComponent\n" +
                "        [onGiveFocus]\n" +
                "      TextBoxComponent\n" +
                "        [refreshed]\n"
        );

        // dialog should now be closed
        this.checkEquals(
            2,
            table.dialogReset
        );
    }

    @Override
    public TestDialogComponentLifecycle createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return new TestDialogComponentLifecycle();
    }

    final static class TestDialogComponentLifecycle implements DialogComponentLifecycle,
        TreePrintable {

        TestDialogComponentLifecycle() {
            this.onGiveFocus = TextBoxComponent.empty()
                .setValue(
                    Optional.of("NOT onGiveFocus")
                );
            this.refreshed = TextBoxComponent.empty()
                .setValue(
                    Optional.of("NOT refreshed")
                );
            this.dialog = DialogComponent.with(
                    null,
                    null,
                    "id123",
                    DialogComponent.INCLUDE_CLOSE,
                    DialogComponentContexts.fake()
                ).appendChild(this.onGiveFocus)
                .appendChild(this.refreshed);
        }

        @Override
        public void dialogReset() {
            this.dialogReset++;
            this.dialog.setTitle("");
        }

        int dialogReset;

        @Override
        public void openGiveFocus(final RefreshContext context) {
            this.onGiveFocus.setValue(
                Optional.of("onGiveFocus")
            );
        }

        private final TextBoxComponent onGiveFocus;

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
            this.dialog.setTitle("Title456");
            this.refreshed.setValue(
                Optional.of("refreshed")
            );
        }

        private final TextBoxComponent refreshed;

        @Override
        public DialogComponent dialog() {
            return this.dialog;
        }

        private final DialogComponent dialog;

        @Override
        public String idPrefix() {
            return "id123-";
        }

        @Override
        public boolean shouldLogLifecycleChanges() {
            return false;
        }
    }

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<TestDialogComponentLifecycle> type() {
        return TestDialogComponentLifecycle.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
