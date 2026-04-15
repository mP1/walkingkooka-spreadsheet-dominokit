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
import walkingkooka.spreadsheet.dominokit.HtmlComponent;

import java.util.Objects;

/**
 * A {@link AbstractFormElement} that wraps the given {@link HtmlComponent} adding the label, error messages and helper text.
 * All value related operations such as getting/setting and validation will throw {@link UnsupportedOperationException}.
 */
public final class FormElement<V, E extends HTMLElement, C extends HtmlComponent<E, C>> extends AbstractFormElement<FormElement<V, E, C>, V> {

    public static <V, E extends HTMLElement, C extends HtmlComponent<E, C>> FormElement<V, E, C> with(final C component) {
        return new FormElement<>(
            Objects.requireNonNull(component, "component")
        );
    }

    private FormElement(final C component) {
        super();
        this.wrapperElement.appendChild(component);
        this.wrapperElement.removeCss(dui_input_wrapper); // remove rounded corner border
    }

    @Override
    public FormElement<V, E, C> withValue(final V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FormElement<V, E, C> withValue(final V value,
                                          final boolean silent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setValue(final V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V getValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FormElement<V, E, C> clear() {
        return this;
    }

    @Override
    public FormElement<V, E, C> clear(final boolean silent) {
        return this;
    }

    @Override
    public AutoValidator createAutoValidator(final ApplyFunction autoValidate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FormElement<V, E, C> triggerChangeListeners(final V oldValue,
                                                       final V newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FormElement<V, E, C> triggerClearListeners(final V oldValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FormElement<V, E, C> setName(final String name) {
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
}
