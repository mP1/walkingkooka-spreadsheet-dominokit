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

import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.spreadsheet.convert.provider.MissingConverter;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Set;

/**
 * The event payload used by {@link ConverterFetcherWatchers}.
 */
final class ConverterFetcherWatchersVerifyEvent extends FetcherWatchersEvent<ConverterFetcherWatcher> {

    static ConverterFetcherWatchersVerifyEvent with(final SpreadsheetId id,
                                                    final SpreadsheetMetadataPropertyName<ConverterSelector> metadataPropertyName,
                                                    final Set<MissingConverter> missingConverters) {
        return new ConverterFetcherWatchersVerifyEvent(
            id,
            metadataPropertyName,
            missingConverters
        );
    }

    private ConverterFetcherWatchersVerifyEvent(final SpreadsheetId id,
                                                final SpreadsheetMetadataPropertyName<ConverterSelector> metadataPropertyName,
                                                final Set<MissingConverter> missingConverters) {
        super();
        this.id = id;
        this.metadataPropertyName = metadataPropertyName;
        this.missingConverters = missingConverters;
    }

    @Override
    void fire(final ConverterFetcherWatcher watcher) {
        watcher.onVerify(
            this.id,
            this.metadataPropertyName,
            this.missingConverters
        );
    }

    private final SpreadsheetId id;
    private final SpreadsheetMetadataPropertyName<ConverterSelector> metadataPropertyName;
    private final Set<MissingConverter> missingConverters;

    @Override
    public String toString() {
        return this.id + " " + this.metadataPropertyName + " " + this.missingConverters;
    }
}
