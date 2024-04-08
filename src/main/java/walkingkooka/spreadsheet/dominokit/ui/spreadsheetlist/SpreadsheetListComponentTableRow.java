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

    String created() {
        final SpreadsheetMetadata metadata = this.metadata;

        return metadata.getOrFail(SpreadsheetMetadataPropertyName.CREATOR) +
                " " +
                metadata.getOrFail(SpreadsheetMetadataPropertyName.CREATE_DATE_TIME);
    }

    String lastModified() {
        final SpreadsheetMetadata metadata = this.metadata;

        return metadata.getOrFail(SpreadsheetMetadataPropertyName.MODIFIED_BY) +
                " " +
                metadata.getOrFail(SpreadsheetMetadataPropertyName.MODIFIED_DATE_TIME);
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
                .value(this.created())
                .value(this.lastModified())
                .value(this.links())
                .build();
    }
}
