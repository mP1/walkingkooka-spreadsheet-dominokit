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

import elemental2.dom.HTMLElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.validation.ValueTypeName;

import java.util.Optional;

public final class ValueTypeViewComponentTest implements ValueComponentTesting<HTMLElement, ValueTypeName, ValueTypeViewComponent> {

    @Test
    public void testSetValueWithEmpty() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.empty()
                ),
            "ValueTypeViewComponent\n" +
                "  LabelComponent\n"
        );
    }

    @Test
    public void testSetValueWithAbsoluteUrl() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetValueType.ABSOLUTE_URL
                    )
                ),
            "ValueTypeViewComponent\n" +
                "  LabelComponent\n" +
                "    \"absolute-url\"\n"
        );
    }

    @Test
    public void testSetValueWithText() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetValueType.TEXT
                    )
                ),
            "ValueTypeViewComponent\n" +
                "  LabelComponent\n" +
                "    \"text\"\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public ValueTypeViewComponent createComponent() {
        return ValueTypeViewComponent.empty();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ValueTypeViewComponent> type() {
        return ValueTypeViewComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
