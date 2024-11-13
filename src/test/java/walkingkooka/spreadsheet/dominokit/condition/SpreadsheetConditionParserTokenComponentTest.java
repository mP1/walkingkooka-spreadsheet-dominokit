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
import walkingkooka.spreadsheet.parser.SpreadsheetConditionParserToken;

import java.util.Optional;

public final class SpreadsheetConditionParserTokenComponentTest implements ValueComponentTesting<HTMLFieldSetElement, SpreadsheetConditionParserToken, SpreadsheetConditionParserTokenComponent>,
        SpreadsheetMetadataTesting {

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
                SpreadsheetConditionParserTokenComponent.empty(
                                () -> SPREADSHEET_PARSER_CONTEXT
                        ).setStringValue(
                                Optional.of(
                                        "< 123"
                                )
                        ),
                "SpreadsheetConditionParserTokenComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [< 123]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
                SpreadsheetConditionParserTokenComponent.empty(
                                () -> SPREADSHEET_PARSER_CONTEXT
                        ).setStringValue(
                                Optional.of(
                                        "1+2"
                                )
                        ),
                "SpreadsheetConditionParserTokenComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [1+2]\n" +
                        "      Errors\n" +
                        "        Invalid character '1' at 0 in \"1+2\"\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetConditionParserTokenComponent> type() {
        return SpreadsheetConditionParserTokenComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
