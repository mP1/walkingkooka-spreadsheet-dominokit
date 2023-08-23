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

import walkingkooka.locale.HasLocale;
import walkingkooka.spreadsheet.dominokit.CrudDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public interface SpreadsheetMetadataPanelComponentContext extends CrudDialogComponentContext<SpreadsheetMetadata>,
        HasLocale,
        HistoryTokenContext,
        LoggingContext {

    Runnable addSpreadsheetMetadataWatcher(final SpreadsheetMetadataWatcher watcher);

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
}
