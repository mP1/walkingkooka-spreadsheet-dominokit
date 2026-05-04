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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontweight;

import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyValueTextBoxComponentLike;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStyleDialogComponentFilter;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;

/**
 * A text box that accepts a {@link FontWeight}.
 */
public final class FontWeightComponent implements TextStylePropertyValueTextBoxComponentLike<FontWeightComponent, FontWeight> {

    public static FontWeightComponent with(final String idPrefix) {
        return new FontWeightComponent(idPrefix);
    }

    private FontWeightComponent(final String idPrefix) {
        this.textBox = ValueTextBoxComponent.with(
            FontWeight::parse,
            FontWeight::toString
        );
        this.setIdPrefix(
            idPrefix,
            SpreadsheetElementIds.TEXT_BOX
        );
    }

    @Override
    public TextStylePropertyName<FontWeight> name() {
        return TextStylePropertyName.FONT_WEIGHT;
    }

    // TextStylePropertyComponent.......................................................................................

    @Override
    public boolean filterTest(final TextStyleDialogComponentFilter filter) {
        Objects.requireNonNull(filter, "filter");

        return filter.testComponent(this) ||
            filter.test(FontWeight.BOLD.toString()) ||
            filter.test(FontWeight.NORMAL.toString());
    }

    // TextStylePropertyValueTextBoxComponentLike.......................................................................

    @Override
    public ValueTextBoxComponent<FontWeight> textStylePropertyValueTextBoxComponentLike() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<FontWeight> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}