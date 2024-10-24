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

package walkingkooka.spreadsheet.dominokit.pluginaliassetlike;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import walkingkooka.naming.Name;
import walkingkooka.plugin.PluginAliasLike;
import walkingkooka.plugin.PluginAliasSetLike;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.plugin.PluginSelectorLike;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.card.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Objects;

/**
 * A component that contains a panel holding links of for each present {@link PluginAliasLike}, removing each from a {@link PluginAliasSetLike}.
 */
public final class RemovePluginAliasSetLikeComponent<N extends Name & Comparable<N>,
        I extends PluginInfoLike<I, N>,
        IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
        S extends PluginSelectorLike<N>,
        A extends PluginAliasLike<N, S, A>,
        AS extends PluginAliasSetLike<N, I, IS, S, A, AS>>
        implements HtmlElementComponent<HTMLDivElement, RemovePluginAliasSetLikeComponent<N, I, IS, S, A, AS>>,
        TreePrintable {

    /**
     * Creates an empty {@link RemovePluginAliasSetLikeComponent}.
     */
    public static <N extends Name & Comparable<N>,
            I extends PluginInfoLike<I, N>,
            IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
            S extends PluginSelectorLike<N>,
            A extends PluginAliasLike<N, S, A>,
            AS extends PluginAliasSetLike<N, I, IS, S, A, AS>>
    RemovePluginAliasSetLikeComponent<N, I, IS, S, A, AS> empty(final String id) {
        return new RemovePluginAliasSetLikeComponent<>(
                CharSequences.failIfNullOrEmpty(id, "id")
        );
    }

    private RemovePluginAliasSetLikeComponent(final String id) {
        this.id = id;

        this.flex = SpreadsheetFlexLayout.row();
        this.root = SpreadsheetCard.empty()
                .setTitle("Remove")
                .appendChild(this.flex);
    }

    public void refresh(final AS aliases, // value from SpreadsheetMetadata
                        final AS providerAliases, // list of available aliases from provider
                        final RemovePluginAliasSetLikeComponentContext context) {
        this.root.hide();
        final SpreadsheetFlexLayout flex = this.flex.removeAllChildren();

        int i = 0;

        // for each provider Alias present in $aliases add a link.
        for (final A providerAlias : providerAliases) {
            final N name = providerAlias.name();

            if (aliases.containsAliasOrName(name)) {
                flex.appendChild(
                        this.anchor(
                                providerAlias,
                                aliases.deleteAliasOrName(name),
                                i,
                                context
                        )
                );

                i++;
            }
        }

        if (i > 0) {
            this.root.show();
        }
    }

    /**
     * Creates an anchor which will when clicked saves the plugin without this {@link PluginAliasLike}.
     */
    private HistoryTokenAnchorComponent anchor(final A remove,
                                               final AS aliases,
                                               final int index,
                                               final RemovePluginAliasSetLikeComponentContext context) {
        return context.historyToken()
                .saveLink(
                        this.id + index,
                        CaseKind.kebabToTitle(
                                remove.name()
                                        .value()
                        ),
                        aliases.text()
                );
    }

    /**
     * The base id prefix
     */
    private final String id;

    private final SpreadsheetCard root;

    /**
     * The parent holding LINKS
     */
    private final SpreadsheetFlexLayout flex;

    // setCssText.......................................................................................................

    @Override
    public RemovePluginAliasSetLikeComponent<N, I, IS, S, A, AS> setCssText(final String css) {
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
