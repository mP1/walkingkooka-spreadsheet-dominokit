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

import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.button.ButtonSize;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.style.StyleType;
import org.jboss.elemento.EventType;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

final class SpreadsheetViewportToolbarComponentButton<T> extends SpreadsheetViewportToolbarComponent {

    static <T> SpreadsheetViewportToolbarComponentButton<T> with(final TextStylePropertyName<T> propertyName,
                                                                 final T propertyValue,
                                                                 final MdiIcon icon,
                                                                 final AppContext context) {
        Objects.requireNonNull(propertyName, "propertyName");
        Objects.requireNonNull(propertyValue, "propertyValue");
        Objects.requireNonNull(icon, "icon");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetViewportToolbarComponentButton(
                propertyName,
                propertyValue,
                icon,
                context
        );
    }

    private SpreadsheetViewportToolbarComponentButton(final TextStylePropertyName<T> propertyName,
                                                      final T propertyValue,
                                                      final MdiIcon icon,
                                                      final AppContext context) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;

        final Button button = Button.create(icon)
                .circle()
                .setSize(ButtonSize.MEDIUM)
                .setButtonType(StyleType.DEFAULT)
                .addEventListener(
                        EventType.click,
                        (event) -> this.onClick()
                ).addEventListener(
                        EventType.focus,
                        (event) -> this.onFocus()
                );

        button.style()
                .setMargin("5px");
        final HTMLElement element = button.element();
        element.id = SpreadsheetViewportToolbar.id(
                propertyName,
                propertyValue
        );
        element.tabIndex = 0;

        this.button = button;

        this.context = context;
    }

    private void onClick() {
        this.context.historyToken()
                .viewportSelectionHistoryTokenOrEmpty()
                .map(
                        t -> t.setStyle(this.propertyName)
                                .setSave(save(this.saveValue))
                ).ifPresent(this.context::pushHistoryToken);
    }

    private static String save(final Object value) {
        return null == value ?
                "" :
                value.toString();
    }

    /**
     * Upon focus the history token is set {@link walkingkooka.spreadsheet.reference.SpreadsheetSelection} and the {@link TextStylePropertyName}.
     */
    private void onFocus() {
        this.context.historyToken()
                .viewportSelectionHistoryTokenOrEmpty()
                .map(
                        t -> t.setStyle(this.propertyName)
                ).ifPresent(this.context::pushHistoryToken);
    }

    @Override
    public HTMLElement element() {
        return this.button.element();
    }

    private final TextStylePropertyName<T> propertyName;

    private final T propertyValue;

    private final AppContext context;

    private final Button button;

    @Override
    void onToolbarRefreshBegin() {
        this.setCellCounter = 0;
    }

    /**
     * Counts the number of cells in the selection that have this {@link #propertyName} and {@link #propertyValue}.
     */
    private int setCellCounter;

    @Override
    void onToolbarRefreshSelectedCell(final SpreadsheetCell cell,
                                      final AppContext context) {
        final TextStylePropertyName<T> propertyName = this.propertyName;
        final T effectiveValue;

        final Optional<TextNode> maybeFormatted = cell.formatted();
        if (maybeFormatted.isPresent()) {
            final Optional<T> maybeStyleValue = cell.style()
                    .get(propertyName);
            if (maybeStyleValue.isPresent()) {
                effectiveValue = maybeStyleValue.get();
            } else {
                effectiveValue = context.spreadsheetMetadata()
                        .getEffectiveStyleProperty(propertyName)
                        .orElse(null);
            }
        } else {
            effectiveValue = context.spreadsheetMetadata()
                    .getEffectiveStyleProperty(propertyName)
                    .orElse(null);
        }

        if (Objects.equals(this.propertyValue, effectiveValue)) {
            this.setCellCounter++;
        }
    }

    /**
     * Counts the number of cells with this {@link #propertyName} and {@link #propertyValue}.
     * - If {@link #setCellCounter} is 0 then the button will not be highlighted other values will highlight the button.
     * - if {@link #setCellCounter} is equal to the cellPresentCount then the {@link #saveValue} will be null otherwise it will be
     * {@link #propertyValue}.
     */
    @Override
    void onToolbarRefreshEnd(final int cellPresentCount,
                             final AppContext context) {
        final T propertyValue = this.propertyValue;

        final int setCellCounter = this.setCellCounter;

        final boolean selected = setCellCounter == cellPresentCount;
        final T saveValue = setCellCounter == cellPresentCount ?
                null :
                propertyValue;

        this.setButtonSelected(
                selected,
                context
        );

        this.setSaveValue(saveValue);

        context.debug("SpreadsheetViewportToolbarComponentButton.onToolbarRefreshEnd " + this.propertyName + "=" + propertyValue + " " + setCellCounter + "/" + cellPresentCount + " selected: " + selected + " saveValue: " + saveValue);
    }

    private void setButtonSelected(final boolean selected,
                                   final AppContext context) {
        final Button button = this.button;
        if (selected) {
            button.style(context.selectedIconStyle().css());
        } else {
            button.style("");
        }
    }

    private void setSaveValue(final T saveValue) {
        this.saveValue = saveValue;
    }

    /**
     * This is the value used when the button is clicked. Each time the selection changes this is recomputed,
     * where null means clear and non-null is set.
     */
    private T saveValue;

    @Override
    public String toString() {
        return this.element().id;
    }
}
