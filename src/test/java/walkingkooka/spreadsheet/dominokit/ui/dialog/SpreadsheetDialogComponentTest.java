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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContexts;
import walkingkooka.spreadsheet.dominokit.ui.textbox.SpreadsheetTextBox;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;

public final class SpreadsheetDialogComponentTest implements TreePrintableTesting,
        ClassTesting<SpreadsheetDialogComponent> {

    @Test
    public void testTitleCloseableChildren() {
        final SpreadsheetDialogComponent dialog = SpreadsheetDialogComponent.with(
                "id123",
                "Title456",
                true, // includeClose,
                HistoryTokenContexts.fake()
        );
        dialog.appendChild(
                SpreadsheetTextBox.empty()
                        .setId("TextBoxId111")
                        .setValue(
                                Optional.of(
                                        "Value111"
                                )
                        )
        );
        dialog.appendChild(
                SpreadsheetTextBox.empty()
                        .setId("TextBoxId222")
                        .setValue(
                                Optional.of(
                                        "Value222"
                                )
                        )
        );

        this.treePrintAndCheck(
                dialog,
                "SpreadsheetDialogComponent\n" +
                        "  Title456\n" +
                        "  id=id123 includeClose=true\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [Value111] id=TextBoxId111\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [Value222] id=TextBoxId222\n"
        );
    }

    @Override
    public Class<SpreadsheetDialogComponent> type() {
        return SpreadsheetDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
