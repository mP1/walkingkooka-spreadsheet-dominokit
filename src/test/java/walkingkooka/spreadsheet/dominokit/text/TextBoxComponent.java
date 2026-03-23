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

package walkingkooka.spreadsheet.dominokit.text;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ValidatorHelper;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.ValueWatchers;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A mock of main/TextBoxComponent with the same public interface and a helpful {@link TreePrintable}. This will be useful for unit tests to verify the rough apperance of a component that includes
 * {@link TextBoxComponent}.
 */
public final class TextBoxComponent extends TextBoxComponentLike
    implements TestHtmlElementComponent<HTMLFieldSetElement, TextBoxComponent>,
    ValidatorHelper {

    public static TextBoxComponent empty() {
        return new TextBoxComponent();
    }

    private TextBoxComponent() {
        super();
        this.required();
    }

    @Override
    public TextBoxComponent setId(final String id) {
        CharSequences.failIfNullOrEmpty(id, "id");

        this.id = id;
        return this;
    }

    @Override
    public String id() {
        return this.id;
    }

    private String id;

    @Override
    public TextBoxComponent setLabel(final String label) {
        this.label = label;
        return this;
    }

    @Override
    public String label() {
        return this.label;
    }

    private String label;

    @Override
    public TextBoxComponent setValue(final Optional<String> value) {
        Objects.requireNonNull(value, "value");

        final Optional<String> oldValue = this.value;
        final Optional<String> newValue = EMPTY_STRING.equals(value) ?
            Optional.empty() :
            value;
        this.value = newValue;

        // only if value changed
        if(false == oldValue.equals(newValue)) {
            this.validate();

            // watchers can also test if validation reported any errors
            this.watchers.onValue(newValue);
        }

        return this;
    }

    private final static Optional<String> EMPTY_STRING = Optional.of("");

    @Override
    public Optional<String> value() {
        return this.value;
    }

    private Optional<String> value = Optional.empty();

    @Override
    public TextBoxComponent focus() {
        return this;
    }

    @Override
    public TextBoxComponent blur() {
        return this;
    }

    @Override
    public TextBoxComponent setValidator(final Optional<Validator<Optional<String>>> validator) {
        final boolean shouldValidate = false == this.validator.equals(validator);
        this.validator = validator;

        if (shouldValidate) {
            this.validate();
        }

        return this;
    }

    @Override
    public TextBoxComponent validate() {
        final Optional<String> value = this.value();
        if(false == this.required && value.isEmpty()) {
            this.clearErrors();
        } else {
            this.setErrors(
                this.validateAndGetErrors(
                    value,
                    this.validator
                )
            );
        }
        return this;
    }

    @Override
    public List<String> errors() {
        return this.errors;
    }

    @Override
    public TextBoxComponent setErrors(final List<String> errors) {
        Objects.requireNonNull(errors, "errors");
        this.errors = Lists.immutable(errors);
        return this;
    }

    private List<String> errors = Lists.empty();

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public TextBoxComponent setDisabled(final boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    private boolean disabled;

    @Override
    public TextBoxComponent addChangeListener(final ChangeListener<Optional<String>> listener) {
        return this;
    }

    @Override
    TextBoxComponent addEventListener(final EventType eventType,
                                      final EventListener listener) {
        Objects.requireNonNull(listener, "listener");
        return this;
    }

    @Override
    public TextBoxComponent alwaysShowHelperText() {
        return this;
    }

    @Override
    public TextBoxComponent setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");
        this.helperText = text;
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return this.helperText;
    }

    private Optional<String> helperText = Optional.empty();

    @Override
    public TextBoxComponent hideMarginBottom() {
        return this;
    }

    @Override
    public TextBoxComponent removeBorders() {
        return this;
    }

    @Override
    public TextBoxComponent removePadding() {
        return this;
    }

    @Override
    public TextBoxComponent autocompleteOff() {
        return this;
    }

    @Override
    public TextBoxComponent clearIcon() {
        return this;
    }

    @Override
    public TextBoxComponent disableSpellcheck() {
        return this;
    }

    @Override
    public TextBoxComponent enterFiresValueChange() {
        return this;
    }

    @Override
    public TextBoxComponent magnifyingGlassIcon() {
        return this;
    }

    // FIXES
    //
    // java.lang.NoSuchMethodError: walkingkooka.spreadsheet.dominokit.value.TextBoxComponent.setCssText(Ljava/lang/String;)Lwalkingkooka/spreadsheet/dominokit/ui/textbox/TextBoxComponent;
    @Override
    public TextBoxComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");
        return this;
    }

    @Override
    public TextBoxComponent setCssProperty(final String name,
                                           final String value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");
        return this;
    }

    @Override
    public TextBoxComponent removeCssProperty(final String name) {
        Objects.requireNonNull(name, "name");
        return this;
    }

    @Override
    public boolean isEditing() {
        return false;
    }

    // HasValueWatchers.................................................................................................

    @Override
    public Runnable addValueWatcher(final ValueWatcher<String> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        return this.watchers.add(watcher);
    }

    private final ValueWatchers<String> watchers = ValueWatchers.empty();

    // FormValueComponentTreePrintable..................................................................................

    @Override
    public void treePrintAlternateValues(final IndentingPrinter printer) {
        // NOP
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        final ToStringBuilder b = ToStringBuilder.empty()
            .valueSeparator(" ")
            .disable(ToStringBuilderOption.QUOTE)
            .labelSeparator(" ");

        final String label = this.label();
        if (null != label) {
            b.label(label);
        }

        b.value(
            "[" +
                this.value()
                    .map(Object::toString)
                    .orElse("") +
                "]"
        );

        b.labelSeparator("=");
        b.label("id");
        b.value(
            this.id()
        );

        b.enable(ToStringBuilderOption.QUOTE);
        b.label("helperText");
        b.value(
            this.helperText()
        );

        b.disable(ToStringBuilderOption.QUOTE);
        if (this.isDisabled()) {
            b.value("DISABLED");
        }

        if (this.isRequired()) {
            b.value("REQUIRED");
        }

        b.enable(ToStringBuilderOption.QUOTE)
                .valueSeparator(", ");
        b.label("Errors");
        b.value(this.errors);

        return b.build();
    }
}
