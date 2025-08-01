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

package walkingkooka.spreadsheet.dominokit.plugin;

import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponent;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.PluginUploadSelectHistoryToken;

import java.util.Optional;

/**
 * An anchor that when clicked displays a file browser to select a file to upload.
 */
public final class PluginUploadSelectAnchorComponent implements AnchorComponentDelegator<PluginUploadSelectAnchorComponent, Boolean> {

    public static PluginUploadSelectAnchorComponent empty(final String id) {
        return new PluginUploadSelectAnchorComponent()
            .setId(id);
    }

    private PluginUploadSelectAnchorComponent() {
        this.component = HistoryTokenAnchorComponent.empty()
            .setDisabled(true);
    }

    @Override
    public PluginUploadSelectAnchorComponent setDisabled(final boolean disabled) {
        this.component.setHistoryToken(
            Optional.ofNullable(
                disabled ?
                    null :
                    HistoryToken.pluginUploadSelect()
            )
        );
        return this;
    }

    @Override
    public Optional<Boolean> value() {
        final HistoryToken historyToken = this.component.historyToken()
            .orElse(null);

        return Optional.ofNullable(
            historyToken instanceof PluginUploadSelectHistoryToken ?
                Boolean.TRUE :
                null
        );
    }

    @Override
    public PluginUploadSelectAnchorComponent setValue(final Optional<Boolean> value) {
        return this.setDisabled(
            value.orElse(false)
        );
    }

    // AnchorComponentDelegator.....................................................................................

    @Override
    public AnchorComponent<?, ?> anchorComponent() {
        return this.component;
    }

    private final HistoryTokenAnchorComponent component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
