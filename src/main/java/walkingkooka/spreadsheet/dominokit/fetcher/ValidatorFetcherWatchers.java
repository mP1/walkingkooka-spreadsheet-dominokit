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

import walkingkooka.validation.provider.ValidatorInfo;
import walkingkooka.validation.provider.ValidatorInfoSet;

public final class ValidatorFetcherWatchers extends FetcherWatchers<ValidatorFetcherWatcher>
    implements ValidatorFetcherWatcher {

    public static ValidatorFetcherWatchers empty() {
        return new ValidatorFetcherWatchers();
    }

    private ValidatorFetcherWatchers() {
        super();
    }

    @Override
    public void onValidatorInfo(final ValidatorInfo info) {
        this.fire(
            ValidatorFetcherWatchersInfoEvent.with(info)
        );
    }

    @Override
    public void onValidatorInfoSet(final ValidatorInfoSet infos) {
        this.fire(
            ValidatorFetcherWatchersInfoSetEvent.with(infos)
        );
    }
}
