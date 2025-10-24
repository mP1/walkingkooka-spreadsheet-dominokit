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

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public class SpreadsheetFormatterNameLinkListComponentTest implements HtmlComponentTesting<SpreadsheetFormatterNameLinkListComponent, HTMLDivElement>,
    SpreadsheetMetadataTesting {

    private final static String ID = "ID123-";

    @Test
    public void testRefresh() {
        this.refreshAndCheck(
            Optional.empty(),
            "SpreadsheetFormatterNameLinkListComponent\n" +
                "  CardLinkListComponent\n" +
                "    CardComponent\n" +
                "      Card\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Automatic\" [#/1/SpreadsheetName123/cell/A1/formatter/save/automatic] id=ID123-0-Link\n" +
                "            \"Badge Error\" [#/1/SpreadsheetName123/cell/A1/formatter/save/badge-error] id=ID123-1-Link\n" +
                "            \"Collection\" [#/1/SpreadsheetName123/cell/A1/formatter/save/collection] id=ID123-2-Link\n" +
                "            \"Currency\" [#/1/SpreadsheetName123/cell/A1/formatter/save/currency] id=ID123-3-Link\n" +
                "            \"Date\" [#/1/SpreadsheetName123/cell/A1/formatter/save/date] id=ID123-4-Link\n" +
                "            \"Date Time\" [#/1/SpreadsheetName123/cell/A1/formatter/save/date-time] id=ID123-5-Link\n" +
                "            \"Default Text\" [#/1/SpreadsheetName123/cell/A1/formatter/save/default-text] id=ID123-6-Link\n" +
                "            \"Expression\" [#/1/SpreadsheetName123/cell/A1/formatter/save/expression] id=ID123-7-Link\n" +
                "            \"Full Date\" [#/1/SpreadsheetName123/cell/A1/formatter/save/full-date] id=ID123-8-Link\n" +
                "            \"General\" [#/1/SpreadsheetName123/cell/A1/formatter/save/general] id=ID123-9-Link\n" +
                "            \"Long Date\" [#/1/SpreadsheetName123/cell/A1/formatter/save/long-date] id=ID123-10-Link\n" +
                "            \"Medium Date\" [#/1/SpreadsheetName123/cell/A1/formatter/save/medium-date] id=ID123-11-Link\n" +
                "            \"Medium Date Time\" [#/1/SpreadsheetName123/cell/A1/formatter/save/medium-date-time] id=ID123-12-Link\n" +
                "            \"Number\" [#/1/SpreadsheetName123/cell/A1/formatter/save/number] id=ID123-13-Link\n" +
                "            \"Percent\" [#/1/SpreadsheetName123/cell/A1/formatter/save/percent] id=ID123-14-Link\n" +
                "            \"Scientific\" [#/1/SpreadsheetName123/cell/A1/formatter/save/scientific] id=ID123-15-Link\n" +
                "            \"Short Date\" [#/1/SpreadsheetName123/cell/A1/formatter/save/short-date] id=ID123-16-Link\n" +
                "            \"Short Date Time\" [#/1/SpreadsheetName123/cell/A1/formatter/save/short-date-time] id=ID123-17-Link\n" +
                "            \"Spreadsheet Pattern Collection\" [#/1/SpreadsheetName123/cell/A1/formatter/save/spreadsheet-pattern-collection] id=ID123-18-Link\n" +
                "            \"Text\" [#/1/SpreadsheetName123/cell/A1/formatter/save/text] id=ID123-19-Link\n" +
                "            \"Time\" [#/1/SpreadsheetName123/cell/A1/formatter/save/time] id=ID123-20-Link\n"
        );
    }

    @Test
    public void testRefreshWithSelectedFormatterName() {
        this.refreshAndCheck(
            Optional.of(
                SpreadsheetFormatterName.DATE
            ),
            "SpreadsheetFormatterNameLinkListComponent\n" +
                "  CardLinkListComponent\n" +
                "    CardComponent\n" +
                "      Card\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Automatic\" [#/1/SpreadsheetName123/cell/A1/formatter/save/automatic] id=ID123-0-Link\n" +
                "            \"Badge Error\" [#/1/SpreadsheetName123/cell/A1/formatter/save/badge-error] id=ID123-1-Link\n" +
                "            \"Collection\" [#/1/SpreadsheetName123/cell/A1/formatter/save/collection] id=ID123-2-Link\n" +
                "            \"Currency\" [#/1/SpreadsheetName123/cell/A1/formatter/save/currency] id=ID123-3-Link\n" +
                "            \"Date\" DISABLED id=ID123-4-Link\n" +
                "            \"Date Time\" [#/1/SpreadsheetName123/cell/A1/formatter/save/date-time] id=ID123-5-Link\n" +
                "            \"Default Text\" [#/1/SpreadsheetName123/cell/A1/formatter/save/default-text] id=ID123-6-Link\n" +
                "            \"Expression\" [#/1/SpreadsheetName123/cell/A1/formatter/save/expression] id=ID123-7-Link\n" +
                "            \"Full Date\" [#/1/SpreadsheetName123/cell/A1/formatter/save/full-date] id=ID123-8-Link\n" +
                "            \"General\" [#/1/SpreadsheetName123/cell/A1/formatter/save/general] id=ID123-9-Link\n" +
                "            \"Long Date\" [#/1/SpreadsheetName123/cell/A1/formatter/save/long-date] id=ID123-10-Link\n" +
                "            \"Medium Date\" [#/1/SpreadsheetName123/cell/A1/formatter/save/medium-date] id=ID123-11-Link\n" +
                "            \"Medium Date Time\" [#/1/SpreadsheetName123/cell/A1/formatter/save/medium-date-time] id=ID123-12-Link\n" +
                "            \"Number\" [#/1/SpreadsheetName123/cell/A1/formatter/save/number] id=ID123-13-Link\n" +
                "            \"Percent\" [#/1/SpreadsheetName123/cell/A1/formatter/save/percent] id=ID123-14-Link\n" +
                "            \"Scientific\" [#/1/SpreadsheetName123/cell/A1/formatter/save/scientific] id=ID123-15-Link\n" +
                "            \"Short Date\" [#/1/SpreadsheetName123/cell/A1/formatter/save/short-date] id=ID123-16-Link\n" +
                "            \"Short Date Time\" [#/1/SpreadsheetName123/cell/A1/formatter/save/short-date-time] id=ID123-17-Link\n" +
                "            \"Spreadsheet Pattern Collection\" [#/1/SpreadsheetName123/cell/A1/formatter/save/spreadsheet-pattern-collection] id=ID123-18-Link\n" +
                "            \"Text\" [#/1/SpreadsheetName123/cell/A1/formatter/save/text] id=ID123-19-Link\n" +
                "            \"Time\" [#/1/SpreadsheetName123/cell/A1/formatter/save/time] id=ID123-20-Link\n"
        );
    }

    private void refreshAndCheck(final Optional<SpreadsheetFormatterName> spreadsheetFormatterName,
                                 final String expected) {
        final SpreadsheetFormatterNameLinkListComponent formatters = SpreadsheetFormatterNameLinkListComponent.empty(ID);
        formatters.refresh(
            new FakeSpreadsheetFormatterNameLinkListComponentContext() {
                @Override
                public SpreadsheetFormatterInfoSet spreadsheetFormatterInfos() {
                    return SPREADSHEET_FORMATTER_PROVIDER.spreadsheetFormatterInfos();
                }

                @Override
                public Optional<SpreadsheetFormatterName> formatterName() {
                    return spreadsheetFormatterName;
                }

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.cellFormatterSelect(
                        SpreadsheetId.with(1),
                        SpreadsheetName.with("SpreadsheetName123"),
                        SpreadsheetSelection.A1.setDefaultAnchor()
                    );
                }
            }
        );

        this.treePrintAndCheck(
            formatters,
            expected
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
