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

package walkingkooka.spreadsheet.dominokit.value.textstyle.border;

import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyValueTextBoxComponentLike;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilter;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilterKind;
import walkingkooka.tree.text.Border;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public abstract class BorderSharedComponent<C extends BorderSharedComponent<C>> implements TextStylePropertyValueTextBoxComponentLike<C, Border> {

    BorderSharedComponent(final String idPrefix) {
        this.textBox = ValueTextBoxComponent.with(
            this.boxEdge()::parseBorder,
            Border::text
        );
        this.setIdPrefix(
            idPrefix,
            SpreadsheetElementIds.TEXT_BOX
        );
    }

    public abstract BoxEdge boxEdge();

    @Override
    public final TextStylePropertyName<Border> name() {
        return this.boxEdge()
            .borderPropertyName();
    }

    // TextStylePropertyComponent.......................................................................................

    @Override
    public final boolean filterTest(final TextStylePropertyFilter filter) {
        Objects.requireNonNull(filter, "filter");

        return filter.testComponent(this);
    }

    @Override
    public final Set<TextStylePropertyFilterKind> textStylePropertyFilterKinds() {
        return TEXT_STYLE_PROPERTY_FILTER_KINDS_BORDER_BOX;
    }

    // TextStylePropertyValueTextBoxComponentLike.......................................................................

    @Override
    public final C setValue(final Optional<Border> value) {
        Objects.requireNonNull(value, "value");

        this.textBox.setValue(
            value.map(
                b -> b.setEdge(this.boxEdge())
            )
        );
        return (C) this;
    }


    @Override
    public final ValueTextBoxComponent<Border> textStylePropertyValueTextBoxComponentLike() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<Border> textBox;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.textBox.toString();
    }
}