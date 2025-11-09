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

package walkingkooka.spreadsheet.dominokit.fetcher;

import walkingkooka.convert.provider.ConverterInfoSet;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.convert.provider.MissingConverter;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Set;

public class FakeConverterFetcherWatcher extends FakeFetcherWatcher implements ConverterFetcherWatcher {

    @Override
    public void onConverterInfoSet(final ConverterInfoSet infos) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onVerify(final SpreadsheetId id,
                         final SpreadsheetMetadataPropertyName<ConverterSelector> metadataPropertyName,
                         final Set<MissingConverter> missingConverters) {
        throw new UnsupportedOperationException();
    }
}
