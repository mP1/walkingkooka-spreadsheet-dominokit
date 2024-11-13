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
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionProviders;

import java.util.Optional;

public final class ExpressionFunctionInfoSetComponentTest implements ValueComponentTesting<HTMLFieldSetElement, ExpressionFunctionInfoSet, ExpressionFunctionInfoSetComponent> {

    @Test
    public void testParseAndText() {
        final ExpressionFunctionInfoSet infos = ExpressionFunctionProviders.expressionFunctions()
                .expressionFunctionInfos();

        this.checkEquals(
                infos,
                ExpressionFunctionInfoSet.parse(infos.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
                ExpressionFunctionInfoSetComponent.empty()
                        .setStringValue(
                                Optional.of(
                                        ExpressionFunctionProviders.expressionFunctions()
                                                .expressionFunctionInfos()
                                                .text()
                                )
                        ),
                "ExpressionFunctionInfoSetComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [https://github.com/mP1/walkingkooka-tree-expression-function-provider/ExpressionFunction/name name,https://github.com/mP1/walkingkooka-tree-expression-function-provider/ExpressionFunction/node node,https://github.com/mP1/walkingkooka-tree-expression-function-provider/ExpressionFunction/typeName typeName]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
                ExpressionFunctionInfoSetComponent.empty()
                        .setStringValue(
                                Optional.of(
                                        "https://www.example.com/Hello !"
                                )
                        ),
                "ExpressionFunctionInfoSetComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [https://www.example.com/Hello !]\n" +
                        "      Errors\n" +
                        "        Invalid character '!' at 30\n"
        );
    }


    // class............................................................................................................

    @Override
    public Class<ExpressionFunctionInfoSetComponent> type() {
        return ExpressionFunctionInfoSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
