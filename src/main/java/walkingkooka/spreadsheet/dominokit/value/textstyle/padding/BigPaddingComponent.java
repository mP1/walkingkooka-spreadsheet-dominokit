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

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.value.FormElementComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.BigMarginOrPaddingComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.MarginOrPaddingKind;
import walkingkooka.tree.text.Padding;

/**
 * A {@link TextStylePropertyComponent} that supports editing individual margin {@link Length} or the entire {@link Padding}
 * as text.
 */
public final class BigPaddingComponent implements BigMarginOrPaddingComponent<Padding, BigPaddingComponent> {

    public static BigPaddingComponent with(final String idPrefix) {
        return new BigPaddingComponent(idPrefix);
    }

    private BigPaddingComponent(final String idPrefix) {
        super();

        this.all = PaddingComponent.with(idPrefix);
        this.top = PaddingTopComponent.with(idPrefix);
        this.right = PaddingRightComponent.with(idPrefix);
        this.bottom = PaddingBottomComponent.with(idPrefix);
        this.left = PaddingLeftComponent.with(idPrefix);

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

    // @VisibleForTesting
    final PaddingTopComponent top;

    // @VisibleForTesting
    final PaddingRightComponent right;

    // @VisibleForTesting
    final PaddingBottomComponent bottom;

    // @VisibleForTesting
    final PaddingLeftComponent left;

    @Override
    public PaddingComponent all() {
        return this.all;
    }

    // @VisibleForTesting
    final PaddingComponent all;

    @Override
    public MarginOrPaddingKind marginOrPaddingKind() {
        return MarginOrPaddingKind.PADDING;
    }

    // FormElementComponentDelegator....................................................................................

    @Override
    public FormElementComponent<Padding, ?, ?> formElementComponent() {
        return this.formElementComponent;
    }

    private final FormElementComponent<Padding, HTMLDivElement, ?> formElementComponent;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.formElementComponent.toString();
    }
}
