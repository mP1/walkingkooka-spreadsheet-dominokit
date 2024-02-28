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

package walkingkooka.spreadsheet.dominokit.ui.meta;

import org.junit.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.Calendar;
import java.util.Date;

public final class SpreadsheetMetadataPanelComponentItemDateTimeOffsetTest implements ClassTesting<SpreadsheetMetadataPanelComponentItemDateTimeOffset> {

    @Test
    public void testToDateToLongToDate1900() {
        this.toDateToLongToDateAndCheck(
                new Date(
                        Date.UTC(
                                1900 - 1900,
                                Calendar.JANUARY,
                                1,
                                0,
                                0,
                                0
                        )
                )
        );
    }

    @Test
    public void testToDateToLongToDate1901() {
        this.toDateToLongToDateAndCheck(
                new Date(
                        Date.UTC(
                                1901 - 1900,
                                Calendar.JANUARY,
                                1,
                                0,
                                0,
                                0
                        )
                )
        );
    }

    @Test
    public void testToDateToLongToDate1903() {
        this.toDateToLongToDateAndCheck(
                new Date(
                        Date.UTC(
                                1903 - 1900,
                                Calendar.JANUARY,
                                1,
                                0,
                                0,
                                0
                        )
                )
        );
    }

    @Test
    public void testToDateToLongToDate1999() {
        this.toDateToLongToDateAndCheck(
                new Date(
                        Date.UTC(
                                1999 - 1900,
                                Calendar.DECEMBER,
                                31,
                                0,
                                0,
                                0
                        )
                )
        );
    }

    @Test
    public void testToDateToLongToDate2023() {
        this.toDateToLongToDateAndCheck(
                new Date(
                        Date.UTC(
                                2023 - 1900,
                                Calendar.AUGUST,
                                29,
                                0,
                                0,
                                0
                        )
                )
        );
    }

    private void toDateToLongToDateAndCheck(final Date date) {
        this.checkEquals(
                date,
                SpreadsheetMetadataPanelComponentItemDateTimeOffset.toDate(
                        SpreadsheetMetadataPanelComponentItemDateTimeOffset.toLong(date)
                ),
                date::toString
        );
    }

    // toLong...........................................................................................................

    @Test
    public void testToLong() {
        this.checkEquals(
                2L,
                SpreadsheetMetadataPanelComponentItemDateTimeOffset.toLong(
                        new Date(
                                Date.UTC(
                                        1900 - 1900,
                                        Calendar.JANUARY,
                                        1,
                                        0,
                                        0,
                                        0
                                )
                        )
                )
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
