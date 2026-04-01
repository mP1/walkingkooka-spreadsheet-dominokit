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

package walkingkooka.spreadsheet.dominokit.select;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.validation.ValueType;

import java.util.Optional;

public final class SelectComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, ValueType, SelectComponent<ValueType>> {

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabel("Label123")
                .setValue(
                    Optional.of(ValueType.TIME)
                ).setId("id987")
                .setDisabled(true)
                .required()
                .appendOption(
                    Optional.of(ValueType.DATE)
                ).appendOption(
                    Optional.of(ValueType.TEXT)
                ).appendOption(
                    Optional.of(ValueType.TIME)
                )
            ,
            "SelectComponent\n" +
                "  Label123 [time] id=id987 DISABLED REQUIRED\n" +
                "    \"Text date\" [#/1/SpreadsheetName111/cell/A1/valueType/save/date] id=TestId-date\n" +
                "    \"Text text\" [#/1/SpreadsheetName111/cell/A1/valueType/save/text] id=TestId-text\n" +
                "    \"Text time\" [#/1/SpreadsheetName111/cell/A1/valueType/save/time] id=TestId-time\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SelectComponent<ValueType> createComponent() {
        return SelectComponent.empty(
            (Optional<ValueType> value) -> new FakeHistoryContext()
                .selectOption(
                    "TestId-" + value.map(ValueType::value).get(), // id
                    "Text " + value.get(), // text
                    value, // value
                    Optional.of(
                        HistoryToken.cellValueTypeSave(
                            SpreadsheetId.with(1),
                            SpreadsheetName.with("SpreadsheetName111"),
                            SpreadsheetSelection.A1.setDefaultAnchor(),
                            value
                        )
                    )
                )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SelectComponent<ValueType>> type() {
        return Cast.to(SelectComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
