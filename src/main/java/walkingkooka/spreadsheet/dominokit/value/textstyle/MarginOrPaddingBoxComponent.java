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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import walkingkooka.color.Color;
import walkingkooka.spreadsheet.dominokit.dom.DivComponent;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.tree.text.Border;
import walkingkooka.tree.text.BorderStyle;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.Margin;
import walkingkooka.tree.text.MarginOrPadding;

import java.util.Optional;

/**
 * A simple component that displays a box and sets the border with black to match if the {@link walkingkooka.tree.text.MarginOrPadding}
 * component is set.
 */
public interface MarginOrPaddingBoxComponent<V extends MarginOrPadding, C extends MarginOrPaddingBoxComponent<V, C>>
    extends BoxComponent<V, C> {

    /**
     * Factory that creates a default box root element.
     */
    default DivComponent component() {
        return HtmlElementComponent.div()
            .setCssText(BoxComponent.DEFAULT_BOX_COMPONENT_CSS_TEXT + "background-color: #ddd;");
            //.setCssText("width: 20px; height: 20px; background-color: #ddd;");
    }

    // ValueComponent...................................................................................................

    default void refresh(final Optional<V> value,
                         final DivComponent component) {
        final V valueOrNull = value.orElse(null);

        for (final BoxEdge boxEdge : BoxEdge.topRightBottomLeft()) {
            final Border border = null == valueOrNull ||
                valueOrNull.getProperty(
                    valueOrNull instanceof Margin ?
                        boxEdge.marginPropertyName() :
                        boxEdge.paddingPropertyName()
                ).isEmpty() ?
                UNSELECTED :
                SELECTED;

            component.setStyleProperty(
                boxEdge.borderPropertyName(),
                border
            );
        }
    }

    /**
     * A black border edge for present/absent {@link MarginOrPadding}.
     */
    Border SELECTED = BoxEdge.ALL.setBorder(
        Optional.of(
            Color.BLACK
        ),
        Optional.of(
            BorderStyle.SOLID
        ),
        Optional.of(
            Length.pixel(3.0)
        )
    );

    Border UNSELECTED = BoxEdge.ALL.setBorder(
        Optional.of(
            Color.parse("#444")
        ),
        Optional.of(
            BorderStyle.DOTTED
        ),
        Optional.of(
            Length.pixel(3.0)
        )
    );
}
