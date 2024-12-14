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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;

import java.util.Optional;
import java.util.OptionalInt;

public abstract class SpreadsheetMetadataHistoryToken extends SpreadsheetNameHistoryToken {

    SpreadsheetMetadataHistoryToken(final SpreadsheetId id,
                                    final SpreadsheetName name) {
        super(
                id,
                name
        );
    }

    @Override //
    final UrlFragment spreadsheetNameUrlFragment() {
        return SPREADSHEET.appendSlashThen(
                this.metadataUrlFragment()
        );
    }

    abstract UrlFragment metadataUrlFragment();

    @Override //
    final HistoryToken parse0(final String component,
                              final TextCursor cursor) {
        final HistoryToken result;

        Switch:
        switch (component) {
            case SAVE_STRING:
                result = this.parseSave(cursor);
                break;
            case STYLE_STRING:
                result = this.parseStyle(cursor);
                break;
            default:
                for (final SpreadsheetPatternKind kind : SpreadsheetPatternKind.values()) {
                    if (kind.urlFragment().value().equals(component)) {
                        result = this.setPatternKind(
                                Optional.of(kind)
                        );
                        break Switch;
                    }
                }

                cursor.end();
                result = this; // ignore
                break;
        }

        return result;
    }

    @Override //
    final HistoryToken setClear0() {
        return this;
    }

    @Override //
    final HistoryToken setDelete0() {
        return this;
    }

    @Override //
    final HistoryToken setFreeze0() {
        return this;
    }

    @Override //
    final HistoryToken setInsertAfter0(final OptionalInt count) {
        return this;
    }

    @Override //
    final HistoryToken setInsertBefore0(final OptionalInt count) {
        return this;
    }

    @Override //
    final HistoryToken setMenu1() {
        return this;
    }

    @Override //
    final AnchoredSpreadsheetSelection setMenuSelection(final SpreadsheetSelection selection) {
        return selection.setDefaultAnchor();
    }

    @Override //
    final HistoryToken setUnfreeze0() {
        return this;
    }
}
