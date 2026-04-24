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

package walkingkooka.spreadsheet.dominokit.value.valuetype;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetValueType;
import walkingkooka.validation.ValueType;

import java.util.Optional;

public final class ValueTypeEditComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, ValueType, ValueTypeEditComponent> {

    @Test
    public void testTreePrintWithoutValue() {
        this.treePrintAndCheck(
            this.createComponent(),
            "ValueTypeEditComponent\n" +
                "  SelectComponent\n" +
                "    [] id=ValueType123-Select\n" +
                "      \"Any\" DISABLED id=ValueType123-*-Option\n" +
                "      \"Boolean\" DISABLED id=ValueType123-boolean-Option\n" +
                "      \"Date\" DISABLED id=ValueType123-date-Option\n" +
                "      \"Date Time\" DISABLED id=ValueType123-date-time-Option\n" +
                "      \"Email\" DISABLED id=ValueType123-email-Option\n" +
                "      \"Error(spreadsheet)\" DISABLED id=ValueType123-error(spreadsheet)-Option\n" +
                "      \"Number\" DISABLED id=ValueType123-number-Option\n" +
                "      \"Text\" DISABLED id=ValueType123-text-Option\n" +
                "      \"Time\" DISABLED id=ValueType123-time-Option\n" +
                "      \"Url\" DISABLED id=ValueType123-url-Option\n" +
                "      \"Whole Number\" DISABLED id=ValueType123-whole-number-Option\n"
        );
    }

    @Test
    public void testTreePrintWithAnyValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(Optional.of(SpreadsheetValueType.ANY)),
            "ValueTypeEditComponent\n" +
                "  SelectComponent\n" +
                "    [*] id=ValueType123-Select\n" +
                "      \"Any\" DISABLED id=ValueType123-*-Option\n" +
                "      \"Boolean\" DISABLED id=ValueType123-boolean-Option\n" +
                "      \"Date\" DISABLED id=ValueType123-date-Option\n" +
                "      \"Date Time\" DISABLED id=ValueType123-date-time-Option\n" +
                "      \"Email\" DISABLED id=ValueType123-email-Option\n" +
                "      \"Error(spreadsheet)\" DISABLED id=ValueType123-error(spreadsheet)-Option\n" +
                "      \"Number\" DISABLED id=ValueType123-number-Option\n" +
                "      \"Text\" DISABLED id=ValueType123-text-Option\n" +
                "      \"Time\" DISABLED id=ValueType123-time-Option\n" +
                "      \"Url\" DISABLED id=ValueType123-url-Option\n" +
                "      \"Whole Number\" DISABLED id=ValueType123-whole-number-Option\n"
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
            "ValueTypeEditComponent\n" +
                "  SelectComponent\n" +
                "    [boolean] id=ValueType123-Select\n" +
                "      \"Any\" DISABLED id=ValueType123-*-Option\n" +
                "      \"Boolean\" DISABLED id=ValueType123-boolean-Option\n" +
                "      \"Date\" DISABLED id=ValueType123-date-Option\n" +
                "      \"Date Time\" DISABLED id=ValueType123-date-time-Option\n" +
                "      \"Email\" DISABLED id=ValueType123-email-Option\n" +
                "      \"Error(spreadsheet)\" DISABLED id=ValueType123-error(spreadsheet)-Option\n" +
                "      \"Number\" DISABLED id=ValueType123-number-Option\n" +
                "      \"Text\" DISABLED id=ValueType123-text-Option\n" +
                "      \"Time\" DISABLED id=ValueType123-time-Option\n" +
                "      \"Url\" DISABLED id=ValueType123-url-Option\n" +
                "      \"Whole Number\" DISABLED id=ValueType123-whole-number-Option\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public ValueTypeEditComponent createComponent() {
        return ValueTypeEditComponent.empty(
            "ValueType123-",
            new FakeValueTypeEditComponentContext() {

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.cellSelect(
                        SpreadsheetId.with(1),
                        SpreadsheetName.with("SpreadsheetName111"),
                        SpreadsheetSelection.A1.setDefaultAnchor()
                    );
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<ValueTypeEditComponent> type() {
        return ValueTypeEditComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
