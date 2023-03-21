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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.SpreadsheetMetadataWatcher;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.text.TextStylePropertyName;

/**
 * Instances represent a token within a history hash.
 */
public abstract class SpreadsheetHistoryToken extends HistoryToken implements SpreadsheetMetadataWatcher {

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
     * {@see SpreadsheetCellSelectHistoryToken}
     */
    public static SpreadsheetCellSelectHistoryToken cell(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellSelectHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellFormulaSelectHistoryToken}
     */
    public static SpreadsheetCellFormulaSelectHistoryToken formula(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellFormulaSelectHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellFormulaSaveHistoryToken}
     */
    public static SpreadsheetCellFormulaSaveHistoryToken formulaSave(final SpreadsheetId id,
                                                                     final SpreadsheetName name,
                                                                     final SpreadsheetViewportSelection viewportSelection,
                                                                     final SpreadsheetFormula formula) {
        return SpreadsheetCellFormulaSaveHistoryToken.with(
                id,
                name,
                viewportSelection,
                formula
        );
    }

    /**
     * {@see SpreadsheetCellClearHistoryToken}
     */
    public static SpreadsheetCellClearHistoryToken cellClear(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellClearHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellDeleteHistoryToken}
     */
    public static SpreadsheetCellDeleteHistoryToken cellDelete(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellDeleteHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellFreezeHistoryToken}
     */
    public static SpreadsheetCellFreezeHistoryToken cellFreeze(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellFreezeHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellMenuHistoryToken}
     */
    public static SpreadsheetCellMenuHistoryToken cellMenu(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellMenuHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellPatternSelectHistoryToken}
     */
    public static SpreadsheetCellPatternSelectHistoryToken cellPattern(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final SpreadsheetViewportSelection viewportSelection,
                                                                       final SpreadsheetPatternKind patternKind) {
        return SpreadsheetCellPatternSelectHistoryToken.with(
                id,
                name,
                viewportSelection,
                patternKind
        );
    }

    /**
     * {@see SpreadsheetCellPatternSaveHistoryToken}
     */
    public static SpreadsheetCellPatternSaveHistoryToken cellPatternSave(final SpreadsheetId id,
                                                                         final SpreadsheetName name,
                                                                         final SpreadsheetViewportSelection viewportSelection,
                                                                         final SpreadsheetPattern pattern) {
        return SpreadsheetCellPatternSaveHistoryToken.with(
                id,
                name,
                viewportSelection,
                pattern
        );
    }

    /**
     * {@see SpreadsheetCellStyleSelectHistoryToken}
     */
    public static <T> SpreadsheetCellStyleSelectHistoryToken<T> cellStyle(final SpreadsheetId id,
                                                                          final SpreadsheetName name,
                                                                          final SpreadsheetViewportSelection viewportSelection,
                                                                          final TextStylePropertyName<T> propertyName) {
        return SpreadsheetCellStyleSelectHistoryToken.with(
                id,
                name,
                viewportSelection,
                propertyName
        );
    }

    /**
     * {@see SpreadsheetCellStyleSaveHistoryToken}
     */
    public static <T> SpreadsheetCellStyleSaveHistoryToken<T> cellStyleSave(final SpreadsheetId id,
                                                                            final SpreadsheetName name,
                                                                            final SpreadsheetViewportSelection viewportSelection,
                                                                            final TextStylePropertyName<T> propertyName,
                                                                            final T propertyValue) {
        return SpreadsheetCellStyleSaveHistoryToken.with(
                id,
                name,
                viewportSelection,
                propertyName,
                propertyValue
        );
    }

    /**
     * {@see SpreadsheetCellUnfreezeHistoryToken}
     */
    public static SpreadsheetCellUnfreezeHistoryToken cellUnfreeze(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellUnfreezeHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnSelectHistoryToken}
     */
    public static SpreadsheetColumnSelectHistoryToken column(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnSelectHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnClearHistoryToken}
     */
    public static SpreadsheetColumnClearHistoryToken columnClear(final SpreadsheetId id,
                                                                 final SpreadsheetName name,
                                                                 final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnClearHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnDeleteHistoryToken}
     */
    public static SpreadsheetColumnDeleteHistoryToken columnDelete(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnDeleteHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnFreezeHistoryToken}
     */
    public static SpreadsheetColumnFreezeHistoryToken columnFreeze(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnFreezeHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnMenuHistoryToken}
     */
    public static SpreadsheetColumnMenuHistoryToken columnMenu(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnMenuHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnUnfreezeHistoryToken}
     */
    public static SpreadsheetColumnUnfreezeHistoryToken columnUnfreeze(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnUnfreezeHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetLabelMappingSelectHistoryToken}
     */
    public static SpreadsheetLabelMappingSelectHistoryToken labelMapping(final SpreadsheetId id,
                                                                         final SpreadsheetName name,
                                                                         final SpreadsheetLabelName label) {
        return SpreadsheetLabelMappingSelectHistoryToken.with(
                id,
                name,
                label
        );
    }

    /**
     * {@see SpreadsheetLabelMappingDeleteHistoryToken}
     */
    public static SpreadsheetLabelMappingDeleteHistoryToken labelMappingDelete(final SpreadsheetId id,
                                                                               final SpreadsheetName name,
                                                                               final SpreadsheetLabelName labelName) {
        return SpreadsheetLabelMappingDeleteHistoryToken.with(
                id,
                name,
                labelName
        );
    }

