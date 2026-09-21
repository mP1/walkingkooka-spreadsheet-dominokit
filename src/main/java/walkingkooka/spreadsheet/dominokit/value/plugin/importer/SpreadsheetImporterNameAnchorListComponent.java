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
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either impress or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.dominokit.value.plugin.importer;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.plugin.PluginNameAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.plugin.PluginNameAnchorListComponentContexts;
import walkingkooka.spreadsheet.dominokit.value.plugin.PluginNameAnchorListComponentDelegator;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterAlias;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterAliasSet;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterInfo;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterInfoSet;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterName;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * Holds a list of anchors for each given {@link SpreadsheetImporterName}.
 */
public final class SpreadsheetImporterNameAnchorListComponent implements ValueComponent<HTMLDivElement, SpreadsheetImporterName, SpreadsheetImporterNameAnchorListComponent>,
    PluginNameAnchorListComponentDelegator<SpreadsheetImporterNameAnchorListComponent, SpreadsheetImporterName, SpreadsheetImporterInfo, SpreadsheetImporterInfoSet, SpreadsheetImporterSelector, SpreadsheetImporterAlias, SpreadsheetImporterAliasSet> {

    public static SpreadsheetImporterNameAnchorListComponent with(final String idPrefix,
                                                                  final SpreadsheetImporterNameAnchorListComponentContext context) {
        return new SpreadsheetImporterNameAnchorListComponent(
            CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix"),
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetImporterNameAnchorListComponent(final String idPrefix,
                                                       final SpreadsheetImporterNameAnchorListComponentContext context) {
        super();

        this.valueComponent = PluginNameAnchorListComponent.with(
            idPrefix,
            PluginNameAnchorListComponentContexts.basic(
                SpreadsheetMetadataPropertyName.IMPORTERS,
                context, // HasSpreadsheetMetadata
                context // HistoryContext
            )
        );
    }

    // PluginNameAnchorListComponentDelegator...........................................................................

    @Override
    public PluginNameAnchorListComponent<SpreadsheetImporterName, SpreadsheetImporterInfo, SpreadsheetImporterInfoSet, SpreadsheetImporterSelector, SpreadsheetImporterAlias, SpreadsheetImporterAliasSet> valueComponent() {
        return this.valueComponent;
    }

    private final PluginNameAnchorListComponent<SpreadsheetImporterName, SpreadsheetImporterInfo, SpreadsheetImporterInfoSet, SpreadsheetImporterSelector, SpreadsheetImporterAlias, SpreadsheetImporterAliasSet> valueComponent;
}
