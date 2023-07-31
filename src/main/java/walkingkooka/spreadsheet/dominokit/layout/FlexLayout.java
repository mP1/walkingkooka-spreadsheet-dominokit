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

package walkingkooka.spreadsheet.dominokit.layout;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.style.SpacingCss;
import org.dominokit.domino.ui.utils.ElementsFactory;

/**
 * A very basic attempt at re-creating the old DominoUI 1.x FlexLayout.
 */
public class FlexLayout implements IsElement<HTMLDivElement> {

    public FlexLayout appendChild(final Node node) {
        this.element()
                .append(node);
        return this;
    }


    @Override
    public HTMLDivElement element() {
        return this.div.element();
    }

    private final DivElement div = ElementsFactory.elements.div()
            .addCss(
                    SpacingCss.dui_flex_row,
                    SpacingCss.dui_h_full,
                    SpacingCss.dui_items_start,
                    SpacingCss.dui_gap_4);
}
