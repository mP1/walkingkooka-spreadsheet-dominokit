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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Optional;

/**
 * When clicked, updates the {@link SpreadsheetMetadataPropertyName#AUTO_HIDE_SCROLLBARS} with the opposite of its current value.
 */
final class ToolbarComponentItemAnchorMetadataBooleanAutoHideScrollbars extends ToolbarComponentItemAnchorMetadataBoolean<ToolbarComponentItemAnchorMetadataBooleanAutoHideScrollbars> {

    static ToolbarComponentItemAnchorMetadataBooleanAutoHideScrollbars with(final ToolbarComponentContext context) {
        return new ToolbarComponentItemAnchorMetadataBooleanAutoHideScrollbars(context);
    }

    private ToolbarComponentItemAnchorMetadataBooleanAutoHideScrollbars(final ToolbarComponentContext context) {
        super(
            ToolbarComponent.autoHideScrollbarsId(),
            Optional.of(
                SpreadsheetIcons.autoHideScrollbars()
            ),
            "Auto hide scrollbars",
            "Auto hide scrollbars", // let refresh load tooltip
            context
        );
    }

    @Override
    SpreadsheetMetadataPropertyName<Boolean> propertyName() {
        return SpreadsheetMetadataPropertyName.AUTO_HIDE_SCROLLBARS;
    }

    @Override
    String tooltipText(final boolean disabled) {
        return disabled ?
            "Always show scrollbars" :
            "Auto hide scrollbars";
    }
}
