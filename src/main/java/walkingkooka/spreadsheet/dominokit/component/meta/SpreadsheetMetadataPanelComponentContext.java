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

package walkingkooka.spreadsheet.dominokit.component.meta;

import walkingkooka.locale.HasLocale;
import walkingkooka.spreadsheet.dominokit.history.CloseableHistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CharSequences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public interface SpreadsheetMetadataPanelComponentContext extends CloseableHistoryTokenContext,
        HasLocale,
        HasSpreadsheetMetadata,
        LoggingContext {

    Runnable addSpreadsheetMetadataWatcher(final SpreadsheetMetadataFetcherWatcher watcher);

    /**
     * Helper useful for formatting create and last modified timestamps.
     */
    default String formatDateTime(final LocalDateTime dateTime) {
        return DateTimeFormatter.ofLocalizedDateTime(
                        FormatStyle.SHORT,
                        FormatStyle.SHORT
                ).withLocale(this.locale())
                .format(dateTime);
    }

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
                        .setSave(saveText)
        );
    }
}
