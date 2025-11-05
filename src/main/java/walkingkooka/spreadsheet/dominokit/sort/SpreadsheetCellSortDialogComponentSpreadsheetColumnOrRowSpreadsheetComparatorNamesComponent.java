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
import walkingkooka.spreadsheet.compare.provider.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorNameAndDirection;
import walkingkooka.spreadsheet.dominokit.Component;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.comparator.SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
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
 * A {@link Component} that supports editing an individual {@link walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorName}
 * within a larger {@link SpreadsheetColumnOrRowSpreadsheetComparatorNamesList}.
 */
final class SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent implements FormValueComponent<HTMLDivElement, SpreadsheetColumnOrRowSpreadsheetComparatorNames, SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent>,
    HtmlComponentDelegator<HTMLDivElement, SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent> {

    static SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent with(final String id,
                                                                                                            final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> moveUp,
                                                                                                            final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> moveDown,
                                                                                                            final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> setter,
                                                                                                            final HistoryContext context) {
        CharSequences.failIfNullOrEmpty(id, "id");
        Objects.requireNonNull(moveUp, "moveUp");
        Objects.requireNonNull(moveDown, "moveDown");
        Objects.requireNonNull(setter, "setter");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent(
            id,
            moveUp,
            moveDown,
            setter,
            context
        );
    }

    private SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent(final String id,
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

        final SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionAppenderComponent appender = SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionAppenderComponent.empty(
            id,
            (newNames) -> setter.apply(
                Optional.of(newNames)
            )
        );
        this.appender = appender;

        final SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent remover = SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent.empty(
            id,
            setter
        );
        this.remover = remover;

        //
        // NAMES MOVEUP MOVEDOWN
        // APPENDER
        // REMOVER
        //
        this.root = FlexLayoutComponent.column()
            .appendChild(
                FlexLayoutComponent.row()
                    .appendChild(names)
                    .appendChild(moveUpLink)
                    .appendChild(moveDownLink)
            )
            .appendChild(appender)
            .appendChild(remover);
    }

    void refresh(final String columnOrRowSpreadsheetComparatorNames,
                 final SpreadsheetCellSortDialogComponentContext context) {
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

    private final SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionAppenderComponent appender;

    private final SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent remover;

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent setId(final String id) {
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
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent setDisabled(final boolean disabled) {
        this.names.setDisabled(disabled);
        return this;
    }

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent setLabel(final String label) {
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
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent setValue(final Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames> value) {
        this.names.setValue(value);
        return this;
    }

    public Optional<String> stringValue() {
        return this.names.stringValue();
    }

    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent setStringValue(final Optional<String> value) {
        this.names.setStringValue(value);
        return this;
    }


    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent alwaysShowHelperText() {
        this.names.alwaysShowHelperText();
        return this;
    }

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent setHelperText(final Optional<String> text) {
        this.names.setHelperText(text);
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return this.names.helperText();
    }

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent optional() {
        this.names.optional();
        return this;
    }

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent required() {
        this.names.required();
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.names.isRequired();
    }

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent validate() {
        this.names.validate();
        return this;
    }

    @Override
    public List<String> errors() {
        return this.names.errors();
    }

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent setErrors(final List<String> errors) {
        this.names.setErrors(errors);
        return this;
    }

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent focus() {
        this.names.focus();
        return this;
    }

    @Override
    public boolean isEditing() {
        return HtmlComponent.hasFocus(this.element());
    }

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent addBlurListener(final EventListener listener) {
        this.names.addBlurListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent addChangeListener(final ChangeListener<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>> listener) {
        this.names.addChangeListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent addClickListener(final EventListener listener) {
        this.names.addClickListener(listener);
        return this;
    }


    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent addContextMenuListener(final EventListener listener) {
        this.names.addContextMenuListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent addFocusListener(final EventListener listener) {
        this.names.addFocusListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent addInputListener(final EventListener listener) {
        this.names.addInputListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent addKeyDownListener(final EventListener listener) {
        this.names.addKeyDownListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent addKeyUpListener(final EventListener listener) {
        this.names.addKeyUpListener(listener);
        return this;
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.root;
    }

    private final FlexLayoutComponent root;

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
