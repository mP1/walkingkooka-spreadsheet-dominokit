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
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;

/**
 * Instances represent a token within a history hash.
 */
public abstract class SpreadsheetHistoryHashToken extends HistoryHashToken {

    final static UrlFragment CLEAR = UrlFragment.SLASH.append(UrlFragment.with("clear"));

    final static UrlFragment DELETE = UrlFragment.SLASH.append(UrlFragment.with("delete"));

    final static UrlFragment FREEZE = UrlFragment.SLASH.append(UrlFragment.with("freeze"));

    final static UrlFragment LABEL = UrlFragment.SLASH.append(UrlFragment.with("label"))
            .append(UrlFragment.SLASH);

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
    public static SpreadsheetCellSelectHistoryHashToken cell(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellSelectHistoryHashToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellFormulaSelectHistoryHashToken}
     */
    public static SpreadsheetCellFormulaSelectHistoryHashToken formula(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellFormulaSelectHistoryHashToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellFormulaSaveHistoryHashToken}
     */
    public static SpreadsheetCellFormulaSaveHistoryHashToken formulaSave(final SpreadsheetId id,
                                                                         final SpreadsheetName name,
                                                                         final SpreadsheetViewportSelection viewportSelection,
                                                                         final SpreadsheetFormula formula) {
        return SpreadsheetCellFormulaSaveHistoryHashToken.formulaSave(
                id,
                name,
                viewportSelection,
                formula
        );
    }

    /**
     * {@see SpreadsheetCellClearHistoryHashToken}
     */
    public static SpreadsheetCellClearHistoryHashToken cellClear(final SpreadsheetId id,
                                                                 final SpreadsheetName name,
                                                                 final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellClearHistoryHashToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellDeleteHistoryHashToken}
     */
    public static SpreadsheetCellDeleteHistoryHashToken cellDelete(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellDeleteHistoryHashToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellFreezeHistoryHashToken}
     */
    public static SpreadsheetCellFreezeHistoryHashToken cellFreeze(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellFreezeHistoryHashToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellMenuHistoryHashToken}
     */
    public static SpreadsheetCellMenuHistoryHashToken cellMenu(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellMenuHistoryHashToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellPatternSelectHistoryHashToken}
     */
    public static SpreadsheetCellPatternSelectHistoryHashToken cellPattern(final SpreadsheetId id,
                                                                           final SpreadsheetName name,
                                                                           final SpreadsheetViewportSelection viewportSelection,
                                                                           final SpreadsheetPatternKind patternKind) {
        return SpreadsheetCellPatternSelectHistoryHashToken.with(
                id,
                name,
                viewportSelection,
                patternKind
        );
    }

    /**
     * {@see SpreadsheetCellPatternSaveHistoryHashToken}
     */
    public static SpreadsheetCellPatternSaveHistoryHashToken cellPatternSave(final SpreadsheetId id,
                                                                             final SpreadsheetName name,
                                                                             final SpreadsheetViewportSelection viewportSelection,
                                                                             final SpreadsheetPattern pattern) {
        return SpreadsheetCellPatternSaveHistoryHashToken.with(
                id,
                name,
                viewportSelection,
                pattern
        );
    }

    /**
     * {@see SpreadsheetCellStyleSelectHistoryHashToken}
     */
    public static SpreadsheetCellStyleSelectHistoryHashToken cellStyle(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final SpreadsheetViewportSelection viewportSelection,
                                                                       final TextStylePropertyName<?> propertyName) {
        return SpreadsheetCellStyleSelectHistoryHashToken.with(
                id,
                name,
                viewportSelection,
                propertyName
        );
    }

    /**
     * {@see SpreadsheetCellStyleSaveHistoryHashToken}
     */
    public static <T> SpreadsheetCellStyleSaveHistoryHashToken<T> cellStyleSave(final SpreadsheetId id,
                                                                                final SpreadsheetName name,
                                                                                final SpreadsheetViewportSelection viewportSelection,
                                                                                final TextStylePropertyName<T> propertyName,
                                                                                final T propertyValue) {
        return SpreadsheetCellStyleSaveHistoryHashToken.with(
                id,
                name,
                viewportSelection,
                propertyName,
                propertyValue
        );
    }

