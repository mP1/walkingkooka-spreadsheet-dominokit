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

package walkingkooka.spreadsheet.dominokit.spreadsheet;

import walkingkooka.environment.AuditInfo;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.Component;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponentCellRenderer;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.text.SpreadsheetTextComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Function;

final class SpreadsheetListTableComponentSpreadsheetDataTableComponentCellRenderer implements SpreadsheetDataTableComponentCellRenderer<SpreadsheetMetadata> {

    static SpreadsheetListTableComponentSpreadsheetDataTableComponentCellRenderer with(final String id,
                                                                                       final SpreadsheetListDialogComponentContext context) {
        return new SpreadsheetListTableComponentSpreadsheetDataTableComponentCellRenderer(
            id,
            context
        );
    }

    private SpreadsheetListTableComponentSpreadsheetDataTableComponentCellRenderer(final String id,
                                                                                   final SpreadsheetListDialogComponentContext context) {
        this.id = id;
        this.context = context;
    }

    @Override
    public Component render(final int column,
                            final SpreadsheetMetadata metadata) {
        final Component component;

        switch (column) {
            case 0: // name
                component = spreadsheetName(metadata);
                break;
            case 1: // created by
                component = this.auditInfoUser(
                    metadata,
                    AuditInfo::createdBy
                );
                break;
            case 2: // create-date-time
                component = this.auditInfoTimestamp(
                    metadata,
                    AuditInfo::createdTimestamp
                );
                break;
            case 3: // lastmod by
                component = this.auditInfoUser(
                    metadata,
                    AuditInfo::modifiedBy
                );
                break;
            case 4: // modified timestamp
                component = this.auditInfoTimestamp(
                    metadata,
                    AuditInfo::modifiedTimestamp
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

    private SpreadsheetTextComponent auditInfoUser(final SpreadsheetMetadata metadata,
                                                   final Function<AuditInfo, EmailAddress> userEmailGetter) {
        return this.text(
            metadata.get(SpreadsheetMetadataPropertyName.AUDIT_INFO)
                .map(a -> userEmailGetter.apply(a).value())
        );
    }

    private SpreadsheetTextComponent auditInfoTimestamp(final SpreadsheetMetadata metadata,
                                                        final Function<AuditInfo, LocalDateTime> dateTimeGetter) {
        return this.text(
            metadata.get(SpreadsheetMetadataPropertyName.AUDIT_INFO)
                .map(a -> this.context.formatDateTime(
                        dateTimeGetter.apply(a)
                    )
                )
        );
    }

    private HtmlElementComponent<?, ?> spreadsheetName(final SpreadsheetMetadata metadata) {
        final SpreadsheetId spreadsheetId = metadata.id()
            .orElse(null);

        return HistoryToken.spreadsheetLoad(spreadsheetId)
            .link(
                this.id + spreadsheetId.toString()
            ).setTextContent(
                metadata.name()
                    .orElse(null)
                    .toString()
            );
    }

    private SpreadsheetTextComponent text(final Optional<String> text) {
        return SpreadsheetTextComponent.with(text);
    }

    private SpreadsheetFlexLayout links(final SpreadsheetMetadata metadata) {
        final String id = this.id;
        final SpreadsheetId spreadsheetId = metadata.id()
            .orElse(null);

        final HistoryTokenAnchorComponent rename = HistoryToken.spreadsheetListRenameSelect(
                spreadsheetId
            ).link(id + spreadsheetId + "-rename")
            .setTextContent("Rename");

        final HistoryTokenAnchorComponent delete = HistoryToken.spreadsheetListDelete(
                spreadsheetId
            ).link(id + spreadsheetId + "-delete")
            .setTextContent("Delete");

        return SpreadsheetFlexLayout.row()
            .appendChild(rename)
            .appendChild(delete);
    }

    private final String id;

    private final SpreadsheetListDialogComponentContext context;
}
