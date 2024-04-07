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

package walkingkooka.spreadsheet.dominokit.ui.toolbar;

import elemental2.dom.Event;
import org.dominokit.domino.ui.icons.MdiIcon;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetDominoKitColor;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * A button ui that may exist withing a toolbar, which updates the a {@link TextStylePropertyName} with a fixed
 * {@link Object value} when selected(clicked).
 */
final class SpreadsheetToolbarComponentItemButtonTextStyleProperty<T> extends SpreadsheetToolbarComponentItemButtonTextStyle<SpreadsheetToolbarComponentItemButtonTextStyleProperty<T>> {

    static <T> SpreadsheetToolbarComponentItemButtonTextStyleProperty<T> with(final TextStylePropertyName<T> propertyName,
                                                                              final T propertyValue,
                                                                              final MdiIcon icon,
                                                                              final String tooltipText,
                                                                              final HistoryTokenContext context) {
        Objects.requireNonNull(propertyName, "propertyName");
        Objects.requireNonNull(propertyValue, "propertyValue");
        Objects.requireNonNull(icon, "icon");
        CharSequences.failIfNullOrEmpty(tooltipText, "tooltipText");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetToolbarComponentItemButtonTextStyleProperty<>(
                propertyName,
                propertyValue,
                icon,
                tooltipText,
                context
        );
    }

    private SpreadsheetToolbarComponentItemButtonTextStyleProperty(final TextStylePropertyName<T> propertyName,
                                                                   final T propertyValue,
                                                                   final MdiIcon icon,
                                                                   final String tooltipText,
                                                                   final HistoryTokenContext context) {
        super(
                SpreadsheetToolbarComponent.id(
                        propertyName,
                        Optional.of(propertyValue)
                ),
                icon,
                tooltipText,
                context
        );
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    /**
     * When clicked perform a save on the {@link walkingkooka.spreadsheet.dominokit.history.HistoryToken} and push that.
     */
    @Override //
    void onClick(final Event event) {
        final HistoryTokenContext context = this.context;

        context.historyToken()
                .anchoredSelectionHistoryTokenOrEmpty()
                .map(
                        t -> t.setStyle(this.propertyName)
                                .setSave(save(this.saveValue))
                ).ifPresent(context::pushHistoryToken);
    }

    private static String save(final Object value) {
        return null == value ?
                "" :
                value.toString();
    }

    /**
     * Upon focus the history token is set {@link walkingkooka.spreadsheet.reference.SpreadsheetSelection} and the {@link TextStylePropertyName}.
     */
    @Override //
    void onFocus(final Event event) {
        final HistoryTokenContext context = this.context;

        context.historyToken()
                .anchoredSelectionHistoryTokenOrEmpty()
                .map(
                        t -> t.setStyle(this.propertyName)
                ).ifPresent(context::pushHistoryToken);
    }

    @Override
    public void refresh(final AppContext context) {
        final T saveValue = context.viewportCache()
                .selectionSummary()
                .style()
                .get(this.propertyName)
                .orElse(null);

        final boolean selected = this.propertyValue.equals(saveValue);

        this.setButtonSelected(
                selected,
                SpreadsheetDominoKitColor.TOOLBAR_ICON_SELECTED_BACKGROUND_COLOR
        );

        this.saveValue =
                selected ?
                        null :
                        this.propertyValue;
    }

    private final TextStylePropertyName<T> propertyName;

    private final T propertyValue;

    /**
     * This is the value used when the button is clicked. Each time the selection changes this is recomputed,
     * where null means clear and non-null is set.
     */
    private T saveValue;
}
