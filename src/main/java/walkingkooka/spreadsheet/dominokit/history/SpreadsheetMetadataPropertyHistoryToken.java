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
import walkingkooka.spreadsheet.format.pattern.HasSpreadsheetPatternKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Objects;
import java.util.Optional;

public abstract class SpreadsheetMetadataPropertyHistoryToken<T> extends SpreadsheetMetadataHistoryToken
        implements HasSpreadsheetPatternKind {

    SpreadsheetMetadataPropertyHistoryToken(final SpreadsheetId id,
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

    final Optional<SpreadsheetPatternKind> patternKind0() {
        return this.propertyName.patternKind();
    }

    // HasUrlFragment...................................................................................................

    @Override //
    final UrlFragment metadataUrlFragment() {
        return this.propertyName()
                .urlFragment()
                .appendSlashThen(
                        this.metadataPropertyUrlFragment()
                );
    }

    abstract UrlFragment metadataPropertyUrlFragment();
}
