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
import org.dominokit.domino.ui.forms.TextBox;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

/**
 * A {@link SpreadsheetMetadataItemComponent} that displays {@link String text}
 */
final class SpreadsheetMetadataItemComponentText extends SpreadsheetMetadataItemComponent<String> {

    static SpreadsheetMetadataItemComponentText with(final SpreadsheetMetadataPropertyName<String> propertyName,
                                                     final SpreadsheetMetadataPanelComponentContext context) {
        checkPropertyName(propertyName);
        checkContext(context);

        return new SpreadsheetMetadataItemComponentText(
                propertyName,
                context
        );
    }

    private SpreadsheetMetadataItemComponentText(final SpreadsheetMetadataPropertyName<String> propertyName,
                                                 final SpreadsheetMetadataPanelComponentContext context) {
        super(
                propertyName,
                context
        );

        this.textBox = this.textBox();
    }

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final AppContext context) {
        this.textBox.setValue(
                context.spreadsheetMetadata()
                        .get(this.propertyName)
                        .orElse(null)
        );
    }

    // isElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.textBox.element();
    }

    private final TextBox textBox;
}
