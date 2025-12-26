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

import elemental2.dom.EventListener;
import elemental2.dom.HTMLUListElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.dom.UlComponent;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetNameComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetName;

/**
 * A {@link SpreadsheetMetadataPanelComponentItem} that displays a {@link SpreadsheetNameComponent}.
 */
final class SpreadsheetMetadataPanelComponentItemSpreadsheetName extends SpreadsheetMetadataPanelComponentItem<SpreadsheetName, SpreadsheetMetadataPanelComponentItemSpreadsheetName, HTMLUListElement> {

    static SpreadsheetMetadataPanelComponentItemSpreadsheetName with(final SpreadsheetMetadataPanelComponentContext context) {
        return new SpreadsheetMetadataPanelComponentItemSpreadsheetName(
            context
        );
    }

    private final static SpreadsheetMetadataPropertyName<SpreadsheetName> PROPERTY_NAME = SpreadsheetMetadataPropertyName.SPREADSHEET_NAME;

    private SpreadsheetMetadataPanelComponentItemSpreadsheetName(final SpreadsheetMetadataPanelComponentContext context) {
        super(
            PROPERTY_NAME,
            context
        );

        final UlComponent list = this.ul();
        this.list = list;

        this.spreadsheetNameComponent = SpreadsheetNameComponent.empty()
            .setId(SpreadsheetMetadataPanelComponent.id(PROPERTY_NAME) + "-TextBox")
            .addValueWatcher2(
                (v) -> this.save(
                    v.map(SpreadsheetName::value)
                        .orElse("")
                )
            );
        list.appendChild(
            this.li()
                .appendChild(
                    this.spreadsheetNameComponent
                )
        );
    }

    @Override//
    SpreadsheetMetadataPanelComponentItemSpreadsheetName addFocusListener(final EventListener listener) {
        this.spreadsheetNameComponent.addFocusListener(listener);
        return this;
    }

    @Override
    void focus() {
        this.spreadsheetNameComponent.focus();
    }

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final RefreshContext context) {
        this.spreadsheetNameComponent.setValue(
            this.context.spreadsheetMetadata()
                .get(this.propertyName)
        );
    }

    private final SpreadsheetNameComponent spreadsheetNameComponent;

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLUListElement, ?> htmlComponent() {
        return this.list;
    }

    private final UlComponent list;
}
