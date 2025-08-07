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
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;
import walkingkooka.text.cursor.TextCursor;

/**
 * A history token that displays a file browser allowing the user to upload a plugin *.JAR file.
 */
public final class PluginUploadSelectHistoryToken extends PluginUploadHistoryToken {

    /**
     * Singleton
     */
    final static PluginUploadSelectHistoryToken INSTANCE = new PluginUploadSelectHistoryToken();

    private PluginUploadSelectHistoryToken() {
        super();
    }

    // HistoryToken.....................................................................................................

    @Override
    HistoryToken parseNext(final String component,
                           final TextCursor cursor) {
        PluginUploadHistoryToken historyToken = this;

        switch (component) {
            case SAVE_STRING:
                historyToken = pluginUploadSave(
                    BrowserFile.parse(
                        parseUntilEmpty(
                            cursor,
                            true // skipLeadingSlash
                        )
                    )
                );
                break;
            case "":
            default:
                historyToken = this;
                break;
        }

        return historyToken; // ignore anything that follows /plugin-upload
    }

    //
    // /plugin-upload
    //
    @Override
    UrlFragment pluginUploadUrlFragment() {
        return UrlFragment.EMPTY;
    }

    // /plugin/Plugin123 -> /plugin

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.pluginListSelect(HistoryTokenOffsetAndCount.EMPTY);
    }
}
