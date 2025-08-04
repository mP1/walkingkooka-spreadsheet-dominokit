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

package walkingkooka.spreadsheet.dominokit.parser;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.link.SpreadsheetCardLinkListComponent;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfo;
import walkingkooka.spreadsheet.parser.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserProvider;
import walkingkooka.text.CaseKind;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.stream.Collectors;

/**
 * A component that list all available {@link SpreadsheetParserName} by querying {@link SpreadsheetParserProvider#spreadsheetParserInfos()}.
 */
public final class SpreadsheetParserNameLinkListComponent implements HtmlComponent<HTMLDivElement, SpreadsheetParserNameLinkListComponent> {

    static SpreadsheetParserNameLinkListComponent empty(final String id) {
        return new SpreadsheetParserNameLinkListComponent(id);
    }

    private SpreadsheetParserNameLinkListComponent(final String id) {
        super();
        this.list = SpreadsheetCardLinkListComponent.with(
            id,
            "", // title
            CaseKind::kebabToTitle
        );
    }

    public void refresh(final SpreadsheetParserNameLinkListComponentContext context) {
        this.list.refresh(
            context.spreadsheetParserInfos()
                .stream()
                .map(SpreadsheetParserNameLinkListComponent::linkText)
                .collect(Collectors.toList()),
            SpreadsheetParserNameLinkListComponentSpreadsheetCardLinkListComponentContext.with(
                context.parserName(),
                context
            ) // context
        );
    }

    private static String linkText(final SpreadsheetParserInfo info) {
        return info.name()
            .value();
    }

    @Override
    public SpreadsheetParserNameLinkListComponent setCssText(final String css) {
        this.list.setCssText(css);
        return this;
    }

    @Override
    public SpreadsheetParserNameLinkListComponent setCssProperty(final String name,
                                                                 final String value) {
        this.list.setCssProperty(
            name,
            value
        );
        return this;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return this.list.isEditing();
    }

    @Override
    public HTMLDivElement element() {
        return this.list.element();
    }

    private final SpreadsheetCardLinkListComponent list;

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
