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

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.link.CardLinkListComponent;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfo;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterProvider;
import walkingkooka.text.CaseKind;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.stream.Collectors;

/**
 * A component that list all available {@link walkingkooka.spreadsheet.format.SpreadsheetFormatterName} by querying {@link SpreadsheetFormatterProvider#spreadsheetFormatterInfos()}.
 */
public final class SpreadsheetFormatterNameLinkListComponent implements HtmlComponent<HTMLDivElement, SpreadsheetFormatterNameLinkListComponent> {

    static SpreadsheetFormatterNameLinkListComponent empty(final String id) {
        return new SpreadsheetFormatterNameLinkListComponent(id);
    }

    private SpreadsheetFormatterNameLinkListComponent(final String id) {
        super();
        this.list = CardLinkListComponent.with(
            id,
            "", // title
            CaseKind::kebabToTitle
        );
    }

    public void refresh(final SpreadsheetFormatterNameLinkListComponentContext context) {
        this.list.refresh(
            context.spreadsheetFormatterInfos()
                .stream()
                .map(SpreadsheetFormatterNameLinkListComponent::linkText)
                .collect(Collectors.toList()),
            SpreadsheetFormatterNameLinkListComponentCardLinkListComponentContext.with(
                context.formatterName(),
                context
            ) // context
        );
    }

    private static String linkText(final SpreadsheetFormatterInfo info) {
        return info.name()
            .value();
    }

    @Override
    public SpreadsheetFormatterNameLinkListComponent setCssText(final String css) {
        this.list.setCssText(css);
        return this;
    }

    @Override
    public SpreadsheetFormatterNameLinkListComponent setCssProperty(final String name,
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
        return HtmlComponent.hasFocus(this.element()) ||
            this.list.isEditing();
    }

    @Override
    public HTMLDivElement element() {
        return this.list.element();
    }

    private final CardLinkListComponent list;

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
