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

package walkingkooka.spreadsheet.dominokit.ui.spreadsheetvaluetype;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;

public final class SpreadsheetValueTypeComponentTest implements ClassTesting<SpreadsheetValueTypeComponent>,
        TreePrintableTesting {

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
                SpreadsheetValueTypeComponent.empty(),
                "SpreadsheetValueTypeComponent\n" +
                        "  SpreadsheetSelectComponent\n" +
                        "    \n" +
                        "      Any=*\n" +
                        "      Boolean=boolean\n" +
                        "      Date=date\n" +
                        "      Error=error\n" +
                        "      DateTime=date-time\n" +
                        "      Number=number\n" +
                        "      Text=text\n" +
                        "      Time=time\n"
        );
    }

    @Test
    public void testTreePrintWithAnyValue() {
        this.treePrintAndCheck(
                SpreadsheetValueTypeComponent.empty()
                        .setValue(Optional.of(SpreadsheetValueType.ANY)),
                "SpreadsheetValueTypeComponent\n" +
                        "  SpreadsheetSelectComponent\n" +
                        "    [*]\n" +
                        "      Any=*\n" +
                        "      Boolean=boolean\n" +
                        "      Date=date\n" +
                        "      Error=error\n" +
                        "      DateTime=date-time\n" +
                        "      Number=number\n" +
                        "      Text=text\n" +
                        "      Time=time\n"
        );
    }

    @Test
    public void testTreePrintWithBooleanValue() {
        this.treePrintAndCheck(
                SpreadsheetValueTypeComponent.empty()
                        .setValue(Optional.of(SpreadsheetValueType.BOOLEAN)),
                "SpreadsheetValueTypeComponent\n" +
                        "  SpreadsheetSelectComponent\n" +
                        "    [boolean]\n" +
                        "      Any=*\n" +
                        "      Boolean=boolean\n" +
                        "      Date=date\n" +
                        "      Error=error\n" +
                        "      DateTime=date-time\n" +
                        "      Number=number\n" +
                        "      Text=text\n" +
                        "      Time=time\n"
        );
    }

    @Override
    public Class<SpreadsheetValueTypeComponent> type() {
        return SpreadsheetValueTypeComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
