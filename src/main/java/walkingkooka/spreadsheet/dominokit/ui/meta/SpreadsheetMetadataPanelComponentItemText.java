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
import org.dominokit.domino.ui.forms.TextBox;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ui.Anchor;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

/**
 * A {@link SpreadsheetMetadataPanelComponentItem} that displays {@link String text}
 */
final class SpreadsheetMetadataPanelComponentItemText extends SpreadsheetMetadataPanelComponentItem<String> {

    static SpreadsheetMetadataPanelComponentItemText with(final SpreadsheetMetadataPropertyName<String> propertyName,
                                                          final SpreadsheetMetadataPanelComponentContext context) {
        checkPropertyName(propertyName);
        checkContext(context);

        return new SpreadsheetMetadataPanelComponentItemText(
                propertyName,
                context
        );
    }

    private SpreadsheetMetadataPanelComponentItemText(final SpreadsheetMetadataPropertyName<String> propertyName,
                                                      final SpreadsheetMetadataPanelComponentContext context) {
        super(
                propertyName,
                context
        );

        final UListElement list = this.uListElement();
        this.list = list;

        this.textBox = this.textBox();
        list.appendChild(
                this.liElement()
                        .appendChild(
                                this.textBox
                        )
        );

        final Anchor defaultValueAnchor = this.defaultValueAnchor();
        list.appendChild(
                this.liElement()
                        .appendChild(
                                defaultValueAnchor
                        )
        );
        this.defaultValueAnchor = defaultValueAnchor;
    }

    @Override
    void focus() {
        this.textBox.focus();
    }

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final AppContext context) {
        this.textBox.setValue(
                context.spreadsheetMetadata()
                        .getIgnoringDefaults(this.propertyName)
                        .orElse(null)
        );

        this.refreshDefaultValue(
                this.defaultValueAnchor,
                context.spreadsheetMetadata()
                        .defaults()
                        .get(propertyName)
                        .map(Object::toString)
                        .orElse("")
        );
    }

    private final TextBox textBox;

    private final Anchor defaultValueAnchor;

    // isElement........................................................................................................

    @Override
    public HTMLUListElement element() {
        return this.list.element();
    }

    private final UListElement list;
}
