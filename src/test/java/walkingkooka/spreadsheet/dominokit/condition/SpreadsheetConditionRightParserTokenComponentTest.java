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

package walkingkooka.spreadsheet.dominokit.condition;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.parser.SpreadsheetConditionRightParserToken;

import java.util.Optional;

public final class SpreadsheetConditionRightParserTokenComponentTest implements ValueComponentTesting<HTMLFieldSetElement, SpreadsheetConditionRightParserToken, SpreadsheetConditionRightParserTokenComponent>,
    SpreadsheetMetadataTesting {

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "< 123"
                    )
                ),
            "SpreadsheetConditionRightParserTokenComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    SpreadsheetTextBox\n" +
                "      [< 123]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "1+2"
                    )
                ),
            "SpreadsheetConditionRightParserTokenComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    SpreadsheetTextBox\n" +
                "      [1+2]\n" +
                "      Errors\n" +
                "        Invalid character '1' at 0\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetConditionRightParserTokenComponent createComponent() {
        return SpreadsheetConditionRightParserTokenComponent.empty(
            () -> SPREADSHEET_PARSER_CONTEXT
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetConditionRightParserTokenComponent> type() {
        return SpreadsheetConditionRightParserTokenComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
