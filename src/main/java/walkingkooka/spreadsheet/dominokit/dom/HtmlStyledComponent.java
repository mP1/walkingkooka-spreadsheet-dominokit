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

package walkingkooka.spreadsheet.dominokit.dom;

import walkingkooka.tree.text.TextStylePropertyName;

/**
 * Adds methods to set various styles upon a target like a {@link walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponentLike}.
 */
public interface HtmlStyledComponent<C extends HtmlStyledComponent<C>> {

    default C setBackgroundColor(final String color) {
        return this.setCssProperty(
            TextStylePropertyName.BACKGROUND_COLOR,
            color
        );
    }

    default C setColor(final String color) {
        return this.setCssProperty(
            TextStylePropertyName.COLOR,
            color
        );
    }

    default C setDisplay(final String display) {
        return this.setCssProperty(
            "display",
            display
        );
    }

    default C setHeight(final String height) {
        return this.setCssProperty(
            TextStylePropertyName.HEIGHT,
            height
        );
    }

    default C setLeft(final String left) {
        return this.setCssProperty(
            "left",
            left
        );
    }

    default C setMargin(final String margin) {
        return this.setCssProperty(
            TextStylePropertyName.MARGIN_LEFT,
            margin
        ).setCssProperty(
            TextStylePropertyName.MARGIN_RIGHT,
            margin
        ).setCssProperty(
            TextStylePropertyName.MARGIN_TOP,
            margin
        ).setCssProperty(
            TextStylePropertyName.MARGIN_BOTTOM,
            margin
        );
    }

    default C setMarginBottom(final String margin) {
        return this.setCssProperty(
            TextStylePropertyName.MARGIN_BOTTOM,
            margin
        );
    }

    default C setMarginLeft(final String margin) {
        return this.setCssProperty(
            TextStylePropertyName.MARGIN_LEFT,
            margin
        );
    }

    default C setMarginRight(final String margin) {
        return this.setCssProperty(
            TextStylePropertyName.MARGIN_RIGHT,
            margin
        );
    }

    default C setMarginTop(final String overflow) {
        return this.setCssProperty(
            TextStylePropertyName.MARGIN_TOP,
            overflow
        );
    }

    default C setOverflow(final String overflow) {
        return this.setCssProperty(
            TextStylePropertyName.OVERFLOW_X,
            overflow
        ).setCssProperty(
            TextStylePropertyName.OVERFLOW_Y,
            overflow
        );
    }

    default C setPadding(final String padding) {
        return this.setCssProperty(
            TextStylePropertyName.PADDING_LEFT,
            padding
        ).setCssProperty(
            TextStylePropertyName.PADDING_RIGHT,
            padding
        ).setCssProperty(
            TextStylePropertyName.PADDING_TOP,
            padding
        ).setCssProperty(
            TextStylePropertyName.PADDING_BOTTOM,
            padding
        );
    }

    default C setPaddingBottom(final String padding) {
        return this.setCssProperty(
            TextStylePropertyName.PADDING_BOTTOM,
            padding
        );
    }

    default C setPaddingLeft(final String padding) {
        return this.setCssProperty(
            TextStylePropertyName.PADDING_LEFT,
            padding
        );
    }

    default C setPaddingRight(final String padding) {
        return this.setCssProperty(
            TextStylePropertyName.PADDING_RIGHT,
            padding
        );
    }

    default C setPaddingTop(final String overflow) {
        return this.setCssProperty(
            TextStylePropertyName.PADDING_TOP,
            overflow
        );
    }

    default C setTop(final String top) {
        return this.setCssProperty(
            "top",
            top
        );
    }

    default C setWidth(final String width) {
        return this.setCssProperty(
            TextStylePropertyName.WIDTH,
            width
        );
    }

    default C setCssProperty(final TextStylePropertyName<?> name,
                             final String value) {
        return this.setCssProperty(
            name.toString(),
            value
        );
    }

    C setCssProperty(final String name,
                     final String value);
}
