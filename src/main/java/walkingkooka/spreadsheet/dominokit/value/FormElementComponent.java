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

package walkingkooka.spreadsheet.dominokit.value;

import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.forms.AbstractFormElement;
import org.dominokit.domino.ui.forms.AutoValidator;
import org.dominokit.domino.ui.utils.ApplyFunction;
import org.gwtproject.core.shared.GWT;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link AbstractFormElement} that wraps the given {@link HtmlComponent} adding the label, error messages and helper text.
 * Value operations such as {@link #getValue()}, {@link #setValue(Object)} will throw {@link UnsupportedOperationException}
 * if the wrapped component is not a {@link ValueComponent}.
 * Validation {@link #validate()}} will throw {@link UnsupportedOperationException}.
 */
public final class FormElementComponent<V, E extends HTMLElement, C extends HtmlComponent<E, C>> extends AbstractFormElement<FormElementComponent<V, E, C>, V>
    implements TreePrintable {

    public static <V, E extends HTMLElement, C extends HtmlComponent<E, C>> FormElementComponent<V, E, C> with(final C component) {
        return new FormElementComponent<>(
            Objects.requireNonNull(component, "component")
        );
    }

    private FormElementComponent(final C component) {
        super();
        if(GWT.isScript()) {
            this.wrapperElement.appendChild(component);
            this.wrapperElement.removeCss(dui_input_wrapper); // remove rounded corner border
        }

        this.component = component;
    }

    private final static boolean SET_VALUE_SILENT = false;

    @Override
    public FormElementComponent<V, E, C> withValue(final V value) {
        return this.withValue(
            value,
            SET_VALUE_SILENT
        );
    }

    @Override
    public FormElementComponent<V, E, C> withValue(final V value,
                                                   final boolean silent) {
        this.valueComponentOrFail()
            .setValue(
                Optional.of(value)
            );
        return this;
    }

    @Override
    public void setValue(final V value) {
        this.withValue(value);
    }

    @Override
    public V getValue() {
        return this.valueComponentOrFail()
            .value()
            .orElse(null);
    }

    @Override
    public FormElementComponent<V, E, C> clear() {
        this.clear(SET_VALUE_SILENT);
        return this;
    }

    @Override
    public FormElementComponent<V, E, C> clear(final boolean silent) {
        this.valueComponentOrFail()
            .clearValue();
        return this;
    }

    private ValueComponent<?, V, ?> valueComponentOrFail() {
        final C component = this.component;
        if (false == component instanceof ValueComponent) {
            throw new UnsupportedOperationException();
        }
        return (ValueComponent<?, V, ?>) component;
    }

    private final C component;

    @Override
    public AutoValidator createAutoValidator(final ApplyFunction autoValidate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FormElementComponent<V, E, C> triggerChangeListeners(final V oldValue,
                                                                final V newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FormElementComponent<V, E, C> triggerClearListeners(final V oldValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FormElementComponent<V, E, C> setName(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isEmptyIgnoreSpaces() {
        return this.isEmpty();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());

        printer.indent();
        {
            int i = 0;

            final String label = this.getLabel();
            if (false == CharSequences.isNullOrEmpty(label)) {
                printer.println("label");
                printer.println(label);

                i++;
            }

            final String helperText = this.getHelperText();
            if (false == CharSequences.isNullOrEmpty(helperText)) {
                printer.println("helperText");
                printer.println(helperText);

                i++;
            }

            final List<String> errors = this.getErrors();
            if (false == errors.isEmpty()) {
                printer.println("errors");
                printer.indent();

                for (final String error : errors) {
                    printer.println(error);
                    i++;
                }

                printer.outdent();
            }

            if (i > 0) {
                printer.indent();
            }
            {
                this.component.printTree(printer);
            }
            if (i > 0) {
                printer.outdent();
            }
        }
        printer.outdent();
    }
}
