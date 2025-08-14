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
 * When clicked, updates the {@link SpreadsheetMetadataPropertyName#SHOW_GRID_LINES} with the opposite of its current value.
 */
final class SpreadsheetToolbarComponentItemAnchorMetadataBooleanShowGridLines extends SpreadsheetToolbarComponentItemAnchorMetadataBoolean<SpreadsheetToolbarComponentItemAnchorMetadataBooleanShowGridLines> {

    static SpreadsheetToolbarComponentItemAnchorMetadataBooleanShowGridLines with(final SpreadsheetToolbarComponentContext context) {
        return new SpreadsheetToolbarComponentItemAnchorMetadataBooleanShowGridLines(context);
    }

    private SpreadsheetToolbarComponentItemAnchorMetadataBooleanShowGridLines(final SpreadsheetToolbarComponentContext context) {
        super(
            SpreadsheetToolbarComponent.showGridLinesIds(),
            Optional.of(
                SpreadsheetIcons.gridLines()
            ),
            "Show Grid Lines",
            "Show Grid Lines", // let refresh load tooltip
            context
        );
    }

    @Override
    SpreadsheetMetadataPropertyName<Boolean> propertyName() {
        return SpreadsheetMetadataPropertyName.SHOW_GRID_LINES;
    }

    @Override
    String tooltipText(final boolean disabled) {
        return disabled ?
            "Hide Grid Lines" :
            "Show Grid Lines";
    }
}
