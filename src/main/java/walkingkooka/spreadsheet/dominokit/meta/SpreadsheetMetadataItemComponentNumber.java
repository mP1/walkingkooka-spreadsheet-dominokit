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

import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.forms.IntegerBox;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

/**
 * A {@link SpreadsheetMetadataItemComponent} that displays a number text box.
 */
final class SpreadsheetMetadataItemComponentNumber extends SpreadsheetMetadataItemComponent<Integer> {

    static SpreadsheetMetadataItemComponentNumber with(final SpreadsheetMetadataPropertyName<Integer> propertyName,
                                                       final int min,
                                                       final int max,
                                                       final SpreadsheetMetadataPanelComponentContext context) {
        checkPropertyName(propertyName);
        checkContext(context);

        return new SpreadsheetMetadataItemComponentNumber(
                propertyName,
                min,
                max,
                context
        );
    }

    private SpreadsheetMetadataItemComponentNumber(final SpreadsheetMetadataPropertyName<Integer> propertyName,
                                                   final int min,
                                                   final int max,
                                                   final SpreadsheetMetadataPanelComponentContext context) {
        super(
                propertyName,
                context
        );

        this.integerBox = this.integerBox()
                .setPattern("#")
                .setMinValue(min)
                .setMaxValue(max)
                .setStep(1);
    }

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final AppContext context) {
        this.integerBox.setValue(
                context.spreadsheetMetadata()
                        .getIgnoringDefaults(this.propertyName)
                        .orElse(null)
        );
    }

    // isElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.integerBox.element();
    }

    private final IntegerBox integerBox;
}
