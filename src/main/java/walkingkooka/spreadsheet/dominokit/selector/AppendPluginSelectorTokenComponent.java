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
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A component which displays links which append possible tokens to the current selector text.
 */
public final class AppendPluginSelectorTokenComponent<T extends PluginSelectorTokenLike<A>, A extends PluginSelectorTokenAlternativeLike> implements HtmlElementComponent<HTMLDivElement, AppendPluginSelectorTokenComponent<T, A>>,
        TreePrintable {

    /**
     * Creates an empty {@link AppendPluginSelectorTokenComponent}.
     */
    public static <T extends PluginSelectorTokenLike<A>, A extends PluginSelectorTokenAlternativeLike> AppendPluginSelectorTokenComponent<T, A> empty(final String id) {
        return new AppendPluginSelectorTokenComponent<>(
                CharSequences.failIfNullOrEmpty(id, "id")
        );
    }

    private AppendPluginSelectorTokenComponent(final String id) {
        this.id = id;

        this.flex = SpreadsheetFlexLayout.row();
        this.root = SpreadsheetCard.empty()
                .setTitle("Append component(s)")
                .appendChild(this.flex);
    }

    public void refresh(final List<T> tokens,
                        final List<A> alternatives,
                        final AppendPluginSelectorTokenComponentContext context) {
        this.refresh0(
                Lists.immutable(
                        Objects.requireNonNull(tokens, "tokens")
                ),
                Lists.immutable(
                        Objects.requireNonNull(alternatives, "alternatives")
                ),
                Objects.requireNonNull(context, "context")
        );
    }

    void refresh0(final List<T> tokens,
                  final ImmutableList<A> alternatives,
                  final AppendPluginSelectorTokenComponentContext context) {
        this.root.hide();
        final SpreadsheetFlexLayout flex = this.flex.removeAllChildren();

        final String baseTextComponent = tokens.stream()
                .map(PluginSelectorTokenLike::text)
                .collect(Collectors.joining());

        int i = 0;
        for (final A alternative : alternatives) {
            flex.appendChild(
                    this.anchor(
                            baseTextComponent,
                            alternative,
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

    private HistoryTokenAnchorComponent anchor(final String token,
                                               final A alternative,
                                               final int index,
                                               final AppendPluginSelectorTokenComponentContext context) {
        final HistoryToken historyToken = context.historyToken();

        return historyToken.saveLink(
                this.id + "append-" + index,
                alternative.label(),
                context.saveText(
                        token + alternative.text()
                )
        );
    }

    /**
     * The base id prefix
     */
    private final String id;

    private final SpreadsheetCard root;

    /**
     * The parent holding the links.
     */
    private final SpreadsheetFlexLayout flex;

    // setCssText.......................................................................................................

    @Override
    public AppendPluginSelectorTokenComponent<T, A> setCssText(final String css) {
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
