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

package walkingkooka.spreadsheet.dominokit.navigate;

import elemental2.dom.HTMLAnchorElement;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetNameHistoryToken;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;
import java.util.Optional;

public final class SpreadsheetNavigateLinkComponent implements HtmlComponentDelegator<HTMLAnchorElement, SpreadsheetNavigateLinkComponent>,
    HistoryTokenWatcher {

    public static SpreadsheetNavigateLinkComponent with(final HistoryContext context) {
        return new SpreadsheetNavigateLinkComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetNavigateLinkComponent(final HistoryContext context) {
        context.addHistoryTokenWatcher(this);

        this.anchor = HistoryTokenAnchorComponent.empty()
            .setTextContent("Navigate");
    }

    // id...............................................................................................................

    public String id() {
        return this.anchor.id();
    }

    public SpreadsheetNavigateLinkComponent setId(final String id) {
        this.anchor.setId(id);
        return this;
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLAnchorElement, ?> htmlComponent() {
        return this.anchor;
    }

    private final HistoryTokenAnchorComponent anchor;

    @Override
    public boolean isEditing() {
        return this.htmlComponent().isEditing();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());

        printer.indent();
        {
            this.anchor.printTree(printer);
        }
        printer.outdent();
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        final HistoryToken historyToken = context.historyToken();

        this.anchor.setValue(
            Optional.ofNullable(
                historyToken instanceof SpreadsheetNameHistoryToken ?
                    historyToken.setNavigation(
                        Optional.empty()
                    ) :
                    null
            )
        );
    }
}
