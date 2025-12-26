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

import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.net.UrlFragment;
import walkingkooka.plugin.PluginName;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardKind;
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.validation.ValueType;
import walkingkooka.validation.form.Form;
import walkingkooka.validation.form.FormName;
import walkingkooka.validation.provider.ValidatorSelector;
import walkingkooka.visit.Visiting;
import walkingkooka.visit.Visitor;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

public class HistoryTokenVisitor extends Visitor<HistoryToken> {

    public HistoryTokenVisitor() {
        super();
    }

    @Override
    public final void accept(final HistoryToken historyToken) {
        Objects.requireNonNull(historyToken, "historyToken");

        if (Visiting.CONTINUE == this.startVisit(historyToken)) {
            historyToken.accept(this);
        }
        this.endVisit(historyToken);
    }

    protected Visiting startVisit(final HistoryToken historyToken) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final HistoryToken historyToken) {
        // NOP
    }

    // HistoryTokenVisitor..............................................................................................

    // CELL.............................................................................................................

    protected void visitCellClearAndFormula(final SpreadsheetId id,
                                            final SpreadsheetName name,
                                            final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellClipboardCopy(final SpreadsheetId id,
                                          final SpreadsheetName name,
                                          final AnchoredSpreadsheetSelection anchoredSelection,
                                          final SpreadsheetCellClipboardKind kind) {
        // NOP
    }

    protected void visitCellClipboardCut(final SpreadsheetId id,
                                         final SpreadsheetName name,
                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                         final SpreadsheetCellClipboardKind kind) {
        // NOP
    }

    protected void visitCellClipboardPaste(final SpreadsheetId id,
                                           final SpreadsheetName name,
                                           final AnchoredSpreadsheetSelection anchoredSelection,
                                           final SpreadsheetCellClipboardKind kind) {
        // NOP
    }

    protected void visitCellDateTimeSymbolsSave(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection,
                                                final Optional<DateTimeSymbols> dateTimeSymbols) {
        // NOP
    }

    protected void visitCellDateTimeSymbolsSelect(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellDateTimeSymbolsUnselect(final SpreadsheetId id,
                                                    final SpreadsheetName name,
                                                    final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellDecimalNumberSymbolsSave(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final Optional<DecimalNumberSymbols> decimalNumberSymbols) {
        // NOP
    }

    protected void visitCellDecimalNumberSymbolsSelect(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellDecimalNumberSymbolsUnselect(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellDelete(final SpreadsheetId id,
                                   final SpreadsheetName name,
                                   final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellFind(final SpreadsheetId id,
                                 final SpreadsheetName name,
                                 final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellFormatterSave(final SpreadsheetId id,
                                          final SpreadsheetName name,
                                          final AnchoredSpreadsheetSelection anchoredSelection,
                                          final Optional<SpreadsheetFormatterSelector> formatter) {
        // NOP
    }

    protected void visitCellFormatterSelect(final SpreadsheetId id,
                                            final SpreadsheetName name,
                                            final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellFormatterUnselect(final SpreadsheetId id,
                                              final SpreadsheetName name,
                                              final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellFormSave(final SpreadsheetId id,
                                     final SpreadsheetName name,
                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                     final FormName formName,
                                     final Map<SpreadsheetCellReference, Optional<Object>> value) {
        // NOP
    }

    protected void visitCellFormSelect(final SpreadsheetId id,
                                       final SpreadsheetName name,
                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                       final FormName formName) {
        // NOP
    }

    protected void visitCellFormulaMenu(final SpreadsheetId id,
                                        final SpreadsheetName name,
                                        final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellFormulaSave(final SpreadsheetId id,
                                        final SpreadsheetName name,
                                        final AnchoredSpreadsheetSelection anchoredSelection,
                                        final String text) {
        // NOP
    }

    protected void visitCellFormulaSelect(final SpreadsheetId id,
                                          final SpreadsheetName name,
                                          final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellFreeze(final SpreadsheetId id,
                                   final SpreadsheetName name,
                                   final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellLabelList(final SpreadsheetId id,
                                      final SpreadsheetName name,
                                      final AnchoredSpreadsheetSelection anchoredSelection,
                                      final HistoryTokenOffsetAndCount offsetAndCount) {
        // NOP
    }

    protected void visitCellLabelSave(final SpreadsheetId id,
                                      final SpreadsheetName name,
                                      final AnchoredSpreadsheetSelection anchoredSelection,
                                      final SpreadsheetLabelName labelName) {
        // NOP
    }

    protected void visitCellLabelSelect(final SpreadsheetId id,
                                        final SpreadsheetName name,
                                        final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellLocaleSave(final SpreadsheetId id,
                                       final SpreadsheetName name,
                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                       final Optional<Locale> locale) {
        // NOP
    }

    protected void visitCellLocaleSelect(final SpreadsheetId id,
                                         final SpreadsheetName name,
                                         final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellLocaleUnselect(final SpreadsheetId id,
                                           final SpreadsheetName name,
                                           final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellMenu(final SpreadsheetId id,
                                 final SpreadsheetName name,
                                 final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellNavigate(final SpreadsheetId id,
                                     final SpreadsheetName name,
                                     final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellParserSave(final SpreadsheetId id,
                                       final SpreadsheetName name,
                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                       final Optional<SpreadsheetParserSelector> parser) {
        // NOP
    }

    protected void visitCellParserSelect(final SpreadsheetId id,
                                         final SpreadsheetName name,
                                         final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellParserUnselect(final SpreadsheetId id,
                                           final SpreadsheetName name,
                                           final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellReference(final SpreadsheetId id,
                                      final SpreadsheetName name,
                                      final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellReload(final SpreadsheetId id,
                                   final SpreadsheetName name,
                                   final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellSaveCell(final SpreadsheetId id,
                                     final SpreadsheetName name,
                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                     final Set<SpreadsheetCell> value) {
        // NOP
    }

    protected void visitCellSaveMap(final SpreadsheetId id,
                                    final SpreadsheetName name,
                                    final AnchoredSpreadsheetSelection anchoredSelection,
                                    final Map<SpreadsheetCellReference, ?> values) {
        // NOP
    }

    protected void visitCellSelect(final SpreadsheetId id,
                                   final SpreadsheetName name,
                                   final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellSortEdit(final SpreadsheetId id,
                                     final SpreadsheetName name,
                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                     final String comparatorNames) {
        // NOP
    }

    protected void visitCellSortSave(final SpreadsheetId id,
                                     final SpreadsheetName name,
                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                     final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames) {
        // NOP
    }

    protected <T> void visitCellStyleSave(final SpreadsheetId id,
                                          final SpreadsheetName name,
                                          final AnchoredSpreadsheetSelection anchoredSelection,
                                          final TextStylePropertyName<T> propertyName,
                                          final Optional<T> propertyValue) {
        // NOP
    }

    protected void visitCellStyleSelect(final SpreadsheetId id,
                                        final SpreadsheetName name,
                                        final AnchoredSpreadsheetSelection anchoredSelection,
                                        final TextStylePropertyName<?> propertyName) {
        // NOP
    }

    protected void visitCellUnfreeze(final SpreadsheetId id,
                                     final SpreadsheetName name,
                                     final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellValidatorSave(final SpreadsheetId id,
                                          final SpreadsheetName name,
                                          final AnchoredSpreadsheetSelection anchoredSelection,
                                          final Optional<ValidatorSelector> validator) {
        // NOP
    }

    protected void visitCellValidatorSelect(final SpreadsheetId id,
                                            final SpreadsheetName name,
                                            final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellValidatorUnselect(final SpreadsheetId id,
                                              final SpreadsheetName name,
                                              final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellValueSave(final SpreadsheetId id,
                                      final SpreadsheetName name,
                                      final AnchoredSpreadsheetSelection anchoredSelection,
                                      final ValueType valueType,
                                      final Optional<?> value) {
        // NOP
    }

    protected void visitCellValueSelect(final SpreadsheetId id,
                                        final SpreadsheetName name,
                                        final AnchoredSpreadsheetSelection anchoredSelection,
                                        final ValueType valueType) {
        // NOP
    }

    protected void visitCellValueUnselect(final SpreadsheetId id,
                                          final SpreadsheetName name,
                                          final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellValueTypeSave(final SpreadsheetId id,
                                          final SpreadsheetName name,
                                          final AnchoredSpreadsheetSelection anchoredSelection,
                                          final Optional<ValueType> valueType) {
        // NOP
    }

    protected void visitCellValueTypeSelect(final SpreadsheetId id,
                                            final SpreadsheetName name,
                                            final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitCellValueTypeUnselect(final SpreadsheetId id,
                                              final SpreadsheetName name,
                                              final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    // COLUMN...........................................................................................................

    protected void visitColumnClear(final SpreadsheetId id,
                                    final SpreadsheetName name,
                                    final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitColumnDelete(final SpreadsheetId id,
                                     final SpreadsheetName name,
                                     final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitColumnFreeze(final SpreadsheetId id,
                                     final SpreadsheetName name,
                                     final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitColumnInsertAfter(final SpreadsheetId id,
                                          final SpreadsheetName name,
                                          final AnchoredSpreadsheetSelection anchoredSelection,
                                          final OptionalInt count) {
        // NOP
    }

    protected void visitColumnInsertBefore(final SpreadsheetId id,
                                           final SpreadsheetName name,
                                           final AnchoredSpreadsheetSelection anchoredSelection,
                                           final OptionalInt count) {
        // NOP
    }

    protected void visitColumnMenu(final SpreadsheetId id,
                                   final SpreadsheetName name,
                                   final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitColumnNavigate(final SpreadsheetId id,
                                       final SpreadsheetName name,
                                       final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitColumnSelect(final SpreadsheetId id,
                                     final SpreadsheetName name,
                                     final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitColumnSortEdit(final SpreadsheetId id,
                                       final SpreadsheetName name,
                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                       final String comparatorNames) {
        // NOP
    }

    protected void visitColumnSortSave(final SpreadsheetId id,
                                       final SpreadsheetName name,
                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                       final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames) {
        // NOP
    }

    protected void visitColumnUnfreeze(final SpreadsheetId id,
                                       final SpreadsheetName name,
                                       final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    // FORM.............................................................................................................

    protected void visitFormDelete(final SpreadsheetId id,
                                   final SpreadsheetName name,
                                   final FormName formName) {
        // NOP
    }

    protected void visitFormList(final SpreadsheetId id,
                                 final SpreadsheetName name,
                                 final HistoryTokenOffsetAndCount offsetAndCount) {
        // NOP
    }

    protected void visitFormSave(final SpreadsheetId id,
                                 final SpreadsheetName name,
                                 final Form<SpreadsheetExpressionReference> form) {
        // NOP
    }

    protected void visitFormSelect(final SpreadsheetId id,
                                   final SpreadsheetName name,
                                   final FormName formName) {
        // NOP
    }

    // METADATA.........................................................................................................

    protected <T> void visitMetadataPropertySave(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final SpreadsheetMetadataPropertyName<T> propertyName,
                                                 final Optional<T> propertyValue) {
        // NOP
    }

    protected <T> void visitMetadataPropertySelect(final SpreadsheetId id,
                                                   final SpreadsheetName name,
                                                   final SpreadsheetMetadataPropertyName<T> propertyName) {
        // NOP
    }

    protected <T> void visitMetadataSelect(final SpreadsheetId id,
                                           final SpreadsheetName name) {
        // NOP
    }

    protected <T> void visitMetadataStyleSave(final SpreadsheetId id,
                                              final SpreadsheetName name,
                                              final TextStylePropertyName<T> propertyName,
                                              final Optional<T> propertyValue) {
        // NOP
    }

    protected <T> void visitMetadataStyleSelect(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final TextStylePropertyName<T> propertyName) {
        // NOP
    }

    // NAVIGATE.........................................................................................................

    protected void visitNavigate(final SpreadsheetId id,
                                 final SpreadsheetName name) {
        // NOP
    }

    // PLUGIN...........................................................................................................

    protected void visitPluginDelete(final PluginName name) {
        // NOP
    }

    protected void visitPluginFileView(final PluginName name,
                                       final Optional<JarEntryInfoName> file) {
        // NOP
    }

    protected void visitPluginListReload(final HistoryTokenOffsetAndCount offsetAndCount) {
        // NOP
    }

    protected void visitPluginListSelect(final HistoryTokenOffsetAndCount offsetAndCount) {
        // NOP
    }

    protected void visitPluginSave(final PluginName name,
                                   final String value) {
        // NOP
    }

    protected void visitPluginSelect(final PluginName name) {
        // NOP
    }

    protected void visitPluginUploadSave(final BrowserFile file) {
        // NOP
    }

    protected void visitPluginUploadSelect() {
        // NOP
    }

    protected void visitSpreadsheetCreate() {
        // NOP
    }

    // ROW..............................................................................................................

    protected void visitRowClear(final SpreadsheetId id,
                                 final SpreadsheetName name,
                                 final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitRowDelete(final SpreadsheetId id,
                                  final SpreadsheetName name,
                                  final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitRowFreeze(final SpreadsheetId id,
                                  final SpreadsheetName name,
                                  final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitRowInsertAfter(final SpreadsheetId id,
                                       final SpreadsheetName name,
                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                       final OptionalInt count) {
        // NOP
    }

    protected void visitRowInsertBefore(final SpreadsheetId id,
                                        final SpreadsheetName name,
                                        final AnchoredSpreadsheetSelection anchoredSelection,
                                        final OptionalInt count) {
        // NOP
    }

    protected void visitRowMenu(final SpreadsheetId id,
                                final SpreadsheetName name,
                                final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitRowNavigate(final SpreadsheetId id,
                                    final SpreadsheetName name,
                                    final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitRowSelect(final SpreadsheetId id,
                                  final SpreadsheetName name,
                                  final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    protected void visitRowSortEdit(final SpreadsheetId id,
                                    final SpreadsheetName name,
                                    final AnchoredSpreadsheetSelection anchoredSelection,
                                    final String comparatorNames) {
        // NOP
    }

    protected void visitRowSortSave(final SpreadsheetId id,
                                    final SpreadsheetName name,
                                    final AnchoredSpreadsheetSelection anchoredSelection,
                                    final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames) {
        // NOP
    }

    protected void visitRowUnfreeze(final SpreadsheetId id,
                                    final SpreadsheetName name,
                                    final AnchoredSpreadsheetSelection anchoredSelection) {
        // NOP
    }

    // SPREADSHEET......................................................................................................

    protected void visitSpreadsheetListDelete(final SpreadsheetId id) {
        // NOP
    }

    protected void visitSpreadsheetListReload(final HistoryTokenOffsetAndCount offsetAndCount) {
        // NOP
    }

    protected void visitSpreadsheetListRenameSave(final SpreadsheetId id,
                                                  final SpreadsheetName value) {
        // NOP
    }

    protected void visitSpreadsheetListRenameSelect(final SpreadsheetId id) {
        // NOP
    }

    protected void visitSpreadsheetListSelect(final HistoryTokenOffsetAndCount offsetAndCount) {
        // NOP
    }

    protected void visitSpreadsheetLabelMappingCreate(final SpreadsheetId id,
                                                      final SpreadsheetName name) {
        // NOP
    }

    protected void visitSpreadsheetLabelMappingDelete(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final SpreadsheetLabelName labelName) {
        // NOP
    }

    protected void visitSpreadsheetLabelMappingList(final SpreadsheetId id,
                                                    final SpreadsheetName name,
                                                    final HistoryTokenOffsetAndCount offsetAndCount) {
        // NOP
    }

    protected void visitSpreadsheetLabelMappingSave(final SpreadsheetId id,
                                                    final SpreadsheetName name,
                                                    final SpreadsheetLabelMapping mapping) {
        // NOP
    }

    protected void visitSpreadsheetLabelMappingSelect(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final SpreadsheetLabelName labelName) {
        // NOP
    }

    protected void visitSpreadsheetLoad() {
        // NOP
    }

    protected void visitSpreadsheetReload(final SpreadsheetId id,
                                          final SpreadsheetName name) {
        // NOP
    }

    protected void visitSpreadsheetRenameSave(final SpreadsheetId id,
                                              final SpreadsheetName name,
                                              final SpreadsheetName value) {
        // NOP
    }

    protected void visitSpreadsheetRenameSelect(final SpreadsheetId id,
                                                final SpreadsheetName name) {
        // NOP
    }

    protected void visitSpreadsheetSelect(final SpreadsheetId id,
                                          final SpreadsheetName name) {
        // NOP
    }

    // UNKNOWN..........................................................................................................

    protected void visitUnknown(final UrlFragment fragment) {
        // NOP
    }
}
