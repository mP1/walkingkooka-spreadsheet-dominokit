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

import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentLike;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentLikeDelegator;
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;

import java.util.Objects;
import java.util.Optional;

/**
 * An anchor that maps to {@link walkingkooka.spreadsheet.dominokit.history.PluginUploadSaveHistoryToken}.
 */
public final class PluginUploadSaveAnchorComponent implements AnchorComponentLikeDelegator<PluginUploadSaveAnchorComponent> {

    public static PluginUploadSaveAnchorComponent empty(final String id) {
        return new PluginUploadSaveAnchorComponent()
            .setId(id);
    }

    private PluginUploadSaveAnchorComponent() {
        this.component = HistoryTokenAnchorComponent.empty()
            .setDisabled(true);
    }

    public PluginUploadSaveAnchorComponent clearValue() {
        return this.setValue(Optional.empty());
    }

    public PluginUploadSaveAnchorComponent setValue(final Optional<BrowserFile> file) {
        Objects.requireNonNull(file, "file");

        this.component.setHistoryToken(
            file.map(
                HistoryToken::pluginUploadSave
            )
        );
        return this;
    }

    // AnchorComponentLikeDelegator.....................................................................................

    @Override
    public AnchorComponentLike<?> anchorComponentLike() {
        return this.component;
    }

    private final HistoryTokenAnchorComponent component;
}
