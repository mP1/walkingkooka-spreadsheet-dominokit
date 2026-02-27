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

package walkingkooka.spreadsheet.dominokit.currency;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.currency.HasCurrency;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponent;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Currency;
import java.util.Objects;
import java.util.Optional;

/**
 * A component that supports displaying or editing as text or picking from a list of {@link Currency}.
 */
public final class CurrencyComponent<T> implements SuggestBoxComponentDelegator<HTMLFieldSetElement, Currency, CurrencyComponent<T>>,
    TreePrintable {

    /**
     * A {@link CurrencyComponent} which is initially empty, possible options to select must be added after
     * creation.
     */
    public static <T> CurrencyComponent<T> empty(final CurrencyComponentContext<T> context) {
        return new CurrencyComponent<>(
            Objects.requireNonNull(context, "context")
        );
    }

    private CurrencyComponent(final CurrencyComponentContext<T> context) {
        this.suggestBox = SuggestBoxComponent.with(
            context,
            context::createMenuItem
        );
        this.context = context;
    }

    @Override
    public CurrencyComponent<T> focus() {
        this.suggestBox.focus();
        return this;
    }

    @Override
    public CurrencyComponent<T> blur() {
        this.suggestBox.blur();
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.suggestBox.isEditing();
    }

    // Value............................................................................................................

    @Override
    public CurrencyComponent<T> setValue(final Optional<Currency> currency) {
        Objects.requireNonNull(currency, "currency");

        this.suggestBox.setValue(
            currency.flatMap(this.context::toValue)
        );
        return this;
    }

    private final CurrencyComponentContext<T> context;

    @Override //
    public Optional<Currency> value() {
        return this.suggestBox.value()
            .map(HasCurrency::currency);
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<Currency> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        return this.suggestBox.addValueWatcher(
            v -> v.map(HasCurrency::currency)
        );
    }

    // SuggestBoxComponentDelegator.....................................................................................

    @Override
    public SuggestBoxComponent<CurrencyComponentSuggestionsValue<T>> suggestBoxComponent() {
        return this.suggestBox;
    }

    private final SuggestBoxComponent<CurrencyComponentSuggestionsValue<T>> suggestBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.suggestBox.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.suggestBox.printTree(printer);
        }
        printer.outdent();
    }
}