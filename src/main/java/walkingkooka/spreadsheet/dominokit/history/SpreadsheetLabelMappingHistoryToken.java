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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.text.TextStylePropertyName;

public abstract class SpreadsheetLabelMappingHistoryToken extends SpreadsheetSelectionHistoryToken {

    SpreadsheetLabelMappingHistoryToken(final SpreadsheetId id,
                                        final SpreadsheetName name) {
        super(
                id,
                name
        );
    }

    /**
     * /label/$label...
     */
    @Override //
    final UrlFragment selectionUrlFragment() {
        return LABEL.append(UrlFragment.with(this.labelName().value()))
                .append(this.labelUrlFragment());
    }

    abstract SpreadsheetLabelName labelName();

    abstract UrlFragment labelUrlFragment();

    @Override //
    final HistoryToken setClear0() {
        return this;
    }

    @Override
    public final HistoryToken formulaHistoryToken() {
        return this;
    }

    @Override
    public HistoryToken formulaSaveHistoryToken(final String text) {
        throw new UnsupportedOperationException();
    }

    @Override //
    final HistoryToken setFreeze0() {
        return this;
    }

    @Override //
    final HistoryToken setMenu1() {
        return this;
    }

    @Override //
    final SpreadsheetViewportSelection setMenu2ViewportSelection(final SpreadsheetSelection selection) {
        return selection.setDefaultAnchor();
    }

    @Override //
    final HistoryToken setPattern0(final SpreadsheetPatternKind patternKind) {
        return this;
    }

    @Override //
    final HistoryToken setStyle0(final TextStylePropertyName<?> propertyName) {
        return this;
    }

    @Override//
    final HistoryToken setUnfreeze0() {
        return this;
    }
}
