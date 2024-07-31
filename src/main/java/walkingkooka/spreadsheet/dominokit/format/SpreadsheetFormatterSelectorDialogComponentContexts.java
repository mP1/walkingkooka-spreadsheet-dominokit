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

package walkingkooka.spreadsheet.dominokit.format;

import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.dominokit.AppContext;

/**
 * A collection of {@link SpreadsheetFormatterSelectorDialogComponentContext} factory methods.
 */
public final class SpreadsheetFormatterSelectorDialogComponentContexts implements PublicStaticHelper {

    /**
     * {@see SpreadsheetFormatterSelectorDialogComponentContextBasicCell}
     */
    public static SpreadsheetFormatterSelectorDialogComponentContext cell(final AppContext context) {
        return SpreadsheetFormatterSelectorDialogComponentContextBasicCell.with(context);
    }

    /**
     * {@see SpreadsheetFormatterSelectorDialogComponentContextBasicMetadata}
     */
    public static SpreadsheetFormatterSelectorDialogComponentContext metadata(final AppContext context) {
        return SpreadsheetFormatterSelectorDialogComponentContextBasicMetadata.with(context);
    }

    private SpreadsheetFormatterSelectorDialogComponentContexts() {
        throw new UnsupportedOperationException();
    }
}
