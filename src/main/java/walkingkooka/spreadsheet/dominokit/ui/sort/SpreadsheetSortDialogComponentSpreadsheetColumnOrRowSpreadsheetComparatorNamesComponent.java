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

package walkingkooka.spreadsheet.dominokit.ui.sort;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorNameAndDirection;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.spreadsheet.dominokit.ui.columnorrowcomparatornames.SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent;
import walkingkooka.spreadsheet.dominokit.ui.flexlayout.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link walkingkooka.spreadsheet.dominokit.ui.Component} that supports editing an individual {@link walkingkooka.spreadsheet.compare.SpreadsheetComparatorName}
 * within a larger {@link SpreadsheetColumnOrRowSpreadsheetComparatorNamesList}.
 */
final class SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent implements ValueComponent<HTMLDivElement, SpreadsheetColumnOrRowSpreadsheetComparatorNames, SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent> {

    static SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent with(final String id,
                                                                                                        final int index,
                                                                                                        final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> columnOrRowSpreadsheetComparatorNamesToHistoryToken) {
        CharSequences.failIfNullOrEmpty(id, "id");
        if (index < 0) {
            throw new IllegalArgumentException("Invalid index " + index + " < 0");
        }
        Objects.requireNonNull(columnOrRowSpreadsheetComparatorNamesToHistoryToken, "columnOrRowSpreadsheetComparatorNamesToHistoryToken");
        return new SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent(
                id,
                index,
                columnOrRowSpreadsheetComparatorNamesToHistoryToken
        );
    }

    private SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent(final String id,
                                                                                                    final int index,
                                                                                                    final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> columnOrRowSpreadsheetComparatorNamesToHistoryToken) {
        final SpreadsheetFlexLayout parent = SpreadsheetFlexLayout.emptyRow();

        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent names = SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.empty()
                .setId(
                        CharSequences.subSequence(
                                id,
                                0,
                                -1
                        ) + SpreadsheetIds.TEXT_BOX);
        parent.appendChild(names);
        this.names = names;

        final SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender appender = SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender.empty(
                id,
                (newNames) -> columnOrRowSpreadsheetComparatorNamesToHistoryToken.apply(
                        Optional.of(newNames)
                )
        );
        parent.appendChild(appender);
        this.appender = appender;

        final SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover remover = SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover.empty(
                id,
                columnOrRowSpreadsheetComparatorNamesToHistoryToken
        );
        parent.appendChild(remover);
        this.remover = remover;

        this.parent = parent;
    }

    void refresh(final String columnOrRowSpreadsheetComparatorNames,
                 final SpreadsheetSortDialogComponentContext context) {
        this.names.setStringValue(
                Optional.ofNullable(
                        columnOrRowSpreadsheetComparatorNames.isEmpty() ?
                                null :
                                columnOrRowSpreadsheetComparatorNames
                )
        );

        final Optional<SpreadsheetColumnOrRowReference> columnOrRow = SpreadsheetColumnOrRowSpreadsheetComparatorNames.tryParseColumnOrRow(columnOrRowSpreadsheetComparatorNames);

        List<SpreadsheetComparatorNameAndDirection> comparatorNameAndDirections;

        try {
            comparatorNameAndDirections = SpreadsheetColumnOrRowSpreadsheetComparatorNames.parse(columnOrRowSpreadsheetComparatorNames)
                    .comparatorNameAndDirections();
        } catch (final RuntimeException ignore) {
            comparatorNameAndDirections = Lists.empty();
        }

        this.appender.refresh(
                columnOrRow,
                comparatorNameAndDirections,
                context
        );
        this.remover.refresh(
                columnOrRow,
                comparatorNameAndDirections,
                context
        );
    }

    private final SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent names;

    private final SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender appender;

    private final SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover remover;

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent setId(final String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String id() {
        return null;
    }

    @Override
    public boolean isDisabled() {
        return this.names.isDisabled();
    }

    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent setDisabled(final boolean disabled) {
        this.names.setDisabled(disabled);
        return this;
    }

    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent setLabel(final String label) {
        this.names.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.names.label();
    }

    @Override
    public Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames> value() {
        return this.names.value();
    }

    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent setValue(final Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames> value) {
        this.names.setValue(value);
        return this;
    }

    public Optional<String> stringValue() {
        return this.names.stringValue();
    }

    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent setStringValue(final Optional<String> value) {
        this.names.setStringValue(value);
        return this;
    }


    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent alwaysShowHelperText() {
        this.names.alwaysShowHelperText();
        return this;
    }

    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent setHelperText(final Optional<String> text) {
        this.names.setHelperText(text);
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return this.names.helperText();
    }

    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent optional() {
        this.names.optional();
        return this;
    }

    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent required() {
        this.names.required();
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.names.isRequired();
    }

    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent validate() {
        this.names.validate();
        return this;
    }

    @Override
    public List<String> errors() {
        return this.names.errors();
    }

    @Override
    public ValueComponent<HTMLDivElement, SpreadsheetColumnOrRowSpreadsheetComparatorNames, SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent> setErrors(final List<String> errors) {
        this.names.setErrors(errors);
        return this;
    }

    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent focus() {
        this.names.focus();
        return this;
    }

    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent addChangeListener(final ChangeListener<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>> listener) {
        this.names.addChangeListener(listener);
        return this;
    }

    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent addFocusListener(final EventListener listener) {
        this.names.addFocusListener(listener);
        return this;
    }

    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent addKeydownListener(final EventListener listener) {
        this.names.addKeydownListener(listener);
        return this;
    }

    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent addKeyupListener(final EventListener listener) {
        this.names.addKeyupListener(listener);
        return this;
    }

    // HtmlElementComponent.............................................................................................

    @Override
    public HTMLDivElement element() {
        return this.parent.element();
    }

    private final SpreadsheetFlexLayout parent;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.parent.printTree(printer);
        }
        printer.outdent();
    }
}
