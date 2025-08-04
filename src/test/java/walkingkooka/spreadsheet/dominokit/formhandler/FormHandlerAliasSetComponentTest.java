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

package walkingkooka.spreadsheet.dominokit.formhandler;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.validation.form.provider.FormHandlerAliasSet;

import java.util.Optional;

public final class FormHandlerAliasSetComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, FormHandlerAliasSet, FormHandlerAliasSetComponent> {

    @Test
    public void testParseAndText() {
        final FormHandlerAliasSet alias = FormHandlerAliasSet.parse("alias1 plugin1, plugin2");

        this.checkEquals(
            alias,
            FormHandlerAliasSet.parse(alias.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            FormHandlerAliasSetComponent.empty()
                .setStringValue(
                    Optional.of("alias1 name1, alias2, name2")
                ),
            "FormHandlerAliasSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [alias1 name1, alias2, name2]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidValue() {
        this.treePrintAndCheck(
            FormHandlerAliasSetComponent.empty()
                .setStringValue(
                    Optional.of("alias1 name1, alias2 !")
                ),
            "FormHandlerAliasSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [alias1 name1, alias2 !]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 21\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public FormHandlerAliasSetComponent createComponent() {
        return FormHandlerAliasSetComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<FormHandlerAliasSetComponent> type() {
        return FormHandlerAliasSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
