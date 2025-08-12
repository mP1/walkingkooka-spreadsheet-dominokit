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
import walkingkooka.spreadsheet.dominokit.find.FindHighlighting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertyHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Optional;

/**
 * When clicked, updates the {@link SpreadsheetMetadataPropertyName#HIDE_ZERO_VALUES} with the opposite of its current value.
 */
final class SpreadsheetToolbarComponentItemAnchorMetadataFindHighlighting extends SpreadsheetToolbarComponentItemAnchorMetadata<SpreadsheetToolbarComponentItemAnchorMetadataFindHighlighting> {

    static SpreadsheetToolbarComponentItemAnchorMetadataFindHighlighting with(final SpreadsheetToolbarComponentContext context) {
        return new SpreadsheetToolbarComponentItemAnchorMetadataFindHighlighting(context);
    }

    private SpreadsheetToolbarComponentItemAnchorMetadataFindHighlighting(final SpreadsheetToolbarComponentContext context) {
        super(
            SpreadsheetToolbarComponent.findHighlightId(),
            Optional.of(
                SpreadsheetIcons.highlight()
            ),
            "Highlight",
            "Highlight", // let refresh load tooltip
            context
        );
    }

    // SpreadsheetToolbarComponentItemLink............................................................................

    @Override
    void onFocus(final Event event) {
        // nop
    }

    // HistoryTokenAwareComponentLifecycle..............................................................................

    @Override
    public void refresh(final RefreshContext context) {
        final boolean enabled = FindHighlighting.isEnabled(this.context);

        this.setTooltipText(
            FindHighlighting.label(false == enabled)
        );

        this.anchor.setChecked(
            enabled
        ).setHistoryToken(
            Optional.of(
                context.historyToken()
                    .setMetadataPropertyName(SpreadsheetMetadataPropertyName.FIND_HIGHLIGHTING)
                    .setSaveValue(
                        Optional.of(false == enabled)
                    )
            )
        );
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        boolean ignore = false;

        if (token instanceof SpreadsheetMetadataPropertyHistoryToken) {
            final SpreadsheetMetadataPropertyHistoryToken<?> metadata = token.cast(SpreadsheetMetadataPropertyHistoryToken.class);
            ignore = false == SpreadsheetMetadataPropertyName.FIND_HIGHLIGHTING.equals(metadata.propertyName());
        }

        return ignore;
    }
}
