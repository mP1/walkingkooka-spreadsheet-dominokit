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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.text.cursor.TextCursor;

import java.util.OptionalInt;

/**
 * A history token that displays a file browser allowing the user to upload a plugin *.JAR file.
 * <br>
 * Note that the urlFragment is <code>/plugin-upload</code> and not <code>/plugin/upload</code> because upload is also a
 * valid {@link PluginName}.
 */
public final class PluginUploadHistoryToken extends PluginHistoryToken {

    /**
     * Singleton
     */
    final static PluginUploadHistoryToken INSTANCE = new PluginUploadHistoryToken();

    private PluginUploadHistoryToken() {
        super();
    }

    // HistoryToken.....................................................................................................

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        return this; // ignore anything that follows /plugin-upload
    }

    //
    // /plugin-upload
    //
    @Override
    UrlFragment pluginUrlFragment() {
        return UrlFragment.SLASH.append(PLUGIN_UPLOAD);
    }

    // /plugin/Plugin123 -> /plugin

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.pluginListSelect(
                OptionalInt.empty(), // offset
                OptionalInt.empty() // count
        );
    }

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        // NOP
    }
}
