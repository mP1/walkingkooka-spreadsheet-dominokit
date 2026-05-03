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

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.value.FormElementComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.BigMarginOrPaddingComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyLengthComponentLike;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.Margin;
import walkingkooka.tree.text.MarginOrPaddingKind;

/**
 * A {@link TextStylePropertyComponent} that supports editing individual margin {@link Length} or the entire {@link Margin}
 * as text.
 */
public final class BigMarginComponent implements BigMarginOrPaddingComponent<Margin, BigMarginComponent> {

    public static BigMarginComponent with(final String idPrefix) {
        return new BigMarginComponent(idPrefix);
    }

    private BigMarginComponent(final String idPrefix) {
        super();

        this.all = MarginComponent.with(idPrefix);
        this.top = MarginTopComponent.with(idPrefix);
        this.right = MarginRightComponent.with(idPrefix);
        this.bottom = MarginBottomComponent.with(idPrefix);
        this.left = MarginLeftComponent.with(idPrefix);

        this.formElementComponent = this.createFormElementComponent(
            this.top,
            this.right,
            this.bottom,
            this.left
        );

        this.setIdPrefix(
            idPrefix,
            "" // no suffix
        );
        this.setLabelFromPropertyName();
    }

    @Override
    public MarginComponent all() {
        return this.all;
    }

    // @VisibleForTesting
    final MarginComponent all;

    @Override
    public TextStylePropertyLengthComponentLike<?> top() {
        return this.top;        
    }

    private final MarginTopComponent top;

    @Override
    public TextStylePropertyLengthComponentLike<?> right() {
        return this.right;
    }

    private final MarginRightComponent right;

    @Override
    public TextStylePropertyLengthComponentLike<?> bottom() {
        return this.bottom;
    }

    private final MarginBottomComponent bottom;

    @Override
    public TextStylePropertyLengthComponentLike<?> left() {
        return this.left;
    }

    private final MarginLeftComponent left;

    @Override
    public MarginOrPaddingKind marginOrPaddingKind() {
        return MarginOrPaddingKind.MARGIN;
    }

    // FormElementComponentDelegator....................................................................................

    @Override
    public FormElementComponent<Margin, ?, ?> formElementComponent() {
        return this.formElementComponent;
    }

    private final FormElementComponent<Margin, HTMLDivElement, ?> formElementComponent;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.formElementComponent.toString();
    }
}
