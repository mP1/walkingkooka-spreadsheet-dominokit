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
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A component with a checkbox holding a {@link Boolean} value.
 */
public final class CheckboxComponent extends CheckboxComponentLike
    implements TestHtmlElementComponent<HTMLFieldSetElement, CheckboxComponent> {

    public static CheckboxComponent empty() {
        return new CheckboxComponent();
    }

    private CheckboxComponent() {
    }

    // id...............................................................................................................

    @Override
    public CheckboxComponent setId(final String id) {
        this.id = id;
        return this;
    }

    @Override
    public String id() {
        return this.id;
    }

    private String id;

    // label............................................................................................................

    @Override
    public CheckboxComponent setLabel(final String label) {
        this.label = label;
        return this;
    }

    @Override
    public String label() {
        return this.label;
    }

    private String label;

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
        this.value = Objects.requireNonNull(value, "value");
        return this;
    }

    @Override //
    public Optional<Boolean> value() {
        return this.value;
    }

    private Optional<Boolean> value = Optional.empty();

    // disabled.........................................................................................................

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public CheckboxComponent setDisabled(final boolean disabled) {
        this.disabled = true;
        return this;
    }

    private boolean disabled;

    // validate.........................................................................................................

    @Override
    public CheckboxComponent validate() {
        return this;
    }

    // errors...........................................................................................................

    @Override
    public List<String> errors() {
        return Lists.immutable(this.errors);
    }

    @Override
    public CheckboxComponent setErrors(final List<String> errors) {
        Objects.requireNonNull(errors, "errors");

        this.errors = Lists.immutable(errors);
        return this;
    }

    private List<String> errors = Lists.empty();

    // focus............................................................................................................

    @Override
    public CheckboxComponent focus() {
        return this;
    }

    // events...........................................................................................................

    @Override
    public CheckboxComponent addChangeListener(final ChangeListener<Optional<Boolean>> listener) {
        Objects.requireNonNull(listener, "listener");

        return this;
    }

    @Override
    CheckboxComponent addEventListener(final EventType type,
                                       final EventListener listener) {
        return this;
    }

    @Override
    void removeEventListener(final EventType type,
                             final EventListener listener) {
        // nop
    }

    // styling..........................................................................................................
    
    @Override
    public CheckboxComponent hideMarginBottom() {
        return this;
    }

    @Override
    public CheckboxComponent removeBorders() {
        return this;
    }

    @Override
    public CheckboxComponent removePadding() {
        return this;
    }
}
