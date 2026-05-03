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

package walkingkooka.spreadsheet.dominokit.value.textstyle.opacity;

import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStyleDialogComponentFilter;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyValueTextBoxComponentLike;
import walkingkooka.tree.text.Opacity;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;

/**
 * A text box that accepts text entry and validates it as a {@link Opacity}.
 */
public final class OpacityComponent implements TextStylePropertyValueTextBoxComponentLike<OpacityComponent, Opacity> {

    public static OpacityComponent with(final String idPrefix) {
        return new OpacityComponent(idPrefix);
    }

    private OpacityComponent(final String idPrefix) {
        this.textBox = ValueTextBoxComponent.with(
            Opacity::parse,
            Opacity::text
        );
        this.setIdPrefix(
            idPrefix,
            SpreadsheetElementIds.TEXT_BOX
        );
    }

    // HasName..........................................................................................................

    @Override
    public TextStylePropertyName<Opacity> name() {
        return TextStylePropertyName.OPACITY;
    }

    // TextStylePropertyComponent.......................................................................................

    @Override
    public boolean filterTest(final TextStyleDialogComponentFilter filter) {
        Objects.requireNonNull(filter, "filter");

        return filter.testComponent(this) ||
            filter.test(Opacity.OPAQUE.text()) ||
            filter.test(Opacity.TRANSPARENT.text());
    }

    // TextStylePropertyValueTextBoxComponentLikeDelegator..............................................................

    @Override
    public ValueTextBoxComponent<Opacity> textStylePropertyValueTextBoxComponentLike() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<Opacity> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}