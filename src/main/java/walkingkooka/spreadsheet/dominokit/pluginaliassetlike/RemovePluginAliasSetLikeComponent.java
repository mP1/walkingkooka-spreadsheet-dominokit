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
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.naming.Name;
import walkingkooka.plugin.PluginAliasLike;
import walkingkooka.plugin.PluginAliasSetLike;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.plugin.PluginSelectorLike;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.card.CardComponent;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * A component that contains a panel holding links of for each present {@link PluginAliasLike}, removing each from a {@link PluginAliasSetLike}.
 */
public final class RemovePluginAliasSetLikeComponent<N extends Name & Comparable<N>,
    I extends PluginInfoLike<I, N>,
    IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
    S extends PluginSelectorLike<N>,
    A extends PluginAliasLike<N, S, A>,
    AS extends PluginAliasSetLike<N, I, IS, S, A, AS>>
    implements HtmlComponentDelegator<HTMLDivElement, RemovePluginAliasSetLikeComponent<N, I, IS, S, A, AS>> {

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

        this.flex = FlexLayoutComponent.row();
        this.root = CardComponent.empty()
            .setTitle("Remove")
            .appendChild(this.flex);
    }

    RemovePluginAliasSetLikeComponent<N, I, IS, S, A, AS> setFilterValueChangeListener(final ChangeListener<Optional<String>> listener) {
        this.root.setFilterValueChangeListener(listener);
        return this;
    }

    RemovePluginAliasSetLikeComponent<N, I, IS, S, A, AS> setFilter(final Predicate<CharSequence> filter) {
        this.filter = filter;
        return this;
    }

    /**
     * A {@link Predicate} which is used to filter links by testing the text against the given {@link Predicate}.
     */
    private Predicate<CharSequence> filter;

    public void refresh(final AS aliases, // value from SpreadsheetMetadata
                        final AS providerAliases, // list of available aliases from provider
                        final RemovePluginAliasSetLikeComponentContext context) {
        final FlexLayoutComponent flex = this.flex.removeAllChildren();

        int i = 0;

        flex.appendChild(
            this.anchor(
                "*",
                aliases,
                i,
                context
            )
        );

        i++;

        final Predicate<CharSequence> filter = this.filter;

        // for each provider Alias present in $aliases add a link.
        for (final A providerAlias : providerAliases) {
            final N name = providerAlias.name();

            if (aliases.containsAliasOrName(name) && (null == filter || filter.test(providerAlias.name().text()))) {
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

        if (i > 1) {
            this.root.show();
        } else {
            this.flex.removeChild(0); // remove "*" link
            this.root.hide();
        }
    }

    /**
     * Creates an anchor which will when clicked saves the plugin without this {@link PluginAliasLike}.
     */
    private HistoryTokenAnchorComponent anchor(final A remove,
                                               final AS aliases,
                                               final int index,
                                               final RemovePluginAliasSetLikeComponentContext context) {
        return this.anchor(
            CaseKind.kebabToTitle(
                remove.name()
                    .value()
            ),
            aliases,
            index,
            context
        );
    }

    /**
     * Creates an anchor which will when clicked saves the plugin without this {@link PluginAliasLike}.
     */
    private HistoryTokenAnchorComponent anchor(final String title,
                                               final AS aliases,
                                               final int index,
                                               final RemovePluginAliasSetLikeComponentContext context) {
        return context.historyToken()
            .saveLink(
                this.id + index,
                title,
                aliases.text()
            );
    }

    /**
     * The base id prefix
     */
    private final String id;

    /**
     * The parent holding LINKS
     */
    private final FlexLayoutComponent flex;

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return false;
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.root;
    }

    private final CardComponent root;

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
