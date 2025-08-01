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

/**
 * Base class for several plugin upload related {@link HistoryToken}.
 * <br>
 * Note that the urlFragment is <code>/plugin-upload</code> and not <code>/plugin/upload</code> because upload is also a
 * valid {@link PluginName}.
 */
public abstract class PluginUploadHistoryToken extends PluginHistoryToken {

    PluginUploadHistoryToken() {
        super();
    }

    // HistoryToken.....................................................................................................

    //
    // /plugin-upload
    //
    @Override //
    final UrlFragment pluginUrlFragment() {
        return UrlFragment.SLASH.append(PLUGIN_UPLOAD)
            .appendSlashThen(this.pluginUploadUrlFragment());
    }

    abstract UrlFragment pluginUploadUrlFragment();
}
