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

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.suggestbox.SpreadsheetSuggestBoxComponent;
import walkingkooka.spreadsheet.dominokit.suggestbox.SpreadsheetSuggestBoxComponentDelegator;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.util.HasLocale;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * A drop down that supports picking an optional {@link Locale}.
 */
public final class SpreadsheetLocaleComponent implements SpreadsheetSuggestBoxComponentDelegator<HTMLFieldSetElement, Locale, SpreadsheetLocaleComponent>,
    TreePrintable {

    /**
     * A {@link SpreadsheetLocaleComponent} which is initially empty, possible options to select must be added after
     * creation.
     */
    public static SpreadsheetLocaleComponent empty(final SpreadsheetLocaleComponentContext context) {
        return new SpreadsheetLocaleComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetLocaleComponent(final SpreadsheetLocaleComponentContext context) {
        this.suggestBox = SpreadsheetSuggestBoxComponent.with(
            context,
            context::createMenuItem
        );
        this.context = context;
    }

    @Override
    public SpreadsheetLocaleComponent focus() {
        this.suggestBox.focus();
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.suggestBox.isEditing();
    }

    @Override
    public SpreadsheetLocaleComponent addChangeListener(final ChangeListener<Optional<Locale>> listener) {
        Objects.requireNonNull(listener, "listener");

        this.suggestBox.addChangeListener(
            (Optional<SpreadsheetLocaleComponentSuggestionsValue> oldLocale, Optional<SpreadsheetLocaleComponentSuggestionsValue> newLocale) -> listener.onValueChanged(
                oldLocale.map(HasLocale::locale),
                newLocale.map(HasLocale::locale)
            )
        );
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent addClickListener(final EventListener listener) {
        this.suggestBox.addClickListener(
            listener
        );
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent addFocusListener(final EventListener listener) {
        this.suggestBox.addFocusListener(
            listener
        );
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent addKeydownListener(final EventListener listener) {
        this.suggestBox.addKeydownListener(listener);
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent addKeyupListener(final EventListener listener) {
        this.suggestBox.addKeyupListener(listener);
        return this;
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetLocaleComponent setCssText(final String css) {
        this.suggestBox.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetLocaleComponent setCssProperty(final String name,
                                                     final String value) {
        this.suggestBox.setCssProperty(
            name,
            value
        );
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.suggestBox.element();
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetLocaleComponent setValue(final Optional<Locale> locale) {
        Objects.requireNonNull(locale, "locale");

        this.suggestBox.setValue(
            locale.flatMap(this.context::toValue)
        );
        return this;
    }

    private final SpreadsheetLocaleComponentContext context;

    @Override //
    public Optional<Locale> value() {
        return this.suggestBox.value()
            .map(HasLocale::locale);
    }

    // SpreadsheetSuggestBoxComponentDelegator..........................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<SpreadsheetLocaleComponentSuggestionsValue> spreadsheetSuggestBoxComponent() {
        return this.suggestBox;
    }

    private final SpreadsheetSuggestBoxComponent<SpreadsheetLocaleComponentSuggestionsValue> suggestBox;

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