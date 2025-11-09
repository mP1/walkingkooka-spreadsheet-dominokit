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

import walkingkooka.validation.form.provider.FormHandlerInfoSet;

/**
 * The event payload used by {@link FormHandlerFetcherWatchers}.
 */
final class FormHandlerFetcherWatchersInfoSetEvent extends FetcherWatchersEvent<FormHandlerFetcherWatcher> {

    static FormHandlerFetcherWatchersInfoSetEvent with(final FormHandlerInfoSet infos) {
        return new FormHandlerFetcherWatchersInfoSetEvent(infos);
    }

    private FormHandlerFetcherWatchersInfoSetEvent(final FormHandlerInfoSet infos) {
        super();
        this.infos = infos;
    }

    @Override
    void fire(final FormHandlerFetcherWatcher watcher) {
        watcher.onFormHandlerInfoSet(this.infos);
    }

    private final FormHandlerInfoSet infos;

    @Override
    public String toString() {
        return this.infos.toString();
    }
}
