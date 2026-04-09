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
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

/**
 * A link that when clicked displays the {@link walkingkooka.spreadsheet.dominokit.textstyle.TextStyleDialogComponent}.
 */
final class ToolbarComponentItemAnchorTextStyleEdit extends ToolbarComponentItemAnchorTextStyle<ToolbarComponentItemAnchorTextStyleEdit> {

    static ToolbarComponentItemAnchorTextStyleEdit with(final ToolbarComponentContext context) {
        return new ToolbarComponentItemAnchorTextStyleEdit(context);
    }

    private ToolbarComponentItemAnchorTextStyleEdit(final ToolbarComponentContext context) {
        super(
            ToolbarComponent.id(
                PROPERTY,
                Optional.empty()
            ),
            Optional.empty(), //
            "Edit styling",
            "Edit styling",
            context
        );
    }

    // SpreadsheetToolbarComponentItemLink..............................................................................

    @Override //
    void onFocus(final Event event) {
        // NOP
    }

    /**
     * Refresh the anchor.
     */
    @Override
    public void refresh(final RefreshContext context) {
        this.anchor.setHistoryToken(
            context.historyToken()
                .anchoredSelectionHistoryTokenOrEmpty()
                .map(
                    t -> t.setStylePropertyName(
                        Optional.empty()
                    )
                )
        );
    }

    private static final TextStylePropertyName<TextStyle> PROPERTY = TextStylePropertyName.ALL;
}
