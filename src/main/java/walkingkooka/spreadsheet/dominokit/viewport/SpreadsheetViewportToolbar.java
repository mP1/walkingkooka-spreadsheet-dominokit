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

package walkingkooka.spreadsheet.dominokit.viewport;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.grid.flex.FlexItem;
import org.dominokit.domino.ui.grid.flex.FlexLayout;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.style.StyleType;
import org.jboss.elemento.IsElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.List;
import java.util.Optional;

/**
 * A toolbar that contains icons that trigger an action for the viewport selection.
 */
public final class SpreadsheetViewportToolbar implements HistoryTokenWatcher, IsElement<HTMLDivElement> {

    public static SpreadsheetViewportToolbar create() {
        return new SpreadsheetViewportToolbar();
    }

    // isElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.flexLayout.element();
    }

    private final FlexLayout flexLayout = createFlexLayout();

    /**
     * Creates a {@link FlexLayout} and populates it with the toolbar icons etc.
     */
    private static FlexLayout createFlexLayout() {
        final FlexItem<HTMLDivElement> flexItem = FlexItem.create();

        for (final HTMLElement item : items()) {
            flexItem.appendChild(item);
        }

        return FlexLayout.create()
                .appendChild(flexItem);
    }

    private static List<HTMLElement> items() {
        final Button home = Button.create(Icons.ALL.home_mdi())
                .circle()
                .setButtonType(StyleType.DEFAULT);

        home.style()
                .setMargin("5px");

        return Lists.of(
                home.element()
        );
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        this.selectedCellReference = context.viewportNonLabelSelection();
    }

    private Optional<SpreadsheetSelection> selectedCellReference = Optional.empty();
}
