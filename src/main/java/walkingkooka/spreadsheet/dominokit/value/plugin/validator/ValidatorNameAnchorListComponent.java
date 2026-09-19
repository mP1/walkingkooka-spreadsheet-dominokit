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

package walkingkooka.spreadsheet.dominokit.value.plugin.validator;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.plugin.PluginNameAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.plugin.PluginNameAnchorListComponentContexts;
import walkingkooka.spreadsheet.dominokit.value.plugin.PluginNameAnchorListComponentDelegator;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CharSequences;
import walkingkooka.validation.provider.ValidatorAlias;
import walkingkooka.validation.provider.ValidatorAliasSet;
import walkingkooka.validation.provider.ValidatorInfo;
import walkingkooka.validation.provider.ValidatorInfoSet;
import walkingkooka.validation.provider.ValidatorName;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Objects;

/**
 * Holds a list of anchors for each given {@link ValidatorName}.
 */
public final class ValidatorNameAnchorListComponent implements ValueComponent<HTMLDivElement, ValidatorName, ValidatorNameAnchorListComponent>,
    PluginNameAnchorListComponentDelegator<ValidatorNameAnchorListComponent, ValidatorName, ValidatorInfo, ValidatorInfoSet, ValidatorSelector, ValidatorAlias, ValidatorAliasSet> {

    public static ValidatorNameAnchorListComponent with(final String idPrefix,
                                                        final ValidatorNameAnchorListComponentContext context) {
        return new ValidatorNameAnchorListComponent(
            CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix"),
            Objects.requireNonNull(context, "context")
        );
    }

    private ValidatorNameAnchorListComponent(final String idPrefix,
                                             final ValidatorNameAnchorListComponentContext context) {
        super();

        this.valueComponent = PluginNameAnchorListComponent.with(
            idPrefix,
            PluginNameAnchorListComponentContexts.basic(
                SpreadsheetMetadataPropertyName.VALIDATION_VALIDATORS,
                context, // HasSpreadsheetMetadata
                context // HistoryContext
            )
        );
    }

    // PluginNameAnchorListComponentDelegator...........................................................................

    @Override
    public PluginNameAnchorListComponent<ValidatorName, ValidatorInfo, ValidatorInfoSet, ValidatorSelector, ValidatorAlias, ValidatorAliasSet> valueComponent() {
        return this.valueComponent;
    }

    private final PluginNameAnchorListComponent<ValidatorName, ValidatorInfo, ValidatorInfoSet, ValidatorSelector, ValidatorAlias, ValidatorAliasSet> valueComponent;
}
