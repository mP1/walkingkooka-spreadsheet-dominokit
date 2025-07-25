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
 * A {@link SpreadsheetMetadataPanelComponentItem} for {@link SpreadsheetMetadataPropertyName#PRECISION}
 */
final class SpreadsheetMetadataPanelComponentItemGeneralFormatNumberDigitCount extends SpreadsheetMetadataPanelComponentItem<Integer> {

    static SpreadsheetMetadataPanelComponentItemGeneralFormatNumberDigitCount with(final SpreadsheetMetadataPanelComponentContext context) {
        return new SpreadsheetMetadataPanelComponentItemGeneralFormatNumberDigitCount(
            context
        );
    }

    private SpreadsheetMetadataPanelComponentItemGeneralFormatNumberDigitCount(final SpreadsheetMetadataPanelComponentContext context) {
        super(
            SpreadsheetMetadataPropertyName.GENERAL_NUMBER_FORMAT_DIGIT_COUNT,
            context
        );

        final UListElement list = this.uListElement();

        final IntegerBox integerBox = this.integerBox()
            .setMinValue(0)
            .setStep(1);
        this.integerBox = integerBox;

        list.appendChild(
            liElement()
                .appendChild(integerBox)
        );

        final HistoryTokenAnchorComponent defaultValueAnchor = this.defaultValueAnchor();
        list.appendChild(
            liElement()
                .appendChild(defaultValueAnchor)
        );
        this.defaultValueAnchor = defaultValueAnchor;

        this.list = list;
    }

    @Override
    void focus() {
        this.integerBox.focus();
    }

    private final IntegerBox integerBox;

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
                .get(propertyName)
                .map(Object::toString)
                .orElse("")
        );
    }

    private final HistoryTokenAnchorComponent defaultValueAnchor;

    // isElement........................................................................................................

    @Override
    public HTMLUListElement element() {
        return this.list.element();
    }

    private final UListElement list;
}