    /**
     * {@see SpreadsheetLabelMappingSaveHistoryToken}
     */
    public static SpreadsheetLabelMappingSaveHistoryToken labelMappingSave(final SpreadsheetId id,
                                                                           final SpreadsheetName name,
                                                                           final SpreadsheetLabelMapping mapping) {
        return SpreadsheetLabelMappingSaveHistoryToken.with(
                id,
                name,
                mapping
        );
    }

    /**
     * {@see SpreadsheetMetadataPropertySelectHistoryToken}
     */
    public static SpreadsheetMetadataPropertySelectHistoryToken metadataPropertySelect(final SpreadsheetId id,
                                                                                       final SpreadsheetName name,
                                                                                       final SpreadsheetMetadataPropertyName<?> propertyName) {
        return SpreadsheetMetadataPropertySelectHistoryToken.with(
                id,
                name,
                propertyName
        );
    }

    /**
     * {@see SpreadsheetMetadataPropertySaveHistoryToken}
     */
    public static <T> SpreadsheetMetadataPropertySaveHistoryToken<T> metadataPropertySave(final SpreadsheetId id,
                                                                                          final SpreadsheetName name,
                                                                                          final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                                          final T propertyValue) {
        return SpreadsheetMetadataPropertySaveHistoryToken.with(
                id,
                name,
                propertyName,
                propertyValue
        );
    }

    /**
     * {@see SpreadsheetMetadataPropertyStyleSelectHistoryToken}
     */
    public static <T> SpreadsheetMetadataPropertyStyleSelectHistoryToken<T> metadataPropertyStyle(final SpreadsheetId id,
                                                                                                  final SpreadsheetName name,
                                                                                                  final TextStylePropertyName<T> stylePropertyName) {
        return SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
                id,
                name,
                stylePropertyName
        );
    }

    /**
     * {@see SpreadsheetMetadataPropertyStyleSaveHistoryToken}
     */
    public static <T> SpreadsheetMetadataPropertyStyleSaveHistoryToken<T> metadataPropertyStyleSave(final SpreadsheetId id,
                                                                                                    final SpreadsheetName name,
                                                                                                    final TextStylePropertyName<T> stylePropertyName,
                                                                                                    final T stylePropertyValue) {
        return SpreadsheetMetadataPropertyStyleSaveHistoryToken.with(
                id,
                name,
                stylePropertyName,
                stylePropertyValue
        );
    }

    /**
     * {@see SpreadsheetMetadataSelectHistoryToken}
     */
    public static SpreadsheetMetadataSelectHistoryToken metadataSelect(final SpreadsheetId id,
                                                                       final SpreadsheetName name) {
        return SpreadsheetMetadataSelectHistoryToken.with(
                id,
                name
        );
    }

    /**
     * {@see SpreadsheetRowSelectHistoryToken}
     */
    public static SpreadsheetRowSelectHistoryToken row(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetRowSelectHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetRowClearHistoryToken}
     */
    public static SpreadsheetRowClearHistoryToken rowClear(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetRowClearHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetRowDeleteHistoryToken}
     */
    public static SpreadsheetRowDeleteHistoryToken rowDelete(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetRowDeleteHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetRowFreezeHistoryToken}
     */
    public static SpreadsheetRowFreezeHistoryToken rowFreeze(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetRowFreezeHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetRowMenuHistoryToken}
     */
    public static SpreadsheetRowMenuHistoryToken rowMenu(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetRowMenuHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetRowUnfreezeHistoryToken}
     */
    public static SpreadsheetRowUnfreezeHistoryToken rowUnfreeze(final SpreadsheetId id,
                                                                 final SpreadsheetName name,
                                                                 final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetRowUnfreezeHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCreateHistoryToken}
     */
    public static SpreadsheetCreateHistoryToken spreadsheetCreate() {
        return SpreadsheetCreateHistoryToken.with();
    }
    
    /**
     * {@see SpreadsheetLoadHistoryToken}
     */
    public static SpreadsheetLoadHistoryToken spreadsheetLoad(final SpreadsheetId id) {
        return SpreadsheetLoadHistoryToken.with(
                id
        );
    }

    /**
     * {@see SpreadsheetSelectHistoryToken}
     */
    public static SpreadsheetSelectHistoryToken spreadsheetSelect(final SpreadsheetId id,
                                                                  final SpreadsheetName name) {
        return SpreadsheetSelectHistoryToken.with(
                id,
                name
        );
    }

    SpreadsheetHistoryToken() {
        super();
    }

    /**
     * Creates a {@link UrlFragment} with a save and value.
     */
    final UrlFragment saveUrlFragment(final Object value) {
        final UrlFragment urlFragment;

        if (value instanceof HasUrlFragment) {
            final HasUrlFragment has = (HasUrlFragment) value;
            urlFragment = has.urlFragment();
        } else {
            urlFragment = UrlFragment.with(
                    String.valueOf(
                            String.valueOf(value)
                    )
            );
        }

        return SAVE.append(urlFragment);
    }

    public abstract SpreadsheetHistoryToken setIdAndName(final SpreadsheetId id,
                                                         final SpreadsheetName name);
}
