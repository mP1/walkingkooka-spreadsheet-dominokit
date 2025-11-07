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
import walkingkooka.plugin.PluginAliasLike;
import walkingkooka.plugin.PluginAliasSetLike;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.PluginNameSet;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.card.CardComponent;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.function.Predicate;

/**
 * A component that contains a panel holding links of for each present {@link PluginAliasLike}, removing each from a {@link PluginAliasSetLike}.
 */
public final class RemovePluginNameSetComponent implements HtmlComponentDelegator<HTMLDivElement, RemovePluginNameSetComponent> {

    /**
     * Creates an empty {@link RemovePluginNameSetComponent}.
     */
    public static RemovePluginNameSetComponent empty(final String id) {
        return new RemovePluginNameSetComponent(
            CharSequences.failIfNullOrEmpty(id, "id")
        );
    }

    private RemovePluginNameSetComponent(final String id) {
        this.id = id;

        this.flex = FlexLayoutComponent.row();
        this.root = CardComponent.empty()
            .setTitle("Remove")
            .appendChild(this.flex);
    }

    RemovePluginNameSetComponent setFilterValueWatcher(final ValueWatcher<String> watcher) {
        this.root.setFilterValueWatcher(watcher);
        return this;
    }

    RemovePluginNameSetComponent setFilter(final Predicate<CharSequence> filter) {
        this.filter = filter;
        return this;
    }

    /**
     * A {@link Predicate} which is used to filter links by testing the text against the given {@link Predicate}.
     */
    private Predicate<CharSequence> filter;

    public void refresh(final PluginNameSet aliases, // value from SpreadsheetMetadata
                        final PluginNameSet providerAliases, // list of available aliases from provider
                        final RemovePluginNameSetComponentContext context) {
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
        for (final PluginName name : providerAliases) {

            if (aliases.contains(name) && (null == filter || filter.test(name.text()))) {
                flex.appendChild(
                    this.anchor(
                        name,
                        aliases.delete(name),
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
     * Creates an anchor which will when clicked saves the plugin without this {@link PluginName}.
     */
    private HistoryTokenAnchorComponent anchor(final PluginName remove,
                                               final PluginNameSet aliases,
                                               final int index,
                                               final RemovePluginNameSetComponentContext context) {
        return this.anchor(
            CaseKind.kebabToTitle(
                remove.value()
            ),
            aliases,
            index,
            context
        );
    }

    /**
     * Creates an anchor which will when clicked saves the plugin without this {@link PluginName}.
     */
    private HistoryTokenAnchorComponent anchor(final String title,
                                               final PluginNameSet aliases,
                                               final int index,
                                               final RemovePluginNameSetComponentContext context) {
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
