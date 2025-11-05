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

package walkingkooka.spreadsheet.dominokit.datetime;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.elements.SpanElement;
import org.dominokit.domino.ui.forms.FormsStyles;
import org.dominokit.domino.ui.utils.BaseDominoElement;
import org.dominokit.domino.ui.utils.LazyChild;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.dominokit.domino.ui.utils.Domino.div;
import static org.dominokit.domino.ui.utils.Domino.span;

/**
 * Abstract base class for date/datetime/time pickers.
 */
abstract class DominoKitPickerComponent<V, C extends DominoKitPickerComponent<V, C>> implements FormValueComponent<HTMLDivElement, V, C> {

    /**
     * Parent of the picker and messages.
     */
    protected final DivElement bodyElement;
    protected final LazyChild<DivElement> messagesWrapper;
    protected final LazyChild<SpanElement> helperTextElement;
    protected final Function<String, SpanElement> errorElementSupplier;

    protected final List<String> errors = new ArrayList<>();

    DominoKitPickerComponent(final Supplier<V> clearValue) {
        this.clearValue = Objects.requireNonNull(clearValue, "clearValue");

        // AbstractFormElement
        this.bodyElement =
            div()
                .addCss(FormsStyles.dui_field_body);
        this.messagesWrapper = LazyChild.of(
            div()
                .addCss(FormsStyles.dui_messages_wrapper),
            this.bodyElement
        );
        this.helperTextElement = LazyChild.of(
            span()
                .addCss(FormsStyles.dui_field_helper),
            this.messagesWrapper
        );
        this.errorElementSupplier =
            errorMessage -> span()
                .addCss(FormsStyles.dui_field_error)
                .setTextContent(errorMessage);
    }

    @Override
    public final C setId(final String id) {
        this.element().id = id;
        return (C) this;
    }

    @Override
    public final String id() {
        return this.element().id;
    }

    @Override
    public final C setLabel(final String label) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String label() {
        return "";
    }

    @Override
    public final boolean isDisabled() {
        return false;
    }

    @Override
    public final C setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Optional<String> helperText() {
        final String text = this.helperTextElement.get()
            .getTextContent();
        return Optional.ofNullable(
            CharSequences.isNullOrEmpty(text) ?
                null :
                text
        );
    }

    @Override
    public final C setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");

        this.helperTextElement.get()
            .setTextContent(
                text.orElse("")
            );

        return (C) this;
    }

    final Supplier<V> clearValue;

    @Override
    public final C validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequired() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> errors() {
        return this.errors;
    }

    @Override
    public final C setErrors(final List<String> errors) {
        this.removeErrors();
        this.invalidate(errors);

        return (C) this;
    }

    // AbstractFormElement.invalidate
    private void invalidate(List<String> errorMessages) {
        removeErrors();
        errorMessages.forEach(
            message -> {
                this.errors.add(message);
                messagesWrapper.get().appendChild(errorElementSupplier.apply(message));
            });

        if (!errorMessages.isEmpty()) {
            this.bodyElement.addCss(FormsStyles.dui_field_invalid);
        }
    }

    // AbstractFormElement.removeErrors
    private void removeErrors() {
        this.errors.clear();
        this.messagesWrapper
            .get()
            .querySelectorAll("." + FormsStyles.dui_field_error.getCssClass())
            .forEach(BaseDominoElement::remove);
        FormsStyles.dui_field_invalid.remove(this);
    }

    @Override
    public final C addBlurListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C addContextMenuListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C addFocusListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C addInputListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C addKeyDownListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C addKeyUpListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C removePadding() {
        throw new UnsupportedOperationException();
    }

    // node.............................................................................................................

    @Override
    public final Node node() {
        return this.element();
    }

    // width............................................................................................................

    @Override
    public int width() {
        return this.element()
            .offsetWidth;
    }

    // height...........................................................................................................

    @Override
    public int height() {
        return this.element()
            .offsetHeight;
    }

    // IsElement........................................................................................................

    @Override
    public final HTMLDivElement element() {
        return this.bodyElement.element();
    }

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            printer.println(
                this.toString()
            );
        }
        printer.outdent();
    }

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.value()
            .map(Object::toString)
            .orElse("");
    }
}
