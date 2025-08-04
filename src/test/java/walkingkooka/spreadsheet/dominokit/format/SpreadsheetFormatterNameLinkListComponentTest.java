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
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
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
                "            \"Collection\" [#/1/SpreadsheetName123/cell/A1/formatter/save/collection] id=ID123-1-Link\n" +
                "            \"Date Format Pattern\" [#/1/SpreadsheetName123/cell/A1/formatter/save/date-format-pattern] id=ID123-2-Link\n" +
                "            \"Date Time Format Pattern\" [#/1/SpreadsheetName123/cell/A1/formatter/save/date-time-format-pattern] id=ID123-3-Link\n" +
                "            \"Default Text\" [#/1/SpreadsheetName123/cell/A1/formatter/save/default-text] id=ID123-4-Link\n" +
                "            \"Expression\" [#/1/SpreadsheetName123/cell/A1/formatter/save/expression] id=ID123-5-Link\n" +
                "            \"General\" [#/1/SpreadsheetName123/cell/A1/formatter/save/general] id=ID123-6-Link\n" +
                "            \"Number Format Pattern\" [#/1/SpreadsheetName123/cell/A1/formatter/save/number-format-pattern] id=ID123-7-Link\n" +
                "            \"Spreadsheet Pattern Collection\" [#/1/SpreadsheetName123/cell/A1/formatter/save/spreadsheet-pattern-collection] id=ID123-8-Link\n" +
                "            \"Text Format Pattern\" [#/1/SpreadsheetName123/cell/A1/formatter/save/text-format-pattern] id=ID123-9-Link\n" +
                "            \"Time Format Pattern\" [#/1/SpreadsheetName123/cell/A1/formatter/save/time-format-pattern] id=ID123-10-Link\n"
        );
    }

    @Test
    public void testRefreshWithSelectedFormatterName() {
        this.refreshAndCheck(
            Optional.of(
                SpreadsheetFormatterName.DATE_FORMAT_PATTERN
            ),
            "SpreadsheetFormatterNameLinkListComponent\n" +
                "  CardLinkListComponent\n" +
                "    CardComponent\n" +
                "      Card\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Automatic\" [#/1/SpreadsheetName123/cell/A1/formatter/save/automatic] id=ID123-0-Link\n" +
                "            \"Collection\" [#/1/SpreadsheetName123/cell/A1/formatter/save/collection] id=ID123-1-Link\n" +
                "            \"Date Format Pattern\" DISABLED id=ID123-2-Link\n" +
                "            \"Date Time Format Pattern\" [#/1/SpreadsheetName123/cell/A1/formatter/save/date-time-format-pattern] id=ID123-3-Link\n" +
                "            \"Default Text\" [#/1/SpreadsheetName123/cell/A1/formatter/save/default-text] id=ID123-4-Link\n" +
                "            \"Expression\" [#/1/SpreadsheetName123/cell/A1/formatter/save/expression] id=ID123-5-Link\n" +
                "            \"General\" [#/1/SpreadsheetName123/cell/A1/formatter/save/general] id=ID123-6-Link\n" +
                "            \"Number Format Pattern\" [#/1/SpreadsheetName123/cell/A1/formatter/save/number-format-pattern] id=ID123-7-Link\n" +
                "            \"Spreadsheet Pattern Collection\" [#/1/SpreadsheetName123/cell/A1/formatter/save/spreadsheet-pattern-collection] id=ID123-8-Link\n" +
                "            \"Text Format Pattern\" [#/1/SpreadsheetName123/cell/A1/formatter/save/text-format-pattern] id=ID123-9-Link\n" +
                "            \"Time Format Pattern\" [#/1/SpreadsheetName123/cell/A1/formatter/save/time-format-pattern] id=ID123-10-Link\n"
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
