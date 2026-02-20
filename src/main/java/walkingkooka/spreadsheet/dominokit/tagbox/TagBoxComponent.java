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

package walkingkooka.spreadsheet.dominokit.tagbox;

import elemental2.dom.Element;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.chips.Chip;
import org.dominokit.domino.ui.forms.suggest.SuggestionsStore;
import org.dominokit.domino.ui.forms.suggest.TagBox;
import org.dominokit.domino.ui.forms.suggest.TagOption;
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
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.dominokit.domino.ui.utils.ElementsFactory.elements;

/**
 * A text box component that includes support for finding a label.
 */
public final class TagBoxComponent<T> extends TagBoxComponentLike<T> {

    public static <T> TagBoxComponent<T> with(final TagBoxComponentSuggestionsProvider<T> suggestionsProvider,
                                              final Function<T, MenuItem<T>> menuItemCreator) {
        Objects.requireNonNull(suggestionsProvider, "suggestionsProvider");
        Objects.requireNonNull(menuItemCreator, "menuItemCreator");

        return new TagBoxComponent<>(
            suggestionsProvider,
            menuItemCreator
        );
    }

    private TagBoxComponent(final TagBoxComponentSuggestionsProvider<T> suggestionsProvider,
                            final Function<T, MenuItem<T>> menuItemCreator) {
        this.tagBox = new TagBoxComponentTagBox<>(
            new SuggestionsStore<T, Chip, TagOption<T>>() {
                @Override
                public void filter(final String value,
                                   final SuggestionsHandler<T, Chip, TagOption<T>> suggestionsHandler) {
                    suggestionsProvider.filter(
                        value,
                        TagBoxComponent.this
                    );
                }

                @Override
                public void find(final T searchValue,
                                 final Consumer<TagOption<T>> handler) {
                    if (null != searchValue) {
                        suggestionsProvider.verifyOption(
                            searchValue,
                            TagBoxComponent.this
                        );
                    }
                }
            }
        );
        this.suggestionsProvider = suggestionsProvider;

        this.tagBox.setEmptyAsNull(true);
        this.tagBox.setAutoValidation(true);
        this.tagBox.withLoaderElement(
            (parent, loaderElement) ->
                this.loader = Loader.create(loaderElement, LoaderEffect.FACEBOOK)
                    .setLoadingTextPosition(Loader.LoadingTextPosition.TOP)
                    .setRemoveLoadingText(true)
        );
        this.tagBox.setLoader(this.loader);

        this.required();

        this.options = Lists.empty();

        this.menuItemCreator = menuItemCreator;
    }

    @Override
    public List<T> options() {
        return this.options;
    }

