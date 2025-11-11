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

import walkingkooka.Value;
import walkingkooka.net.UrlFragment;
import walkingkooka.plugin.PluginName;

import java.util.Objects;

/**
 * Represents an action to prompt the user to upload a plugin.
 */
public final class PluginSaveHistoryToken extends PluginNameHistoryToken implements Value<String> {

    static PluginSaveHistoryToken with(final PluginName name,
                                       final String value) {
        return new PluginSaveHistoryToken(
            Objects.requireNonNull(name, "name"),
            Objects.requireNonNull(value, "value")
        );
    }

    private PluginSaveHistoryToken(final PluginName name,
                                   final String value) {
        super(name);
        this.value = value;
    }

    @Override
    public String value() {
        return this.value;
    }

    final String value;

    // HistoryToken.....................................................................................................

    //
    // /plugin/PluginName123/save/XYZ
    //
    @Override
    UrlFragment pluginNameUrlFragment() {
        return saveUrlFragment(this.value);
    }

    // /plugin/Plugin123 -> /plugin

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.pluginListSelect(HistoryTokenOffsetAndCount.EMPTY);
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitPluginSave(
            this.name,
            this.value
        );
    }
}
