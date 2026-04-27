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

package walkingkooka.spreadsheet.dominokit.value;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.text.TextBoxComponent;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;

public final class FormElementComponentTest implements ClassTesting<FormElementComponent<?, ?, ?>>,
    TreePrintableTesting {

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            FormElementComponent.with(
                TextBoxComponent.empty()
                    .setValue(
                        Optional.of("Text123")
                    )
            ),
            "FormElementComponent\n" +
                "  TextBoxComponent\n" +
                "    [Text123] REQUIRED\n"
        );
    }

    @Test
    public void testSetId() {
        this.treePrintAndCheck(
            FormElementComponent.with(
                TextBoxComponent.empty()
                    .setValue(
                        Optional.of("Text123")
                    )
            ).setId("TestIdPrefix123-form"),
            "FormElementComponent\n" +
                "  TextBoxComponent\n" +
                "    [Text123] REQUIRED\n"
        );
    }

    @Test
    public void testSetLabelSetHelperTextSetErrorsTreePrint() {
        final FormElementComponent<?, ?, ?> formElementComponent = FormElementComponent.with(
            TextBoxComponent.empty()
                .setValue(
                    Optional.of("Text123")
                )
        );
        formElementComponent.setLabel("Label123");
        formElementComponent.setHelperText("HelperText123");
        formElementComponent.setErrors(
            Lists.of(
                "Error111",
                "Error22",
                "Error333"
            )
        );

        this.treePrintAndCheck(
            formElementComponent,
            "FormElementComponent\n" +
                "  label\n" +
                "    Label123\n" +
                "  helperText\n" +
                "    HelperText123\n" +
                "  errors\n" +
                "    Error111\n" +
                "    Error22\n" +
                "    Error333\n" +
                "  TextBoxComponent\n" +
                "    [Text123] REQUIRED\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<FormElementComponent<?, ?, ?>> type() {
        return Cast.to(FormElementComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
