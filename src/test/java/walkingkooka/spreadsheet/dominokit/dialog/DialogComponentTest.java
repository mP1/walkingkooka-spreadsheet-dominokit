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

package walkingkooka.spreadsheet.dominokit.dialog;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.text.TextBoxComponent;

import java.util.Optional;

public final class DialogComponentTest implements HtmlComponentTesting<DialogComponent, HTMLDivElement> {

    @Test
    public void testOpenTitleCloseableChildren() {
        final DialogComponent dialog = DialogComponent.with(
            null,
            null,
            "id123",
            DialogComponent.INCLUDE_CLOSE,
            DialogComponentContexts.fake()
        );
        dialog.appendChild(
            TextBoxComponent.empty()
                .setId("TextBoxId111")
                .setValue(
                    Optional.of(
                        "Value111"
                    )
                )
        );
        dialog.appendChild(
            TextBoxComponent.empty()
                .setId("TextBoxId222")
                .setValue(
                    Optional.of(
                        "Value222"
                    )
                )
        );

        dialog.open();

        this.treePrintAndCheck(
            dialog,
            "DialogComponent\n" +
                "  id=id123 includeClose=true\n" +
                "    TextBoxComponent\n" +
                "      [Value111] id=TextBoxId111\n" +
                "    TextBoxComponent\n" +
                "      [Value222] id=TextBoxId222\n"
        );
    }

    @Test
    public void testClosedTitleCloseableChildren() {
        final DialogComponent dialog = DialogComponent.with(
            null,
            null,
            "id123",
            DialogComponent.INCLUDE_CLOSE,
            DialogComponentContexts.fake()
        ).appendChild(
            TextBoxComponent.empty()
                .setId("TextBoxId111")
                .setValue(
                    Optional.of(
                        "Value111"
                    )
                )
        );

        this.treePrintAndCheck(
            dialog,
            "DialogComponent\n" +
                "  id=id123 includeClose=true CLOSED\n" +
                "    TextBoxComponent\n" +
                "      [Value111] id=TextBoxId111\n"
        );
    }

    @Override
    public Class<DialogComponent> type() {
        return DialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
