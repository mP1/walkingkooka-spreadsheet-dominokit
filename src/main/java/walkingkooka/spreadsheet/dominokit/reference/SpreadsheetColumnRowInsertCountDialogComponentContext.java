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

package walkingkooka.spreadsheet.dominokit.reference;

import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.label.SpreadsheetLabelMappingDialogComponent;

/**
 * The {@link walkingkooka.Context} accompanying a {@link SpreadsheetLabelMappingDialogComponent}.
 */
public interface SpreadsheetColumnRowInsertCountDialogComponentContext extends
    SpreadsheetDialogComponentContext,
    ComponentLifecycleMatcher {

    /**
     * A label which is used for the title of the dialog box.
     */
    String dialogTitle();


    /**
     * Saves the given value, preparing the {@link walkingkooka.spreadsheet.dominokit.history.HistoryToken}
     * which will cause the column/row to be inserted.
     */
    void save(final int count);
}
