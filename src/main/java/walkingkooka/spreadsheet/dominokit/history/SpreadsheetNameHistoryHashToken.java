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
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Objects;

public abstract class SpreadsheetNameHistoryHashToken extends SpreadsheetHistoryHashToken {

    SpreadsheetNameHistoryHashToken(final SpreadsheetId id,
                                    final SpreadsheetName name) {
        super(id);

        this.name = Objects.requireNonNull(name, "name");
    }

    public final SpreadsheetName name() {
        return this.name;
    }

    private final SpreadsheetName name;

    @Override
    final UrlFragment spreadsheetIdUrlFragment() {
        return this.name.urlFragment()
                .append(
                        this.spreadsheetUrlFragment()
                );
    }

    abstract UrlFragment spreadsheetUrlFragment();

    final SpreadsheetHistoryHashToken cell(final SpreadsheetViewportSelection viewportSelection) {
        return cell(
                this.id(),
                this.name(),
                viewportSelection
        );
    }

    final SpreadsheetHistoryHashToken column(final SpreadsheetViewportSelection viewportSelection) {
        return column(
                this.id(),
                this.name(),
                viewportSelection
        );
    }

    final SpreadsheetHistoryHashToken labelMapping(final SpreadsheetLabelName labelName) {
        return labelMapping(
                this.id(),
                this.name(),
                labelName
        );
    }

    final SpreadsheetHistoryHashToken row(final SpreadsheetViewportSelection viewportSelection) {
        return row(
                this.id(),
                this.name(),
                viewportSelection
        );
    }
}
