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

package walkingkooka.spreadsheet.dominokit.suggestbox;

import elemental2.dom.Element;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.elements.SpanElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.forms.suggest.SuggestBox;
import org.dominokit.domino.ui.forms.suggest.SuggestOption;
import org.dominokit.domino.ui.forms.suggest.SuggestionsStore;
import org.dominokit.domino.ui.loaders.Loader;
import org.dominokit.domino.ui.loaders.LoaderEffect;
import org.dominokit.domino.ui.menu.AbstractMenuItem;
import org.dominokit.domino.ui.menu.Menu;
import org.dominokit.domino.ui.menu.MenuItem;
import org.dominokit.domino.ui.utils.DominoElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.validator.SpreadsheetValidators;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.dominokit.domino.ui.utils.Domino.span;
import static org.dominokit.domino.ui.utils.ElementsFactory.elements;

/**
 * A text box component that includes support for finding a label.
 */
public final class SpreadsheetSuggestBoxComponent<T> implements SpreadsheetSuggestBoxComponentLike<T> {

    public static <T> SpreadsheetSuggestBoxComponent<T> with(final Function<String, T> parser,
                                                             final SpreadsheetSuggestBoxComponentSuggestionsProvider<T> suggestionsProvider,
                                                             final Function<T, MenuItem<T>> menuItemCreator) {
        Objects.requireNonNull(parser, "parser");
        Objects.requireNonNull(suggestionsProvider, "suggestionsProvider");
        Objects.requireNonNull(menuItemCreator, "menuItemCreator");

        return new SpreadsheetSuggestBoxComponent<>(
            parser,
            suggestionsProvider,
            menuItemCreator
        );
    }

    private SpreadsheetSuggestBoxComponent(final Function<String, T> parser,
                                           final SpreadsheetSuggestBoxComponentSuggestionsProvider<T> suggestionsProvider,
                                           final Function<T, MenuItem<T>> menuItemCreator) {
        this.parser = parser;

        this.suggestBox = new SpreadsheetSuggestBoxComponentSuggestBox<>(
            new SuggestionsStore<T, SpanElement, SuggestOption<T>>() {
                @Override
                public void filter(final String value,
                                   final SuggestionsHandler<T, SpanElement, SuggestOption<T>> suggestionsHandler) {
                    suggestionsProvider.filter(
                        value,
                        SpreadsheetSuggestBoxComponent.this
                    );
                }

                @Override
                public void find(final T searchValue,
                                 final Consumer<SuggestOption<T>> handler) {
                    if (null != searchValue) {
                        suggestionsProvider.verifyOption(
                            searchValue,
                            SpreadsheetSuggestBoxComponent.this
                        );
                    }
                }
            }
        );
        this.suggestionsProvider = suggestionsProvider;

        suggestBox.setEmptyAsNull(true);
        suggestBox.setAutoValidation(true);
        suggestBox.withLoaderElement(
            (parent, loaderElement) -> {
                this.loader = Loader.create(loaderElement, LoaderEffect.FACEBOOK)
                    .setLoadingTextPosition(Loader.LoadingTextPosition.TOP)
                    .setRemoveLoadingText(true);
            }
        );
        suggestBox.setLoader(this.loader);

        this.required();

        this.options = Lists.empty();

        this.menuItemCreator = menuItemCreator;
    }

    @Override
    public List<T> options() {
        return this.options;
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> setOptions(final List<T> options) {
        Objects.requireNonNull(options, "options");

        // stop the loader, necessary as this might be called by SuggestBoxStore#filter
        this.loader.stop();

        this.options = Lists.immutable(options);

        final SuggestBox<T, SpanElement, SuggestOption<T>> suggestBox = this.suggestBox;

        final Menu<T> menu = suggestBox.getOptionsMenu();
        menu.removeAll();
        menu.clearSelection(true);

        for (final T option : options) {
            final AbstractMenuItem<T> menuItem = this.suggestOption(option)
                .getMenuItem();

            menu.appendChild(
                menuItem
            );
        }

        if (options.isEmpty()) {
            menu.close();
        } else {
            menu.open(false);
        }

        return this;
    }

    private List<T> options;

    /**
     * A reference to the {@link Loader} is necessary because {@link SuggestBox} does not provide a public getter
     * to stop the loader.
     */
    private Loader loader;

    @Override
    public SpreadsheetSuggestBoxComponent<T> setVerifiedOption(final T option) {
        Objects.requireNonNull(option, "option");

        this.suggestBox.applyOptionValue(
            this.suggestOption(option)
        );
        return this;
    }

    private SuggestOption<T> suggestOption(final T option) {
        return new SuggestOption<>(
            this.suggestionsProvider.menuItemKey(option),
            option, // value
            (String k, T v) -> span().textContent(v.toString()), // ignored
            (String k, T v) -> this.menuItemCreator.apply(v)
        );
    }

    private final SpreadsheetSuggestBoxComponentSuggestionsProvider<T> suggestionsProvider;

    /**
     * This factory will be called for each option creating the {@link MenuItem}.
     */
    private final Function<T, MenuItem<T>> menuItemCreator;

    // id...............................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> setId(final String id) {
        this.suggestBox.getInputElement()
            .setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.suggestBox.getInputElement()
            .getId();
    }

