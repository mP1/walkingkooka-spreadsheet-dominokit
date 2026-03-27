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

import walkingkooka.collect.set.SortedSets;
import walkingkooka.currency.CurrencyContexts;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Currency;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * A {@link SpreadsheetMetadataPanelComponentItemAnchor} that displays a link which probably opens a dialog for editing to
 * edit {@link SpreadsheetMetadataPropertyName#CURRENCY}.
 */
final class SpreadsheetMetadataPanelComponentItemAnchorCurrency extends SpreadsheetMetadataPanelComponentItemAnchor<Currency> {

    static SpreadsheetMetadataPanelComponentItemAnchorCurrency with(final SpreadsheetMetadataPanelComponentContext context) {
        return new SpreadsheetMetadataPanelComponentItemAnchorCurrency(context);
    }

    private SpreadsheetMetadataPanelComponentItemAnchorCurrency(final SpreadsheetMetadataPanelComponentContext context) {
        super(
            SpreadsheetMetadataPropertyName.CURRENCY,
            context
        );
    }

    @Override
    void refreshAnchor() {
        final SpreadsheetMetadataPanelComponentContext context = this.context;

        this.anchor.setFlags(
            context.spreadsheetMetadata()
                .get(SpreadsheetMetadataPropertyName.CURRENCY)
                .map(c -> context.localesForCurrencyCode(c.getCurrencyCode())
                    .stream()
                    .map(Locale::getCountry)
                    .collect(
                        Collectors.toCollection(
                            () -> SortedSets.tree(
                                CurrencyContexts.CASE_SENSITIVITY.comparator()
                            )
                        )
                    )
                ).orElse(SortedSets.empty())
        );
    }
}
