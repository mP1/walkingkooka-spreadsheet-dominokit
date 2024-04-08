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

package walkingkooka.spreadsheet.dominokit.ui.spreadsheetlist;

import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

/**
 * A row within a {@link SpreadsheetListComponentTable}
 */
final class SpreadsheetListComponentTableRow {

    static SpreadsheetListComponentTableRow with(final SpreadsheetMetadata metadata) {
        return new SpreadsheetListComponentTableRow(metadata);
    }

    private SpreadsheetListComponentTableRow(final SpreadsheetMetadata metadata) {
        this.metadata = metadata;
    }

    HistoryTokenAnchorComponent name() {
        final SpreadsheetMetadata metadata = this.metadata;

        final SpreadsheetId id = metadata.id().orElse(null);

        return HistoryToken.spreadsheetLoad(id)
                .link(
                        SpreadsheetListComponent.ID_PREFIX + id.toString() + SpreadsheetIds.LINK
                ).setTextContent(
                        metadata.name()
                                .orElse(null)
                                .toString()
                );
    }

    String createdBy() {
        return this.metadata.getOrFail(SpreadsheetMetadataPropertyName.CREATOR)
                .toString();
    }

    String lastModifiedBy() {
        return this.metadata.getOrFail(SpreadsheetMetadataPropertyName.MODIFIED_BY)
                .toString();
    }

    String createDateTime() {
        return this.format(SpreadsheetMetadataPropertyName.CREATE_DATE_TIME);
    }

    String lastModifiedDateTime() {
        return this.format(SpreadsheetMetadataPropertyName.MODIFIED_DATE_TIME);
    }

    private String format(final SpreadsheetMetadataPropertyName<LocalDateTime> dateTime) {
        final SpreadsheetMetadata metadata = this.metadata;

        return DateTimeFormatter.ofLocalizedDateTime(
                        FormatStyle.SHORT,
                        FormatStyle.SHORT
                ).withLocale(metadata.getOrFail(SpreadsheetMetadataPropertyName.LOCALE))
                .format(
                        metadata.getOrFail(dateTime)
                );
    }

    List<HistoryTokenAnchorComponent> links() {
        return Lists.empty();
    }

    private final SpreadsheetMetadata metadata;

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .separator("|")
                .valueSeparator(" ")
                .disable(ToStringBuilderOption.QUOTE)
                .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
                .value(this.name())
                .value(this.createdBy())
                .value(this.createDateTime())
                .value(this.lastModifiedBy())
                .value(this.lastModifiedDateTime())
                .value(this.links())
                .build();
    }
}
