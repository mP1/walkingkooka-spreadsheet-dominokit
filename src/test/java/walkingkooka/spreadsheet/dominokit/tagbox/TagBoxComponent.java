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

import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.forms.suggest.SuggestionsStore;
import org.dominokit.domino.ui.menu.MenuItem;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ValidatorHelper;
import walkingkooka.spreadsheet.dominokit.validator.SpreadsheetValidators;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.HasText;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A text box component that includes support for suggestions using a {@link SuggestionsStore}.
 */
public final class TagBoxComponent<T extends HasText> extends TagBoxComponentLike<T>
    implements TestHtmlElementComponent<HTMLFieldSetElement, TagBoxComponent<T>>,
    ValidatorHelper {

    public static <T extends HasText> TagBoxComponent<T> with(final TagBoxComponentSuggestionsProvider<T> suggestionsProvider,
                                                              final Function<T, MenuItem<T>> menuItemCreator) {
        Objects.requireNonNull(suggestionsProvider, "suggestionsProvider");
        Objects.requireNonNull(menuItemCreator, "menuItemCreator");

        return new TagBoxComponent<>();
    }

    private TagBoxComponent() {
        this.helperText = Optional.empty();

        this.value = Optional.empty();
        this.errors = Lists.empty();
        this.options = Lists.empty();

        this.validator = null;
        this.required();
        this.validate();
    }

    @Override
    public List<T> options() {
        return this.options;
    }

    @Override
    public TagBoxComponent<T> setOptions(final List<T> options) {
        Objects.requireNonNull(options, "options");

        this.options = Lists.immutable(options);
        return this;
    }

    private List<T> options;

    @Override
    public TagBoxComponent<T> setVerifiedOption(final T option) {
//        this.setValue(
//            Optional.of(option)
//        );
        return this;
    }

    // id...............................................................................................................

    @Override
    public TagBoxComponent<T> setId(final String id) {
        this.id = id;
        return this;
    }

    @Override
    public String id() {
        return this.id;
    }

    private String id;

    // label............................................................................................................

    @Override
    public TagBoxComponent<T> setLabel(final String label) {
        this.label = label;
        return this;
    }

    @Override
    public String label() {
        return this.label;
    }

    private String label;

    // helperText.......................................................................................................

    @Override
    public TagBoxComponent<T> alwaysShowHelperText() {
        return this;
    }

    @Override
    public TagBoxComponent<T> setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");

        this.helperText = text;
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return this.helperText;
    }

    private Optional<String> helperText;

    // StringValue......................................................................................................

    @Override
    public TagBoxComponent<T> setStringValue(final Optional<String> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> stringValue() {
        return this.value.map(
            c -> c.stream()
                .map(HasText::text)
                .collect(Collectors.joining())
        );
    }

    // Value............................................................................................................

    @Override
    public TagBoxComponent<T> setValue(final Optional<List<T>> value) {
        Objects.requireNonNull(value, "value");

        this.value = value;
        this.validate();
        return this;
    }

    @Override //
    public Optional<List<T>> value() {
        return this.value;
    }

    private Optional<List<T>> value;

    @Override
    public Runnable addValueWatcher(final ValueWatcher<List<T>> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        return () -> {
        };
    }

    // isDisabled.......................................................................................................

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public TagBoxComponent<T> setDisabled(final boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    private boolean disabled;

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
        this.validator = validator;
        return this.validate();
    }

    private Validator<Optional<List<T>>> validator;

    @Override
    public boolean isRequired() {
        return this.required;
    }

    boolean required;

    @Override
    public TagBoxComponent<T> validate() {
        this.setErrors(
            this.validateAndGetErrors(
                this.value,
                Optional.ofNullable(
                    this.validator
                )
            )
        );
        return this;
    }

    @Override
    public List<String> errors() {
        return this.errors;
    }

    @Override
    public TagBoxComponent<T> setErrors(final List<String> errors) {
        this.errors = Lists.immutable(errors);
        return this;
    }

    private List<String> errors;

    // focus............................................................................................................

    @Override
    public TagBoxComponent<T> focus() {
        return this;
    }

    // blur............................................................................................................

    @Override
    public TagBoxComponent<T> blur() {
        return this;
    }

    // styling..........................................................................................................

    @Override
    public TagBoxComponent<T> hideMarginBottom() {
        return this;
    }

    @Override
    public TagBoxComponent<T> removeBorders() {
        return this;
    }

    @Override
    public TagBoxComponent<T> removePadding() {
        return this;
    }
}
