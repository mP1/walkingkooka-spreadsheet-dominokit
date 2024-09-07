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

import walkingkooka.convert.provider.ConverterInfoSet;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;

public final class ConverterFetcherWatchers extends FetcherWatchers<ConverterFetcherWatcher>
        implements ConverterFetcherWatcher {

    private ConverterFetcherWatchers() {
        super();
    }

    public static ConverterFetcherWatchers empty() {
        return new ConverterFetcherWatchers();
    }

    @Override
    public void onConverterInfoSet(final SpreadsheetId id,
                                   final ConverterInfoSet infos,
                                   final AppContext context) {
        this.fire(
                ConverterFetcherWatchersInfoSetEvent.with(
                        id,
                        infos,
                        context
                )
        );
    }
}
