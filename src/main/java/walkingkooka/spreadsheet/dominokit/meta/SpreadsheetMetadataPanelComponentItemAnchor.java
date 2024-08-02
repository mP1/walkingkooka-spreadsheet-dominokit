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

import elemental2.dom.HTMLAnchorElement;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CaseKind;

import java.util.Optional;

/**
 * A {@link SpreadsheetMetadataPanelComponentItem} that displays a link which probably opens a dialog for editing.
 */
final class SpreadsheetMetadataPanelComponentItemAnchor<T> extends SpreadsheetMetadataPanelComponentItem<T> {

    static <T> SpreadsheetMetadataPanelComponentItemAnchor<T> with(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                   final SpreadsheetMetadataPanelComponentContext context) {
        checkPropertyName(propertyName);
        checkContext(context);

        return new SpreadsheetMetadataPanelComponentItemAnchor(
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
                CaseKind.KEBAB.change(
                        propertyName.value(),
                        CaseKind.CAMEL
                )
        ).setTextContent(
                propertyName.text()
        ).setHistoryToken(
                Optional.of(
                        historyToken.setMetadataPropertyName(propertyName)
                )
        );
    }

    @Override
    void focus() {
        this.anchor.focus();
    }

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final AppContext context) {
        // anchor is never refreshed. Assumes the panel will be recreated when the SpreadsheetId changes.
    }

    // isElement........................................................................................................

    @Override
    public HTMLAnchorElement element() {
        return this.anchor.element();
    }

    private final HistoryTokenAnchorComponent anchor;
}
