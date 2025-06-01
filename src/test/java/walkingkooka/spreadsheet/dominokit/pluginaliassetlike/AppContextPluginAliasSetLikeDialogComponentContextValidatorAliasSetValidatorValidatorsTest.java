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

package walkingkooka.spreadsheet.dominokit.pluginaliassetlike;


import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CaseKind;
import walkingkooka.validation.provider.ValidatorAlias;
import walkingkooka.validation.provider.ValidatorAliasSet;
import walkingkooka.validation.provider.ValidatorInfo;
import walkingkooka.validation.provider.ValidatorInfoSet;
import walkingkooka.validation.provider.ValidatorName;
import walkingkooka.validation.provider.ValidatorSelector;

public final class AppContextPluginAliasSetLikeDialogComponentContextValidatorAliasSetValidatorValidatorsTest implements PluginAliasSetLikeDialogComponentContextTesting<AppContextPluginAliasSetLikeDialogComponentContextValidatorAliasSetValidatorValidators,
    ValidatorName,
    ValidatorInfo,
    ValidatorInfoSet,
    ValidatorSelector,
    ValidatorAlias,
    ValidatorAliasSet> {

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertySelectHistoryTokenConverters() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.CONVERTERS
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertySelectHistoryTokenValidators() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.VALIDATORS
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertySelectHistoryTokenValidatorValidators() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.VALIDATOR_VALIDATORS
            ),
            true
        );
    }

    @Override
    public AppContextPluginAliasSetLikeDialogComponentContextValidatorAliasSetValidatorValidators createContext() {
        return AppContextPluginAliasSetLikeDialogComponentContextValidatorAliasSetValidatorValidators.with(AppContexts.fake());
    }

    // Context..........................................................................................................

    @Override
    public String typeNameSuffix() {
        return ValidatorAliasSet.class.getSimpleName() +
            CaseKind.CAMEL.change(
                SpreadsheetMetadataPropertyName.VALIDATOR_VALIDATORS.text(),
                CaseKind.PASCAL
            );
    }

    // Class............................................................................................................

    @Override
    public Class<AppContextPluginAliasSetLikeDialogComponentContextValidatorAliasSetValidatorValidators> type() {
        return AppContextPluginAliasSetLikeDialogComponentContextValidatorAliasSetValidatorValidators.class;
    }
}
