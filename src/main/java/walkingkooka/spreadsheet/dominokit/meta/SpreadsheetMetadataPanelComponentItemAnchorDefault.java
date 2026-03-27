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

package walkingkooka.spreadsheet.dominokit.meta;

import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

/**
 * A {@link SpreadsheetMetadataPanelComponentItem} that displays a link which probably opens a dialog for editing.
 */
final class SpreadsheetMetadataPanelComponentItemAnchorDefault<T> extends SpreadsheetMetadataPanelComponentItemAnchor<T> {

    static <T> SpreadsheetMetadataPanelComponentItemAnchorDefault<T> with(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                          final SpreadsheetMetadataPanelComponentContext context) {
        return new SpreadsheetMetadataPanelComponentItemAnchorDefault<>(
            propertyName,
            context
        );
    }

    private SpreadsheetMetadataPanelComponentItemAnchorDefault(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                               final SpreadsheetMetadataPanelComponentContext context) {
        super(
            propertyName,
            context
        );
    }

    @Override
    void refreshAnchor() {
        // NOP
    }
}
