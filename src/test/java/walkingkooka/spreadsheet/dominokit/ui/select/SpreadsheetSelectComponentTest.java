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

package walkingkooka.spreadsheet.dominokit.ui.select;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;

public final class SpreadsheetSelectComponentTest implements ClassTesting<SpreadsheetSelectComponent<String>>,
        TreePrintableTesting {

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
                SpreadsheetSelectComponent.empty()
                        .setLabel("Label123")
                        .setValue(Optional.of("Value456"))
                        .setId("id987")
                        .setDisabled(true)
                        .required()
                        .appendValue("text1", "value1")
                        .appendValue("text2", "value2")
                        .appendValue("text3", "value3")
                ,
                "SpreadsheetSelectComponent\n" +
                        "  Label123 [Value456] id=id987 DISABLED REQUIRED\n" +
                        "    text1=value1\n" +
                        "    text2=value2\n" +
                        "    text3=value3\n"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetSelectComponent<String>> type() {
        return Cast.to(SpreadsheetSelectComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
