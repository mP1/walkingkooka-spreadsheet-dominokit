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

package walkingkooka.spreadsheet.dominokit.locale;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponent;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.util.HasLocale;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * A drop down that supports picking an optional {@link Locale}.
 */
public final class LocaleComponent<T> implements SuggestBoxComponentDelegator<HTMLFieldSetElement, Locale, LocaleComponent<T>>,
    TreePrintable {

    /**
     * A {@link LocaleComponent} which is initially empty, possible options to select must be added after
     * creation.
     */
    public static <T> LocaleComponent<T> empty(final LocaleComponentContext<T> context) {
        return new LocaleComponent<>(
            Objects.requireNonNull(context, "context")
        );
    }

    private LocaleComponent(final LocaleComponentContext<T> context) {
        this.suggestBox = SuggestBoxComponent.with(
            context,
            context::createMenuItem
        );
        this.context = context;
    }

    @Override
    public LocaleComponent<T> focus() {
        this.suggestBox.focus();
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.suggestBox.isEditing();
    }

    // Value............................................................................................................

    @Override
    public LocaleComponent<T> setValue(final Optional<Locale> locale) {
        Objects.requireNonNull(locale, "locale");

        this.suggestBox.setValue(
            locale.flatMap(this.context::toValue)
        );
        return this;
    }

    private final LocaleComponentContext<T> context;

    @Override //
    public Optional<Locale> value() {
        return this.suggestBox.value()
            .map(HasLocale::locale);
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<Locale> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        return this.suggestBox.addValueWatcher(
            v -> v.map(HasLocale::locale)
        );
    }

    // SuggestBoxComponentDelegator.....................................................................................

    @Override
    public SuggestBoxComponent<LocaleComponentSuggestionsValue<T>> suggestBoxComponent() {
        return this.suggestBox;
    }

    private final SuggestBoxComponent<LocaleComponentSuggestionsValue<T>> suggestBox;

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