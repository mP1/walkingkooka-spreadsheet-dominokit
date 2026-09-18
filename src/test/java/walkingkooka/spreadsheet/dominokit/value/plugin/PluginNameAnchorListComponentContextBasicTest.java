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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.validation.provider.ValidatorAlias;
import walkingkooka.validation.provider.ValidatorAliasSet;
import walkingkooka.validation.provider.ValidatorInfo;
import walkingkooka.validation.provider.ValidatorInfoSet;
import walkingkooka.validation.provider.ValidatorName;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PluginNameAnchorListComponentContextBasicTest implements PluginNameAnchorListComponentContextTesting<PluginNameAnchorListComponentContextBasic<ValidatorName, ValidatorInfo, ValidatorInfoSet, ValidatorSelector, ValidatorAlias, ValidatorAliasSet>,
    ValidatorName, ValidatorInfo, ValidatorInfoSet, ValidatorSelector, ValidatorAlias, ValidatorAliasSet>,
    SpreadsheetMetadataTesting {

    @Test
    public void testWithNullSpreadsheetMetadataPropertyNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> PluginNameAnchorListComponentContextBasic.with(
                null,
                METADATA_EN_AU,
                HistoryContexts.fake()
            )
        );
    }

    @Test
    public void testWithNullHasSpreadsheetMetadataFails() {
        assertThrows(
            NullPointerException.class,
            () -> PluginNameAnchorListComponentContextBasic.with(
                SpreadsheetMetadataPropertyName.VALIDATION_VALIDATORS,
                null,
                HistoryContexts.fake()
            )
        );
    }

    @Test
    public void testWithNullHistoryContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> PluginNameAnchorListComponentContextBasic.with(
                SpreadsheetMetadataPropertyName.VALIDATION_VALIDATORS,
                METADATA_EN_AU,
                null
            )
        );
    }

    @Override
    public PluginNameAnchorListComponentContextBasic<ValidatorName, ValidatorInfo, ValidatorInfoSet, ValidatorSelector, ValidatorAlias, ValidatorAliasSet> createContext() {
        return PluginNameAnchorListComponentContextBasic.with(
            SpreadsheetMetadataPropertyName.VALIDATION_VALIDATORS,
            METADATA_EN_AU,
            new FakeHistoryContext() {
                @Override
                public Runnable addHistoryWatcher(final HistoryWatcher watcher) {
                    Objects.requireNonNull(watcher, "watcher");

                    throw new UnsupportedOperationException();
                }

                @Override
                public Runnable addHistoryWatcherOnce(final HistoryWatcher watcher) {
                    Objects.requireNonNull(watcher, "watcher");

                    throw new UnsupportedOperationException();
                }

                @Override
                public void pushHistoryToken(final HistoryToken token) {
                    Objects.requireNonNull(token, "token");

                    throw new UnsupportedOperationException();
                }
            }
        );
    }

    @Override
    public Class<PluginNameAnchorListComponentContextBasic<ValidatorName, ValidatorInfo, ValidatorInfoSet, ValidatorSelector, ValidatorAlias, ValidatorAliasSet>> type() {
        return Cast.to(PluginNameAnchorListComponentContextBasic.class);
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
