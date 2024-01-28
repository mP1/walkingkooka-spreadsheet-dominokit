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
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetDominoKitColor;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * A button ui that may exist withing a toolbar, which updates the a {@link TextStylePropertyName} with a fixed
 * {@link Object value} when selected(clicked).
 */
final class SpreadsheetToolbarComponentItemButtonTextStyleProperty<T> extends SpreadsheetToolbarComponentItemButtonTextStyle {

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
                .selectionHistoryTokenOrEmpty()
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
                .selectionHistoryTokenOrEmpty()
                .map(
                        t -> t.setStyle(this.propertyName)
                ).ifPresent(context::pushHistoryToken);
    }

    private final TextStylePropertyName<T> propertyName;

    private final T propertyValue;

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
                SpreadsheetDominoKitColor.TOOLBAR_ICON_SELECTED_BACKGROUND_COLOR
        );

        this.setSaveValue(saveValue);

        context.debug("SpreadsheetToolbarComponentItemButtonTextStyleProperty.onToolbarRefreshEnd " + this.propertyName + "=" + propertyValue + " " + setCellCounter + "/" + cellPresentCount + " selected: " + selected + " saveValue: " + saveValue);
    }

    private void setSaveValue(final T saveValue) {
        this.saveValue = saveValue;
    }

    /**
     * This is the value used when the button is clicked. Each time the selection changes this is recomputed,
     * where null means clear and non-null is set.
     */
    private T saveValue;
}
