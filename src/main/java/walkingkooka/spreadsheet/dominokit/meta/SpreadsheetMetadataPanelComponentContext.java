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

import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.UiFormattingContext;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CharSequences;
import walkingkooka.util.HasLocale;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public interface SpreadsheetMetadataPanelComponentContext extends RefreshContext,
    HasLocale,
    HasSpreadsheetMetadata,
    UiFormattingContext {

    Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher);

    default String datePattern() {
        final SimpleDateFormat simpleDateFormat = (SimpleDateFormat)
            DateFormat.getDateInstance(
                DateFormat.MEDIUM,
                this.locale()
            );
        return simpleDateFormat.toPattern();
    }

    default void save(final SpreadsheetMetadataPropertyName<?> propertyName,
                      final String saveText) {
        this.debug(
            this.getClass().getSimpleName() +
                ".save " +
                CharSequences.quoteAndEscape(saveText)
        );

        this.pushHistoryToken(
            this.historyToken()
                .setMetadataPropertyName(propertyName)
                .setSaveStringValue(saveText)
        );
    }
}
