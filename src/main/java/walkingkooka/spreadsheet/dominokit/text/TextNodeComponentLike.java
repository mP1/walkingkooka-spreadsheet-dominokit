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

import walkingkooka.spreadsheet.dominokit.Component;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.text.TextNode;

import java.util.Optional;

/**
 * Defines the public interface for a {@link TextNodeComponent}.
 */
public interface TextNodeComponentLike extends Component,
    TreePrintable {

    Optional<TextNode> value();

    TextNodeComponent setValue(final Optional<TextNode> value);

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());

        final String text = this.value()
            .map(TextNode::text)
            .orElse("");
        if (false == text.isEmpty()) {
            printer.indent();
            {
                printer.println(text);
            }
            printer.outdent();
        }
    }
}
