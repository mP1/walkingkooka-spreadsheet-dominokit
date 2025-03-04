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

package walkingkooka.spreadsheet.dominokit.sort;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorNameAndDirection;
import walkingkooka.spreadsheet.dominokit.Component;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.comparator.SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReferenceOrRange;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link Component} that supports editing an individual {@link walkingkooka.spreadsheet.compare.SpreadsheetComparatorName}
 * within a larger {@link SpreadsheetColumnOrRowSpreadsheetComparatorNamesList}.
 */
final class SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent implements FormValueComponent<HTMLDivElement, SpreadsheetColumnOrRowSpreadsheetComparatorNames, SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent> {

    static SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent with(final String id,
                                                                                                        final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> moveUp,
                                                                                                        final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> moveDown,
                                                                                                        final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> setter,
                                                                                                        final HistoryContext context) {
        CharSequences.failIfNullOrEmpty(id, "id");
        Objects.requireNonNull(moveUp, "moveUp");
        Objects.requireNonNull(moveDown, "moveDown");
        Objects.requireNonNull(setter, "setter");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent(
            id,
            moveUp,
            moveDown,
            setter,
            context
        );
    }

    private SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent(final String id,
                                                                                                    final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> moveUp,
                                                                                                    final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> moveDown,
                                                                                                    final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> setter,
                                                                                                    final HistoryContext context) {

        // NAMES MOVE_UP MOVE_DOW
        //
        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent names = SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.empty()
            .setId(
                CharSequences.subSequence(
                    id,
                    0,
                    -1
                ) + SpreadsheetElementIds.TEXT_BOX
            ).setCssText("flex-grow: 1; width: fit-content;");
        this.names = names;

        final String paddingTop = "10px";

        final HistoryTokenAnchorComponent moveUpLink = context.historyToken()
            .link(id + "moveUp")
            .setTextContent("Move Up")
            .setCssProperty(
                "padding-top",
                paddingTop
            );
        this.moveUpLink = moveUpLink;
        this.moveUp = moveUp;

        final HistoryTokenAnchorComponent moveDownLink = context.historyToken()
            .link(id + "moveDown")
            .setTextContent("Move Down")
            .setCssProperty(
                "padding-top",
                paddingTop
            );
        this.moveDownLink = moveDownLink;
        this.moveDown = moveDown;

        final SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppenderComponent appender = SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppenderComponent.empty(
            id,
            (newNames) -> setter.apply(
                Optional.of(newNames)
            )
        );
        this.appender = appender;

        final SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent remover = SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent.empty(
            id,
            setter
        );
        this.remover = remover;

        //
        // NAMES MOVEUP MOVEDOWN
        // APPENDER
        // REMOVER
        //
        this.root = SpreadsheetFlexLayout.column()
            .appendChild(
                SpreadsheetFlexLayout.row()
                    .appendChild(names)
                    .appendChild(moveUpLink)
                    .appendChild(moveDownLink)
            )
            .appendChild(appender)
            .appendChild(remover);
    }

    void refresh(final String columnOrRowSpreadsheetComparatorNames,
                 final SpreadsheetSortDialogComponentContext context) {
        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent names = this.names;
        names.setStringValue(
            Optional.ofNullable(
                columnOrRowSpreadsheetComparatorNames.isEmpty() ?
                    null :
                    columnOrRowSpreadsheetComparatorNames
            )
        );

        {
            final Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames> value = names.value();

            this.moveUpLink.setHistoryToken(
                this.moveUp.apply(value)
            );

            this.moveDownLink.setHistoryToken(
                this.moveDown.apply(value)
            );
        }

        final Optional<SpreadsheetColumnOrRowReferenceOrRange> columnOrRow = SpreadsheetColumnOrRowSpreadsheetComparatorNames.tryParseColumnOrRow(columnOrRowSpreadsheetComparatorNames);
        final List<SpreadsheetComparatorNameAndDirection> comparatorNameAndDirections = SpreadsheetColumnOrRowSpreadsheetComparatorNames.tryParseSpreadsheetComparatorNameAndDirections(columnOrRowSpreadsheetComparatorNames);

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

    private final HistoryTokenAnchorComponent moveUpLink;

    private final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> moveUp;

    private final HistoryTokenAnchorComponent moveDownLink;

    private final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> moveDown;

    private final SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppenderComponent appender;

    private final SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent remover;

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
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent setErrors(final List<String> errors) {
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
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent addClickListener(final EventListener listener) {
        this.names.addClickListener(listener);
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

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent setCssText(final String css) {
        this.root.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent setCssProperty(final String name,
                                                                                                                  final String value) {
        this.root.setCssProperty(
            name,
            value
        );
        return this;
    }

    // HtmlElementComponent.............................................................................................

    @Override
    public HTMLDivElement element() {
        return this.root.element();
    }

    private final SpreadsheetFlexLayout root;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.root.printTree(printer);
        }
        printer.outdent();
    }
}
