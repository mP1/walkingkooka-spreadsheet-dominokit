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
import walkingkooka.spreadsheet.convert.provider.MissingConverterSet;
import walkingkooka.spreadsheet.convert.provider.MissingConverterValue;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.value.SpreadsheetValueType;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MissingConverterSetComponentTest implements ValueComponentTesting<HTMLDivElement, MissingConverterSet, MissingConverterSetComponent> {

    @Test
    public void testWithNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> MissingConverterSetComponent.empty(null)
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent(),
            "MissingConverterSetComponent\n" +
                "  MissingConverterSet\n" +
                "    MissingConverter\n" +
                "      alpha\n" +
                "        MissingConverterValue\n" +
                "          123 (walkingkooka.tree.expression.ExpressionNumberBigDecimal)\n" +
                "            number\n" +
                "        MissingConverterValue\n" +
                "          \"HelloWorldText\"\n" +
                "            number\n" +
                "    MissingConverter\n" +
                "      beta\n" +
                "        MissingConverterValue\n" +
                "          1999-12-31 (java.time.LocalDate)\n" +
                "            date\n"
        );
    }

    @Override
    public MissingConverterSetComponent createComponent() {
        return MissingConverterSetComponent.empty(
            MissingConverterSet.EMPTY.concat(
                MissingConverter.with(
                    ConverterName.with("beta"),
                    Sets.of(
                        MissingConverterValue.with(
                            LocalDate.of(
                                1999,
                                12,
                                31
                            ),
                            SpreadsheetValueType.DATE.value()
                        )
                    )
                )
            ).concat(
                MissingConverter.with(
                    ConverterName.with("alpha"),
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
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<MissingConverterSetComponent> type() {
        return MissingConverterSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
