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

package walkingkooka.spreadsheet.dominokit.value.textstyle.padding;

import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyValueTextBoxComponentLike;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilter;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.Padding;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;

/**
 * A text box that accepts text entry and validates it as a {@link Padding}.
 */
public final class PaddingComponent implements TextStylePropertyValueTextBoxComponentLike<PaddingComponent, Padding> {

    public static PaddingComponent with(final String idPrefix) {
        return new PaddingComponent(idPrefix);
    }

    private PaddingComponent(final String idPrefix) {
        super();
        this.box = PaddingBoxComponent.empty();

        this.textBox = ValueTextBoxComponent.with(
            Padding::parse,
            (Padding padding) -> padding.setEdge(BoxEdge.ALL)
                .text()
        );

        this.textBox.setInnerRight(this.box)
            .addValueWatcher(this.box::setValue);

        this.setIdPrefix(
            idPrefix,
            SpreadsheetElementIds.TEXT_BOX
        );
    }

    private final PaddingBoxComponent box;

    // HasName..........................................................................................................

    @Override
    public TextStylePropertyName<Padding> name() {
        return TextStylePropertyName.PADDING;
    }

    // TextStylePropertyComponent.......................................................................................

    @Override
    public boolean filterTest(final TextStylePropertyFilter filter) {
        Objects.requireNonNull(filter, "filter");

        return filter.testComponent(this);
    }

    // TextStylePropertyValueTextBoxComponentLikeDelegator..............................................................

    @Override
    public ValueTextBoxComponent<Padding> textStylePropertyValueTextBoxComponentLike() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<Padding> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}