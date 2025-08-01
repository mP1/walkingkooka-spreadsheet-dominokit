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
import org.dominokit.domino.ui.menu.MenuItem;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.locale.LocaleContext;
import walkingkooka.spreadsheet.dominokit.suggestbox.SpreadsheetSuggestBoxComponent;
import walkingkooka.spreadsheet.dominokit.suggestbox.SpreadsheetSuggestBoxComponentDelegator;
import walkingkooka.spreadsheet.dominokit.suggestbox.SpreadsheetSuggestBoxComponentSuggestionsProvider;
import walkingkooka.spreadsheet.server.locale.LocaleHateosResource;
import walkingkooka.spreadsheet.server.locale.LocaleHateosResourceSet;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.util.HasLocale;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A drop down that supports picking an optional {@link Locale}.
 * https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/5450
 */
public final class SpreadsheetLocaleComponent implements SpreadsheetSuggestBoxComponentDelegator<HTMLFieldSetElement, Locale, SpreadsheetLocaleComponent>,
    TreePrintable {

    /**
     * A {@link SpreadsheetLocaleComponent} which is initially empty, possible options to select must be added after
     * creation.
     */
    public static SpreadsheetLocaleComponent empty(final Function<SpreadsheetLocaleComponentSuggestionsValue, MenuItem<SpreadsheetLocaleComponentSuggestionsValue>> optionMenuItemCreator,
                                                   final LocaleContext context) {
        return new SpreadsheetLocaleComponent(
            Objects.requireNonNull(optionMenuItemCreator, "optionMenuItemCreator"),
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetLocaleComponent(final Function<SpreadsheetLocaleComponentSuggestionsValue, MenuItem<SpreadsheetLocaleComponentSuggestionsValue>> optionMenuItemCreator,
                                       final LocaleContext context) {
        this.context = context;

        this.suggestBox = SpreadsheetSuggestBoxComponent.with(
            // String -> SpreadsheetLocaleComponentSuggestionsValue, exception will be shown as an error text
            (String localeText) -> spreadsheetLocaleComponentValue(
                localeText,
                context
            ),
            new SpreadsheetSuggestBoxComponentSuggestionsProvider<>() {
                @Override
                public void filter(final String startsWith) {
                    SpreadsheetLocaleComponent.this.suggestBox.setOptions(
                        LocaleHateosResourceSet.filter(startsWith, context)
                            .stream()
                            .map(
                                (LocaleHateosResource lhr) ->
                                    SpreadsheetLocaleComponentSuggestionsValue.with(
                                        lhr.locale(),
                                        lhr.text()
                                    )
                            ).sorted()
                            .collect(Collectors.toList())
                    );
                }

                @Override
                public void verifyOption(final SpreadsheetLocaleComponentSuggestionsValue value) {
                    final SpreadsheetLocaleComponentSuggestionsValue verified = verifyLocale(
                        value,
                        context
                    ).orElse(null);

                    if (null != verified) {
                        SpreadsheetLocaleComponent.this.suggestBox.setVerifiedOption(verified);
                    }
                }

                @Override
                public String menuItemKey(final SpreadsheetLocaleComponentSuggestionsValue value) {
                    return value.locale()
                        .toLanguageTag();
                }
            },
            optionMenuItemCreator
        );
    }

    // @VisibleForTesting
    static SpreadsheetLocaleComponentSuggestionsValue spreadsheetLocaleComponentValue(final String localeText,
                                                                                      final LocaleContext context) {
        for (final Locale locale : context.availableLocales()) {
            final String possibleLocaleText = context.localeText(locale)
                .orElse(null);

            if (localeText.equalsIgnoreCase(possibleLocaleText)) {
                return SpreadsheetLocaleComponentSuggestionsValue.with(
                    locale,
                    localeText
                );
            }
        }

        throw new IllegalArgumentException("Unknown locale");
    }

    /**
     * Verifies that the given locale is valid, returning null if it is an unknown locale.
     */
    static Optional<SpreadsheetLocaleComponentSuggestionsValue> verifyLocale(final SpreadsheetLocaleComponentSuggestionsValue value,
                                                                             final LocaleContext context) {
        SpreadsheetLocaleComponentSuggestionsValue verified = null;

        if (null != value) {
            final Locale locale = value.locale();
            final String localeText = context.localeText(locale)
                .orElse(null);
            verified = SpreadsheetLocaleComponentSuggestionsValue.with(
                locale,
                localeText
            );
        }

        return Optional.ofNullable(verified);
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

        // translate Locale -> LocaleHateosResource. The later will have the locale text and Locale for the #suggestBox
        SpreadsheetLocaleComponentSuggestionsValue verified = null;

        if (locale.isPresent()) {
            final Locale gotLocale = locale.get();

            final String localeText = this.context.localeText(gotLocale)
                .orElse(null);

            if(null != localeText) {
                verified = SpreadsheetLocaleComponentSuggestionsValue.with(
                    gotLocale,
                    localeText
                );
            }
        }

        this.suggestBox.setValue(
            Optional.ofNullable(verified)
        );
        return this;
    }

    private final LocaleContext context;

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