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

import walkingkooka.ToStringBuilder;
import walkingkooka.UsesToStringBuilder;
import walkingkooka.naming.Name;
import walkingkooka.plugin.PluginAliasLike;
import walkingkooka.plugin.PluginAliasSetLike;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.plugin.PluginSelectorLike;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Objects;

final class PluginNameAnchorListComponentContextBasic<N extends Name & Comparable<N>,
    I extends PluginInfoLike<I, N>,
    IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
    S extends PluginSelectorLike<N>,
    A extends PluginAliasLike<N, S, A>,
    AS extends PluginAliasSetLike<N, I, IS, S, A, AS>> implements PluginNameAnchorListComponentContext<N, I, IS, S, A, AS>,
    HistoryContextDelegator,
    UsesToStringBuilder {

    static <N extends Name & Comparable<N>,
        I extends PluginInfoLike<I, N>,
        IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
        S extends PluginSelectorLike<N>,
        A extends PluginAliasLike<N, S, A>,
        AS extends PluginAliasSetLike<N, I, IS, S, A, AS>> PluginNameAnchorListComponentContextBasic<N, I, IS, S, A, AS> with(final SpreadsheetMetadataPropertyName<AS> metadataPropertyName,
                                                                                                                              final HasSpreadsheetMetadata hasSpreadsheetMetadata,
                                                                                                                              final HistoryContext historyContext) {
        return new PluginNameAnchorListComponentContextBasic<>(
            Objects.requireNonNull(metadataPropertyName, "metadataPropertyName"),
            Objects.requireNonNull(hasSpreadsheetMetadata, "hasSpreadsheetMetadata"),
            Objects.requireNonNull(historyContext, "historyContext")
        );
    }

    private PluginNameAnchorListComponentContextBasic(final SpreadsheetMetadataPropertyName<AS> metadataPropertyName,
                                                      final HasSpreadsheetMetadata hasSpreadsheetMetadata,
                                                      final HistoryContext historyContext) {
        super();

        this.metadataPropertyName = metadataPropertyName;
        this.hasSpreadsheetMetadata = hasSpreadsheetMetadata;
        this.historyContext = historyContext;
    }

    @Override
    public SpreadsheetMetadataPropertyName<AS> metadataPropertyName() {
        return this.metadataPropertyName;
    }

    private final SpreadsheetMetadataPropertyName<AS> metadataPropertyName;

    // HasSpreadsheetMetadata...........................................................................................

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.hasSpreadsheetMetadata.spreadsheetMetadata();
    }

    private final HasSpreadsheetMetadata hasSpreadsheetMetadata;

    // HistoryContextDelegator..........................................................................................

    @Override
    public HistoryContext historyContext() {
        return this.historyContext;
    }

    private final HistoryContext historyContext;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    public void buildToString(final ToStringBuilder b) {
        b.label("spreadsheetMetadataPropertyName")
            .value(this.metadataPropertyName)
            .label("hasSpreadsheetMetadata")
            .value(this.hasSpreadsheetMetadata)
            .label("historyContext")
            .value(this.historyContext);
    }
}
