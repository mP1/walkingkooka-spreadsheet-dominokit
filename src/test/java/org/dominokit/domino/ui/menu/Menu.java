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

package org.dominokit.domino.ui.menu;

import org.dominokit.domino.ui.icons.Icon;
import org.gwtproject.core.shared.GwtIncompatible;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Optional;

@GwtIncompatible
public class Menu<V> implements TreePrintable {

    public static <V> Menu<V> create(final String id,
                                     final String text,
                                     final Optional<Icon<?>> icon,
                                     final Optional<String> badge) {
        return new Menu<>(
                id,
                text,
                icon,
                badge
        );
    }

    private Menu(final String id,
                 final String text,
                 final Optional<Icon<?>> icon,
                 final Optional<String> badge) {
        this.id = id;
        this.text = text;
        this.icon = icon;
        this.badge = badge;
    }

    private final String id;

    private final String text;

    private final Optional<Icon<?>> icon;

    private final Optional<String> badge;

    public Menu<V> appendChild(final Object child) {
        this.children.add(child);
        return this;
    }

    private final List<Object> children = Lists.array();

    @Override
    public void printTree(final IndentingPrinter printer) {
        final Optional<Icon<?>> icon = this.icon;
        if (icon.isPresent()) {
            printer.print("(");
            printer.print(icon.get().getName());
            printer.print(") ");
        }

        printer.print(this.id);
        printer.print(" ");
        printer.print(CharSequences.quoteAndEscape(this.text));

        final Optional<String> badge = this.badge;
        if (badge.isPresent()) {
            printer.print(" [");
            printer.print(badge.get());
            printer.print("]");
        }

        printer.indent();
        {

            for (final Object child : this.children) {
                printer.lineStart();
                TreePrintable.printTreeOrToString(
                        child,
                        printer
                );
            }
        }

        printer.outdent();
    }
}
