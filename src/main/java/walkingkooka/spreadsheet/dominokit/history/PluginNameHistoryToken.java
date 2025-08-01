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
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;
import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;
import java.util.Optional;

/**
 * Instances represent a token within a history hash including the {@link PluginName}
 */
public abstract class PluginNameHistoryToken extends PluginHistoryToken {

    PluginNameHistoryToken(final PluginName name) {
        super();

        this.name = Objects.requireNonNull(name, "name");
    }

    public final PluginName name() {
        return this.name;
    }

    final PluginName name;

    @Override //
    final HistoryToken parseNext(final String component,
                                 final TextCursor cursor) {
        final PluginName pluginName = PluginName.with(component);
        HistoryToken historyToken = this;

        switch (component) {
            case SELECT_STRING:
                historyToken = pluginSelect(pluginName);
                break;
            case DELETE_STRING:
                historyToken = this.delete();
                break;
            case FILE_STRING:
                historyToken = this.parseFile(cursor);
                break;
            case SAVE_STRING:
                historyToken = this.parseSave(cursor);
                break;
            default:
                cursor.end();
                historyToken = this;
                break;
        }

        return historyToken;
    }

    private HistoryToken parseFile(final TextCursor cursor) {
        final String text = parseUntilEmpty(cursor);

        return pluginFileView(
            this.name,
            Optional.ofNullable(
                text.isEmpty() ?
                    null :
                    JarEntryInfoName.with(
                        '/' + text
                    )
            )
        );
    }

    // UrlFragment......................................................................................................

    @Override //
    final UrlFragment pluginUrlFragment() {
        return UrlFragment.with(name.value())
            .appendSlashThen(
                this.pluginNameUrlFragment()
            );
    }

    // /plugin/PluginName/
    abstract UrlFragment pluginNameUrlFragment();
}
