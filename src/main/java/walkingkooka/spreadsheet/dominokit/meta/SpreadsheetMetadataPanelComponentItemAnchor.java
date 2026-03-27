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

package walkingkooka.spreadsheet.dominokit.meta;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLAnchorElement;
import walkingkooka.collect.set.Sets;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link SpreadsheetMetadataPanelComponentItem} that displays a link which probably opens a dialog for editing.
 */
final class SpreadsheetMetadataPanelComponentItemAnchor<T> extends SpreadsheetMetadataPanelComponentItem<T, SpreadsheetMetadataPanelComponentItemAnchor<T>, HTMLAnchorElement> {

    static <T> SpreadsheetMetadataPanelComponentItemAnchor<T> with(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                   final SpreadsheetMetadataPanelComponentContext context) {
        return new SpreadsheetMetadataPanelComponentItemAnchor<>(
            propertyName,
            context
        );
    }

    private SpreadsheetMetadataPanelComponentItemAnchor(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                        final SpreadsheetMetadataPanelComponentContext context) {
        super(
            propertyName,
            context
        );

        final HistoryToken historyToken = context.historyToken();

        this.anchor = historyToken.link(
            SpreadsheetMetadataPanelComponent.id(propertyName)
        ).setTextContent(
            propertyName.text()
        ).setHistoryToken(
            Optional.of(
                historyToken.setMetadataPropertyName(propertyName)
            )
        );
    }

    @Override//
    SpreadsheetMetadataPanelComponentItemAnchor<T> addFocusListener(final EventListener listener) {
        this.anchor.addFocusListener(listener);
        return this;
    }

    @Override
    void focus() {
        this.anchor.focus();
    }

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final RefreshContext context) {
        final SpreadsheetMetadataPropertyName<?> propertyName = this.propertyName;

        final Set<String> flags;

        if (propertyName.equals(SpreadsheetMetadataPropertyName.LOCALE)) {
            final SpreadsheetMetadataPanelComponentContext spreadsheetMetadataPanelComponentContext = this.context;

            final String flag = spreadsheetMetadataPanelComponentContext.spreadsheetMetadata()
                .get(SpreadsheetMetadataPropertyName.LOCALE)
                .map(Locale::getCountry)
                .orElse(null);

            flags = Sets.of(
                null != flag ?
                    flag :
                    null
            );
        } else {
            flags = Sets.empty();
        }

        this.anchor.setHistoryToken(
            Optional.of(
                context.historyToken()
                    .setMetadataPropertyName(propertyName)
            )
        ).setFlags(flags);
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HistoryTokenAnchorComponent htmlComponent() {
        return this.anchor;
    }

    private final HistoryTokenAnchorComponent anchor;
}
