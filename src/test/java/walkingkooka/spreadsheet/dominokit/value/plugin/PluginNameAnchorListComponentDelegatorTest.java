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

import walkingkooka.reflect.PackagePrivateClassTesting;
import walkingkooka.spreadsheet.dominokit.value.plugin.PluginNameAnchorListComponentDelegatorTest.TestPluginNameAnchorListComponentDelegator;
import walkingkooka.validation.provider.ValidatorAlias;
import walkingkooka.validation.provider.ValidatorAliasSet;
import walkingkooka.validation.provider.ValidatorInfo;
import walkingkooka.validation.provider.ValidatorInfoSet;
import walkingkooka.validation.provider.ValidatorName;
import walkingkooka.validation.provider.ValidatorSelector;

public final class PluginNameAnchorListComponentDelegatorTest implements PackagePrivateClassTesting<TestPluginNameAnchorListComponentDelegator> {

    @Override
    public Class<TestPluginNameAnchorListComponentDelegator> type() {
        return TestPluginNameAnchorListComponentDelegator.class;
    }

    final static class TestPluginNameAnchorListComponentDelegator implements PluginNameAnchorListComponentDelegator<TestPluginNameAnchorListComponentDelegator, ValidatorName, ValidatorInfo, ValidatorInfoSet, ValidatorSelector, ValidatorAlias, ValidatorAliasSet> {

        @Override
        public PluginNameAnchorListComponent<ValidatorName, ValidatorInfo, ValidatorInfoSet, ValidatorSelector, ValidatorAlias, ValidatorAliasSet> valueComponent() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }
}
