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

package walkingkooka.spreadsheet.dominokit.text;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.text.TextNode;

import java.util.Optional;

public final class TextNodeComponentTest implements ClassTesting<TextNodeComponent>,
    TreePrintableTesting {

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            TextNodeComponent.with(
                Optional.of(
                    TextNode.text("Hello123")
                )
            ),
            "TextNodeComponent\n" +
                "  Hello123\n"
        );
    }

    @Test
    public void testTreePrint2() {
        this.treePrintAndCheck(
            TextNodeComponent.with(
                Optional.of(
                    TextNode.text("Hello123\nZebra567")
                )
            ),
            "TextNodeComponent\n" +
                "  Hello123\n" +
                "  Zebra567\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextNodeComponent> type() {
        return TextNodeComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
