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
import java.util.Optional;

public final class SpreadsheetLabelMappingSelectHistoryToken extends SpreadsheetLabelMappingHistoryToken {

    static SpreadsheetLabelMappingSelectHistoryToken with(final SpreadsheetId id,
                                                          final SpreadsheetName name,
                                                          final Optional<SpreadsheetLabelName> labelName) {
        return new SpreadsheetLabelMappingSelectHistoryToken(
                id,
                name,
                labelName
        );
    }

    private SpreadsheetLabelMappingSelectHistoryToken(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final Optional<SpreadsheetLabelName> labelName) {
        super(
                id,
                name
        );
        this.labelName = Objects.requireNonNull(labelName, "labelName");
    }

    @Override
    public Optional<SpreadsheetLabelName> labelName() {
        return this.labelName;
    }

    private final Optional<SpreadsheetLabelName> labelName;

    //
    // Label123
    @Override
    UrlFragment labelUrlFragment() {
        return this.labelName.map(
                l -> UrlFragment.with(
                        "/" + l.value()
                )
        ).orElse(UrlFragment.EMPTY);
    }

    @Override
    HistoryToken setDelete0() {
        final Optional<SpreadsheetLabelName> labelName = this.labelName;
        return labelName.isPresent() ?
                labelMappingDelete(
                        this.id(),
                        this.name(),
                        labelName.get()
                ) : this;
    }

    // new id/name same labelName
    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.labelName
        );
    }

    @Override
    HistoryToken setSave0(final String value) {
        final Optional<SpreadsheetLabelName> labelName = this.labelName;

        return labelName.isPresent() ?
                labelMappingSave(
                        this.id(),
                        this.name(),
                        labelName.get()
                                .mapping(
                                        SpreadsheetSelection.parseExpressionReference(value)
                                )
                ) :
                this;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // show label mapping UI
    }
}
