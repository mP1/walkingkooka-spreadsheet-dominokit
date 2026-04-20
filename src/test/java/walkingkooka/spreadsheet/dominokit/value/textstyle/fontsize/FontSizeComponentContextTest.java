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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontsize;

import org.dominokit.domino.ui.menu.MenuItem;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenMenuItem;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.tree.text.FontSize;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class FontSizeComponentContextTest implements ClassTesting<FontSizeComponentContext> {

    private final static SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1);

    private final static SpreadsheetName SPREADSHEET_NAME = SpreadsheetName.with("SpreadsheetName111");

    @Test
    public void testHistoryTokenMenuItem() {
        final FontSize fontSize = FontSize.with(10);

        this.historyTokenMenuItemAndCheck(
            "TestIdPrefix123",
            fontSize,
            new FakeHistoryContext() {
                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.metadataSelect(
                        SPREADSHEET_ID,
                        SPREADSHEET_NAME
                    );
                }
            },
            new HistoryTokenMenuItem<>(
                HistoryTokenAnchorComponent.empty()
                    .setHistoryToken(
                        Optional.of(
                            HistoryToken.metadataPropertyStyleSave(
                                SPREADSHEET_ID,
                                SPREADSHEET_NAME,
                                TextStylePropertyName.FONT_SIZE,
                                Optional.of(fontSize)
                            )
                        )
                    ).setId("TestIdPrefix123-suggestion-10-Option")
                    .setTextContent("10")
            )
        );
    }

    private void historyTokenMenuItemAndCheck(final String id,
                                              final FontSize value,
                                              final HistoryContext historyContext,
                                              final MenuItem<FontSize> expected) {
        this.checkEquals(
            expected,
            new FakeFontSizeComponentContext()
                .historyTokenMenuItem(
                    id,
                    value,
                    historyContext
                ),
            () -> "id=" + id + " value=" + value + " historyContext=" + historyContext
        );
    }

    // class............................................................................................................

    @Override
    public Class<FontSizeComponentContext> type() {
        return FontSizeComponentContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
