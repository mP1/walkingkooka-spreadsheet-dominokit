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
import walkingkooka.naming.Name;
import walkingkooka.plugin.PluginAliasLike;
import walkingkooka.plugin.PluginAliasSetLike;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.plugin.PluginSelectorLike;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContextTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CaseKind;

import java.util.Optional;

public interface PluginAliasSetLikeDialogComponentContextTesting<C extends PluginAliasSetLikeDialogComponentContext<N, I, IS, S, A, AS>, N extends Name & Comparable<N>,
    I extends PluginInfoLike<I, N>,
    IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
    S extends PluginSelectorLike<N>,
    A extends PluginAliasLike<N, S, A>,
    AS extends PluginAliasSetLike<N, I, IS, S, A, AS>> extends DialogComponentContextTesting<C>,
    ComponentLifecycleMatcherTesting {

    @Test
    default void testIsMatchWithSpreadsheetMetadataPropertySelectHistoryTokenWrong() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.SPREADSHEET_ID
            ),
            false
        );
    }

    @Test
    default void testIsMatchWithSpreadsheetCellSelectHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            false
        );
    }

    @Test
    default void testIsMatchWithSpreadsheetMetadataPropertySelectHistoryTokenWithMetadataPropertyName() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                this.metadataPropertyName()
            ),
            true
        );
    }

    @Test
    default void testIsMatchWithSpreadsheetMetadataPropertySaveHistoryTokenWithMetadataPropertyName() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                this.metadataPropertyName(),
                Optional.empty()
            ),
            false
        );
    }

    // Context..........................................................................................................

    @Override
    default String typeNameSuffix() {
        final SpreadsheetMetadataPropertyName<AS> name = this.metadataPropertyName();

        return name.type()
            .getSimpleName() +
            CaseKind.CAMEL.change(
                name.text(),
                CaseKind.PASCAL
            );
    }

    SpreadsheetMetadataPropertyName<AS> metadataPropertyName();
}