    // label............................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> setLabel(final String label) {
        this.suggestBox.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.suggestBox.getLabel();
    }

    // helperText.......................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> alwaysShowHelperText() {
        final DominoElement<Element> element = elements.elementOf(
            this.suggestBox.element()
                .firstElementChild
        );
        element.setHeight(FormValueComponent.HELPER_TEXT_HEIGHT);
        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");

        this.suggestBox.setHelperText(
            text.orElse(null)
        );
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return Optional.ofNullable(
            this.suggestBox.getHelperText()
        );
    }

    // StringValue......................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> setStringValue(final Optional<String> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> stringValue() {
        final String text = this.suggestBox.getStringValue();

        return Optional.ofNullable(
            CharSequences.isNullOrEmpty(text) ?
                null :
                text
        );
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> setValue(final Optional<T> label) {
        Objects.requireNonNull(label, "label");

        if (label.isPresent()) {
            this.suggestBox.setValue(
                label.get()
            );
        } else {
            this.suggestBox.clear();
        }

        return this;
    }

    @Override //
    public Optional<T> value() {
        return tryParse(
            this.suggestBox.getStringValue()
        );
    }

    private Optional<T> tryParse(final String string) {
        T value;

        try {
            value = this.parser.apply(string);
        } catch (final Exception ignore) {
            value = null;
        }

        return Optional.ofNullable(value);
    }

    private final Function<String, T> parser;

    // isDisabled.......................................................................................................

    @Override
    public boolean isDisabled() {
        return this.suggestBox.isDisabled();
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> setDisabled(final boolean disabled) {
        this.suggestBox.setDisabled(disabled);
        return this;
    }

    // validation.......................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> optional() {
        this.required = false;
        return this.setValidator(
            SpreadsheetValidators.optional(
                SpreadsheetValidators.tryCatch(
                    this.parser::apply
                )
            )
        );
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> required() {
        this.required = true;
        return this.setValidator(
            SpreadsheetValidators.tryCatch(
                this.parser::apply
            )
        );
    }

    private SpreadsheetSuggestBoxComponent<T> setValidator(final Validator<Optional<String>> validator) {
        this.suggestBox.getValidators()
            .clear();
        this.suggestBox.addValidator(
            SpreadsheetSuggestBoxComponentValidator.with(validator)
        );
        return this.validate();
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    boolean required;

    @Override
    public SpreadsheetSuggestBoxComponent<T> validate() {
        this.suggestBox.validate();
        return this;
    }

    @Override
    public List<String> errors() {
        return Lists.readOnly(
            this.suggestBox.getErrors()
        );
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> setErrors(final List<String> errors) {
        this.suggestBox.invalidate(
            Lists.immutable(errors)
        );
        return this;
    }

    // events...........................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> addChangeListener(final ChangeListener<Optional<T>> listener) {
        Objects.requireNonNull(listener, "listener");

        this.suggestBox.addChangeListener(
            (final T oldValue,
             final T newValue) -> listener.onValueChanged(
                Optional.ofNullable(oldValue),
                Optional.ofNullable(newValue)
            )
        );

        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> addClickListener(final EventListener listener) {
        return this.addEventListener(
            EventType.click,
            listener
        );
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> addFocusListener(final EventListener listener) {
        return this.addEventListener(
            EventType.focus,
            listener
        );
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> addKeydownListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keydown,
            listener
        );
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> addKeyupListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keyup,
            listener
        );
    }

    private SpreadsheetSuggestBoxComponent<T> addEventListener(final EventType eventType,
                                                               final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        this.suggestBox.addEventListener(
            eventType,
            listener
        );
        return this;
    }

    // focus............................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> focus() {
        this.suggestBox.focus();
        return this;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return this.suggestBox.isFocused();
    }

    // styling..........................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> hideMarginBottom() {
        this.suggestBox.setMarginBottom("");
        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> removeBorders() {
        this.suggestBox.getInputElement()
            .parent()
            .parent()
            .setBorder("0")
            .setCssProperty("border-radius", 0);
        return this;
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.suggestBox.cssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> setCssProperty(final String name,
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

    private final SpreadsheetSuggestBoxComponentSuggestBox<T> suggestBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.element()
            .toString();
    }
}
