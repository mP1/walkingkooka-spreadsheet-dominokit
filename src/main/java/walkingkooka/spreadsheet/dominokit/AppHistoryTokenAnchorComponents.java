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

package walkingkooka.spreadsheet.dominokit;

import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetNameHistoryToken;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetNameDialogComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetName;

import java.util.Optional;

/**
 * A collection of factory methods to create links that appear in the app layout.
 */
final class AppHistoryTokenAnchorComponents implements PublicStaticHelper {

    /**
     * A link that when clicked pushes a history token which shows the File browser dialog.
     */
    static HistoryTokenAnchorComponent files() {
        return HistoryToken.spreadsheetListSelect(HistoryTokenOffsetAndCount.EMPTY)
            .link("files")
            .setTextContent("Files");
    }

    // name.............................................................................................................

    /**
     * A {@link HistoryTokenAnchorComponent} which is updated to something like #/1/SpreadsheetName/rename, which
     * when clicked will open the {@link SpreadsheetNameDialogComponent}.
     */
    static HistoryTokenAnchorComponent spreadsheetName(final AppContext context) {
        final HistoryTokenAnchorComponent anchor = context.historyToken()
            .link("spreadsheetNameRename");

        context.addHistoryTokenWatcher(
            (previous, c) -> {
                final HistoryToken historyToken = c.historyToken();

                SpreadsheetNameHistoryToken nameHistoryToken = null;
                String nameText = "";

                if (historyToken instanceof SpreadsheetNameHistoryToken) {
                    nameHistoryToken = historyToken.cast(SpreadsheetNameHistoryToken.class);
                    final SpreadsheetName name = nameHistoryToken.name();

                    nameHistoryToken = HistoryToken.spreadsheetRenameSelect(
                        nameHistoryToken.id(),
                        name
                    );
                    nameText = name.text();
                }
                anchor.setHistoryToken(
                    Optional.ofNullable(nameHistoryToken)
                );
                anchor.setTextContent(nameText);
            }
        );
        return anchor;
    }

    /**
     * Stop creation
     */
    private AppHistoryTokenAnchorComponents() {
        throw new UnsupportedOperationException();
    }
}
