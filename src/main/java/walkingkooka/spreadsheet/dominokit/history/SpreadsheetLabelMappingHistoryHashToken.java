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
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.tree.text.TextStylePropertyName;

public abstract class SpreadsheetLabelMappingHistoryHashToken extends SpreadsheetSelectionHistoryHashToken {

    SpreadsheetLabelMappingHistoryHashToken(final SpreadsheetId id,
                                            final SpreadsheetName name) {
        super(
                id,
                name
        );
    }

    /**
     * /label/$label...
     */
    @Override final UrlFragment selectionUrlFragment() {
        return LABEL.append(UrlFragment.with(this.labelName().value()))
                .append(this.labelUrlFragment());
    }

    abstract SpreadsheetLabelName labelName();

    abstract UrlFragment labelUrlFragment();

    @Override
    final SpreadsheetSelectionHistoryHashToken clear() {
        return this;
    }

    @Override
    final SpreadsheetSelectionHistoryHashToken formula() {
        return this;
    }

    @Override
    final SpreadsheetSelectionHistoryHashToken freeze() {
        return this;
    }

    @Override
    final SpreadsheetSelectionHistoryHashToken menu() {
        return this;
    }

    @Override
    SpreadsheetSelectionHistoryHashToken pattern(final SpreadsheetPatternKind patternKind) {
        return this;
    }

    @Override
    final SpreadsheetSelectionHistoryHashToken style(final TextStylePropertyName<?> propertyName) {
        return this;
    }

    @Override
    final SpreadsheetSelectionHistoryHashToken unfreeze() {
        return this;
    }
}
