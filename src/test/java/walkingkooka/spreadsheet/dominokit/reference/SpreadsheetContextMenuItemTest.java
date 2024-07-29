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

package walkingkooka.spreadsheet.dominokit.reference;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;

public class SpreadsheetContextMenuItemTest implements ClassTesting<SpreadsheetContextMenuItem>,
        TreePrintableTesting,
        ToStringTesting<SpreadsheetContextMenuItem> {

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
                SpreadsheetContextMenuItem.with("id1-MenuItem", "text-123"),
                "\"text-123\" id=id1-MenuItem\n"
        );
    }

    @Test
    public void testTreePrintBadge() {
        this.treePrintAndCheck(
                SpreadsheetContextMenuItem.with("id1-MenuItem", "text-123")
                        .badge(
                                Optional.of("Badge-123")
                        ),
                "\"text-123\" [Badge-123] id=id1-MenuItem\n"
        );
    }

    @Test
    public void testTreePrintChecked() {
        this.treePrintAndCheck(
                SpreadsheetContextMenuItem.with("id1-MenuItem", "text-123")
                        .checked(true),
                "\"text-123\" CHECKED id=id1-MenuItem\n"
        );
    }

    @Test
    public void testTreePrintDisabledHistoryToken() {
        this.treePrintAndCheck(
                SpreadsheetContextMenuItem.with("id1-MenuItem", "text-123")
                        .enabled(false)
                        .historyToken(
                                Optional.of(
                                        HistoryToken.parseString("/1/Spreadsheet-name-2/cell/A1:B2/cell")
                                )
                        ),
                "\"text-123\" [/1/Spreadsheet-name-2/cell/A1:B2/bottom-right] DISABLED id=id1-MenuItem\n"
        );
    }

    @Test
    public void testTreePrintHistoryToken() {
        this.treePrintAndCheck(
                SpreadsheetContextMenuItem.with("id1-MenuItem", "text-123")
                        .historyToken(
                                Optional.of(
                                        HistoryToken.parseString("/1/Spreadsheet-name-2/cell/A1:B2/cell")
                                )
                        ),
                "\"text-123\" [/1/Spreadsheet-name-2/cell/A1:B2/bottom-right] id=id1-MenuItem\n"
        );
    }

    @Test
    public void testTreePrintIcon() {
        this.treePrintAndCheck(
                SpreadsheetContextMenuItem.with("id1-MenuItem", "text-123")
                        .icon(
                                Optional.of(
                                        SpreadsheetIcons.alignLeft()
                                )
                        ),
                "(mdi-format-align-left) \"text-123\" id=id1-MenuItem\n"
        );
    }

    @Test
    public void testTreePrintEverything() {
        this.treePrintAndCheck(
                SpreadsheetContextMenuItem.with("id1-MenuItem", "text-123")
                        .badge(
                                Optional.of("123")
                        ).checked(true)
                        .historyToken(
                                Optional.of(
                                        HistoryToken.parseString("/1/Spreadsheet-name-2/cell/A1:B2/cell")
                                )
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.alignLeft()
                                )
                        ).key(
                                "Key1"
                        ),
                "(mdi-format-align-left) \"text-123\" [/1/Spreadsheet-name-2/cell/A1:B2/bottom-right] CHECKED [123] id=id1-MenuItem key=Key1 \n"
        );
    }

    // ToString.........................................................................................................

    @Test
    public void testToStringWithBadge() {
        this.toStringAndCheck(
                SpreadsheetContextMenuItem.with("id1-MenuItem", "text-123")
                        .badge(
                                Optional.of(
                                        "Badge-text-123"
                                )
                        ),
                "id1-MenuItem \"text-123\" Badge-text-123"
        );
    }

    @Test
    public void testToStringWithIcons() {
        this.toStringAndCheck(
                SpreadsheetContextMenuItem.with("id1-MenuItem", "text2")
                        .icon(
                                Optional.of(
                                        SpreadsheetIcons.checked()
                                )
                        ),
                "id1-MenuItem \"text2\" mdi-check"
        );
    }

    @Test
    public void testToStringWithDisabledNoHistory() {
        this.toStringAndCheck(
                SpreadsheetContextMenuItem.with("id4-MenuItem", "text4")
                        .enabled(false),
                "id4-MenuItem \"text4\" disabled=true"
        );
    }

    @Test
    public void testToStringWithDisabledAndHistory() {
        this.toStringAndCheck(
                SpreadsheetContextMenuItem.with("id5-MenuItem", "text5")
                        .enabled(false)
                        .historyToken(
                                Optional.of(
                                        HistoryToken.cellDelete(
                                                SpreadsheetId.with(1),
                                                SpreadsheetName.with("SpreadsheetName2"),
                                                SpreadsheetSelection.A1.setDefaultAnchor()
                                        )
                                )
                        ),
                "id5-MenuItem \"text5\" SpreadsheetCellDeleteHistoryToken \"/1/SpreadsheetName2/cell/A1/delete\" disabled=true"
        );
    }

    @Test
    public void testToStringWithKey() {
        this.toStringAndCheck(
                SpreadsheetContextMenuItem.with("id3-MenuItem", "text3")
                        .key("A"),
                "id3-MenuItem \"A\" \"text3\""
        );
    }

    @Override
    public Class<SpreadsheetContextMenuItem> type() {
        return SpreadsheetContextMenuItem.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
