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

import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import walkingkooka.tree.text.TextNode;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link ValueComponent} that holds text within a {@link TextNode}
 */
public final class SpreadsheetTextNodeComponent implements SpreadsheetTextNodeComponentLike {

    public static SpreadsheetTextNodeComponent with(final Optional<TextNode> value) {
        return new SpreadsheetTextNodeComponent(value);
    }

    private SpreadsheetTextNodeComponent(final Optional<TextNode> value) {
        this.setValue(value);
    }

    // value............................................................................................................

    @Override
    public Optional<TextNode> value() {
        return this.value;
    }

    private Optional<TextNode> value;

    @Override
    public SpreadsheetTextNodeComponent setValue(final Optional<TextNode> value) {
        Objects.requireNonNull(value, "value");
        this.value = value;
        return this;
    }

    // node.............................................................................................................

    @Override
    public HTMLDivElement element() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Node node() {
        throw new UnsupportedOperationException();
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.value.map(TextNode::text)
                .orElse("");
    }
}
