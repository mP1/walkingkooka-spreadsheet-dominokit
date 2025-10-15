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

package walkingkooka.spreadsheet.dominokit.valuetype;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.validation.ValueTypeName;

import java.util.Optional;

public final class SpreadsheetValueTypeComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, ValueTypeName, SpreadsheetValueTypeComponent> {

    @Test
    public void testTreePrintWithoutValue() {
        this.treePrintAndCheck(
            SpreadsheetValueTypeComponent.empty(),
            "SpreadsheetValueTypeComponent\n" +
                "  SelectComponent\n" +
                "    []\n" +
                "      Any=*\n" +
                "      Boolean=boolean\n" +
                "      Date=date\n" +
                "      Date Time=date-time\n" +
                "      Error=error\n" +
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
                "  SelectComponent\n" +
                "    [*]\n" +
                "      Any=*\n" +
                "      Boolean=boolean\n" +
                "      Date=date\n" +
                "      Date Time=date-time\n" +
                "      Error=error\n" +
                "      Number=number\n" +
                "      Text=text\n" +
                "      Time=time\n"
        );
    }

    @Test
    public void testTreePrintWithBooleanValue() {
        this.treePrintAndCheck(
            SpreadsheetValueTypeComponent.empty()
                .setValue(
                    Optional.of(
                        SpreadsheetValueType.BOOLEAN
                    )
                ),
            "SpreadsheetValueTypeComponent\n" +
                "  SelectComponent\n" +
                "    [boolean]\n" +
                "      Any=*\n" +
                "      Boolean=boolean\n" +
                "      Date=date\n" +
                "      Date Time=date-time\n" +
                "      Error=error\n" +
                "      Number=number\n" +
                "      Text=text\n" +
                "      Time=time\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetValueTypeComponent createComponent() {
        return SpreadsheetValueTypeComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetValueTypeComponent> type() {
        return SpreadsheetValueTypeComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
