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

package walkingkooka.spreadsheet.dominokit.ui.meta;

import elemental2.dom.HTMLAnchorElement;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CharSequences;

import java.util.Optional;

/**
 * A {@link SpreadsheetMetadataPanelComponentItem} that only displays a link that when clicked opens a {@link SpreadsheetMetadataPanelComponent}.
 */
final class SpreadsheetMetadataPanelComponentItemSpreadsheetPattern<T extends SpreadsheetPattern> extends SpreadsheetMetadataPanelComponentItem<T> {

    static <T extends SpreadsheetPattern> SpreadsheetMetadataPanelComponentItemSpreadsheetPattern<T> with(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                                                          final SpreadsheetMetadataPanelComponentContext context) {
        checkPropertyName(propertyName);
        checkContext(context);

        return new SpreadsheetMetadataPanelComponentItemSpreadsheetPattern<>(
                propertyName,
                context
        );
    }

    private SpreadsheetMetadataPanelComponentItemSpreadsheetPattern(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                    final SpreadsheetMetadataPanelComponentContext context) {
        super(
                propertyName,
                context
        );
        this.anchor = HistoryTokenAnchorComponent.empty()
                .setId(SpreadsheetMetadataPanelComponent.id(propertyName));
    }

    @Override
    void focus() {
        this.anchor.focus();
    }

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final AppContext context) {
        final SpreadsheetMetadataPropertyName<T> propertyName = this.propertyName;

        final String pattern = context.spreadsheetMetadata()
                .getIgnoringDefaults(propertyName)
                .map(SpreadsheetPattern::text)
                .orElse("edit");

        final HistoryToken historyToken = context.historyToken()
                .setPatternKind(
                        propertyName.patternKind()
                );
        context.debug(this.getClass().getSimpleName() + ".refresh " + historyToken + " " + CharSequences.quoteAndEscape(pattern));

        this.anchor.setHistoryToken(
                Optional.of(historyToken)
        ).setTextContent(pattern)
                .setDisabled(false);
    }

    // isElement........................................................................................................

    @Override
    public HTMLAnchorElement element() {
        return this.anchor.element();
    }

    private final HistoryTokenAnchorComponent anchor;
}
