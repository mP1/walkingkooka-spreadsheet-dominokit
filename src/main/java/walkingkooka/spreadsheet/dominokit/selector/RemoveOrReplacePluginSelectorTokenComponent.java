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

package walkingkooka.spreadsheet.dominokit.selector;

import elemental2.dom.HTMLDivElement;
import walkingkooka.collect.list.ImmutableList;
import walkingkooka.collect.list.Lists;
import walkingkooka.plugin.PluginSelectorTokenAlternativeLike;
import walkingkooka.plugin.PluginSelectorTokenLike;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.card.CardComponent;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A component with links for each {@link PluginSelectorTokenLike}, with context menu items which support replacing the item.
 */
public final class RemoveOrReplacePluginSelectorTokenComponent<T extends PluginSelectorTokenLike<A>, A extends PluginSelectorTokenAlternativeLike> implements HtmlComponent<HTMLDivElement, RemoveOrReplacePluginSelectorTokenComponent<T, A>> {

    /**
     * Creates an empty {@link RemoveOrReplacePluginSelectorTokenComponent}.
     */
    public static <T extends PluginSelectorTokenLike<A>, A extends PluginSelectorTokenAlternativeLike> RemoveOrReplacePluginSelectorTokenComponent<T, A> empty(final String id) {
        return new RemoveOrReplacePluginSelectorTokenComponent<>(
            CharSequences.failIfNullOrEmpty(id, "id")
        );
    }

    private RemoveOrReplacePluginSelectorTokenComponent(final String id) {
        this.id = id;

        this.flex = SpreadsheetFlexLayout.row();
        this.root = CardComponent.empty()
            .setTitle("Remove / Replace component(s)")
            .appendChild(this.flex);
    }

    public void refresh(final List<T> tokens,
                        final RemoveOrReplacePluginSelectorTokenComponentContext context) {
        this.refresh0(
            Lists.immutable(
                Objects.requireNonNull(tokens, "tokens")
            ),
            Objects.requireNonNull(context, "context")
        );
    }

    void refresh0(final ImmutableList<T> tokens,
                  final RemoveOrReplacePluginSelectorTokenComponentContext context) {
        this.root.hide();
        final SpreadsheetFlexLayout flex = this.flex.removeAllChildren();

        final ImmutableList<String> tokenText = Lists.immutable(
            tokens.stream()
                .map(PluginSelectorTokenLike::text)
                .collect(Collectors.toList())
        );

        int i = 0;
        for (final T token : tokens) {
            flex.appendChild(
                this.anchor(
                    token,
                    tokenText,
                    i,
                    context
                )
            );
            i++;
        }

        if (false == tokens.isEmpty()) {
            this.root.show();
        }
    }

    /**
     * Creates an anchor which will contain the label/text and a context menu with alternatives.
     */
    private HistoryTokenAnchorComponent anchor(final T token,
                                               final ImmutableList<String> tokens,
                                               final int index,
                                               final RemoveOrReplacePluginSelectorTokenComponentContext context) {
        final HistoryTokenAnchorComponent anchor = context.historyToken()
            .saveLink(
                this.id + "remove-" + index,
                token.label(),
                context.saveText(
                    tokens.deleteAtIndex(index)
                        .stream()
                        .collect(Collectors.joining(""))
                )
            );
        final Collection<A> alternatives = token.alternatives();

        if (false == alternatives.isEmpty()) {
            this.contextMenuWithAlternatives(
                anchor,
                token,
                tokens,
                index,
                context
            );
        }

        return anchor;
    }

    /**
     * Builds a context menu with the alternatives as items.
     */
    private void contextMenuWithAlternatives(final HistoryTokenAnchorComponent anchor,
                                             final T token,
                                             final ImmutableList<String> tokens,
                                             final int index,
                                             final RemoveOrReplacePluginSelectorTokenComponentContext context) {
        SpreadsheetContextMenu contextMenu = SpreadsheetContextMenu.wrap(
            anchor,
            context
        );
        final HistoryToken historyToken = context.historyToken();

        final ImmutableList<A> alternatives = Lists.immutable(
            token.alternatives()
        );

        final String id = anchor.id();

        int i = 0;
        for (final A alternative : alternatives) {
            contextMenu = contextMenu.item(
                historyToken.setSaveStringValue(
                        context.saveText(
                            tokens.replace(
                                    index,
                                    alternative.text()
                                ).stream()
                                .collect(Collectors.joining())
                        )
                    )
                    .contextMenuItem(
                        id + "-replace-" + i + SpreadsheetElementIds.MENU_ITEM,
                        alternative.label()
                    )
            );

            i++;
        }
    }

    /**
     * The base id prefix
     */
    private final String id;

    private final CardComponent root;

    /**
     * The parent holding remove/replace LINKS
     */
    private final SpreadsheetFlexLayout flex;

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return false;
    }

    // setCssText.......................................................................................................

    @Override
    public RemoveOrReplacePluginSelectorTokenComponent<T, A> setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.root.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public RemoveOrReplacePluginSelectorTokenComponent<T, A> setCssProperty(final String name,
                                                                            final String value) {
        this.root.setCssProperty(
            name,
            value
        );
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.root.element();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.root.printTree(printer);
        }
        printer.outdent();
    }
}
