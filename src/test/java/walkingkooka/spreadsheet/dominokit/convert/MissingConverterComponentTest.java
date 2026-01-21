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
import walkingkooka.collect.set.Sets;
import walkingkooka.convert.provider.ConverterName;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.convert.provider.MissingConverter;
import walkingkooka.spreadsheet.convert.provider.MissingConverterValue;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.value.SpreadsheetValueType;
import walkingkooka.tree.expression.ExpressionNumberKind;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MissingConverterComponentTest implements ValueComponentTesting<HTMLDivElement, MissingConverter, MissingConverterComponent> {

    @Test
    public void testWithNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> MissingConverterComponent.empty(null)
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent(),
            "MissingConverterComponent\n" +
                "  MissingConverter\n" +
                "    object\n" +
                "      MissingConverterValue\n" +
                "        123 (walkingkooka.tree.expression.ExpressionNumberBigDecimal)\n" +
                "          number\n" +
                "      MissingConverterValue\n" +
                "        \"HelloWorldText\"\n" +
                "          number\n"
        );
    }

    @Override
    public MissingConverterComponent createComponent() {
        return MissingConverterComponent.empty(
            MissingConverter.with(
                ConverterName.OBJECT,
                Sets.of(
                    MissingConverterValue.with(
                        ExpressionNumberKind.BIG_DECIMAL.create(123),
                        SpreadsheetValueType.NUMBER.value()
                    ),
                    MissingConverterValue.with(
                        "HelloWorldText",
                        SpreadsheetValueType.NUMBER.value()
                    )
                )
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<MissingConverterComponent> type() {
        return MissingConverterComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
