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
import walkingkooka.spreadsheet.dominokit.AppContext;

/**
 * A token that represents a plugin list files dialog.
 * <pre>
 * /plugin/STAR/offset/1/count/10/
 * </pre>
 */
public final class PluginListSelectHistoryToken extends PluginListHistoryToken implements HistoryTokenWatcher {

    static PluginListSelectHistoryToken with(final HistoryTokenOffsetAndCount offsetAndCount) {
        return new PluginListSelectHistoryToken(offsetAndCount);
    }

    private PluginListSelectHistoryToken(final HistoryTokenOffsetAndCount offsetAndCount) {
        super(offsetAndCount);
    }

    // HasUrlFragment...................................................................................................

    @Override
    UrlFragment pluginListUrlFragment() {
        return UrlFragment.EMPTY;
    }

    // HistoryToken.....................................................................................................

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.spreadsheetListSelect(HistoryTokenOffsetAndCount.EMPTY);
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        this.loadPlugins(context);
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitPluginListSelect(this.offsetAndCount);
    }

}
