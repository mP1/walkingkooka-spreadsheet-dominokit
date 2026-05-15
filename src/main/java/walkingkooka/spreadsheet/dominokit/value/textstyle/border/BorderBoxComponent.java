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

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.dom.DivComponent;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.BoxComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.color.SpreadsheetDominoKitColor;
import walkingkooka.tree.text.Border;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * General purpose {@link BoxComponent} that displays a {@link walkingkooka.tree.text.Border}.
 *
 * Attempts to set any border widths greater than 5px will be clamped to 5px
 */
public final class BorderBoxComponent implements BoxComponent<Border, BorderBoxComponent> {

    public static BorderBoxComponent empty() {
        return new BorderBoxComponent();
    }

    private BorderBoxComponent() {
        super();
        this.component = HtmlElementComponent.div()
            .setCssText(DEFAULT_WIDTH_CSS + "; " + DEFAULT_HEIGHT_CSS + "; background-color: " + SpreadsheetDominoKitColor.BORDER_BOX_BACKGROUND_COLOR);
        this.clearValue();
    }

    // ValueComponent...................................................................................................

    @Override
    public Optional<Border> value() {
        return this.border;
    }

    @Override
    public BorderBoxComponent setValue(final Optional<Border> value) {
        Objects.requireNonNull(value, "value");

        final Optional<Border> clamped = value.map(
            BorderBoxComponent::clampBorder
        );

        this.border = clamped;

        final DivComponent component = this.component;

        component.setDisplay(
            value.isEmpty() ?
                "none" :
                ""
        ).setOrRemoveStyleProperty(
            TextStylePropertyName.BORDER,
            clamped
        );

        return this;
    }

    private Optional<Border> border;

    private static Border clampBorder(final Border border) {
        Border temp = border;

        final BoxEdge boxEdge = border.edge();
        switch (boxEdge) {
            case TOP:
            case RIGHT:
            case BOTTOM:
            case LEFT:
                temp = clampBorderLength(
                    temp,
                    boxEdge.borderWidthPropertyName()
                );
                break;
            default:
                for (BoxEdge b : BoxEdge.topRightBottomLeft()) {
                    temp = clampBorderLength(
                        temp,
                        b.borderWidthPropertyName()
                    );
                }
                break;
        }

        return temp;
    }

    private static Border clampBorderLength(final Border border,
                                            final TextStylePropertyName<Length<?>> property) {
        Border temp = border;

        final Length<?> valueOrNull = temp.getProperty(property)
            .orElse(null);
        if (null != valueOrNull) {
            temp = border.setProperty(
                property,
                clampLength(valueOrNull)
            );
        }

        return temp;
    }

    private static Length<?> clampLength(final Length<?> length) {
        return length.clamp(
            BORDER_MIN,
            BORDER_MAX
        );
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.component;
    }

    // @VisibleForTesting
    final DivComponent component;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.border.map(Border::toString)
            .orElse("");
    }

}
