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

package walkingkooka.spreadsheet.dominokit;

import elemental2.dom.Node;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.popover.Tooltip;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * Wraps a {@link Tooltip}.
 */
public final class SpreadsheetTooltipComponent implements SpreadsheetTooltipComponentLike {

    public static SpreadsheetTooltipComponent attach(final SpreadsheetTooltipComponentTarget component,
                                                     final String text,
                                                     final DropDirection direction) {
        Objects.requireNonNull(component, "component");
        CharSequences.failIfNullOrEmpty(text, "text");
        Objects.requireNonNull(direction, "direction");

        return new SpreadsheetTooltipComponent(
                component,
                text,
                direction
        );
    }

    private SpreadsheetTooltipComponent(final SpreadsheetTooltipComponentTarget component,
                                        final String text,
                                        final DropDirection direction) {
        this.tooltip = Tooltip.create(
                component.element(),
                text
        ).setPosition(direction);
        this.direction = direction;

        component.tooltipAttached(this);
        this.component = component;
    }

    @Override
    public String textContent() {
        return this.tooltipTextNode().textContent;
    }

    @Override
    public SpreadsheetTooltipComponent setTextContent(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");

        this.tooltip.setTooltip(text, this.direction);
        return this;
    }

    private final DropDirection direction;

    private Node tooltipTextNode() {
        return this.tooltip.element().lastChild;
    }

    /**
     * Removes the tooltip.
     */
    @Override
    public void detach() {
        this.tooltip.detach();
        this.component.tooltipDetached();
    }

    private final Tooltip tooltip;

    private final SpreadsheetTooltipComponentTarget component;
}
