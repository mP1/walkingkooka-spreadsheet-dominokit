
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

import walkingkooka.naming.Name;
import walkingkooka.plugin.PluginAliasLike;
import walkingkooka.plugin.PluginAliasSetLike;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.plugin.PluginSelectorLike;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

/**
 * A collection of factory methods to create {@link PluginNameAnchorListComponentContext}.
 */
public final class PluginNameAnchorListComponentContexts implements PublicStaticHelper {

    /**
     * {@link PluginNameAnchorListComponentContextBasic}
     */
    public static <N extends Name & Comparable<N>,
        I extends PluginInfoLike<I, N>,
        IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
        S extends PluginSelectorLike<N>,
        A extends PluginAliasLike<N, S, A>,
        AS extends PluginAliasSetLike<N, I, IS, S, A, AS>> PluginNameAnchorListComponentContext<N, I, IS, S, A, AS> basic(final SpreadsheetMetadataPropertyName<AS> metadataPropertyName,
                                                                                                                          final HasSpreadsheetMetadata hasSpreadsheetMetadata,
                                                                                                                          final HistoryContext historyContext) {
        return PluginNameAnchorListComponentContextBasic.with(
            metadataPropertyName,
            hasSpreadsheetMetadata,
            historyContext
        );
    }

    /**
     * {@link FakePluginNameAnchorListComponentContext}
     */
    public static <N extends Name & Comparable<N>,
        I extends PluginInfoLike<I, N>,
        IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
        S extends PluginSelectorLike<N>,
        A extends PluginAliasLike<N, S, A>,
        AS extends PluginAliasSetLike<N, I, IS, S, A, AS>> FakePluginNameAnchorListComponentContext<N, I, IS, S, A, AS> fake() {
        return new FakePluginNameAnchorListComponentContext<>();
    }

    /**
     * Stop creation
     */
    private PluginNameAnchorListComponentContexts() {
        throw new UnsupportedOperationException();
    }
}
