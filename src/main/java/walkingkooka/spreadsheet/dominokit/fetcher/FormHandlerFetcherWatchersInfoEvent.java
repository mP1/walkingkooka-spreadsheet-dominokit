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

import walkingkooka.validation.form.provider.FormHandlerInfo;

/**
 * The event payload used by {@link FormHandlerFetcherWatchers}.
 */
final class FormHandlerFetcherWatchersInfoEvent extends FetcherWatchersEvent<FormHandlerFetcherWatcher> {

    static FormHandlerFetcherWatchersInfoEvent with(final FormHandlerInfo info) {
        return new FormHandlerFetcherWatchersInfoEvent(info);
    }

    private FormHandlerFetcherWatchersInfoEvent(final FormHandlerInfo info) {
        super();
        this.info = info;
    }

    @Override
    void fire(final FormHandlerFetcherWatcher watcher) {
        watcher.onFormHandlerInfo(this.info);
    }

    private final FormHandlerInfo info;

    @Override
    public String toString() {
        return this.info.toString();
    }
}
