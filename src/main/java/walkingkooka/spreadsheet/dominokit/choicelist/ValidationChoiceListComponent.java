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

package walkingkooka.spreadsheet.dominokit.choicelist;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.select.SelectComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.validation.ValidationChoice;
import walkingkooka.validation.ValidationChoiceList;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A drop down that supports picking an optional {@link Object}.
 */
public final class ValidationChoiceListComponent implements FormValueComponent<HTMLFieldSetElement, Object, ValidationChoiceListComponent>,
    HtmlComponentDelegator<HTMLFieldSetElement, ValidationChoiceListComponent> {

    public static ValidationChoiceListComponent empty(final String id,
                                                      final ValidationChoiceListComponentContext context) {
        return new ValidationChoiceListComponent(
            CharSequences.failIfNullOrEmpty(id, id),
            Objects.requireNonNull(context, "context")
        );
    }

    private ValidationChoiceListComponent(final String id,
                                          final ValidationChoiceListComponentContext context) {
        this.select = SelectComponent.<ValidationChoice>empty(
            (v) -> {
                final ValidationChoice c = v.orElseThrow(() -> new IllegalArgumentException("Missing ValidationChoice"));
                final String nameText = c.label();

                final HistoryToken historyToken = context.historyToken();

                return context.selectOption(
                    id + c.value() + SpreadsheetElementIds.OPTION, // id
                    CaseKind.PASCAL.change(
                        nameText,
                        CaseKind.TITLE
                    ), // text
                    v, // value
                    historyToken.selection()
                        .map(s ->
                            historyToken.setSelection(
                                Optional.of(s)
                            ).setSaveValue(
                                v.flatMap(vv -> vv.value())
                            )
                        )
                );
            }
        ).removePadding();

        this.setId(id);
        this.validationChoiceList = ValidationChoiceList.EMPTY;
    }

    // SelectComponent..................................................................................................

    @Override
    public ValidationChoiceListComponent setId(final String id) {
        this.select.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.select.id();
    }

    @Override
    public ValidationChoiceListComponent setLabel(final String label) {
        this.select.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.select.label();
    }

    @Override
    public boolean isDisabled() {
        return this.select.isDisabled();
    }

    @Override
    public ValidationChoiceListComponent setDisabled(final boolean disabled) {
        this.select.setDisabled(disabled);
        return this;
    }

    @Override
    public ValidationChoiceListComponent optional() {
        this.select.optional();
        return this;
    }

    @Override
    public ValidationChoiceListComponent required() {
        this.select.required();
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.select.isRequired();
    }

    @Override
    public ValidationChoiceListComponent validate() {
        this.select.validate();
        return this;
    }

    @Override
    public List<String> errors() {
        return this.select.errors();
    }

    @Override
    public ValidationChoiceListComponent setErrors(final List<String> errors) {
        this.select.setErrors(errors);
        return this;
    }

    @Override
    public ValidationChoiceListComponent focus() {
        this.select.focus();
        return this;
    }

    @Override
    public ValidationChoiceListComponent alwaysShowHelperText() {
        this.select.alwaysShowHelperText();
        return this;
    }

    @Override
    public ValidationChoiceListComponent setHelperText(final Optional<String> text) {
        this.select.setHelperText(text);
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return this.select.helperText();
    }

    @Override
    public ValidationChoiceListComponent hideMarginBottom() {
        this.select.hideMarginBottom();
        return this;
    }

    @Override
    public ValidationChoiceListComponent removeBorders() {
        this.select.removeBorders();
        return this;
    }

    @Override
    public ValidationChoiceListComponent addBlurListener(final EventListener listener) {
        this.select.addBlurListener(listener);
        return this;
    }

    @Override
    public ValidationChoiceListComponent addClickListener(final EventListener listener) {
        this.select.addClickListener(listener);
        return this;
    }

    @Override
    public ValidationChoiceListComponent addChangeListener(final ChangeListener<Optional<Object>> listener) {
        this.select.addChangeListener(
            (final Optional<ValidationChoice> oldValue,
             final Optional<ValidationChoice> newValue) -> listener.onValueChanged(
                oldValue.map(ValidationChoice::value),
                newValue.map(ValidationChoice::value)
            )
        );
        return this;
    }

    @Override
    public ValidationChoiceListComponent addContextMenuListener(final EventListener listener) {
        this.select.addContextMenuListener(listener);
        return this;
    }

    @Override
    public ValidationChoiceListComponent addFocusListener(final EventListener listener) {
        this.select.addFocusListener(listener);
        return this;
    }

    @Override
    public ValidationChoiceListComponent addKeyDownListener(final EventListener listener) {
        this.select.addKeyDownListener(listener);
        return this;
    }

    @Override
    public ValidationChoiceListComponent addKeyUpListener(final EventListener listener) {
        this.select.addKeyUpListener(listener);
        return this;
    }

    // ValidationChoiceList.............................................................................................

    public ValidationChoiceList validationChoiceList() {
        return this.validationChoiceList;
    }

    public ValidationChoiceListComponent setValidationChoiceList(final ValidationChoiceList validationChoiceList) {
        Objects.requireNonNull(validationChoiceList, "validationChoiceList");

        final SelectComponent<ValidationChoice> select = this.select;
        select.clearOptions();

        for (final ValidationChoice choice : validationChoiceList) {
            select.appendOption(
                Optional.of(choice)
            );
        }

        this.validationChoiceList = validationChoiceList;

        return this;
    }

    private ValidationChoiceList validationChoiceList;

    // Value............................................................................................................

    @Override
    public ValidationChoiceListComponent setValue(final Optional<Object> value) {
        Objects.requireNonNull(value, "value");

        // Select#value only match using ValidationChoice#value and not ValidationChoice#label and #value.
        this.select.setValue(
            this.validationChoiceList.stream()
                .filter(c -> c.value().equals(value))
                .findFirst()
        );
        return this;
    }

    @Override //
    public Optional<Object> value() {
        return this.select.value()
            .map(ValidationChoice::value);
    }

    @Override
    public boolean isEditing() {
        return this.select.isEditing();
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLFieldSetElement, ?> htmlComponent() {
        return this.select;
    }

    private final SelectComponent<ValidationChoice> select;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.select.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.select.printTree(printer);
        }
        printer.outdent();
    }
}