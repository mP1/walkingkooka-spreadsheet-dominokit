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

package walkingkooka.spreadsheet.dominokit.ui.hidezerovalues;

import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Objects;

/**
 * Utility methods concerned with building UI elements for {@link SpreadsheetMetadataPropertyName#HIDE_ZERO_VALUES}.
 */
public final class HideZeroValues implements PublicStaticHelper {

    public static boolean isHideZeroValues(final HasSpreadsheetMetadata context) {
        Objects.requireNonNull(context, "context");

        return context.spreadsheetMetadata()
                .get(SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES)
                .orElse(false);
    }

    /**
     * A label or tool tip text for a menu item or button/icon.
     */
    public static String label(final boolean hide) {
        return hide ?
                "Show Zero Values" :
                "Hide Zero Values";
    }

    /**
     * Stop creation
     */
    private HideZeroValues() {
        throw new UnsupportedOperationException();
    }
}
