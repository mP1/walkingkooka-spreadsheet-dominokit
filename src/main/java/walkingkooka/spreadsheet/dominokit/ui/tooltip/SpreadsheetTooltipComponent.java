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

package walkingkooka.spreadsheet.dominokit.ui.tooltip;

import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.popover.Tooltip;
import walkingkooka.spreadsheet.dominokit.ui.Component;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * Wraps a {@link Tooltip}.
 */
public final class SpreadsheetTooltipComponent {

    public static SpreadsheetTooltipComponent attach(final Component<?> component,
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

    private SpreadsheetTooltipComponent(final Component<?> component,
                                        final String text,
                                        final DropDirection direction) {
        this.tooltip = Tooltip.create(
                component.element(),
                text
        ).setPosition(direction);
    }

    public SpreadsheetTooltipComponent setTextContent(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");

        this.tooltip.setTextContent(text);
        return this;
    }

    /**
     * Removes the tooltip.
     */
    public void detach() {
        this.tooltip.detach();
    }

    private final Tooltip tooltip;
}
