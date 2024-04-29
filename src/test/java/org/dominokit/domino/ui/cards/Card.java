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

package org.dominokit.domino.ui.cards;

import elemental2.dom.Element;
import org.dominokit.domino.ui.IsElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;

public class Card implements IsElement<Element>,
        TreePrintable {

    public static Card create() {
        return new Card();
    }

    public IsElement<?> clearElement() {
        this.components.clear();
        return null;
    }

    public IsElement<?> setDisplay(final String display) {
        return null;
    }

    public IsElement<?> appendChild(final IsElement<?> component) {
        this.components.add(component);
        return this;
    }

    public String toString() {
        return this.components.toString();
    }

    // IsElement........................................................................................................

    @Override
    public Element element() {
        throw new UnsupportedOperationException();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println("Card");
        printer.indent();
        {
            for (final IsElement<?> component : this.components) {
                printer.lineStart();
                TreePrintable.printTreeOrToString(
                        component,
                        printer
                );
            }
        }
        printer.outdent();
    }

    private final List<IsElement<?>> components = Lists.array();
}
