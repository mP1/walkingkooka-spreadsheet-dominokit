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
import elemental2.dom.Node;
import walkingkooka.collect.list.ImmutableList;
import walkingkooka.collect.list.Lists;
import walkingkooka.plugin.PluginSelectorTokenAlternativeLike;
import walkingkooka.plugin.PluginSelectorTokenLike;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetContextMenu;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A component with links for each {@link PluginSelectorTokenLike}, with context menu items which support replacing the item.
 */
public final class RemoveOrReplacePluginSelectorTextComponent<T extends PluginSelectorTokenLike<A>, A extends PluginSelectorTokenAlternativeLike> implements HtmlElementComponent<HTMLDivElement, RemoveOrReplacePluginSelectorTextComponent<T, A>>,
        TreePrintable {

    /**
     * Creates an empty {@link RemoveOrReplacePluginSelectorTextComponent}.
     */
    public static <T extends PluginSelectorTokenLike<A>, A extends PluginSelectorTokenAlternativeLike> RemoveOrReplacePluginSelectorTextComponent<T, A> empty(final String id) {
        return new RemoveOrReplacePluginSelectorTextComponent<>(
                CharSequences.failIfNullOrEmpty(id, "id")
        );
    }

    private RemoveOrReplacePluginSelectorTextComponent(final String id) {
        this.id = id;

        this.flex = SpreadsheetFlexLayout.row();
        this.root = SpreadsheetCard.empty()
                .setTitle("Remove / Replace component(s)")
                .appendChild(this.flex);
    }

    public void refresh(final List<T> textComponents,
                        final RemoveOrReplacePluginSelectorTextComponentContext context) {
        this.refresh0(
                Lists.immutable(
                        Objects.requireNonNull(textComponents, "textComponents")
                ),
                Objects.requireNonNull(context, "context")
        );
    }

    void refresh0(final ImmutableList<T> textComponents,
                  final RemoveOrReplacePluginSelectorTextComponentContext context) {
        this.root.hide();
        final SpreadsheetFlexLayout flex = this.flex.removeAllChildren();

        final ImmutableList<String> textComponentText = Lists.immutable(
                textComponents.stream()
                        .map(PluginSelectorTokenLike::text)
                        .collect(Collectors.toList())
        );

        int i = 0;
        for (final T textComponent : textComponents) {
            flex.appendChild(
                    this.removeAnchor(
                            textComponent,
                            textComponentText,
                            i,
                            context
                    )
            );
            i++;
        }

        if (false == textComponents.isEmpty()) {
            this.root.show();
        }
    }

    /**
     * Creates an anchor which will contain the label/text and a context menu with alternatives.
     */
    private HistoryTokenAnchorComponent removeAnchor(final T textComponent,
                                                     final ImmutableList<String> textComponents,
                                                     final int index,
                                                     final RemoveOrReplacePluginSelectorTextComponentContext context) {
        final HistoryToken historyToken = context.historyToken();

        final HistoryTokenAnchorComponent anchor = historyToken.link(
                        this.id + "remove-" + index
                ).setTextContent(textComponent.label())
                .setHistoryToken(
                        Optional.of(
                                historyToken.setSave(
                                        context.saveText(
                                                textComponents.removeAtIndex(index)
                                                        .stream()
                                                        .collect(Collectors.joining(""))
                                        )
                                )
                        )
                );
        final Collection<A> alternatives = textComponent.alternatives();

        if (false == alternatives.isEmpty()) {
            this.contextMenuWithAlternatives(
                    anchor,
                    textComponent,
                    textComponents,
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
                                             final T textComponent,
                                             final ImmutableList<String> textComponents,
                                             final int index,
                                             final RemoveOrReplacePluginSelectorTextComponentContext context) {
        SpreadsheetContextMenu contextMenu = SpreadsheetContextMenu.wrap(
                anchor,
                context
        );
        final HistoryToken historyToken = context.historyToken();

        final ImmutableList<A> alternatives = Lists.immutable(
                textComponent.alternatives()
        );

        final String id = anchor.id();

        int i = 0;
        for (final A alternative : alternatives) {
            contextMenu = contextMenu.item(
                    historyToken.setSave(
                                    context.saveText(
                                            textComponents.replace(
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

    private final SpreadsheetCard root;

    /**
     * The parent holding LINKS which contain the pattern without a component.
     */
    private final SpreadsheetFlexLayout flex;

    // setCssText.......................................................................................................

    @Override
    public RemoveOrReplacePluginSelectorTextComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.root.setCssText(css);
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.root.element();
    }

    // node.............................................................................................................

    @Override
    public Node node() {
        return this.element();
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
