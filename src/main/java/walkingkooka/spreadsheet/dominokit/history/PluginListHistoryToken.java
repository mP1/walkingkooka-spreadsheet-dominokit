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

import java.util.OptionalInt;

public abstract class PluginListHistoryToken extends PluginHistoryToken {

    PluginListHistoryToken(final OptionalInt offset,
                           final OptionalInt count) {
        super();
        this.offset = offset;
        this.count = count;
    }

    // offset...........................................................................................................

    final OptionalInt offset;

    // count............................................................................................................

    final OptionalInt count;

    // HasUrlFragment...................................................................................................

    @Override //
    final UrlFragment pluginUrlFragment() {
        return countAndOffsetUrlFragment(
                this.offset,
                this.count,
                this.pluginListUrlFragment()
        );
    }

    abstract UrlFragment pluginListUrlFragment();

    // HistoryToken.....................................................................................................

    @Override //
    final HistoryToken parse0(final String component,
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
                .filter(
                        "*", // query
                        this.count()
                                .orElse(DEFAULT_OFFSET),
                        this.count()
                                .orElse(DEFAULT_COUNT)
                );
    }

    private final static int DEFAULT_OFFSET = 0;

    private final static int DEFAULT_COUNT = 20;
}
