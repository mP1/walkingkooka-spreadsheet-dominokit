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

import elemental2.dom.HTMLAnchorElement;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentLike;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentLikeDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;

import java.util.Optional;

/**
 * An anchor that when clicked displays a file browser to select a file to upload.
 */
public final class PluginUploadSelectAnchorComponent implements HtmlElementComponent<HTMLAnchorElement, PluginUploadSelectAnchorComponent>,
        AnchorComponentLikeDelegator<PluginUploadSelectAnchorComponent> {

    public static PluginUploadSelectAnchorComponent empty() {
        return new PluginUploadSelectAnchorComponent();
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

    // AnchorComponentLikeDelegator.....................................................................................

    @Override
    public AnchorComponentLike<?> anchorComponentLike() {
        return this.component;
    }

    private final HistoryTokenAnchorComponent component;
}