    /**
     * {@see SpreadsheetCellUnfreezeHistoryHashToken}
     */
    public static SpreadsheetCellUnfreezeHistoryHashToken cellUnfreeze(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellUnfreezeHistoryHashToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnOrRowSelectHistoryHashToken}
     */
    public static SpreadsheetColumnOrRowSelectHistoryHashToken columnOrRow(final SpreadsheetId id,
                                                                           final SpreadsheetName name,
                                                                           final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnOrRowSelectHistoryHashToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnOrRowClearHistoryHashToken}
     */
    public static SpreadsheetColumnOrRowClearHistoryHashToken columnOrRowClear(final SpreadsheetId id,
                                                                               final SpreadsheetName name,
                                                                               final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnOrRowClearHistoryHashToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnOrRowDeleteHistoryHashToken}
     */
    public static SpreadsheetColumnOrRowDeleteHistoryHashToken columnOrRowDelete(final SpreadsheetId id,
                                                                                 final SpreadsheetName name,
                                                                                 final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnOrRowDeleteHistoryHashToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnOrRowFreezeHistoryHashToken}
     */
    public static SpreadsheetColumnOrRowFreezeHistoryHashToken columnOrRowFreeze(final SpreadsheetId id,
                                                                                 final SpreadsheetName name,
                                                                                 final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnOrRowFreezeHistoryHashToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnOrRowMenuHistoryHashToken}
     */
    public static SpreadsheetColumnOrRowMenuHistoryHashToken columnOrRowMenu(final SpreadsheetId id,
                                                                             final SpreadsheetName name,
                                                                             final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnOrRowMenuHistoryHashToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnOrRowUnfreezeHistoryHashToken}
     */
    public static SpreadsheetColumnOrRowUnfreezeHistoryHashToken columnOrRowUnfreeze(final SpreadsheetId id,
                                                                                     final SpreadsheetName name,
                                                                                     final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnOrRowUnfreezeHistoryHashToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetLabelMappingSelectHistoryHashToken}
     */
    public static SpreadsheetLabelMappingSelectHistoryHashToken labelMapping(final SpreadsheetId id,
                                                                             final SpreadsheetName name,
                                                                             final SpreadsheetLabelName label) {
        return SpreadsheetLabelMappingSelectHistoryHashToken.with(
                id,
                name,
                label
        );
    }

    /**
     * {@see SpreadsheetLabelMappingDeleteHistoryHashToken}
     */
    public static SpreadsheetLabelMappingDeleteHistoryHashToken labelMappingDelete(final SpreadsheetId id,
                                                                                   final SpreadsheetName name,
                                                                                   final SpreadsheetLabelName labelName) {
        return SpreadsheetLabelMappingDeleteHistoryHashToken.with(
                id,
                name,
                labelName
        );
    }

    /**
     * {@see SpreadsheetLabelMappingSaveHistoryHashToken}
     */
    public static SpreadsheetLabelMappingSaveHistoryHashToken labelMappingSave(final SpreadsheetId id,
                                                                               final SpreadsheetName name,
                                                                               final SpreadsheetLabelMapping mapping) {
        return SpreadsheetLabelMappingSaveHistoryHashToken.with(
                id,
                name,
                mapping
        );
    }

    /**
     * {@see SpreadsheetLoadHistoryHashToken}
     */
    public static SpreadsheetLoadHistoryHashToken spreadsheetLoad(final SpreadsheetId id) {
        return SpreadsheetLoadHistoryHashToken.with(
                id
        );
    }

    /**
     * {@see SpreadsheetSelectHistoryHashToken}
     */
    public static SpreadsheetSelectHistoryHashToken spreadsheetSelect(final SpreadsheetId id,
                                                                      final SpreadsheetName name) {
        return SpreadsheetSelectHistoryHashToken.with(
                id,
                name
        );
    }

    SpreadsheetHistoryHashToken(final SpreadsheetId id) {
        super();

        this.id = Objects.requireNonNull(id, "id");
    }

    public final SpreadsheetId id() {
        return this.id;
    }

    private final SpreadsheetId id;

    @Override
    public final UrlFragment urlFragment() {
        return this.id.urlFragment().append(this.spreadsheetUrlFragment());
    }

    /**
     * Sub-classes should append additional components to the final {@link UrlFragment}.
     */
    abstract UrlFragment spreadsheetUrlFragment();
}
