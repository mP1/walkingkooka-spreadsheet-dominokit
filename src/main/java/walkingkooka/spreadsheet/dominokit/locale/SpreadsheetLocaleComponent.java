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
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.locale.LocaleContext;
import walkingkooka.spreadsheet.dominokit.suggestbox.SpreadsheetSuggestBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.server.locale.LocaleHateosResource;
import walkingkooka.spreadsheet.server.locale.LocaleTag;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A drop down that supports picking an optional {@link Locale}.
 * TODO Display flags for each locale
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
        final Map<String, Locale> localeTextToLocale = Maps.sorted(String.CASE_INSENSITIVE_ORDER);
        final Map<String, LocaleHateosResource> languageTagToLocaleHateosResource = Maps.sorted(String.CASE_INSENSITIVE_ORDER);

        for (final Locale locale : context.availableLocales()) {
            final String localeText = context.localeText(locale)
                .orElse(null);

            if (null != localeText) {
                localeTextToLocale.put(
                    localeText,
                    locale
                );

                final String languageTag = locale.toLanguageTag();

                languageTagToLocaleHateosResource.put(
                    languageTag,
                    LocaleHateosResource.with(
                        LocaleTag.with(locale),
                        localeText
                    )
                );
            }
        }

        this.languageTagToLocaleHateosResource = languageTagToLocaleHateosResource;

        this.suggestBox = SpreadsheetSuggestBoxComponent.with(
            // String -> LocaleHateousResource, exception will be shown as an error text
            (String localeText) -> {
                final Locale locale = localeTextToLocale.get(localeText);
                if (null == locale) {
                    throw new IllegalArgumentException("Unknown locale");
                }
                return LocaleHateosResource.with(
                    LocaleTag.with(locale),
                    localeText
                );
            },
            new SuggestionsStore<LocaleHateosResource, SpanElement, SuggestOption<LocaleHateosResource>>() {

                @Override
                public void filter(final String startsWith,
                                   final SuggestionsHandler<LocaleHateosResource, SpanElement, SuggestOption<LocaleHateosResource>> handler) {
                    final List<SuggestOption<LocaleHateosResource>> suggestions = Lists.array();

                    for (final Entry<String, Locale> localeTextAndLocale : localeTextToLocale.entrySet()) {
                        final String localeText = localeTextAndLocale.getKey();
                        if (isMatch(startsWith, localeText)) {
                            suggestions.add(
                                SuggestOption.create(
                                    LocaleHateosResource.with(
                                        LocaleTag.with(
                                            localeTextAndLocale.getValue()
                                        ),
                                        localeText
                                    )
                                )
                            );
                        }
                    }

                    handler.onSuggestionsReady(suggestions);
                }

                @Override
                public void find(final LocaleHateosResource searchValue,
                                 final Consumer<SuggestOption<LocaleHateosResource>> handler) {
                    final LocaleHateosResource localeHateosResource = languageTagToLocaleHateosResource.get(
                        searchValue.locale()
                            .toLanguageTag()
                    );
                    if (null == localeHateosResource) {
                        throw new IllegalArgumentException("Unknown locale");
                    }
                    handler.accept(
                        SuggestOption.create(localeHateosResource)
                    );
                }
            }
        );
    }

    private static boolean isMatch(final String startsWith,
                                   final String text) {
        return text.startsWith(startsWith) || text.equals(startsWith);
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
            (Optional<LocaleHateosResource> oldLocale, Optional<LocaleHateosResource> newLocale) -> listener.onValueChanged(
                oldLocale.map(LocaleHateosResource::locale),
                newLocale.map(LocaleHateosResource::locale)
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
        LocaleHateosResource verified = null;

        if (locale.isPresent()) {
            final String languageLocale = locale.get()
                .toLanguageTag();

            verified = this.languageTagToLocaleHateosResource.get(languageLocale);
        }

        this.suggestBox.setValue(
            Optional.ofNullable(verified)
        );
        return this;
    }

    private final Map<String, LocaleHateosResource> languageTagToLocaleHateosResource;

    @Override //
    public Optional<Locale> value() {
        return this.suggestBox.value()
            .map(LocaleHateosResource::locale);
    }

    private final SpreadsheetSuggestBoxComponent<LocaleHateosResource> suggestBox;

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