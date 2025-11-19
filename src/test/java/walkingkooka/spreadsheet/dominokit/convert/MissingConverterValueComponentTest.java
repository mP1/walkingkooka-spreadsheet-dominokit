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

package walkingkooka.spreadsheet.dominokit.convert;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.convert.provider.MissingConverterValue;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.tree.expression.ExpressionNumberKind;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MissingConverterValueComponentTest implements ValueComponentTesting<HTMLDivElement, MissingConverterValue, MissingConverterValueComponent> {

    @Test
    public void testWithNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> MissingConverterValueComponent.empty(null)
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent(),
            "MissingConverterValueComponent\n" +
                "  123 (walkingkooka.tree.expression.ExpressionNumberBigDecimal)\n" +
                "    number\n"
        );
    }

    @Override
    public MissingConverterValueComponent createComponent() {
        return MissingConverterValueComponent.empty(
            MissingConverterValue.with(
                ExpressionNumberKind.BIG_DECIMAL.create(123),
                SpreadsheetValueType.NUMBER.value()
            )
        );
    }

    // class............................................................................................................

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public Class<MissingConverterValueComponent> type() {
        return MissingConverterValueComponent.class;
    }
}
