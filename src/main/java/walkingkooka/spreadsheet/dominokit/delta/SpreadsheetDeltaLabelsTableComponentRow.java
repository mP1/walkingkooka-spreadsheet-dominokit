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

package walkingkooka.spreadsheet.dominokit.delta;

import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;

import java.util.Objects;
import java.util.Optional;

final class SpreadsheetDeltaLabelsTableComponentRow {

    static SpreadsheetDeltaLabelsTableComponentRow with(final SpreadsheetLabelMapping mapping,
                                                        final Optional<SpreadsheetCell> cell) {
        return new SpreadsheetDeltaLabelsTableComponentRow(
            mapping,
            cell
        );
    }

    private SpreadsheetDeltaLabelsTableComponentRow(final SpreadsheetLabelMapping mapping,
                                                    final Optional<SpreadsheetCell> cell) {
        this.mapping = mapping;
        this.cell = cell;
    }

    final SpreadsheetLabelMapping mapping;

    final Optional<SpreadsheetCell> cell;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.mapping,
            this.cell
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof SpreadsheetDeltaLabelsTableComponentRow &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final SpreadsheetDeltaLabelsTableComponentRow other) {
        return this.mapping.equals(other.mapping) &&
            this.cell.equals(other.cell);
    }

    @Override
    public String toString() {
        return ToStringBuilder.empty()
            .value(this.mapping)
            .value(this.cell)
            .build();
    }
}
