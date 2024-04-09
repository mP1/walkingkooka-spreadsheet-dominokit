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

package walkingkooka.spreadsheet.dominokit.ui.spreadsheetname;

import elemental2.dom.HTMLAnchorElement;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetNameHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Optional;

public final class SpreadsheetNameAnchorComponent implements HtmlElementComponent<HTMLAnchorElement, SpreadsheetNameAnchorComponent>,
        HistoryTokenWatcher {

    public static SpreadsheetNameAnchorComponent empty() {
        return new SpreadsheetNameAnchorComponent();
    }

    private SpreadsheetNameAnchorComponent() {
        this.anchor = HistoryTokenAnchorComponent.empty();
    }

    @Override
    public HTMLAnchorElement element() {
        return this.anchor.element();
    }

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        HistoryToken spreadsheetNameAnchor = null;

        final HistoryToken historyToken = context.historyToken();
        String nameText = "";

        if (historyToken instanceof SpreadsheetNameHistoryToken) {
            final SpreadsheetNameHistoryToken nameHistoryToken = historyToken.cast(SpreadsheetNameHistoryToken.class);
            final SpreadsheetName name = nameHistoryToken.name();

            spreadsheetNameAnchor = HistoryToken.metadataPropertySelect(
                    nameHistoryToken.id(),
                    name,
                    SpreadsheetMetadataPropertyName.SPREADSHEET_NAME
            );
            nameText = name.text();
        }

        this.anchor.setHistoryToken(
                Optional.ofNullable(
                        spreadsheetNameAnchor
                )
        );
        this.anchor.setTextContent(nameText);
    }

    private final HistoryTokenAnchorComponent anchor;
}
