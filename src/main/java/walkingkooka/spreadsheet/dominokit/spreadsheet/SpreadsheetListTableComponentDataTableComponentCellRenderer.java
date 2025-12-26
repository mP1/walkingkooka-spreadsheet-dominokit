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
import walkingkooka.spreadsheet.dominokit.Component;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.datatable.DataTableComponentCellRenderer;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.text.TextComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Function;

final class SpreadsheetListTableComponentDataTableComponentCellRenderer implements DataTableComponentCellRenderer<SpreadsheetMetadata> {

    static SpreadsheetListTableComponentDataTableComponentCellRenderer with(final String id,
                                                                            final SpreadsheetListDialogComponentContext context) {
        return new SpreadsheetListTableComponentDataTableComponentCellRenderer(
            id,
            context
        );
    }

    private SpreadsheetListTableComponentDataTableComponentCellRenderer(final String id,
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

    private TextComponent auditInfoUser(final SpreadsheetMetadata metadata,
                                        final Function<AuditInfo, EmailAddress> userEmailGetter) {
        return this.text(
            metadata.get(SpreadsheetMetadataPropertyName.AUDIT_INFO)
                .map(a -> userEmailGetter.apply(a).value())
        );
    }

    private TextComponent auditInfoTimestamp(final SpreadsheetMetadata metadata,
                                             final Function<AuditInfo, LocalDateTime> dateTimeGetter) {
        return this.text(
            metadata.get(SpreadsheetMetadataPropertyName.AUDIT_INFO)
                .map(a -> this.context.formatDateTime(
                        dateTimeGetter.apply(a)
                    )
                )
        );
    }

    private HtmlComponent<?, ?> spreadsheetName(final SpreadsheetMetadata metadata) {
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

    private TextComponent text(final Optional<String> text) {
        return TextComponent.with(text);
    }

    private FlexLayoutComponent links(final SpreadsheetMetadata metadata) {
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

        return FlexLayoutComponent.row()
            .appendChild(rename)
            .appendChild(delete);
    }

    private final String id;

    private final SpreadsheetListDialogComponentContext context;
}
