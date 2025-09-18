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

package walkingkooka.spreadsheet.dominokit.toolbar;

import elemental2.dom.Event;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

/**
 * A link ui that may exist withing a toolbar, which updates the a {@link TextStylePropertyName} with a fixed
 * {@link Object value} when selected(clicked).
 */
final class SpreadsheetToolbarComponentItemAnchorTextStyleClear extends SpreadsheetToolbarComponentItemAnchorTextStyle<SpreadsheetToolbarComponentItemAnchorTextStyleClear> {

    static SpreadsheetToolbarComponentItemAnchorTextStyleClear with(final SpreadsheetToolbarComponentContext context) {
        return new SpreadsheetToolbarComponentItemAnchorTextStyleClear(context);
    }

    private SpreadsheetToolbarComponentItemAnchorTextStyleClear(final SpreadsheetToolbarComponentContext context) {
        super(
            SpreadsheetToolbarComponent.id(
                PROPERTY,
                Optional.empty()
            ),
            Optional.of(
                SpreadsheetIcons.clearStyle()
            ),
            "Clear styling",
            "Clear styling",
            context
        );
    }

    // SpreadsheetToolbarComponentItemLink............................................................................

    /**
     * Upon focus the history token is set {@link walkingkooka.spreadsheet.reference.SpreadsheetSelection} and the {@link TextStylePropertyName}.
     */
    @Override //
    void onFocus(final Event event) {
        final HistoryContext context = this.context;

        context.historyToken()
            .anchoredSelectionHistoryTokenOrEmpty()
            .map(
                t -> t.setStylePropertyName(PROPERTY)
            ).ifPresent(context::pushHistoryToken);
    }

    /**
     * Counts the number of cells with a non empty {@link TextStyle}.
     */
    @Override
    public void refresh(final RefreshContext context) {
        this.anchor.setChecked(true)
            .setHistoryToken(
                context.historyToken()
                    .anchoredSelectionHistoryTokenOrEmpty()
                    .map(
                        t -> t.setStylePropertyName(PROPERTY)
                            .clearSaveValue()
                    )
            );
    }

    private static final TextStylePropertyName<Void> PROPERTY = TextStylePropertyName.WILDCARD;
}
