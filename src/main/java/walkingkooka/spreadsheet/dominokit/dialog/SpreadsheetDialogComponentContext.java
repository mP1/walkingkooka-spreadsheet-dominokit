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

package walkingkooka.spreadsheet.dominokit.dialog;

import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;

/**
 * Base class for all {@link walkingkooka.Context} that accompanies a {@link SpreadsheetDialogComponent}
 */
public interface SpreadsheetDialogComponentContext extends HistoryContext,
    LoggingContext {

    /**
     * Helper that may be used to create a standard dialog for a {@link SpreadsheetSelection} and some action.
     */
    static String selectionDialogTitle(final SpreadsheetSelection selection,
                                       final String action) {
        Objects.requireNonNull(selection, "selection");
        CharSequences.failIfNullOrEmpty(action, "action");

        return selection.text() + ": " + action;
    }

    /**
     * Helper that may be used to create a standard dialog for a {@link SpreadsheetSelection} and a style view or edit.
     */
    static String selectionTextStylePropertyDialogTitle(final SpreadsheetSelection selection,
                                                        final TextStylePropertyName<?> propertyName) {
        Objects.requireNonNull(selection, "selection");
        Objects.requireNonNull(propertyName, "propertyName");

        return selectionDialogTitle(
            selection,
            CaseKind.kebabToTitle(
                propertyName.value()
            )
        );
    }

    /**
     * Helper that may be used to create a standard dialog title for a {@link SpreadsheetMetadataPropertyName}.
     */
    static String spreadsheetMetadataPropertyNameDialogTitle(final SpreadsheetMetadataPropertyName<?> propertyName) {
        Objects.requireNonNull(propertyName, "propertyName");

        final String text = propertyName.text();

        return "Spreadsheet: " + CaseKind.CAMEL.change(
            text,
            CaseKind.TITLE
        ) + "(" + text + ")";
    }
}
