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
import org.dominokit.domino.ui.elements.SpanElement;
import org.dominokit.domino.ui.forms.suggest.SuggestOption;
import org.dominokit.domino.ui.forms.suggest.SuggestionsStore;
import org.dominokit.domino.ui.menu.MenuItem;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.set.Sets;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.locale.LocaleContext;
import walkingkooka.spreadsheet.dominokit.suggestbox.SpreadsheetSuggestBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.server.locale.LocaleHateosResource;
import walkingkooka.spreadsheet.server.locale.LocaleTag;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.util.HasLocale;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.dominokit.domino.ui.utils.Domino.span;

/**
 * A drop down that supports picking an optional {@link Locale}.
 * https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/5450
 */
public final class SpreadsheetLocaleComponent implements FormValueComponent<HTMLFieldSetElement, Locale, SpreadsheetLocaleComponent>,
    TreePrintable {

    /**
     * A {@link SpreadsheetLocaleComponent} which is initially empty, possible options to select must be added after
     * creation.
     */
    public static SpreadsheetLocaleComponent empty(final LocaleContext context) {
        return new SpreadsheetLocaleComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetLocaleComponent(final LocaleContext context) {
        this.context = context;

        this.suggestBox = SpreadsheetSuggestBoxComponent.with(
            // String -> SpreadsheetLocaleComponentValue, exception will be shown as an error text
            (String localeText) -> spreadsheetLocaleComponentValue(
                localeText,
                context
            ),
            new SuggestionsStore<SpreadsheetLocaleComponentValue, SpanElement, SuggestOption<SpreadsheetLocaleComponentValue>>() {

                @Override
                public void filter(final String startsWith,
                                   final SuggestionsHandler<SpreadsheetLocaleComponentValue, SpanElement, SuggestOption<SpreadsheetLocaleComponentValue>> handler) {
                    SpreadsheetLocaleComponent.this.suggestBox.setOptions(
                        SpreadsheetLocaleComponent.filter(startsWith, context)
                            .stream()
                            .map(
                                (LocaleHateosResource lhr) ->
                                    SpreadsheetLocaleComponentValue.with(
                                        lhr.locale(),
                                        lhr.text()
                                    )
                            ).collect(Collectors.toCollection(SortedSets::tree))
                    );
                }

                @Override
                public void find(final SpreadsheetLocaleComponentValue searchValue,
                                 final Consumer<SuggestOption<SpreadsheetLocaleComponentValue>> handler) {
                    final SpreadsheetLocaleComponentValue verified = verifyLocale(
                        searchValue,
                        context
                    ).orElse(null);

                    if (null != verified) {
                        handler.accept(
                            suggestOption(verified)
                        );
                    }
                }
            }
        );
    }

    // @VisibleForTesting
    static SpreadsheetLocaleComponentValue spreadsheetLocaleComponentValue(final String localeText,
                                                                           final LocaleContext context) {
        for (final Locale locale : context.availableLocales()) {
            final String possibleLocaleText = context.localeText(locale)
                .orElse(null);

            if (localeText.equalsIgnoreCase(possibleLocaleText)) {
                return SpreadsheetLocaleComponentValue.with(
                    locale,
                    localeText
                );
            }
        }

        throw new IllegalArgumentException("Unknown locale");
    }

    // @VisibleForTesting
    static Set<LocaleHateosResource> filter(final String startsWith,
                                            final LocaleContext context) {
        final Set<LocaleHateosResource> matched = Sets.ordered();

        for (final Locale locale : context.availableLocales()) {
            final String localeText = context.localeText(locale)
                .orElse(null);

            if (null != localeText && (CaseSensitivity.INSENSITIVE.startsWith(localeText, startsWith) || CaseSensitivity.INSENSITIVE.equals(localeText, startsWith))) {
                matched.add(
                    LocaleHateosResource.with(
                        LocaleTag.with(locale),
                        localeText
                    )
                );
            }
        }

        return matched;
    }

    /**
     * Verifies that the given locale is valid, returning null if it is an unknown locale.
     */
    static Optional<SpreadsheetLocaleComponentValue> verifyLocale(final SpreadsheetLocaleComponentValue value,
                                                                  final LocaleContext context) {
        SpreadsheetLocaleComponentValue verified = null;

        if (null != value) {
            final Locale locale = value.locale();
            final String localeText = context.localeText(locale)
                .orElse(null);
            verified = SpreadsheetLocaleComponentValue.with(
                locale,
                localeText
            );
        }

        return Optional.ofNullable(verified);
    }

    static SuggestOption<SpreadsheetLocaleComponentValue> suggestOption(final SpreadsheetLocaleComponentValue value) {
        final Locale locale = value.locale();

        return new SuggestOption<>(
            locale.toLanguageTag(), // key
            value, // value
            (String k, SpreadsheetLocaleComponentValue v) -> span().textContent(v.text()),
            (String k, SpreadsheetLocaleComponentValue v) -> MenuItem.create(v.text())
        );
    }

    @Override
    public SpreadsheetLocaleComponent setId(final String id) {
        this.suggestBox.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.suggestBox.id();
    }

    @Override
    public SpreadsheetLocaleComponent setLabel(final String label) {
        this.suggestBox.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.suggestBox.label();
    }

    @Override
    public SpreadsheetLocaleComponent focus() {
        this.suggestBox.focus();
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent alwaysShowHelperText() {
        this.suggestBox.alwaysShowHelperText();
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent setHelperText(final Optional<String> text) {
        this.suggestBox.setHelperText(text);
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return this.suggestBox.helperText();
    }

    @Override
    public List<String> errors() {
        return this.suggestBox.errors();
    }

    @Override
    public SpreadsheetLocaleComponent setErrors(final List<String> errors) {
        this.suggestBox.setErrors(errors);
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent hideMarginBottom() {
        this.suggestBox.hideMarginBottom();
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent removeBorders() {
        this.suggestBox.removeBorders();
        return this;
    }

    @Override
    public boolean isDisabled() {
        return this.suggestBox.isDisabled();
    }

    @Override
    public SpreadsheetLocaleComponent setDisabled(final boolean disabled) {
        this.suggestBox.setDisabled(disabled);
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent required() {
        this.suggestBox.required();
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.suggestBox.isRequired();
    }

    @Override
    public SpreadsheetLocaleComponent optional() {
        this.suggestBox.optional();
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent validate() {
        this.suggestBox.validate();
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent addChangeListener(final ChangeListener<Optional<Locale>> listener) {
        Objects.requireNonNull(listener, "listener");

        this.suggestBox.addChangeListener(
            (Optional<SpreadsheetLocaleComponentValue> oldLocale, Optional<SpreadsheetLocaleComponentValue> newLocale) -> listener.onValueChanged(
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
        SpreadsheetLocaleComponentValue verified = null;

        if (locale.isPresent()) {
            final Locale gotLocale = locale.get();

            final String localeText = this.context.localeText(gotLocale)
                .orElse(null);

            if(null != localeText) {
                verified = SpreadsheetLocaleComponentValue.with(
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

    private final SpreadsheetSuggestBoxComponent<SpreadsheetLocaleComponentValue> suggestBox;

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