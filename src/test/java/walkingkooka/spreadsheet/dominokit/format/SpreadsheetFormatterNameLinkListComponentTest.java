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

package walkingkooka.spreadsheet.dominokit.format;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfo;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Set;

public class SpreadsheetFormatterNameLinkListComponentTest implements ClassTesting<SpreadsheetFormatterNameLinkListComponent>,
        TreePrintableTesting,
        SpreadsheetMetadataTesting {

    private final static String ID = "ID123-";

    @Test
    public void refresh() {
        final SpreadsheetFormatterNameLinkListComponent formatters = SpreadsheetFormatterNameLinkListComponent.empty(ID);
        formatters.refresh(
                new FakeSpreadsheetFormatterNameLinkListComponentContext() {
                    @Override
                    public Set<SpreadsheetFormatterInfo> spreadsheetFormatterInfos() {
                        return SPREADSHEET_FORMATTER_PROVIDER.spreadsheetFormatterInfos();
                    }

                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.cellFormatterSelect(
                                SpreadsheetId.with(1),
                                SpreadsheetName.with("SpreadsheetName123"),
                                SpreadsheetSelection.A1.setDefaultAnchor(),
                                SpreadsheetPatternKind.DATE_FORMAT_PATTERN
                        );
                    }
                }
        );

        this.treePrintAndCheck(
                formatters,
                "SpreadsheetFormatterNameLinkListComponent\n" +
                        "  SpreadsheetLinkListComponent\n" +
                        "    SpreadsheetCard\n" +
                        "      Card\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"Automatic\" [#/1/SpreadsheetName123/cell/A1/formatter/date/save/automatic] id=ID123-0-Link\n" +
                        "            \"Collection\" [#/1/SpreadsheetName123/cell/A1/formatter/date/save/collection] id=ID123-1-Link\n" +
                        "            \"Date Format Pattern\" [#/1/SpreadsheetName123/cell/A1/formatter/date/save/date-format-pattern] id=ID123-2-Link\n" +
                        "            \"Date Time Format Pattern\" [#/1/SpreadsheetName123/cell/A1/formatter/date/save/date-time-format-pattern] id=ID123-3-Link\n" +
                        "            \"General\" [#/1/SpreadsheetName123/cell/A1/formatter/date/save/general] id=ID123-4-Link\n" +
                        "            \"Number Format Pattern\" [#/1/SpreadsheetName123/cell/A1/formatter/date/save/number-format-pattern] id=ID123-5-Link\n" +
                        "            \"Spreadsheet Pattern Collection\" [#/1/SpreadsheetName123/cell/A1/formatter/date/save/spreadsheet-pattern-collection] id=ID123-6-Link\n" +
                        "            \"Text Format Pattern\" [#/1/SpreadsheetName123/cell/A1/formatter/date/save/text-format-pattern] id=ID123-7-Link\n" +
                        "            \"Time Format Pattern\" [#/1/SpreadsheetName123/cell/A1/formatter/date/save/time-format-pattern] id=ID123-8-Link\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFormatterNameLinkListComponent> type() {
        return SpreadsheetFormatterNameLinkListComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
