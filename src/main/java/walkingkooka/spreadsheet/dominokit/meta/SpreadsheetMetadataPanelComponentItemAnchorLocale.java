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

import walkingkooka.collect.set.Sets;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Locale;

/**
 * A {@link SpreadsheetMetadataPanelComponentItemAnchor} that displays a link which probably opens a dialog for editing to
 * edit {@link SpreadsheetMetadataPropertyName#LOCALE}.
 */
final class SpreadsheetMetadataPanelComponentItemAnchorLocale extends SpreadsheetMetadataPanelComponentItemAnchor<Locale> {

    static SpreadsheetMetadataPanelComponentItemAnchorLocale with(final SpreadsheetMetadataPanelComponentContext context) {
        return new SpreadsheetMetadataPanelComponentItemAnchorLocale(context);
    }

    private SpreadsheetMetadataPanelComponentItemAnchorLocale(final SpreadsheetMetadataPanelComponentContext context) {
        super(
            SpreadsheetMetadataPropertyName.LOCALE,
            context
        );
    }

    @Override
    void refreshAnchor() {
        final SpreadsheetMetadataPanelComponentContext context = this.context;

        final String flag = context.spreadsheetMetadata()
            .getIgnoringDefaults(SpreadsheetMetadataPropertyName.LOCALE)
            .map(Locale::getCountry)
            .orElse(null);

        this.anchor.setFlags(
            null != flag ?
                Sets.of(flag) :
                Sets.empty()
        );
    }
}
