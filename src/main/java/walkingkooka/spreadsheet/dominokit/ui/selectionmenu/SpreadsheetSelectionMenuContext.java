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

package walkingkooka.spreadsheet.dominokit.ui.selectionmenu;

import walkingkooka.Context;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.List;
import java.util.Set;

/**
 * A {@link Context} that provides answers when rendering a {@link walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenu}.
 */
public interface SpreadsheetSelectionMenuContext extends Context,
        HasSpreadsheetMetadata,
        HistoryTokenContext {

    /**
     * Returns all {@link SpreadsheetLabelMapping} for the given {@link SpreadsheetSelection}.
     */
    Set<SpreadsheetLabelMapping> labelMappings(final SpreadsheetSelection selection);

    /**
     * Returns the {@link HistoryToken} holding recent {@link walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern}.
     */
    List<HistoryToken> recentFormatPatterns();

    /**
     * Returns the {@link HistoryToken} holding recent {@link walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern}.
     */
    List<HistoryToken> recentParsePatterns();

    /**
     * Returns the id prefix which should be assigned to items in the menu.
     */
    String idPrefix();
}
