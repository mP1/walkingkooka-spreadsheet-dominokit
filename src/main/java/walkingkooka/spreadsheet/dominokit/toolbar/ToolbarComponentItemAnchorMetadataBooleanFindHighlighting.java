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

package walkingkooka.spreadsheet.dominokit.toolbar;

import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.find.FindHighlighting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Optional;

/**
 * When clicked, updates the {@link SpreadsheetMetadataPropertyName#FIND_HIGHLIGHTING} with the opposite of its current value.
 */
final class ToolbarComponentItemAnchorMetadataBooleanFindHighlighting extends ToolbarComponentItemAnchorMetadataBoolean<ToolbarComponentItemAnchorMetadataBooleanFindHighlighting> {

    static ToolbarComponentItemAnchorMetadataBooleanFindHighlighting with(final ToolbarComponentContext context) {
        return new ToolbarComponentItemAnchorMetadataBooleanFindHighlighting(context);
    }

    private ToolbarComponentItemAnchorMetadataBooleanFindHighlighting(final ToolbarComponentContext context) {
        super(
            ToolbarComponent.findHighlightId(),
            Optional.of(
                SpreadsheetIcons.highlight()
            ),
            "Highlight",
            "Highlight", // let refresh load tooltip
            context
        );
    }

    @Override
    SpreadsheetMetadataPropertyName<Boolean> propertyName() {
        return SpreadsheetMetadataPropertyName.FIND_HIGHLIGHTING;
    }

    @Override
    String tooltipText(final boolean disabled) {
        return FindHighlighting.label(disabled);
    }
}
