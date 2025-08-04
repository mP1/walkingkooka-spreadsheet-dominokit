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

package walkingkooka.spreadsheet.dominokit.function;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAliasSet;

import java.util.Optional;

public final class ExpressionFunctionAliasSetComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, ExpressionFunctionAliasSet, ExpressionFunctionAliasSetComponent> {

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            ExpressionFunctionAliasSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "alias1 function1, function2"
                    )
                ),
            "ExpressionFunctionAliasSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [alias1 function1, function2]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            ExpressionFunctionAliasSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "alias1 function1, 9"
                    )
                ),
            "ExpressionFunctionAliasSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [alias1 function1, 9]\n" +
                "      Errors\n" +
                "        Invalid character '9' at 18\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public ExpressionFunctionAliasSetComponent createComponent() {
        return ExpressionFunctionAliasSetComponent.empty();
    }

    // class............................................................................................................
    @Override
    public Class<ExpressionFunctionAliasSetComponent> type() {
        return ExpressionFunctionAliasSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
