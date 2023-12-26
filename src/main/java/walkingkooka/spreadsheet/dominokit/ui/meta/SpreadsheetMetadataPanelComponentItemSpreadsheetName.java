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

package walkingkooka.spreadsheet.dominokit.ui.meta;

import elemental2.dom.HTMLUListElement;
import org.dominokit.domino.ui.elements.UListElement;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ui.spreadsheetname.SpreadsheetNameComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

/**
 * A {@link SpreadsheetMetadataPanelComponentItem} that displays a {@link walkingkooka.spreadsheet.dominokit.ui.spreadsheetname.SpreadsheetNameComponent}.
 */
final class SpreadsheetMetadataPanelComponentItemSpreadsheetName extends SpreadsheetMetadataPanelComponentItem<SpreadsheetName> {

    static SpreadsheetMetadataPanelComponentItemSpreadsheetName with(final SpreadsheetMetadataPanelComponentContext context) {
        checkContext(context);

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

        final UListElement list = this.uListElement();
        this.list = list;

        this.spreadsheetNameComponent = SpreadsheetNameComponent.empty()
                .setId(SpreadsheetMetadataPanelComponent.id(PROPERTY_NAME) + "-TextBox")
                .addChangeListener(
                        (oldValue, newValue) -> this.save(
                                newValue.map(SpreadsheetName::value)
                                        .orElse("")
                        )
                );
        list.appendChild(
                this.liElement()
                        .appendChild(
                                this.spreadsheetNameComponent
                        )
        );
    }

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final AppContext context) {
        this.spreadsheetNameComponent.setValue(
                context.spreadsheetMetadata()
                        .get(this.propertyName)
        );
    }

    private final SpreadsheetNameComponent spreadsheetNameComponent;

    // isElement........................................................................................................

    @Override
    public HTMLUListElement element() {
        return this.list.element();
    }

    private final UListElement list;
}
