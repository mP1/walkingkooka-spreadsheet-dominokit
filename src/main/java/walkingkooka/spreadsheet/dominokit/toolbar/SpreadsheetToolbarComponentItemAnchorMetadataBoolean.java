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
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertyHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Optional;

/**
 * A boolean {@link SpreadsheetMetadataPropertyName}.
 */
abstract class SpreadsheetToolbarComponentItemAnchorMetadataBoolean<T extends SpreadsheetToolbarComponentItemAnchorMetadataBoolean<T>> extends SpreadsheetToolbarComponentItemAnchorMetadata<T> {

    SpreadsheetToolbarComponentItemAnchorMetadataBoolean(final String id,
                                                         final Optional<Icon<?>> icon,
                                                         final String text,
                                                         final String tooltipText,
                                                         final SpreadsheetToolbarComponentContext context) {
        super(
            id,
            icon,
            text,
            tooltipText,
            context
        );
    }

    // SpreadsheetToolbarComponentItemLink..............................................................................

    @Override //
    final void onFocus(final Event event) {
        // nop
    }

    // HistoryTokenAwareComponentLifecycle..............................................................................

    @Override
    public final void refresh(final RefreshContext context) {
        final SpreadsheetMetadataPropertyName<Boolean> propertyName = this.propertyName();

        final boolean enabled = this.context.spreadsheetMetadata()
            .get(propertyName)
            .orElse(false);

        this.setTooltipText(
            tooltipText(false == enabled)
        );

        this.anchor.setChecked(
            enabled
        ).setHistoryToken(
            Optional.of(
                context.historyToken()
                    .setMetadataPropertyName(propertyName)
                    .setSaveValue(
                        Optional.of(false == enabled)
                    )
            )
        );
    }

    abstract String tooltipText(final boolean disabled);

    @Override
    public final boolean shouldIgnore(final HistoryToken token) {
        boolean ignore = false;

        if (token instanceof SpreadsheetMetadataPropertyHistoryToken) {
            final SpreadsheetMetadataPropertyHistoryToken<?> metadata = token.cast(SpreadsheetMetadataPropertyHistoryToken.class);
            ignore = false == this.propertyName().equals(metadata.propertyName());
        }

        return ignore;
    }

    abstract SpreadsheetMetadataPropertyName<Boolean> propertyName();
}
