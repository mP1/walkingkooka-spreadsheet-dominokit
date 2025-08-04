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
import walkingkooka.collect.set.SortedSets;
import walkingkooka.naming.Name;
import walkingkooka.plugin.PluginAliasLike;
import walkingkooka.plugin.PluginAliasSetLike;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.plugin.PluginSelectorLike;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.card.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A component that contains a card filled with links of {@link PluginAliasLike}.
 */
public final class AddPluginAliasSetLikeComponent<N extends Name & Comparable<N>,
    I extends PluginInfoLike<I, N>,
    IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
    S extends PluginSelectorLike<N>,
    A extends PluginAliasLike<N, S, A>,
    AS extends PluginAliasSetLike<N, I, IS, S, A, AS>>
    implements HtmlComponent<HTMLDivElement, AddPluginAliasSetLikeComponent<N, I, IS, S, A, AS>> {

    /**
     * Creates an empty {@link AddPluginAliasSetLikeComponent}.
     */
    public static <N extends Name & Comparable<N>,
        I extends PluginInfoLike<I, N>,
        IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
        S extends PluginSelectorLike<N>,
        A extends PluginAliasLike<N, S, A>,
        AS extends PluginAliasSetLike<N, I, IS, S, A, AS>>
    AddPluginAliasSetLikeComponent<N, I, IS, S, A, AS> empty(final String id) {
        return new AddPluginAliasSetLikeComponent<>(
            CharSequences.failIfNullOrEmpty(id, "id")
        );
    }

    private AddPluginAliasSetLikeComponent(final String id) {
        this.id = id;

        this.flex = SpreadsheetFlexLayout.row();
        this.root = SpreadsheetCard.empty()
            .setTitle("Add")
            .appendChild(this.flex);
    }

    AddPluginAliasSetLikeComponent<N, I, IS, S, A, AS> setFilterValueChangeListener(final ChangeListener<Optional<String>> listener) {
        this.root.setFilterValueChangeListener(listener);
        return this;
    }

    AddPluginAliasSetLikeComponent<N, I, IS, S, A, AS> setFilter(final Predicate<CharSequence> filter) {
        this.filter = filter;
        return this;
    }

    /**
     * A {@link Predicate} which is used to filter plugins.
     */
    private Predicate<CharSequence> filter;

    public void refresh(final AS aliases, // value from SpreadsheetMetadata
                        final AS providerAliases, // list of available aliases from provider
                        final AddPluginAliasSetLikeComponentContext context) {
        final SpreadsheetFlexLayout flex = this.flex.removeAllChildren();
        int i = 0;

        final Predicate<CharSequence> filter = this.filter;

        AS filtered;

        if (null != filter) {
            filtered = providerAliases.setElements(
                providerAliases.stream()
                    .filter(a -> filter.test(a.name().text()))
                    .collect(Collectors.toCollection(SortedSets::tree))
            );
        } else {
            filtered = providerAliases;
        }

        // addAll
        flex.appendChild(
            this.anchor(
                "*",
                filtered,
                i,
                context
            )
        );

        i++;

        // for each provider Alias MISSING from $aliases add a link.
        for (final A providerAlias : filtered) {
            final N name = providerAlias.name();

            if (false == aliases.containsAliasOrName(name)) {
                flex.appendChild(
                    this.anchor(
                        providerAlias,
                        aliases.concat(providerAlias),
                        i,
                        context
                    )
                );

                i++;
            }
        }

        if (1 == i) {
            this.flex.removeChild(0);
            this.root.hide();
        } else {
            this.root.show();
        }
    }

    /**
     * Creates an anchor which will when clicked saves the plugin assuming the {@link PluginAliasSetLike}.
     */
    private HistoryTokenAnchorComponent anchor(final A add,
                                               final AS aliases,
                                               final int index,
                                               final AddPluginAliasSetLikeComponentContext context) {
        return this.anchor(
            CaseKind.kebabToTitle(
                add.name()
                    .value()
            ),
            aliases,
            index,
            context
        );
    }

    /**
     * Creates an anchor which will when clicked saves the plugin assuming the {@link PluginAliasSetLike}.
     */
    private HistoryTokenAnchorComponent anchor(final String title,
                                               final AS aliases,
                                               final int index,
                                               final AddPluginAliasSetLikeComponentContext context) {
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

    private final SpreadsheetCard root;

    /**
     * The parent holding LINKS
     */
    private final SpreadsheetFlexLayout flex;

    // setCssText.......................................................................................................

    @Override
    public AddPluginAliasSetLikeComponent<N, I, IS, S, A, AS> setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.root.setCssText(css);
        return this;
    }

    // setCssText.......................................................................................................

    @Override
    public AddPluginAliasSetLikeComponent<N, I, IS, S, A, AS> setCssProperty(final String name,
                                                                             final String value) {
        this.root.setCssProperty(
            name,
            value
        );
        return this;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return false;
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
