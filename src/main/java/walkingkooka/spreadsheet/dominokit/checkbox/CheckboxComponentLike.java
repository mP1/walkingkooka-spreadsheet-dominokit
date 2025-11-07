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
import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.spreadsheet.dominokit.select.SelectComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTreePrintable;
import walkingkooka.text.printer.IndentingPrinter;

/**
 * Base class for {@link SelectComponent} that captures common members for main/test.
 */
abstract class CheckboxComponentLike implements FormValueComponent<HTMLFieldSetElement, Boolean, CheckboxComponent>,
    FormValueComponentTreePrintable<HTMLFieldSetElement, CheckboxComponent, Boolean> {

    CheckboxComponentLike() {
        super();
    }

    @Override
    public final CheckboxComponent optional() {
        return (CheckboxComponent) this;
    }

    @Override
    public final CheckboxComponent required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean isRequired() {
        return false;
    }

    // addXXXListener...................................................................................................

    @Override
    public final CheckboxComponent addBlurListener(final EventListener listener) {
        return this.addEventListener(
            EventType.blur,
            listener
        );
    }

    @Override
    public final CheckboxComponent addClickListener(final EventListener listener) {
        return this.addEventListener(
            EventType.click,
            listener
        );
    }

    @Override
    public final CheckboxComponent addContextMenuListener(final EventListener listener) {
        return this.addEventListener(
            EventType.contextmenu,
            listener
        );
    }

    @Override
    public final CheckboxComponent addFocusListener(final EventListener listener) {
        return this.addEventListener(
            EventType.focus,
            listener
        );
    }

    @Override
    public final CheckboxComponent addInputListener(final EventListener listener) {
        return this.addEventListener(
            EventType.input,
            listener
        );
    }

    @Override
    public final CheckboxComponent addKeyDownListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keydown,
            listener
        );
    }

    @Override
    public final CheckboxComponent addKeyUpListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keyup,
            listener
        );
    }

    abstract CheckboxComponent addEventListener(final EventType type,
                                                final EventListener listener);

    // Object...........................................................................................................

    @Override
    public final String toString() {
        String disabled = "";
        if (this.isDisabled()) {
            disabled = "DISABLED";
        }

        return ToStringBuilder.empty()
            .value(this.label())
            .disable(ToStringBuilderOption.QUOTE)
            .value(disabled)
            .value(this.value().orElse(Boolean.FALSE) ? "CHECKED" : "")
            .enable(ToStringBuilderOption.QUOTE)
            .label("id")
            .value(this.id())
            .build();
    }

    // FormValueComponentTreePrintable..................................................................................

    @Override
    public final void treePrintAlternateValues(final IndentingPrinter printer) {
        // NOP
    }
}
