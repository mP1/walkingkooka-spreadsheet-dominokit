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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;

public abstract class SpreadsheetMetadataHistoryHashToken<T> extends SpreadsheetNameHistoryHashToken {

    SpreadsheetMetadataHistoryHashToken(final SpreadsheetId id,
                                        final SpreadsheetName name,
                                        final SpreadsheetMetadataPropertyName<T> propertyName) {
        super(
                id,
                name
        );

        this.propertyName = Objects.requireNonNull(propertyName, "propertyName");
    }

    public final SpreadsheetMetadataPropertyName<T> propertyName() {
        return this.propertyName;
    }

    private final SpreadsheetMetadataPropertyName<T> propertyName;

    @Override
    final UrlFragment spreadsheetUrlFragment() {
        return METADATA.append(
                this.propertyName().urlFragment()
        ).append(
                this.metadataUrlFragment()
        );
    }

    private final static UrlFragment METADATA = UrlFragment.parse("/metadata");

    abstract UrlFragment metadataUrlFragment();

    @Override
    HistoryHashToken parse0(final String component,
                            final TextCursor cursor) {
        HistoryHashToken result = this;

        switch(component) {
            case "pattern":
                result = this.parsePattern(cursor);
                break;
            case "save":
                result = this.parseSave(cursor);
                break;
            case "style":
                result = this.parseStyle(cursor);
                break;
            default:
                cursor.end();
                result = this; // ignore
                break;
        }

        return result;
    }

    @Override
    final SpreadsheetNameHistoryHashToken clear() {
        return this;
    }

    @Override
    final SpreadsheetNameHistoryHashToken delete() {
        return this;
    }

    @Override
    final SpreadsheetNameHistoryHashToken formulaHistoryHashToken() {
        return this;
    }

    @Override
    final SpreadsheetNameHistoryHashToken freeze() {
        return this;
    }

    @Override
    final SpreadsheetNameHistoryHashToken menu() {
        return this;
    }

    @Override
    final SpreadsheetNameHistoryHashToken pattern(final SpreadsheetPatternKind patternKind) {
        return this;
    }

    @Override
    final SpreadsheetNameHistoryHashToken unfreeze() {
        return this;
    }
}
