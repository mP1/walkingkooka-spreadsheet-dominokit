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
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.validation.ValueTypeName;

import java.util.Optional;

public final class SpreadsheetValueTypeComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, ValueTypeName, SpreadsheetValueTypeComponent> {

    @Test
    public void testTreePrintWithoutValue() {
        this.treePrintAndCheck(
            this.createComponent(),
            "SpreadsheetValueTypeComponent\n" +
                "  SelectComponent\n" +
                "    [] id=ValueType123-\n" +
                "      *\n" +
                "      boolean\n" +
                "      date\n" +
                "      date-time\n" +
                "      email\n" +
                "      error\n" +
                "      number\n" +
                "      text\n" +
                "      time\n" +
                "      url\n"
        );
    }

    @Test
    public void testTreePrintWithAnyValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(Optional.of(SpreadsheetValueType.ANY)),
            "SpreadsheetValueTypeComponent\n" +
                "  SelectComponent\n" +
                "    [*] id=ValueType123-\n" +
                "      *\n" +
                "      boolean\n" +
                "      date\n" +
                "      date-time\n" +
                "      email\n" +
                "      error\n" +
                "      number\n" +
                "      text\n" +
                "      time\n" +
                "      url\n"
        );
    }

    @Test
    public void testTreePrintWithBooleanValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetValueType.BOOLEAN
                    )
                ),
            "SpreadsheetValueTypeComponent\n" +
                "  SelectComponent\n" +
                "    [boolean] id=ValueType123-\n" +
                "      *\n" +
                "      boolean\n" +
                "      date\n" +
                "      date-time\n" +
                "      email\n" +
                "      error\n" +
                "      number\n" +
                "      text\n" +
                "      time\n" +
                "      url\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetValueTypeComponent createComponent() {
        return SpreadsheetValueTypeComponent.empty(
            "ValueType123-",
            new FakeSpreadsheetValueTypeComponentContext() {

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.parseString("/1/SpreadsheetName1/cell/A1");
                }
            }
        );
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
