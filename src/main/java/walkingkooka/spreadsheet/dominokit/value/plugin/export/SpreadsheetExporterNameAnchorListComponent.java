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

package walkingkooka.spreadsheet.dominokit.value.plugin.export;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.plugin.PluginNameAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.plugin.PluginNameAnchorListComponentContexts;
import walkingkooka.spreadsheet.dominokit.value.plugin.PluginNameAnchorListComponentDelegator;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterAlias;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterAliasSet;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterInfo;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterInfoSet;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterName;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * Holds a list of anchors for each given {@link SpreadsheetExporterName}.
 */
public final class SpreadsheetExporterNameAnchorListComponent implements ValueComponent<HTMLDivElement, SpreadsheetExporterName, SpreadsheetExporterNameAnchorListComponent>,
    PluginNameAnchorListComponentDelegator<SpreadsheetExporterNameAnchorListComponent, SpreadsheetExporterName, SpreadsheetExporterInfo, SpreadsheetExporterInfoSet, SpreadsheetExporterSelector, SpreadsheetExporterAlias, SpreadsheetExporterAliasSet> {

    public static SpreadsheetExporterNameAnchorListComponent with(final String idPrefix,
                                                                  final SpreadsheetExporterNameAnchorListComponentContext context) {
        return new SpreadsheetExporterNameAnchorListComponent(
            CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix"),
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetExporterNameAnchorListComponent(final String idPrefix,
                                                       final SpreadsheetExporterNameAnchorListComponentContext context) {
        super();

        this.valueComponent = PluginNameAnchorListComponent.with(
            idPrefix,
            PluginNameAnchorListComponentContexts.basic(
                SpreadsheetMetadataPropertyName.EXPORTERS,
                context, // HasSpreadsheetMetadata
                context // HistoryContext
            )
        );
    }

    // PluginNameAnchorListComponentDelegator...........................................................................

    @Override
    public PluginNameAnchorListComponent<SpreadsheetExporterName, SpreadsheetExporterInfo, SpreadsheetExporterInfoSet, SpreadsheetExporterSelector, SpreadsheetExporterAlias, SpreadsheetExporterAliasSet> valueComponent() {
        return this.valueComponent;
    }

    private final PluginNameAnchorListComponent<SpreadsheetExporterName, SpreadsheetExporterInfo, SpreadsheetExporterInfoSet, SpreadsheetExporterSelector, SpreadsheetExporterAlias, SpreadsheetExporterAliasSet> valueComponent;
}
