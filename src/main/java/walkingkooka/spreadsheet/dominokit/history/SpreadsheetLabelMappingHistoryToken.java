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
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolvers;
import walkingkooka.text.cursor.TextCursor;

import java.util.Optional;

public abstract class SpreadsheetLabelMappingHistoryToken extends SpreadsheetSelectionHistoryToken {

    SpreadsheetLabelMappingHistoryToken(final SpreadsheetId id,
                                        final SpreadsheetName name) {
        super(
                id,
                name
        );
    }

    /**
     * /label/$label...
     */
    @Override //
    final UrlFragment selectionUrlFragment() {
        return LABEL.appendSlashThen(
                this.labelUrlFragment()
        );
    }

    /**
     * Getter that returns the {@link SpreadsheetLabelName}.
     */
    abstract public Optional<SpreadsheetLabelName> labelName();

    abstract UrlFragment labelUrlFragment();

    final HistoryToken labelMappingSelect() {
        return HistoryToken.labelMapping(
                this.id(),
                this.name(),
                this.labelName()
        );
    }

    // parse............................................................................................................

    @Override
    HistoryToken parse0(final String component,
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
}
