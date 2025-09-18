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
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.link.CardLinkListComponent;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterInfo;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterProvider;
import walkingkooka.text.CaseKind;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.stream.Collectors;

/**
 * A component that list all available {@link SpreadsheetFormatterName} by querying {@link SpreadsheetFormatterProvider#spreadsheetFormatterInfos()}.
 */
public final class SpreadsheetFormatterNameLinkListComponent implements HtmlComponentDelegator<HTMLDivElement, SpreadsheetFormatterNameLinkListComponent> {

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

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return HtmlComponent.hasFocus(this.element()) ||
            this.list.isEditing();
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.list;
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
