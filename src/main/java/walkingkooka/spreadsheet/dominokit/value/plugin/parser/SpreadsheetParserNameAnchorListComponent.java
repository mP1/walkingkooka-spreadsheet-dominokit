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

package walkingkooka.spreadsheet.dominokit.value.plugin.parser;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.link.CardAnchorListComponent;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserInfo;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserProvider;
import walkingkooka.text.CaseKind;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.stream.Collectors;

/**
 * A component that list all available {@link SpreadsheetParserName} by querying {@link SpreadsheetParserProvider#spreadsheetParserInfos()}.
 */
public final class SpreadsheetParserNameAnchorListComponent implements HtmlComponentDelegator<HTMLDivElement, SpreadsheetParserNameAnchorListComponent> {

    static SpreadsheetParserNameAnchorListComponent empty(final String id) {
        return new SpreadsheetParserNameAnchorListComponent(id);
    }

    private SpreadsheetParserNameAnchorListComponent(final String id) {
        super();
        this.list = CardAnchorListComponent.with(
            id,
            "", // title
            CaseKind::kebabToTitle
        );
    }

    public void refresh(final SpreadsheetParserNameAnchorListComponentContext context) {
        this.list.refresh(
            context.spreadsheetParserInfos()
                .stream()
                .map(SpreadsheetParserNameAnchorListComponent::linkText)
                .collect(Collectors.toList()),
            SpreadsheetParserNameAnchorListComponentCardAnchorListComponentContext.with(
                context.parserName(),
                context
            ) // context
        );
    }

    private static String linkText(final SpreadsheetParserInfo info) {
        return info.name()
            .value();
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return this.list.isEditing();
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.list.htmlComponent();
    }

    private final CardAnchorListComponent list;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());

        printer.indent();
        {
            this.list.printTree(printer);
        }
        printer.outdent();
    }
}
