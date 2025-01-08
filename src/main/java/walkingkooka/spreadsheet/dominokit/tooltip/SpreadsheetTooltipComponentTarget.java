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

import elemental2.dom.HTMLElement;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;

/**
 * A target that receives a {@link SpreadsheetTooltipComponent}. It will events as tooltips are attached/detached.
 */
public interface SpreadsheetTooltipComponentTarget<E extends HTMLElement,
    C extends HtmlElementComponent<E, C>
    > extends HtmlElementComponent<E, C> {

    /**
     * Attaches a new {@link SpreadsheetTooltipComponent} to the target.
     */
    void tooltipAttached(final SpreadsheetTooltipComponent tooltip);

    /**
     * Announces that the tooltip has been detached.
     */
    void tooltipDetached();
}
