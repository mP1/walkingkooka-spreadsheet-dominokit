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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;

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

    @Override
    SpreadsheetLabelName labelName() {
        return this.labelName;
    }

    private final SpreadsheetLabelName labelName;

    @Override
    UrlFragment labelUrlFragment() {
        return SELECT;
    }

    @Override
    HistoryToken setDelete0() {
        return labelMappingDelete(
                this.id(),
                this.name(),
                this.labelName()
        );
    }

    // new id/name same labelName
    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.labelName()
        );
    }

    @Override
    HistoryToken setSave0(final String value) {
        return labelMappingSave(
                this.id(),
                this.name(),
                this.labelName().mapping(
                        SpreadsheetSelection.parseExpressionReference(value)
                )
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // show label mapping UI
    }
}
