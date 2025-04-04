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

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.validation.provider.ValidatorInfoSet;

/**
 * The event payload used by {@link ValidatorFetcherWatchers}.
 */
final class ValidatorFetcherWatchersInfoSetEvent extends FetcherWatchersEvent<ValidatorFetcherWatcher> {

    private ValidatorFetcherWatchersInfoSetEvent(final SpreadsheetId id,
                                                 final ValidatorInfoSet infos,
                                                 final AppContext context) {
        super(context);
        this.id = id;
        this.infos = infos;
    }

    static ValidatorFetcherWatchersInfoSetEvent with(final SpreadsheetId id,
                                                     final ValidatorInfoSet infos,
                                                     final AppContext context) {
        return new ValidatorFetcherWatchersInfoSetEvent(
            id,
            infos,
            context
        );
    }

    @Override
    void fire(final ValidatorFetcherWatcher watcher) {
        watcher.onValidatorInfoSet(
            this.id,
            this.infos,
            this.context
        );
    }

    private final SpreadsheetId id;

    private final ValidatorInfoSet infos;

    @Override
    public String toString() {
        return this.id + " " + this.infos;
    }
}
