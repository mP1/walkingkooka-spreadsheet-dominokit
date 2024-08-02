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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfo;
import walkingkooka.spreadsheet.parser.SpreadsheetParserName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;
import java.util.Set;

public class SpreadsheetParserNameLinkListComponentTest implements ClassTesting<SpreadsheetParserNameLinkListComponent>,
        TreePrintableTesting,
        SpreadsheetMetadataTesting {

    private final static String ID = "ID123-";

    @Test
    public void testRefresh() {
        this.refreshAndCheck(
                Optional.empty(),
                "SpreadsheetParserNameLinkListComponent\n" +
                        "  SpreadsheetLinkListComponent\n" +
                        "    SpreadsheetCard\n" +
                        "      Card\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"Date Parse Pattern\" [#/1/SpreadsheetName123/cell/A1/parser/save/date-parse-pattern] id=ID123-0-Link\n" +
                        "            \"Date Time Parse Pattern\" [#/1/SpreadsheetName123/cell/A1/parser/save/date-time-parse-pattern] id=ID123-1-Link\n" +
                        "            \"Number Parse Pattern\" [#/1/SpreadsheetName123/cell/A1/parser/save/number-parse-pattern] id=ID123-2-Link\n" +
                        "            \"Time Parse Pattern\" [#/1/SpreadsheetName123/cell/A1/parser/save/time-parse-pattern] id=ID123-3-Link\n"
        );
    }

    @Test
    public void testRefreshWithSelectedParserName() {
        this.refreshAndCheck(
                Optional.of(
                        SpreadsheetParserName.DATE_PARSER_PATTERN
                ),
                "SpreadsheetParserNameLinkListComponent\n" +
                        "  SpreadsheetLinkListComponent\n" +
                        "    SpreadsheetCard\n" +
                        "      Card\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"Date Parse Pattern\" DISABLED [#/1/SpreadsheetName123/cell/A1/parser/save/date-parse-pattern] id=ID123-0-Link\n" +
                        "            \"Date Time Parse Pattern\" [#/1/SpreadsheetName123/cell/A1/parser/save/date-time-parse-pattern] id=ID123-1-Link\n" +
                        "            \"Number Parse Pattern\" [#/1/SpreadsheetName123/cell/A1/parser/save/number-parse-pattern] id=ID123-2-Link\n" +
                        "            \"Time Parse Pattern\" [#/1/SpreadsheetName123/cell/A1/parser/save/time-parse-pattern] id=ID123-3-Link\n"
        );
    }

    private void refreshAndCheck(final Optional<SpreadsheetParserName> spreadsheetParserName,
                                 final String expected) {
        final SpreadsheetParserNameLinkListComponent parsers = SpreadsheetParserNameLinkListComponent.empty(ID);
        parsers.refresh(
                new FakeSpreadsheetParserNameLinkListComponentContext() {
                    @Override
                    public Set<SpreadsheetParserInfo> spreadsheetParserInfos() {
                        return SPREADSHEET_PARSER_PROVIDER.spreadsheetParserInfos();
                    }

                    public Optional<SpreadsheetParserName> parserName() {
                        return spreadsheetParserName;
                    }

                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.cellParserSelect(
                                SpreadsheetId.with(1),
                                SpreadsheetName.with("SpreadsheetName123"),
                                SpreadsheetSelection.A1.setDefaultAnchor()
                        );
                    }
                }
        );

        this.treePrintAndCheck(
                parsers,
                expected
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetParserNameLinkListComponent> type() {
        return SpreadsheetParserNameLinkListComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
