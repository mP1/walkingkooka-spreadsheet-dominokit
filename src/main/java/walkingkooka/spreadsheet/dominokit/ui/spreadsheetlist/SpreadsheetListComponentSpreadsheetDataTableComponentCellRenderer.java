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

import walkingkooka.Value;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.datatable.SpreadsheetDataTableComponentCellRenderer;
import walkingkooka.spreadsheet.dominokit.ui.flexlayout.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.ui.text.SpreadsheetTextComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.time.LocalDateTime;
import java.util.Optional;

final class SpreadsheetListComponentSpreadsheetDataTableComponentCellRenderer implements SpreadsheetDataTableComponentCellRenderer<SpreadsheetMetadata> {

    static SpreadsheetListComponentSpreadsheetDataTableComponentCellRenderer with(final SpreadsheetListComponentContext context) {
        return new SpreadsheetListComponentSpreadsheetDataTableComponentCellRenderer(context);
    }

    private SpreadsheetListComponentSpreadsheetDataTableComponentCellRenderer(final SpreadsheetListComponentContext context) {
        this.context = context;
    }

    @Override
    public HtmlElementComponent<?, ?> render(final int column,
                                             final SpreadsheetMetadata metadata) {
        final HtmlElementComponent<?, ?> component;

        switch (column) {
            case 0: // name
                component = spreadsheetName(metadata);
                break;
            case 1: // created by
                component = hasText(
                        SpreadsheetMetadataPropertyName.CREATOR,
                        metadata
                );
                break;
            case 2: // create-date-time
                component = dateTime(
                        SpreadsheetMetadataPropertyName.CREATE_DATE_TIME,
                        metadata
                );
                break;
            case 3: // lastmod by
                component = hasText(
                        SpreadsheetMetadataPropertyName.MODIFIED_BY,
                        metadata
                );
                break;
            case 4: // create-date-time
                component = dateTime(
                        SpreadsheetMetadataPropertyName.MODIFIED_DATE_TIME,
                        metadata
                );
                break;
            case 5: // links
                component = links(metadata);
                break;
            default:
                throw new IllegalArgumentException("Unknown column " + column);
        }


        return component;
    }

    private HtmlElementComponent<?, ?> spreadsheetName(final SpreadsheetMetadata metadata) {
        final SpreadsheetId id = metadata.id().orElse(null);

        return HistoryToken.spreadsheetLoad(id)
                .link(
                        SpreadsheetListDialogComponent.ID_PREFIX + id.toString()
                ).setTextContent(
                        metadata.name()
                                .orElse(null)
                                .toString()
                );
    }

    private <TT extends Value<String>> SpreadsheetTextComponent hasText(final SpreadsheetMetadataPropertyName<TT> propertyName,
                                                                        final SpreadsheetMetadata metadata) {
        return text(
                metadata.get(propertyName)
                        .map(Value::value)
        );
    }

    private SpreadsheetTextComponent dateTime(final SpreadsheetMetadataPropertyName<LocalDateTime> propertyName,
                                              final SpreadsheetMetadata metadata) {
        return text(
                metadata.get(propertyName)
                        .map(this.context::formatDateTime)
        );
    }

    private SpreadsheetTextComponent text(final Optional<String> text) {
        return SpreadsheetTextComponent.with(text);
    }

    private SpreadsheetFlexLayout links(final SpreadsheetMetadata metadata) {
        final SpreadsheetId id = metadata.id()
                .orElse(null);

        final HistoryTokenAnchorComponent rename = HistoryToken.spreadsheetListRenameSelect(
                        id
                ).link(SpreadsheetListDialogComponent.ID_PREFIX + id + "-rename")
                .setTextContent("Rename");

        final HistoryTokenAnchorComponent delete = HistoryToken.spreadsheetListDelete(
                        id
                ).link(SpreadsheetListDialogComponent.ID_PREFIX + id + "-delete")
                .setTextContent("Delete");

        return SpreadsheetFlexLayout.row()
                .appendChild(rename)
                .appendChild(delete);
    }

    private final SpreadsheetListComponentContext context;
}
