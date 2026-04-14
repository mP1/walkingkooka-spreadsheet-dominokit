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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import org.dominokit.domino.ui.forms.AbstractFormElement;
import org.dominokit.domino.ui.forms.AutoValidator;
import org.dominokit.domino.ui.utils.ApplyFunction;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorListComponent;

/**
 * A {@link AbstractFormElement} that wraps the given {@link AnchorListComponent} adding the label, error messages and helper text.
 * All value related operations such as getting/setting and validation will throw {@link UnsupportedOperationException}.
 */
final class TextStylePropertyNameEnumHistoryTokenAnchorListComponentFormElement<T extends Enum<T>> extends AbstractFormElement<TextStylePropertyNameEnumHistoryTokenAnchorListComponentFormElement<T>, T> {

    static <T extends Enum<T>> TextStylePropertyNameEnumHistoryTokenAnchorListComponentFormElement<T> with(final AnchorListComponent anchorListComponent) {
        return new TextStylePropertyNameEnumHistoryTokenAnchorListComponentFormElement<>(anchorListComponent);
    }

    private TextStylePropertyNameEnumHistoryTokenAnchorListComponentFormElement(final AnchorListComponent anchorListComponent) {
        super();
        this.anchorListComponent = anchorListComponent;
        this.wrapperElement.appendChild(anchorListComponent);
        this.wrapperElement.removeCss(dui_input_wrapper); // remove rounded corner border
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponentFormElement<T> withValue(final T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponentFormElement<T> withValue(final T value,
                                                                                            final boolean silent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setValue(final T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T getValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponentFormElement<T> clear() {
        return this;
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponentFormElement<T> clear(final boolean silent) {
        return this;
    }

    @Override
    public AutoValidator createAutoValidator(final ApplyFunction autoValidate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponentFormElement<T> triggerChangeListeners(final T oldValue,
                                                                                                         final T newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponentFormElement<T> triggerClearListeners(final T oldValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponentFormElement<T> setName(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return this.anchorListComponent.isEmpty();
    }

    @Override
    public boolean isEmptyIgnoreSpaces() {
        return this.isEmpty();
    }

    private final AnchorListComponent anchorListComponent;
}
