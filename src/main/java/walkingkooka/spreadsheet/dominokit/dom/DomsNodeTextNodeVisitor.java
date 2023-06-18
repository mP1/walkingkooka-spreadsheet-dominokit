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

package walkingkooka.spreadsheet.dominokit.dom;

import elemental2.dom.DomGlobal;
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import elemental2.dom.Node;
import jsinterop.base.Js;
import walkingkooka.collect.stack.Stack;
import walkingkooka.collect.stack.Stacks;
import walkingkooka.tree.text.Text;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextNodeVisitor;
import walkingkooka.tree.text.TextPlaceholderNode;
import walkingkooka.tree.text.TextStyleNameNode;
import walkingkooka.tree.text.TextStyleNode;
import walkingkooka.visit.Visiting;

/**
 * A {@link TextNodeVisitor} that may be used to convert a {@link TextNode} into an equivalent {@link Node elemental node} supporting
 * only {@link TextStyleNode} which are converted into a SPAN with children appended and {@link Text} which are converted to DOM text nodes.
 * <br>
 * {@link walkingkooka.tree.text.TextPlaceholderName} and {@link TextStyleNameNode} will throw {@link UnsupportedOperationException}.
 */
final class DomsNodeTextNodeVisitor extends TextNodeVisitor {

    static Node toNode(final TextNode textNode) {
        return textNode.isText() ?
                DomGlobal.document.createTextNode(textNode.text()) :
                toNode0(textNode);
    }

    private static Node toNode0(final TextNode textNode) {
        final DomsNodeTextNodeVisitor visitor = new DomsNodeTextNodeVisitor();
        visitor.accept(textNode);
        return visitor.parent.firstElementChild;
    }

    private static HTMLElement createSpan() {
        return Js.cast(
                DomGlobal.document.createElement("SPAN")
        );
    }

    // @VisibleForTesting
    DomsNodeTextNodeVisitor() {
        this.parent = createSpan();
    }

    @Override
    protected Visiting startVisit(final TextStyleNode node) {
        final Element parent = this.parent;
        this.ancestors.push(parent);

        final HTMLElement span = createSpan();
        span.style.cssText = node.textStyle().css();
        parent.append(span);

        this.parent = span;
        return Visiting.CONTINUE;
    }

    @Override
    protected void endVisit(final TextStyleNode node) {
        final Stack<Element> ancestors = this.ancestors;
        this.parent = ancestors.peek();
        ancestors.pop();
    }

    @Override
    protected void visit(final TextPlaceholderNode node) {
        throw new UnsupportedOperationException(
                node.getClass().getSimpleName() +
                        " not supported=" +
                        node
        );
    }

    @Override
    protected Visiting startVisit(final TextStyleNameNode node) {
        throw new UnsupportedOperationException(
                node.getClass().getSimpleName() +
                        " not supported=" +
                        node
        );
    }

    /**
     * Appends a text node to the current parent.
     */
    @Override
    protected void visit(final Text node) {
        this.parent.appendChild(
                DomGlobal.document.createTextNode(
                        node.text()
                )
        );
    }

    private Element parent;

    private final Stack<Element> ancestors = Stacks.jdk();

    @Override
    public String toString() {
        return this.parent.toString();
    }
}
