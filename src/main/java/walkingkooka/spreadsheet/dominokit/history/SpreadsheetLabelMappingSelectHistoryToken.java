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

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolvers;
import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;
import java.util.Optional;

/**
 * Displays a modal dialog with a form that allows editing of a {@link walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping}.
 * <pre>
 * /123/SpreadsheetName456/label/Label123
 * /spreadsheet-id/spreadsheet-name/label/label-name-to-create-or-edit
 * </pre>
 */
public final class SpreadsheetLabelMappingSelectHistoryToken extends SpreadsheetLabelMappingHistoryToken {

    static SpreadsheetLabelMappingSelectHistoryToken with(final SpreadsheetId id,
                                                          final SpreadsheetName name,
                                                          final SpreadsheetLabelName labelName) {
        return new SpreadsheetLabelMappingSelectHistoryToken(
            id,
            name,
            labelName
        );
    }

    private SpreadsheetLabelMappingSelectHistoryToken(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final SpreadsheetLabelName labelName) {
        super(
            id,
            name
        );
        this.labelName = Objects.requireNonNull(labelName, "labelName");
    }

    // @see HistoryToken.labelName
    final SpreadsheetLabelName labelName;

    //
    // Label123
    @Override
    UrlFragment labelUrlFragment() {
        return UrlFragment.with(
            this.labelName.value()
        );
    }

    // parse............................................................................................................

    @Override
    HistoryToken parseNext(final String component,
                           final TextCursor cursor) {
        final HistoryToken result;

        switch (component) {
            case DELETE_STRING:
                result = this.delete();
                break;
            case MENU_STRING:
                result = this.menu(
                    Optional.empty(), // no selection
                    SpreadsheetLabelNameResolvers.fake()
                );
                break;
            case REFERENCES_STRING:
                result = this.parseReferences(cursor);
                break;
            case SAVE_STRING:
                result = this.parseSave(cursor);
                break;
            default:
                cursor.end();
                result = this; // ignore
                break;
        }

        return result;
    }

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    // new id/name same labelName
    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return with(
            id,
            name,
            this.labelName
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // show label mapping UI
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitSpreadsheetLabelMappingSelect(
            this.id,
            this.name,
            this.labelName
        );
    }
}
