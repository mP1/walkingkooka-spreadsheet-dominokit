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

package walkingkooka.spreadsheet.dominokit.net;

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;

import java.util.Optional;

public final class SpreadsheetLabelMappingFetcherWatchers extends FetcherWatchers<SpreadsheetLabelMappingFetcherWatcher>
        implements SpreadsheetLabelMappingFetcherWatcher{

    public static SpreadsheetLabelMappingFetcherWatchers empty() {
        return new SpreadsheetLabelMappingFetcherWatchers();
    }

    private SpreadsheetLabelMappingFetcherWatchers() {
        super();
    }

    @Override
    public void onSpreadsheetLabelMapping(final SpreadsheetId id,
                                          final Optional<SpreadsheetLabelMapping> mapping,
                                          final AppContext context) {
        this.fire(
                SpreadsheetLabelMappingFetcherWatchersEvent.with(
                        id,
                        mapping,
                        context
                )
        );
    }
}
