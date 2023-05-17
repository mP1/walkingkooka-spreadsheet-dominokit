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
import org.dominokit.domino.ui.grid.flex.FlexItem;
import org.dominokit.domino.ui.grid.flex.FlexLayout;
import org.jboss.elemento.IsElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextDecorationLine;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.VerticalAlign;

import java.util.List;

/**
 * A toolbar that contains icons that trigger an action for the viewport selection.
 */
public final class SpreadsheetViewportToolbar implements HistoryTokenWatcher, IsElement<HTMLDivElement> {

    public static SpreadsheetViewportToolbar create(final AppContext context) {
        return new SpreadsheetViewportToolbar(context);
    }

    private SpreadsheetViewportToolbar(final AppContext context) {
        this.flexLayout = this.createFlexLayout(context);

        context.addHistoryWatcher(this);
    }

    // isElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.flexLayout.element();
    }

    private final FlexLayout flexLayout;

    /**
     * Creates a {@link FlexLayout} and populates it with the toolbar icons etc.
     */
    private FlexLayout createFlexLayout(final AppContext context) {
        final FlexItem<HTMLDivElement> flexItem = FlexItem.create();

        for (final SpreadsheetViewportToolbarComponent item : items(context)) {
            flexItem.appendChild(
                    item.element()
            );
        }

        return FlexLayout.create()
                .appendChild(flexItem);
    }

    private List<SpreadsheetViewportToolbarComponent> items(final AppContext context) {
        return Lists.of(
                SpreadsheetViewportToolbarComponent.bold(
                        SpreadsheetViewportToolbarComponentButton.watcher(
                                TextStylePropertyName.FONT_WEIGHT,
                                FontWeight.BOLD,
                                context
                        )
                ),
                SpreadsheetViewportToolbarComponent.italics(
                        SpreadsheetViewportToolbarComponentButton.watcher(
                                TextStylePropertyName.FONT_STYLE,
                                FontStyle.ITALIC,
                                context
                        )
                ),
                SpreadsheetViewportToolbarComponent.strikeThru(
                        SpreadsheetViewportToolbarComponentButton.watcher(
                                TextStylePropertyName.TEXT_DECORATION_LINE,
                                TextDecorationLine.LINE_THROUGH,
                                context
                        )
                ),
                SpreadsheetViewportToolbarComponent.underline(
                        SpreadsheetViewportToolbarComponentButton.watcher(
                                TextStylePropertyName.TEXT_DECORATION_LINE,
                                TextDecorationLine.UNDERLINE,
                                context
                        )
                ),
                SpreadsheetViewportToolbarComponent.textAlignLeft(
                        SpreadsheetViewportToolbarComponentButton.watcher(
                                TextStylePropertyName.TEXT_ALIGN,
                                TextAlign.LEFT,
                                context
                        )
                ),
                SpreadsheetViewportToolbarComponent.textAlignCenter(
                        SpreadsheetViewportToolbarComponentButton.watcher(
                                TextStylePropertyName.TEXT_ALIGN,
                                TextAlign.CENTER,
                                context
                        )
                ),
                SpreadsheetViewportToolbarComponent.textAlignRight(
                        SpreadsheetViewportToolbarComponentButton.watcher(
                                TextStylePropertyName.TEXT_ALIGN,
                                TextAlign.RIGHT,
                                context
                        )
                ),
                SpreadsheetViewportToolbarComponent.textAlignJustify(
                        SpreadsheetViewportToolbarComponentButton.watcher(
                                TextStylePropertyName.TEXT_ALIGN,
                                TextAlign.JUSTIFY,
                                context
                        )
                ),
                SpreadsheetViewportToolbarComponent.verticalAlignTop(
                        SpreadsheetViewportToolbarComponentButton.watcher(
                                TextStylePropertyName.VERTICAL_ALIGN,
                                VerticalAlign.TOP,
                                context
                        )
                ),
                SpreadsheetViewportToolbarComponent.verticalAlignMiddle(
                        SpreadsheetViewportToolbarComponentButton.watcher(
                                TextStylePropertyName.VERTICAL_ALIGN,
                                VerticalAlign.MIDDLE,
                                context
                        )
                ),
                SpreadsheetViewportToolbarComponent.verticalAlignBottom(
                        SpreadsheetViewportToolbarComponentButton.watcher(
                                TextStylePropertyName.VERTICAL_ALIGN,
                                VerticalAlign.BOTTOM,
                                context
                        )
                )
        );
    }

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
