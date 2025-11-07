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

package walkingkooka.spreadsheet.dominokit.checkbox;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.forms.CheckBox;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A component with a checkbox holding a {@link Boolean} value.
 */
public final class CheckboxComponent extends CheckboxComponentLike {

    public static CheckboxComponent empty() {
        return new CheckboxComponent();
    }

    private CheckboxComponent() {
        this.checkbox = CheckBox.create();
    }

    // id...............................................................................................................

    @Override
    public CheckboxComponent setId(final String id) {
        this.checkbox.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.checkbox.getId();
    }

    // label............................................................................................................

    @Override
    public CheckboxComponent setLabel(final String label) {
        this.checkbox.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.checkbox.getLabel();
    }

    // helperText.......................................................................................................

    @Override
    public CheckboxComponent alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CheckboxComponent setHelperText(final Optional<String> text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> helperText() {
        return Optional.empty();
    }

    // Value............................................................................................................

    @Override
    public CheckboxComponent setValue(final Optional<Boolean> value) {
        Objects.requireNonNull(value, "value");

        this.checkbox.setValue(
            value.orElse(Boolean.FALSE)
        );
        return this;
    }

    @Override //
    public Optional<Boolean> value() {
        return Optional.ofNullable(
            this.checkbox.getValue()
        );
    }

    @Override
    public boolean isDisabled() {
        return this.checkbox.isDisabled();
    }

    @Override
    public CheckboxComponent setDisabled(final boolean disabled) {
        this.checkbox.setDisabled(disabled);
        return this;
    }

    @Override
    public CheckboxComponent validate() {
        this.checkbox.validate();
        return this;
    }

    @Override
    public List<String> errors() {
        return this.checkbox.getErrors();
    }

    @Override
    public CheckboxComponent setErrors(final List<String> errors) {
        Objects.requireNonNull(errors, "errors");

        this.checkbox.invalidate(
            Lists.immutable(errors)
        );
        return this;
    }

    // events...........................................................................................................

    @Override
    public CheckboxComponent addChangeListener(final ChangeListener<Optional<Boolean>> listener) {
        Objects.requireNonNull(listener, "listener");

        this.checkbox.addChangeListener(
            (oldValue, newValue) -> listener.onValueChanged(
                Optional.ofNullable(oldValue),
                Optional.ofNullable(newValue)
            )
        );
        return this;
    }

    @Override
    CheckboxComponent addEventListener(final EventType type,
                                       final EventListener listener) {
        this.checkbox.addEventListener(
            type,
            Objects.requireNonNull(listener, "listener")
        );
        return this;
    }

    @Override
    void removeEventListener(final EventType type,
                             final EventListener listener) {
        this.checkbox.removeEventListener(
            type,
            listener
        );
    }

    // focus............................................................................................................

    @Override
    public CheckboxComponent focus() {
        this.checkbox.focus();
        return this;
    }

    // styling..........................................................................................................

    @Override
    public CheckboxComponent hideMarginBottom() {
        this.checkbox.setMarginBottom("");
        return this;
    }

    @Override
    public CheckboxComponent removeBorders() {
        this.checkbox.getInputElement()
            .parent()
            .setBorder("0")
            .setCssProperty("border-radius", 0);
        return this;
    }

    @Override
    public CheckboxComponent removePadding() {
        this.checkbox.getInputElement()
            .parent()
            .setPadding("0");
        return this;
    }

    // HtmlComponent....................................................................................................

    @Override
    public int width() {
        return this.element()
            .offsetWidth;
    }

    @Override
    public int height() {
        return this.element()
            .offsetHeight;
    }

    // setCssText.......................................................................................................

    @Override
    public CheckboxComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.checkbox.cssText(css);
        return this;
    }

    // setCssText.......................................................................................................

    @Override
    public CheckboxComponent setCssProperty(final String name,
                                            final String value) {
        this.checkbox.setCssProperty(
            name,
            value
        );
        return this;
    }

    // removeCssProperty................................................................................................

    @Override
    public CheckboxComponent removeCssProperty(final String name) {
        this.checkbox.removeCssProperty(name);
        return this;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return HtmlComponent.hasFocus(
            this.checkbox.element()
        );
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.checkbox.element();
    }

    private final CheckBox checkbox;
}
