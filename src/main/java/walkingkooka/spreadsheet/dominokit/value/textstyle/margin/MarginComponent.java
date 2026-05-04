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

package walkingkooka.spreadsheet.dominokit.value.textstyle.margin;

import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyValueTextBoxComponentLike;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilter;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilterKind;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.Margin;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Set;

/**
 * A text box that accepts text entry and validates it as a {@link Margin}.
 */
public final class MarginComponent implements TextStylePropertyValueTextBoxComponentLike<MarginComponent, Margin> {

    public static MarginComponent with(final String idPrefix) {
        return new MarginComponent(idPrefix);
    }

    private MarginComponent(final String idPrefix) {
        super();
        this.box = MarginBoxComponent.empty();

        this.textBox = ValueTextBoxComponent.with(
            Margin::parse,
            (Margin margin) -> margin.setEdge(BoxEdge.ALL)
                .text()
        );

        this.textBox.setInnerRight(this.box)
            .addValueWatcher(this.box::setValue);

        this.setIdPrefix(
            idPrefix,
            SpreadsheetElementIds.TEXT_BOX
        );
    }

    private final MarginBoxComponent box;

    // HasName..........................................................................................................

    @Override
    public TextStylePropertyName<Margin> name() {
        return TextStylePropertyName.MARGIN;
    }

    // TextStylePropertyComponent.......................................................................................

    @Override
    public boolean filterTest(final TextStylePropertyFilter filter) {
        Objects.requireNonNull(filter, "filter");

        return filter.testComponent(this);
    }

    @Override
    public Set<TextStylePropertyFilterKind> textStylePropertyFilterKinds() {
        return TEXT_STYLE_PROPERTY_FILTER_KINDS_BORDER_BOX;
    }

    // TextStylePropertyValueTextBoxComponentLikeDelegator..............................................................

    @Override
    public ValueTextBoxComponent<Margin> textStylePropertyValueTextBoxComponentLike() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<Margin> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}