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

package walkingkooka.spreadsheet.dominokit.error;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.value.SpreadsheetError;
import walkingkooka.spreadsheet.value.SpreadsheetErrorKind;

import java.util.Optional;

public final class SpreadsheetErrorComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetError, SpreadsheetErrorComponent> {

    private final static SpreadsheetError ERROR = SpreadsheetErrorKind.DIV0.setMessage("Hello Error 123");

    @Test
    public void testParseAndText() {
        this.checkEquals(
            ERROR,
            SpreadsheetError.parse(
                ERROR.textIncludingMessage()
            )
        );
    }

    @Test
    public void testSetStringValueAndValue() {
        this.checkEquals(
            Optional.of(ERROR),
            SpreadsheetErrorComponent.empty()
                .setStringValue(
                    Optional.of(
                        ERROR.textIncludingMessage()
                    )
                ).value()
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            SpreadsheetErrorComponent.empty()
                .setStringValue(
                    Optional.of(
                        ERROR.textIncludingMessage()
                    )
                ),
            "SpreadsheetErrorComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [#DIV/0! Hello Error 123]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            SpreadsheetErrorComponent.empty()
                .setStringValue(
                    Optional.of(
                        "#BAD Message123"
                    )
                ),
            "SpreadsheetErrorComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [#BAD Message123]\n" +
                "      Errors\n" +
                "        Invalid error kind\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetErrorComponent createComponent() {
        return SpreadsheetErrorComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetErrorComponent> type() {
        return SpreadsheetErrorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
