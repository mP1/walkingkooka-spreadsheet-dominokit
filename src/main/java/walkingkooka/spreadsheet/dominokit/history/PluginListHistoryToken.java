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

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.plugin.PluginName;
import walkingkooka.spreadsheet.dominokit.fetcher.HasPluginFetcher;
import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;

public abstract class PluginListHistoryToken extends PluginHistoryToken {

    PluginListHistoryToken(final HistoryTokenOffsetAndCount offsetAndCount) {
        super();
        this.offsetAndCount = Objects.requireNonNull(
            offsetAndCount,
            "offsetAndCount"
        );
    }

    final HistoryTokenOffsetAndCount offsetAndCount;

    // HasUrlFragment...................................................................................................

    @Override //
    final UrlFragment pluginUrlFragment() {
        return countAndOffsetUrlFragment(
            offsetAndCount,
            this.pluginListUrlFragment()
        );
    }

    abstract UrlFragment pluginListUrlFragment();

    // HistoryToken.....................................................................................................

    @Override //
    final HistoryToken parseNext(final String component,
                                 final TextCursor cursor) {
        HistoryToken historyToken = this;

        switch (component) {
            case WILDCARD_STRING:
                historyToken = this.parseOffsetCountReload(cursor);
                break;
            default:
                historyToken = pluginSelect(
                    PluginName.with(component)
                );
                break;
        }

        return historyToken;
    }

    final void loadPlugins(final HasPluginFetcher fetcher) {
        fetcher.pluginFetcher()
            .getPluginFilter(
                "*", // query
                this.count(),
                this.count()
            );
    }
}
