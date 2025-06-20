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

import elemental2.dom.HTMLUListElement;
import org.dominokit.domino.ui.elements.UListElement;
import org.dominokit.domino.ui.forms.IntegerBox;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

/**
 * A {@link SpreadsheetMetadataPanelComponentItem} that displays a number text box.
 */
final class SpreadsheetMetadataPanelComponentItemNumber extends SpreadsheetMetadataPanelComponentItem<Integer> {

    static SpreadsheetMetadataPanelComponentItemNumber with(final SpreadsheetMetadataPropertyName<Integer> propertyName,
                                                            final int min,
                                                            final int max,
                                                            final SpreadsheetMetadataPanelComponentContext context) {
        return new SpreadsheetMetadataPanelComponentItemNumber(
            propertyName,
            min,
            max,
            context
        );
    }

    private SpreadsheetMetadataPanelComponentItemNumber(final SpreadsheetMetadataPropertyName<Integer> propertyName,
                                                        final int min,
                                                        final int max,
                                                        final SpreadsheetMetadataPanelComponentContext context) {
        super(
            propertyName,
            context
        );


        this.list = this.uListElement();

        this.integerBox = this.integerBox()
            .setPattern("#")
            .setMinValue(min)
            .setMaxValue(max)
            .setStep(1);
        this.list.appendChild(
            liElement()
                .appendChild(this.integerBox)
        );

        final HistoryTokenAnchorComponent defaultValueAnchor = this.defaultValueAnchor();
        this.list.appendChild(
            liElement()
                .appendChild(defaultValueAnchor)
        );
        this.defaultValueAnchor = defaultValueAnchor;
    }

    @Override
    void focus() {
        this.integerBox.focus();
    }

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final RefreshContext context) {
        final SpreadsheetMetadata metadata = this.context.spreadsheetMetadata();

        this.integerBox.setValue(
            metadata.getIgnoringDefaults(this.propertyName)
                .orElse(null)
        );

        this.refreshDefaultValue(
            this.defaultValueAnchor,
            metadata.defaults()
                .get(this.propertyName)
                .map(Object::toString)
                .orElse("")
        );
    }

    private final IntegerBox integerBox;

    private final HistoryTokenAnchorComponent defaultValueAnchor;

    // isElement........................................................................................................

    @Override
    public HTMLUListElement element() {
        return this.list.element();
    }

    private final UListElement list;
}
