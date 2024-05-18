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

package walkingkooka.spreadsheet.dominokit.ui.datatable;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

public class SpreadsheetDataTableComponent<T> implements SpreadsheetDataTableComponentLike<T>,
        TestHtmlElementComponent<HTMLDivElement, SpreadsheetDataTableComponent<T>> {

    public static <T> SpreadsheetDataTableComponent<T> with(final String id,
                                                            final List<ColumnConfig<T>> columnConfigs,
                                                            final BiFunction<Integer, T, HtmlElementComponent<?, ?>> cellRenderer) {
        return new SpreadsheetDataTableComponent<>(
                id,
                columnConfigs,
                cellRenderer
        );
    }

    private SpreadsheetDataTableComponent(final String id,
                                          final List<ColumnConfig<T>> columnConfigs,
                                          final BiFunction<Integer, T, HtmlElementComponent<?, ?>> cellRenderer) {
        CharSequences.failIfNullOrEmpty(id, "id");

        this.columnConfigs = Lists.immutable(columnConfigs);
        this.cellRenderer = cellRenderer;
        this.plugins = Lists.array();

        this.setId(id);
    }

    // id...............................................................................................................

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public SpreadsheetDataTableComponent<T> setId(final String id) {
        CharSequences.failIfNullOrEmpty(id, "id");
        this.id = id;
        return this;
    }

    private String id;

    // disabled.........................................................................................................

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public SpreadsheetDataTableComponent<T> setDisabled(final boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    private boolean disabled;

    // value............................................................................................................

    @Override
    public Optional<List<T>> value() {
        return this.value;
    }

    @Override
    public SpreadsheetDataTableComponent<T> setValue(final Optional<List<T>> value) {
        Objects.requireNonNull(value, "value");

        this.value = value.map(Lists::immutable);
        return this;
    }

    private Optional<List<T>> value = Optional.empty();

    // children.........................................................................................................

    @Override
    public SpreadsheetDataTableComponent<T> appendChild(final IsElement<?> child) {
        this.children.add(child);
        return this;
    }

    @Override
    public SpreadsheetDataTableComponent<T> removeChild(final int child) {
        this.children.remove(child);
        return this;
    }

    @Override
    public List<IsElement<?>> children() {
        return Lists.immutable(
                this.children
        );
    }

    private final List<IsElement<?>> children = Lists.array();

    // header...........................................................................................................

    @Override
    public SpreadsheetDataTableComponent<T> hideHeaders() {
        this.headersHidden = true;
        return this;
    }

    private boolean headersHidden;

    // plugins..........................................................................................................

    @Override
    public SpreadsheetDataTableComponent<T> bodyScrollPlugin() {
        this.plugins.add(
                SpreadsheetDataTableComponentLike.bodyScrollPluginText()
        );
        return this;
    }

    @Override
    public SpreadsheetDataTableComponent<T> emptyStatePlugin(final Icon<?> icon,
                                                             final String title) {
        this.plugins.add(
                SpreadsheetDataTableComponentLike.emptyStatePluginText(
                        icon,
                        title
                )
        );
        return this;
    }

    private final List<String> plugins;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        this.printTreeTable(
                this.columnConfigs,
                this.headersHidden,
                this.cellRenderer,
                this.plugins,
                printer
        );
    }

    private final List<ColumnConfig<T>> columnConfigs;

    private final BiFunction<Integer, T, HtmlElementComponent<?, ?>> cellRenderer;
}
