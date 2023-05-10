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
import org.dominokit.domino.ui.button.ButtonSize;
import org.dominokit.domino.ui.grid.flex.FlexItem;
import org.dominokit.domino.ui.grid.flex.FlexLayout;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.style.StyleType;
import org.jboss.elemento.EventType;
import org.jboss.elemento.IsElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;

/**
 * A toolbar that contains icons that trigger an action for the viewport selection.
 */
public final class SpreadsheetViewportToolbar implements HistoryTokenWatcher, IsElement<HTMLDivElement> {

    public static SpreadsheetViewportToolbar create(final AppContext context) {
        return new SpreadsheetViewportToolbar(context);
    }

    private SpreadsheetViewportToolbar(final AppContext context) {
        this.context = context;

        context.addHistoryWatcher(this);
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
    private FlexLayout createFlexLayout() {
        final FlexItem<HTMLDivElement> flexItem = FlexItem.create();

        for (final HTMLElement item : items()) {
            flexItem.appendChild(item);
        }

        return FlexLayout.create()
                .appendChild(flexItem);
    }

    private List<HTMLElement> items() {
        final Button home = Button.create(Icons.ALL.home_mdi())
                .circle()
                .setButtonType(StyleType.DEFAULT);

        home.style()
                .setMargin("5px");

        return Lists.of(
                textAlignLeft().element(),
                textAlignCenter().element(),
                textAlignRight().element(),
                textAlignJustify().element()
        );
    }

    private Button textAlignLeft() {
        return this.buttonPatchStyleProperty(
                Icons.ALL.format_align_left(),
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );
    }

    private Button textAlignCenter() {
        return this.buttonPatchStyleProperty(
                Icons.ALL.format_align_center(),
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.CENTER
        );
    }

    private Button textAlignRight() {
        return this.buttonPatchStyleProperty(
                Icons.ALL.format_align_right(),
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.RIGHT
        );
    }

    private Button textAlignJustify() {
        return this.buttonPatchStyleProperty(
                Icons.ALL.format_align_justify(),
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.JUSTIFY
        );
    }

    /**
     * Creates a button with the icon and when clicked pushes a save history token with the provided value.
     */
    private <T> Button buttonPatchStyleProperty(final Icon icon,
                                                final TextStylePropertyName<T> propertyName,
                                                final T propertyValue) {
        return this.button(
                icon,
                id(propertyName, propertyValue),
                () -> {
                    final AppContext context = this.context;
                    context.historyToken()
                            .viewportSelectionHistoryTokenOrEmpty()
                            .map(
                                    t -> t.setStyle(propertyName)
                                            .setSave(this.save(propertyValue))
                            ).ifPresent(context::pushHistoryToken);
                },
                () -> {
                    final AppContext context = this.context;
                    context.historyToken()
                            .viewportSelectionHistoryTokenOrEmpty()
                            .map(
                                    t -> t.setStyle(propertyName)
                            ).ifPresent(context::pushHistoryToken);
                }
        );
    }

    private String save(final Object value) {
        return null == value ?
                "" :
                value.toString();
    }

    /**
     * Creates a button with the given icon and executes the onClick {@link Runnable}.
     */
    private Button button(final Icon icon,
                          final String id,
                          final Runnable onClick,
                          final Runnable onFocus) {
        final Button button = Button.create(icon)
                .circle()
                .setSize(ButtonSize.MEDIUM)
                .setButtonType(StyleType.DEFAULT)
                .addEventListener(
                        EventType.click,
                        (event) -> onClick.run()
                ).addEventListener(
                        EventType.focus,
                        (event) -> onFocus.run()
                );

        button.style()
                .setMargin("5px");
        final HTMLElement element = button.element();
        element.id = id;
        element.tabIndex = 0;

        return button;
    }

    private final AppContext context;

    // HistoryTokenWatcher..............................................................................................

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        final boolean show = context.viewportNonLabelSelection()
                .map(s -> s.isCellReference() || s.isCellRange())
                .orElse(false);
        this.element()
                .style.set(
                        "visibility",
                        show ?
                                "visible" :
                                "hidden"
                );
    }

    // element..........................................................................................................

    // viewport-column-A
    public static <T> String id(final TextStylePropertyName<T> propertyName,
                                final T value) {
        return VIEWPORT_TOOLBAR_ID_PREFIX +
                propertyName.constantName().toLowerCase() +
                '-' +
                value.toString().toUpperCase();
    }

    private final static String VIEWPORT_TOOLBAR_ID_PREFIX = "viewport-toolbar-";
}
