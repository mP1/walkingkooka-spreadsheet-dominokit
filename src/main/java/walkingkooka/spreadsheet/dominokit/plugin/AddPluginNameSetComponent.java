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

package walkingkooka.spreadsheet.dominokit.plugin;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.PluginNameSet;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.card.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.function.Predicate;

/**
 * A component that contains a card filled with links of {@link PluginName}, which when clicked add a plugin.
 */
public final class AddPluginNameSetComponent implements HtmlComponent<HTMLDivElement, AddPluginNameSetComponent> {

    /**
     * Creates an empty {@link AddPluginNameSetComponent}.
     */
    public static AddPluginNameSetComponent empty(final String id) {
        return new AddPluginNameSetComponent(
            CharSequences.failIfNullOrEmpty(id, "id")
        );
    }

    private AddPluginNameSetComponent(final String id) {
        this.id = id;

        this.flex = SpreadsheetFlexLayout.row();
        this.root = SpreadsheetCard.empty()
            .setTitle("Add")
            .appendChild(this.flex);
    }

    Optional<String> filterValue() {
        return this.root.filterValue();
    }

    AddPluginNameSetComponent setFilterValueChangeListener(final ChangeListener<Optional<String>> listener) {
        this.root.setFilterValueChangeListener(listener);
        return this;
    }

    AddPluginNameSetComponent setFilter(final Predicate<CharSequence> filter) {
        this.filter = filter;
        return this;
    }

    /**
     * A {@link Predicate} which is used to filter plugins.
     */
    private Predicate<CharSequence> filter;

    public void refresh(final PluginNameSet selectedPluginNames, // value from SpreadsheetMetadata
                        final PluginNameSet availablePluginNames, // list of available plugin names from provider
                        final AddPluginNameSetComponentContext context) {
        final SpreadsheetFlexLayout flex = this.flex.removeAllChildren();
        int i = 0;

        final Predicate<CharSequence> filter = this.filter;

        PluginNameSet filtered = availablePluginNames;
        if (null != filter) {
            final SortedSet<PluginName> names = SortedSets.tree();

            for (final PluginName plugin : availablePluginNames) {
                if (filter.test(plugin.text())) {
                    names.add(plugin);
                }
            }
            filtered = PluginNameSet.with(names);
        }

        // addAll should only contain $availablePluginNames after filtering...
        flex.appendChild(
            this.anchor(
                "*",
                selectedPluginNames.concatAll(filtered),
                i,
                context
            )
        );

        i++;

        for (final PluginName name : filtered) {

            // missing create a link for $name
            if (false == selectedPluginNames.contains(name)) {
                flex.appendChild(
                    this.anchor(
                        name,
                        selectedPluginNames.concat(name),
                        i,
                        context
                    )
                );

                i++;
            }
        }

        // never hide if there are no links otherwise filter box will also be invisible, leaving the user unable to
        // filter again
    }

    /**
     * Creates an anchor which will when clicked saves the plugin.
     */
    private HistoryTokenAnchorComponent anchor(final PluginName add,
                                               final PluginNameSet aliases,
                                               final int index,
                                               final AddPluginNameSetComponentContext context) {
        return this.anchor(
            CaseKind.kebabToTitle(
                add.value()
            ),
            aliases,
            index,
            context
        );
    }

    /**
     * Creates an anchor which will when clicked saves the plugin.
     */
    private HistoryTokenAnchorComponent anchor(final String title,
                                               final PluginNameSet aliases,
                                               final int index,
                                               final AddPluginNameSetComponentContext context) {
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
    public AddPluginNameSetComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.root.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public AddPluginNameSetComponent setCssProperty(final String name,
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
