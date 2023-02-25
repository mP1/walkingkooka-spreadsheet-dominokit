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
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;

import java.util.Objects;

public final class SpreadsheetLabelMappingSaveHistoryHashToken extends SpreadsheetLabelMappingHistoryHashToken {

    static SpreadsheetLabelMappingSaveHistoryHashToken with(final SpreadsheetId id,
                                                            final SpreadsheetName name,
                                                            final SpreadsheetLabelMapping mapping) {
        return new SpreadsheetLabelMappingSaveHistoryHashToken(
                id,
                name,
                mapping
        );
    }

    private SpreadsheetLabelMappingSaveHistoryHashToken(final SpreadsheetId id,
                                                        final SpreadsheetName name,
                                                        final SpreadsheetLabelMapping mapping) {
        super(
                id,
                name
        );
        this.mapping = Objects.requireNonNull(mapping, "mapping");
    }

    @Override
    SpreadsheetLabelName labelName() {
        return this.mapping.label();
    }

    private final SpreadsheetLabelMapping mapping;

    @Override
    UrlFragment labelUrlFragment() {
        return SAVE.append(
                UrlFragment.with(this.mapping.reference().toString())
        );
    }

    @Override
    SpreadsheetNameHistoryHashToken delete() {
        return this;
    }

    @Override
    SpreadsheetNameHistoryHashToken save(final String value) {
        return this;
    }
}
