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

import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.text.TextStylePropertyName;

/**
 * Instances represent a token within a history hash.
 */
public abstract class SpreadsheetHistoryHashToken implements HasUrlFragment {

    final static UrlFragment CLEAR = UrlFragment.SLASH.append(UrlFragment.with("clear"));

    final static UrlFragment DELETE = UrlFragment.SLASH.append(UrlFragment.with("delete"));

    final static UrlFragment FREEZE = UrlFragment.SLASH.append(UrlFragment.with("freeze"));

    final static UrlFragment MENU = UrlFragment.SLASH.append(UrlFragment.with("menu"));

    final static UrlFragment SELECT = UrlFragment.EMPTY;

    final static UrlFragment SAVE = UrlFragment.SLASH
            .append(UrlFragment.with("save"))
            .append(UrlFragment.SLASH);

    final static UrlFragment STYLE = UrlFragment.SLASH
            .append(UrlFragment.with("style"))
            .append(UrlFragment.SLASH);

    final static UrlFragment UNFREEZE = UrlFragment.SLASH
            .append(UrlFragment.with("unfreeze"));

    /**
     * {@see SpreadsheetCellSelectHistoryHashToken}
     */
    public static SpreadsheetCellSelectHistoryHashToken cell(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellSelectHistoryHashToken.with(viewportSelection);
    }

    /**
     * {@see SpreadsheetCellFormulaSelectHistoryHashToken}
     */
    public static SpreadsheetCellFormulaSelectHistoryHashToken formula(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellFormulaSelectHistoryHashToken.with(viewportSelection);
    }

    /**
     * {@see SpreadsheetCellFormulaSaveHistoryHashToken}
     */
    public static SpreadsheetCellFormulaSaveHistoryHashToken formulaSave(final SpreadsheetViewportSelection viewportSelection,
                                                                         final SpreadsheetFormula formula) {
        return SpreadsheetCellFormulaSaveHistoryHashToken.formulaSave(
                viewportSelection,
                formula
        );
    }

    /**
     * {@see SpreadsheetCellClearHistoryHashToken}
     */
    public static SpreadsheetCellClearHistoryHashToken cellClear(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellClearHistoryHashToken.with(viewportSelection);
    }

    /**
     * {@see SpreadsheetCellDeleteHistoryHashToken}
     */
    public static SpreadsheetCellDeleteHistoryHashToken cellDelete(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellDeleteHistoryHashToken.with(viewportSelection);
    }

    /**
     * {@see SpreadsheetCellFreezeHistoryHashToken}
     */
    public static SpreadsheetCellFreezeHistoryHashToken cellFreeze(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellFreezeHistoryHashToken.with(viewportSelection);
    }

    /**
     * {@see SpreadsheetCellMenuHistoryHashToken}
     */
    public static SpreadsheetCellMenuHistoryHashToken cellMenu(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellMenuHistoryHashToken.with(viewportSelection);
    }

    /**
     * {@see SpreadsheetCellPatternSelectHistoryHashToken}
     */
    public static SpreadsheetCellPatternSelectHistoryHashToken cellPattern(final SpreadsheetViewportSelection viewportSelection,
                                                                           final SpreadsheetPatternKind patternKind) {
        return SpreadsheetCellPatternSelectHistoryHashToken.with(
                viewportSelection,
                patternKind
        );
    }

    /**
     * {@see SpreadsheetCellPatternSaveHistoryHashToken}
     */
    public static SpreadsheetCellPatternSaveHistoryHashToken cellPatternSave(final SpreadsheetViewportSelection viewportSelection,
                                                                             final SpreadsheetPattern pattern) {
        return SpreadsheetCellPatternSaveHistoryHashToken.with(
                viewportSelection,
                pattern
        );
    }

    /**
     * {@see SpreadsheetCellStyleSelectHistoryHashToken}
     */
    public static SpreadsheetCellStyleSelectHistoryHashToken cellStyle(final SpreadsheetViewportSelection viewportSelection,
                                                                       final TextStylePropertyName<?> propertyName) {
        return SpreadsheetCellStyleSelectHistoryHashToken.with(
                viewportSelection,
                propertyName
        );
    }

    /**
     * {@see SpreadsheetCellStyleSaveHistoryHashToken}
     */
    public static <T> SpreadsheetCellStyleSaveHistoryHashToken<T> cellStyleSave(final SpreadsheetViewportSelection viewportSelection,
                                                                                final TextStylePropertyName<T> propertyName,
                                                                                final T propertyValue) {
        return SpreadsheetCellStyleSaveHistoryHashToken.with(
                viewportSelection,
                propertyName,
                propertyValue
        );
    }

    /**
     * {@see SpreadsheetCellUnfreezeHistoryHashToken}
     */
    public static SpreadsheetCellUnfreezeHistoryHashToken cellUnfreeze(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellUnfreezeHistoryHashToken.with(viewportSelection);
    }


    /**
     * {@see SpreadsheetColumnOrRowSelectHistoryHashToken}
     */
    public static SpreadsheetColumnOrRowSelectHistoryHashToken columnOrRow(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnOrRowSelectHistoryHashToken.with(viewportSelection);
    }

    /**
     * {@see SpreadsheetColumnOrRowClearHistoryHashToken}
     */
    public static SpreadsheetColumnOrRowClearHistoryHashToken columnOrRowClear(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnOrRowClearHistoryHashToken.with(viewportSelection);
    }

    /**
     * {@see SpreadsheetColumnOrRowDeleteHistoryHashToken}
     */
    public static SpreadsheetColumnOrRowDeleteHistoryHashToken columnOrRowDelete(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnOrRowDeleteHistoryHashToken.with(viewportSelection);
    }

    /**
     * {@see SpreadsheetColumnOrRowFreezeHistoryHashToken}
     */
    public static SpreadsheetColumnOrRowFreezeHistoryHashToken columnOrRowFreeze(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnOrRowFreezeHistoryHashToken.with(viewportSelection);
    }

    /**
     * {@see SpreadsheetColumnOrRowMenuHistoryHashToken}
     */
    public static SpreadsheetColumnOrRowMenuHistoryHashToken columnOrRowMenu(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnOrRowMenuHistoryHashToken.with(viewportSelection);
    }

    /**
     * {@see SpreadsheetColumnOrRowUnfreezeHistoryHashToken}
     */
    public static SpreadsheetColumnOrRowUnfreezeHistoryHashToken columnOrRowUnfreeze(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnOrRowUnfreezeHistoryHashToken.with(viewportSelection);
    }

    SpreadsheetHistoryHashToken() {
        super();
    }

    @Override
    public int hashCode() {
        return this.urlFragment().hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                (other != null && this.getClass().equals(other.getClass()) && this.equals0((SpreadsheetHistoryHashToken) other));
    }

    private boolean equals0(final SpreadsheetHistoryHashToken other) {
        return this.urlFragment().equals(other.urlFragment());
    }

    @Override
    public final String toString() {
        return this.urlFragment().toString();
    }
}
