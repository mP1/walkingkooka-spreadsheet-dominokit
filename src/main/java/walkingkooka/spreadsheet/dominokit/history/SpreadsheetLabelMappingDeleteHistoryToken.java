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

import java.util.Objects;

public final class SpreadsheetLabelMappingDeleteHistoryToken extends SpreadsheetLabelMappingHistoryToken {

    static SpreadsheetLabelMappingDeleteHistoryToken with(final SpreadsheetId id,
                                                          final SpreadsheetName name,
                                                          final SpreadsheetLabelName labelName) {
        return new SpreadsheetLabelMappingDeleteHistoryToken(
                id,
                name,
                labelName
        );
    }

    private SpreadsheetLabelMappingDeleteHistoryToken(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final SpreadsheetLabelName labelName) {
        super(
                id,
                name
        );
        this.labelName = Objects.requireNonNull(labelName, "labelName");
    }

    @Override
    SpreadsheetLabelName labelName() {
        return this.labelName;
    }

    private final SpreadsheetLabelName labelName;

    @Override
    UrlFragment labelUrlFragment() {
        return DELETE;
    }

    // new id/name same labelName
    @Override
    public HistoryToken idName(final SpreadsheetId id,
                               final SpreadsheetName name) {
        return with(
                id,
                name,
                this.labelName()
        );
    }

    @Override
    SpreadsheetNameHistoryToken delete() {
        return this;
    }

    @Override
    SpreadsheetNameHistoryToken save(final String value) {
        return this;
    }

    @Override
    public void onHashChange(final HistoryToken previous,
                             final AppContext context) {
        // DELETE label mapping
    }
}