    @Override
    public TagBoxComponent<T> setOptions(final List<T> options) {
        Objects.requireNonNull(options, "options");

        // stop the loader, necessary as this might be called by TagBoxStore#filter
        this.loader.stop();

        this.options = Lists.immutable(options);

        final TagBoxComponentTagBox<T> tagBox = this.tagBox;

        final Menu<T> menu = tagBox.getOptionsMenu();
        menu.removeAll();
        menu.clearSelection(true);

        for (final T option : options) {
            final AbstractMenuItem<T> menuItem = this.tagOption(option)
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
     * A reference to the {@link Loader} is necessary because {@link TagBox} does not provide a public getter
     * to stop the loader.
     */
    private Loader loader;

    @Override
    public TagBoxComponent<T> setVerifiedOption(final T option) {
        Objects.requireNonNull(option, "option");

        this.tagBox.applyOptionValue(
            this.tagOption(option)
        );
        return this;
    }

    private TagOption<T> tagOption(final T option) {
        return new TagOption<>(
            this.suggestionsProvider.menuItemKey(option),
            option, // value
            (String k, T v) -> Chip.create(k), // ignored
            (String k, T v) -> this.menuItemCreator.apply(v)
        );
    }

    private final TagBoxComponentSuggestionsProvider<T> suggestionsProvider;

    /**
     * This factory will be called for each option creating the {@link MenuItem}.
     */
    private final Function<T, MenuItem<T>> menuItemCreator;

    // id...............................................................................................................

    @Override
    public TagBoxComponent<T> setId(final String id) {
        this.tagBox.getInputElement()
            .setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.tagBox.getInputElement()
            .getId();
    }

    // label............................................................................................................

    @Override
    public TagBoxComponent<T> setLabel(final String label) {
        this.tagBox.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.tagBox.getLabel();
    }

    // helperText.......................................................................................................

    @Override
    public TagBoxComponent<T> alwaysShowHelperText() {
        final DominoElement<Element> element = elements.elementOf(
            this.tagBox.element()
                .firstElementChild
        );
        element.setHeight(FormValueComponent.HELPER_TEXT_HEIGHT);
        return this;
    }

    @Override
    public TagBoxComponent<T> setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");

        this.tagBox.setHelperText(
            text.orElse(null)
        );
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return Optional.ofNullable(
            this.tagBox.getHelperText()
        );
    }

    // StringValue......................................................................................................

    @Override
    public TagBoxComponent<T> setStringValue(final Optional<String> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> stringValue() {
        final String text = this.tagBox.getStringValue();

        return Optional.ofNullable(
            CharSequences.isNullOrEmpty(text) ?
                null :
                text
        );
    }

    // Value............................................................................................................

    @Override
    public TagBoxComponent<T> setValue(final Optional<List<T>> value) {
        Objects.requireNonNull(value, "value");

        if (value.isPresent()) {
            this.tagBox.setValue(
                value.get()
            );
        } else {
            this.tagBox.clear();
        }

        return this;
//        throw new UnsupportedOperationException();
    }

    @Override //
    public Optional<List<T>> value() {
        return Optional.ofNullable(
            this.tagBox.getValue()
        );
        //throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<List<T>> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        final ChangeListener<List<T>> changeListener = (final List<T> oldValue,
                                                        final List<T> newValue) -> {
            watcher.onValue(
                Optional.ofNullable(newValue)
            );
        };
        this.tagBox.addChangeListener(changeListener);

        return () -> this.tagBox.removeChangeListener(changeListener);
    }

    // isDisabled.......................................................................................................

    @Override
    public boolean isDisabled() {
        return this.tagBox.isDisabled();
    }

    @Override
    public TagBoxComponent<T> setDisabled(final boolean disabled) {
        this.tagBox.setDisabled(disabled);
        return this;
    }

    // validation.......................................................................................................

    @Override
    public TagBoxComponent<T> optional() {
        this.required = false;
        return this.setValidator(
            SpreadsheetValidators.optional(
                SpreadsheetValidators.required()
            )
        );
    }

    @Override
    public TagBoxComponent<T> required() {
        this.required = true;
        return this.setValidator(
            SpreadsheetValidators.required()
        );
    }

    private TagBoxComponent<T> setValidator(final Validator<Optional<List<T>>> validator) {
        this.tagBox.getValidators()
            .clear();
        this.tagBox.addValidator(
            TagBoxComponentValidator.with(validator)
        );
        return this.validate();
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    boolean required;

    @Override
    public TagBoxComponent<T> validate() {
        this.tagBox.validate();
        return this;
    }

    @Override
    public List<String> errors() {
        return Lists.readOnly(
            this.tagBox.getErrors()
        );
    }

    @Override
    public TagBoxComponent<T> setErrors(final List<String> errors) {
        this.tagBox.invalidate(
            Lists.immutable(errors)
        );
        return this;
    }

    // focus............................................................................................................

    @Override
    public TagBoxComponent<T> focus() {
        this.tagBox.focus();
        return this;
    }

    // blur.............................................................................................................

    @Override
    public TagBoxComponent<T> blur() {
        this.tagBox.blur();
        return this;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return this.tagBox.isFocused();
    }

    // styling..........................................................................................................

    @Override
    public TagBoxComponent<T> hideMarginBottom() {
        this.tagBox.setMarginBottom("");
        return this;
    }

    @Override
    public TagBoxComponent<T> removeBorders() {
        this.tagBox.getInputElement()
            .parent()
            .parent()
            .setBorder("0")
            .setCssProperty("border-radius", 0);
        return this;
    }

    @Override
    public TagBoxComponent<T> removePadding() {
        // should work, untested
        this.tagBox.getInputElement()
            .parent()
            .parent()
            .setPadding("0");
        return this;
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

    // setCssText.......................................................................................................

    @Override
    public TagBoxComponent<T> setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.tagBox.cssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public TagBoxComponent<T> setCssProperty(final String name,
                                             final String value) {
        this.tagBox.setCssProperty(
            name,
            value
        );
        return this;
    }

    // removeCssProperty................................................................................................

    @Override
    public TagBoxComponent<T> removeCssProperty(final String name) {
        this.tagBox.removeCssProperty(name);
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.tagBox.element();
    }

    private final TagBoxComponentTagBox<T> tagBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.element()
            .toString();
    }
}
