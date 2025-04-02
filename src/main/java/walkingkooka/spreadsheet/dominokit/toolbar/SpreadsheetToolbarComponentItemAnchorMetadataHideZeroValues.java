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
import walkingkooka.spreadsheet.dominokit.hidezerovalues.HideZeroValues;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertyHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Optional;

/**
 * When clicked, updates the {@link SpreadsheetMetadataPropertyName#HIDE_ZERO_VALUES} with the opposite of its current value.
 */
final class SpreadsheetToolbarComponentItemAnchorMetadataHideZeroValues extends SpreadsheetToolbarComponentItemAnchorMetadata<SpreadsheetToolbarComponentItemAnchorMetadataHideZeroValues> {

    static SpreadsheetToolbarComponentItemAnchorMetadataHideZeroValues with(final SpreadsheetToolbarComponentContext context) {
        return new SpreadsheetToolbarComponentItemAnchorMetadataHideZeroValues(context);
    }

    private SpreadsheetToolbarComponentItemAnchorMetadataHideZeroValues(final SpreadsheetToolbarComponentContext context) {
        super(
            SpreadsheetToolbarComponent.hideZeroValues(),
            Optional.of(
                SpreadsheetIcons.hideZeroValues()
            ),
            "Hide Zeros",
            "Hide Zeros", // let refresh load tooltip
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
        final boolean hide = HideZeroValues.isHideZeroValues(this.context);

        this.setTooltipText(
            HideZeroValues.label(false == hide)
        );

        this.anchor.setChecked(
            hide
        ).setHistoryToken(
            Optional.of(
                context.historyToken()
                    .setMetadataPropertyName(SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES)
                    .setSaveValue(
                        Optional.of(
                            false == HideZeroValues.isHideZeroValues(this.context) // if hide=true then click makes hide=false
                        )
                    )
            )
        );
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        boolean ignore = false;

        if (token instanceof SpreadsheetMetadataPropertyHistoryToken) {
            final SpreadsheetMetadataPropertyHistoryToken<?> metadata = token.cast(SpreadsheetMetadataPropertyHistoryToken.class);
            ignore = false == SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES.equals(metadata.propertyName());
        }

        return ignore;
    }
}
