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

package walkingkooka.spreadsheet.dominokit.meta;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.text.printer.TreePrintableTesting;

import java.time.LocalDate;
import java.util.Optional;

public final class SpreadsheetMetadataPanelComponentItemDateTimeOffsetTest implements TreePrintableTesting,
    SpreadsheetMetadataTesting,
    ClassTesting<SpreadsheetMetadataPanelComponentItemDateTimeOffset> {

    @Test
    public void testToDateToLongToDate1900() {
        this.toDateToLongToDateAndCheck(
            LocalDate.of(
                1900,
                1,
                1
            )
        );
    }

    @Test
    public void testToDateToLongToDate1901() {
        this.toDateToLongToDateAndCheck(
            LocalDate.of(
                1901,
                1,
                1
            )
        );
    }

    @Test
    public void testToDateToLongToDate1903() {
        this.toDateToLongToDateAndCheck(
            LocalDate.of(
                1903,
                1,
                1
            )
        );
    }

    @Test
    public void testToDateToLongToDate1999() {
        this.toDateToLongToDateAndCheck(
            LocalDate.of(
                1999,
                12,
                31
            )
        );
    }

    @Test
    public void testToDateToLongToDate2023() {
        this.toDateToLongToDateAndCheck(
            LocalDate.of(
                2023,
                8, // August
                29
            )
        );
    }

    private void toDateToLongToDateAndCheck(final LocalDate date) {
        this.checkEquals(
            Optional.of(date),
            SpreadsheetMetadataPanelComponentItemDateTimeOffset.toDate(
                SpreadsheetMetadataPanelComponentItemDateTimeOffset.toLong(
                    Optional.of(date)
                )
            ),
            date::toString
        );
    }

    // toLong...........................................................................................................

    @Test
    public void testToLong() {
        this.checkEquals(
            Optional.of(
                2L
            ),
            SpreadsheetMetadataPanelComponentItemDateTimeOffset.toLong(
                Optional.of(
                    LocalDate.of(
                        1970,
                        1,
                        3
                    )
                )
            )
        );
    }

    // refresh..........................................................................................................

    @Test
    public void testRefresh() {
        final SpreadsheetMetadataPanelComponentContext context = new FakeSpreadsheetMetadataPanelComponentContext() {
            @Override
            public HistoryToken historyToken() {
                return HistoryToken.metadataPropertySelect(
                    SpreadsheetId.with(1),
                    SpreadsheetName.with("SpreadsheetName1"),
                    SpreadsheetMetadataPropertyName.DATE_TIME_OFFSET
                );
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return METADATA_EN_AU;
            }
        };
        final SpreadsheetMetadataPanelComponentItemDateTimeOffset component = SpreadsheetMetadataPanelComponentItemDateTimeOffset.with(context);
        component.refresh(context);

        this.treePrintAndCheck(
            component,
            "SpreadsheetMetadataPanelComponentItemDateTimeOffset\n" +
                "  UL\n" +
                "    style=\"align-items: center; display: flex; flex-wrap: wrap; justify-content: flex-start; list-style-type: none; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-left: 0;\"\n" +
                "      LI\n" +
                "        style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "          DateComponent\n" +
                "            [1899-12-30] id=metadata-dateTimeOffset-Date\n" +
                "      LI\n" +
                "        style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "          \"1900\" [#/1/SpreadsheetName1/spreadsheet/dateTimeOffset/save/0] id=metadata-dateTimeOffset-0-Link\n" +
                "      LI\n" +
                "        style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "          \"1904\" [#/1/SpreadsheetName1/spreadsheet/dateTimeOffset/save/1462] id=metadata-dateTimeOffset-1462-Link\n" +
                "      LI\n" +
                "        style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "          \"default\" [#/1/SpreadsheetName1/spreadsheet/dateTimeOffset/save/] id=metadata-dateTimeOffset-default-Link\n" +
                "            TooltipComponent\n" +
                "              \"0\"\n"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetMetadataPanelComponentItemDateTimeOffset> type() {
        return SpreadsheetMetadataPanelComponentItemDateTimeOffset.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
