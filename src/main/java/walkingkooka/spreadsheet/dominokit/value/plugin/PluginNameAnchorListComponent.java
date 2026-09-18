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

package walkingkooka.spreadsheet.dominokit.value.plugin;

import elemental2.dom.HTMLDivElement;
import walkingkooka.naming.Name;
import walkingkooka.plugin.PluginAliasLike;
import walkingkooka.plugin.PluginAliasSetLike;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.PluginSelectorLike;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Holds a list of anchors for each given {@link PluginName}, watching a {@link SpreadsheetMetadataPropertyName} and
 * updating the links to list.
 */
public final class PluginNameAnchorListComponent<N extends Name & Comparable<N>,
    I extends PluginInfoLike<I, N>,
    IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
    S extends PluginSelectorLike<N>,
    A extends PluginAliasLike<N, S, A>,
    AS extends PluginAliasSetLike<N, I, IS, S, A, AS>> implements ValueComponent<HTMLDivElement, N, PluginNameAnchorListComponent<N, I, IS, S, A, AS>>,
    HtmlComponentDelegator<HTMLDivElement, PluginNameAnchorListComponent<N, I, IS, S, A, AS>>,
    SpreadsheetMetadataFetcherWatcher,
    NopFetcherWatcher,

    NopEmptyResponseFetcherWatcher {

    public static <N extends Name & Comparable<N>,
        I extends PluginInfoLike<I, N>,
        IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
        S extends PluginSelectorLike<N>,
        A extends PluginAliasLike<N, S, A>,
        AS extends PluginAliasSetLike<N, I, IS, S, A, AS>> PluginNameAnchorListComponent<N, I, IS, S, A, AS> with(final String idPrefix,
                                                                                                                  final PluginNameAnchorListComponentContext<N, I, IS, S, A, AS> context) {
        return new PluginNameAnchorListComponent<>(
            CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix"),
            Objects.requireNonNull(context, "context")
        );
    }

    private PluginNameAnchorListComponent(final String idPrefix,
                                          final PluginNameAnchorListComponentContext<N, I, IS, S, A, AS> context) {
        super();

        this.idPrefix = idPrefix;

        this.anchors = AnchorListComponent.empty()
            .setId(idPrefix + "links");
        this.value = Optional.empty();
        this.context = context;
    }

    private void refresh() {
        final AnchorListComponent anchors = this.anchors;
        anchors.removeAllChildren();

        final PluginNameAnchorListComponentContext<N, I, IS, S, A, AS> context = this.context;
        final HistoryToken historyToken = context.historyToken();
        final String idPrefix = this.idPrefix;

        for (final A aliases : context.spreadsheetMetadata().getOrFail(context.metadataPropertyName())) {
            final String name = aliases.name()
                .text();

            anchors.appendChild(
                historyToken.validator()
                    .setSaveStringValue(name)
                    .link(idPrefix + name)
                    .setTextContent(
                        CaseKind.kebabToTitle(name)
                    )
            );
        }
    }

    private final String idPrefix;

    private final PluginNameAnchorListComponentContext<N, I, IS, S, A, AS> context;

    // ValueComponent...................................................................................................

    @Override
    public Optional<N> value() {
        return this.value;
    }

    @Override
    public PluginNameAnchorListComponent<N, I, IS, S, A, AS> setValue(final Optional<N> value) {
        Objects.requireNonNull(value, "value");

        this.value = value;
        this.refresh();
        return this;
    }

    private Optional<N> value;

    @Override
    public Runnable addValueWatcher(final ValueWatcher<N> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public PluginNameAnchorListComponent<N, I, IS, S, A, AS> setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PluginNameAnchorListComponent<N, I, IS, S, A, AS> hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PluginNameAnchorListComponent<N, I, IS, S, A, AS> removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PluginNameAnchorListComponent<N, I, IS, S, A, AS> removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PluginNameAnchorListComponent<N, I, IS, S, A, AS> focus() {
        this.anchors.focus();
        return this;
    }

    @Override
    public PluginNameAnchorListComponent<N, I, IS, S, A, AS> blur() {
        this.anchors.blur();
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.anchors.isEditing();
    }

    private final AnchorListComponent anchors;

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.anchors;
    }

    // SpreadsheetMetadataFetcherWatcher................................................................................

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata) {
        this.refresh();
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas) {
        // nop
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        {
            printer.indent();
            {
                this.anchors.printTree(printer);
            }
            printer.outdent();
        }
    }
}
