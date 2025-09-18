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

package walkingkooka.spreadsheet.dominokit.meta;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.tab.SpreadsheetTabsComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CaseKind;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Optional;

/**
 * A tab component that displays tabs for each of the given {@link SpreadsheetMetadataPropertyName}.
 */
public final class SpreadsheetMetadataPropertyNameTabsComponent implements HtmlComponentDelegator<HTMLDivElement, SpreadsheetMetadataPropertyNameTabsComponent> {

    /**
     * Creates an empty {@link SpreadsheetMetadataPropertyNameTabsComponent}.
     */
    public static SpreadsheetMetadataPropertyNameTabsComponent empty(final String id,
                                                                     final List<SpreadsheetMetadataPropertyName<?>> propertyNames,
                                                                     final SpreadsheetMetadataPropertyNameTabsComponentContext context) {
        return new SpreadsheetMetadataPropertyNameTabsComponent(
            id,
            propertyNames,
            context
        );
    }

    private SpreadsheetMetadataPropertyNameTabsComponent(final String id,
                                                         final List<SpreadsheetMetadataPropertyName<?>> propertyNames,
                                                         final SpreadsheetMetadataPropertyNameTabsComponentContext context) {
        this.tabsComponent = this.tabsComponentCreate(
            id,
            propertyNames,
            context
        );

        this.propertyNames = propertyNames;
    }

    // SpreadsheetTabsComponent.........................................................................................

    private SpreadsheetTabsComponent tabsComponentCreate(final String id,
                                                         final List<SpreadsheetMetadataPropertyName<?>> propertyNames,
                                                         final SpreadsheetMetadataPropertyNameTabsComponentContext context) {
        final SpreadsheetTabsComponent tabs = SpreadsheetTabsComponent.with(context);

        for (final SpreadsheetMetadataPropertyName<?> propertyName : propertyNames) {
            tabs.appendTab(
                id + propertyName.value(),
                tabTitle(propertyName)
            );
        }

        return tabs;
    }

    /**
     * Returns the text that will appear on a tab that when clicked switches to the given {@link SpreadsheetMetadataPropertyName}.
     * <pre>
     * SpreadsheetMetadataPropertyName.TEXT_FORMATTER -> Text Formatter
     * </pre>
     */
    private static String tabTitle(final SpreadsheetMetadataPropertyName<?> propertyName) {
        return CaseKind.CAMEL.change(
            propertyName.value()
                .replace("Formatter", "")
                .replace("Parser", ""),
            CaseKind.TITLE
        ).trim();
    }

    // refresh..........................................................................................................

    /**
     * Iterates over the links in each tab updating the link, disabling and activating as necessary.
     */
    public void refresh(final SpreadsheetMetadataPropertyNameTabsComponentContext context) {
        final SpreadsheetTabsComponent tabs = this.tabsComponent;
        final SpreadsheetMetadataPropertyName<?> propertyName = context.historyToken()
            .metadataPropertyName()
            .orElse(null);

        int i = 0;
        for (final SpreadsheetMetadataPropertyName<?> possible : this.propertyNames) {
            final HistoryTokenAnchorComponent anchor = tabs.anchor(i);

            final boolean match = possible.equals(propertyName);
            anchor.setDisabled(match);

            if (match) {
                tabs.setTab(i);
            } else {
                final HistoryToken historyToken = context.historyToken();
                anchor.setHistoryToken(
                    Optional.of(
                        historyToken.setMetadataPropertyName(possible)
                    )
                );
            }

            i++;
        }
    }

    private final List<SpreadsheetMetadataPropertyName<?>> propertyNames;

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.tabsComponent;
    }

    private final SpreadsheetTabsComponent tabsComponent;

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return HtmlComponent.hasFocus(this.element());
    }

    // TreePrinter......................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.tabsComponent.printTree(printer);
        }
        printer.outdent();
    }
}
