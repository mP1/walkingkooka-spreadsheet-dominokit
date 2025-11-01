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

package walkingkooka.spreadsheet.dominokit.parser;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserInfoSet;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public class SpreadsheetParserNameLinkListComponentTest implements HtmlComponentTesting<SpreadsheetParserNameLinkListComponent, HTMLDivElement>,
    SpreadsheetMetadataTesting {

    private final static String ID = "ID123-";

    @Test
    public void testRefresh() {
        this.refreshAndCheck(
            Optional.empty(),
            "SpreadsheetParserNameLinkListComponent\n" +
                "  CardLinkListComponent\n" +
                "    CardComponent\n" +
                "      Card\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Date\" [#/1/SpreadsheetName123/cell/A1/parser/save/date] id=ID123-0-Link\n" +
                "            \"Date Time\" [#/1/SpreadsheetName123/cell/A1/parser/save/date-time] id=ID123-1-Link\n" +
                "            \"General\" [#/1/SpreadsheetName123/cell/A1/parser/save/general] id=ID123-2-Link\n" +
                "            \"Number\" [#/1/SpreadsheetName123/cell/A1/parser/save/number] id=ID123-3-Link\n" +
                "            \"Time\" [#/1/SpreadsheetName123/cell/A1/parser/save/time] id=ID123-4-Link\n"
        );
    }

    @Test
    public void testRefreshWithSelectedParserName() {
        this.refreshAndCheck(
            Optional.of(
                SpreadsheetParserName.DATE
            ),
            "SpreadsheetParserNameLinkListComponent\n" +
                "  CardLinkListComponent\n" +
                "    CardComponent\n" +
                "      Card\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Date\" DISABLED id=ID123-0-Link\n" +
                "            \"Date Time\" [#/1/SpreadsheetName123/cell/A1/parser/save/date-time] id=ID123-1-Link\n" +
                "            \"General\" [#/1/SpreadsheetName123/cell/A1/parser/save/general] id=ID123-2-Link\n" +
                "            \"Number\" [#/1/SpreadsheetName123/cell/A1/parser/save/number] id=ID123-3-Link\n" +
                "            \"Time\" [#/1/SpreadsheetName123/cell/A1/parser/save/time] id=ID123-4-Link\n"
        );
    }

    private void refreshAndCheck(final Optional<SpreadsheetParserName> spreadsheetParserName,
                                 final String expected) {
        final SpreadsheetParserNameLinkListComponent parsers = SpreadsheetParserNameLinkListComponent.empty(ID);
        parsers.refresh(
            new FakeSpreadsheetParserNameLinkListComponentContext() {
                @Override
                public SpreadsheetParserInfoSet spreadsheetParserInfos() {
                    return SPREADSHEET_PARSER_PROVIDER.spreadsheetParserInfos();
                }

                @Override public Optional<SpreadsheetParserName> parserName() {
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
