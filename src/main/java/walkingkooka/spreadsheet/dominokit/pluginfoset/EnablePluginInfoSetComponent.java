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

package walkingkooka.spreadsheet.dominokit.pluginfoset;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import walkingkooka.naming.Name;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A component that contains a horizontal panel holding links of disabled {@link PluginInfoLike} which will enable.
 */
public final class EnablePluginInfoSetComponent<N extends Name & Comparable<N>, I extends PluginInfoLike<I, N>, IS extends PluginInfoSetLike<N, I, IS>> implements HtmlElementComponent<HTMLDivElement, EnablePluginInfoSetComponent<N, I, IS>>,
        TreePrintable {

    /**
     * Creates an empty {@link EnablePluginInfoSetComponent}.
     */
    public static <N extends Name & Comparable<N>, I extends PluginInfoLike<I, N>, IS extends PluginInfoSetLike<N, I, IS>> EnablePluginInfoSetComponent<N, I, IS> empty(final String id) {
        return new EnablePluginInfoSetComponent<>(
                CharSequences.failIfNullOrEmpty(id, "id")
        );
    }

    private EnablePluginInfoSetComponent(final String id) {
        this.id = id;

        this.flex = SpreadsheetFlexLayout.row();
        this.root = SpreadsheetCard.empty()
                .setTitle("Enable")
                .appendChild(this.flex);
    }

    public void refresh(final IS enabledInfos, // value from SpreadsheetMetadata with currently enabled infos
                        final IS providerInfos, // from provider
                        final EnablePluginInfoSetComponentContext context) {
        this.root.hide();
        final SpreadsheetFlexLayout flex = this.flex.removeAllChildren();

        final Set<AbsoluteUrl> enabledUrls = enabledInfos.stream()
                .map(PluginInfoLike::url)
                .collect(Collectors.toSet());

        int i = 0;
        for (final I providerInfo : providerInfos) {
            // if providerInfo.url is absent from enabledInfos.url create link
            if (false == enabledUrls.contains(providerInfo.url())) {
                flex.appendChild(
                        this.anchor(
                                providerInfo,
                                enabledInfos.concat(providerInfo),
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
     * Creates an anchor which will when clicked saves the plugin without this {@link PluginInfoLike}.
     */
    private HistoryTokenAnchorComponent anchor(final I info,
                                               final IS infos,
                                               final int index,
                                               final EnablePluginInfoSetComponentContext context) {
        return context.historyToken()
                .saveLink(
                        this.id + "enable-" + index,
                        CaseKind.kebabToTitle(
                                info.name()
                                        .value()
                        ),
                        infos.text()
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
    public EnablePluginInfoSetComponent<N, I, IS> setCssText(final String css) {
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
