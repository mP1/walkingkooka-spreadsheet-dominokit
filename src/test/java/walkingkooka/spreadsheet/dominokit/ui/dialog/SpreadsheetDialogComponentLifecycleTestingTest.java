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

package walkingkooka.spreadsheet.dominokit.ui.dialog;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContexts;
import walkingkooka.spreadsheet.dominokit.ui.textbox.SpreadsheetTextBox;
import walkingkooka.text.printer.TreePrintable;

import java.util.Optional;

public final class SpreadsheetDialogComponentLifecycleTestingTest implements SpreadsheetDialogComponentLifecycleTesting<SpreadsheetDialogComponentLifecycleTestingTest.TestSpreadsheetDialogComponentLifecycle> {

    @Test
    public void testOpenAndRefreshAndCheck() {
        final TestSpreadsheetDialogComponentLifecycle table = new TestSpreadsheetDialogComponentLifecycle();
        this.openAndRefreshAndCheck(
                table,
                AppContexts.fake(),
                "TestSpreadsheetDialogComponentLifecycle\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Title456\n" +
                        "    id=id123 includeClose=true\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [refreshed]\n"
        );
    }

    final class TestSpreadsheetDialogComponentLifecycle implements SpreadsheetDialogComponentLifecycle,
            TreePrintable {

        TestSpreadsheetDialogComponentLifecycle() {
            this.textBox = SpreadsheetTextBox.empty();
            this.dialog = SpreadsheetDialogComponent.with(
                    "id123",
                    "Title456",
                    true, // includeClose
                    HistoryTokenContexts.fake()
            );
            this.dialog.appendChild(this.textBox);
        }

        @Override
        public void openGiveFocus(AppContext context) {
        }

        @Override
        public boolean shouldIgnore(final HistoryToken token) {
            return false;
        }

        @Override
        public boolean isMatch(final HistoryToken token) {
            return true;
        }

        @Override
        public void refresh(final AppContext context) {
            this.textBox.setValue(
                    Optional.of("refreshed")
            );
        }

        private SpreadsheetTextBox textBox;

        @Override
        public SpreadsheetDialogComponent dialog() {
            return this.dialog;
        }

        private SpreadsheetDialogComponent dialog;

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
