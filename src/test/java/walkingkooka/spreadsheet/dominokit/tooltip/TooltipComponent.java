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

package walkingkooka.spreadsheet.dominokit.tooltip;

import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.popover.Tooltip;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * Wraps a {@link Tooltip}.
 */
public final class TooltipComponent implements TooltipComponentLike {

    public static TooltipComponent attach(final TooltipComponentTarget<?, ?> component,
                                          final String text,
                                          final DropDirection direction) {
        Objects.requireNonNull(component, "component");
        CharSequences.failIfNullOrEmpty(text, "text");
        Objects.requireNonNull(direction, "direction");

        return new TooltipComponent(
            component,
            text,
            direction
        );
    }

    private TooltipComponent(final TooltipComponentTarget<?, ?> component,
                             final String text,
                             final DropDirection direction) {
        this.text = text;
        component.tooltipAttached(this);
        this.component = component;
    }

    /**
     * Getter that returns the text content of this tooltip.
     */
    @Override
    public String textContent() {
        return this.text;
    }

    @Override
    public TooltipComponent setTextContent(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");

        this.text = text;
        return this;
    }

    private String text;

    /**
     * Removes the tooltip.
     */
    @Override
    public void detach() {
        this.component.tooltipDetached();
    }

    private final TooltipComponentTarget<?, ?> component;
}
