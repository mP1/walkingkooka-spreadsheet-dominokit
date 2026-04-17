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

package walkingkooka.spreadsheet.dominokit.value.textstyle.border;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.ToStringTesting;
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.color.SpreadsheetColors;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

public abstract class BorderColorSharedComponentTestCase<C extends BorderColorSharedComponent<C>> implements TextStylePropertyComponentTesting<HTMLFieldSetElement, Color, C>,
    ComponentLifecycleMatcherTesting,
    ToStringTesting<C>,
    SpreadsheetMetadataTesting {

    static {
        SpreadsheetMetadata spreadsheetMetadata = SpreadsheetMetadataTesting.METADATA_EN_AU;

        for(int i = SpreadsheetColors.MIN; i < SpreadsheetColors.MAX; i++) {
            spreadsheetMetadata = spreadsheetMetadata.set(
                SpreadsheetMetadataPropertyName.numberedColor(i),
                Color.fromRgb(i)
            );
        }

        COLOR = spreadsheetMetadata.getOrFail(
            SpreadsheetMetadataPropertyName.numberedColor(
                SpreadsheetColors.MIN
            )
        );
        SPREADSHEET_METADATA = spreadsheetMetadata;
    }

    final static Color COLOR;

    final static SpreadsheetMetadata SPREADSHEET_METADATA;

    public BorderColorSharedComponentTestCase() {
        super();
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
