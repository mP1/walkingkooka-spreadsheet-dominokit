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

package walkingkooka.spreadsheet.dominokit.slider;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A mock of main/TextBoxComponent with the same public interface and a helpful {@link TreePrintable}. This will be useful for unit tests to verify the rough apperance of a component that includes
 * {@link SliderComponent}.
 */
public final class SliderComponent extends SliderComponentLike
    implements TestHtmlElementComponent<HTMLDivElement, SliderComponent> {

    public static SliderComponent horizontal(final double min,
                                             final double max) {
        return new SliderComponent(
            min,
            max,
            false // horizontal
        );
    }

    public static SliderComponent vertical(final double min,
                                           final double max) {
        return new SliderComponent(
            min,
            max,
            true // vertical
        ).setVertical();
    }

    private SliderComponent(final double minValue,
                            final double maxValue,
                            final boolean vertical) {
        super();

        this.setMinValue(minValue)
            .setMaxValue(maxValue);

        this.vertical = vertical;
    }

    @Override
    boolean isVertical() {
        return this.vertical;
    }

    /**
     * In JDK tests this will be used to track whether this slider is horizontal(false) or vertical(true)
     */
    final boolean vertical;

    @Override
    public SliderComponent setId(final String id) {
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
    public SliderComponent setLabel(final String label) {
        this.label = label;
        return this;
    }

    @Override
    public String label() {
        return this.label;
    }

    private String label;

    @Override
    public SliderComponent setValue(final Optional<Double> value) {
        Objects.requireNonNull(value, "value");

        this.value = value.isEmpty() ?
            Optional.of(0.0) :
            value;
        return this;
    }

    @Override
    public Optional<Double> value() {
        return this.value;
    }

    private Optional<Double> value = Optional.empty();

    // minValue.........................................................................................................
    
    @Override
    public double minValue() {
        return this.minValue;
    }
    
    @Override
    public SliderComponent setMinValue(final double minValue) {
        this.minValue = minValue;
        return this;
    }
    
    private double minValue;

    // maxValue.........................................................................................................

    @Override
    public double maxValue() {
        return this.maxValue;
    }

    @Override
    public SliderComponent setMaxValue(final double maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    private double maxValue;

    // step.............................................................................................................

    @Override
    public double step() {
        return this.step;
    }

    @Override
    public SliderComponent setStep(final double step) {
        this.step = step;
        return this;
    }

    private double step;

    // misc.............................................................................................................
    
    @Override
    public SliderComponent focus() {
        return this;
    }

    @Override
    public SliderComponent optional() {
        this.required = false;
        return this;
    }

    @Override
    public SliderComponent required() {
        this.required = true;
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    private boolean required;

    @Override
    public SliderComponent validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> errors() {
        return this.errors;
    }

    @Override
    public SliderComponent setErrors(final List<String> errors) {
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
    public SliderComponent setDisabled(final boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    private boolean disabled;

    @Override
    public SliderComponent addChangeListener(final ChangeListener<Optional<Double>> listener) {
        return this;
    }

    @Override
    public SliderComponent addClickListener(final EventListener listener) {
        return this;
    }

    @Override
    public SliderComponent addContextMenuListener(final EventListener listener) {
        return this;
    }

    @Override
    public SliderComponent addFocusListener(final EventListener listener) {
        return this;
    }

    @Override
    public SliderComponent addKeyDownListener(final EventListener listener) {
        return this;
    }

    @Override
    public SliderComponent addKeyUpListener(final EventListener listener) {
        return this;
    }

    @Override
    public SliderComponent alwaysShowHelperText() {
        return this;
    }

    @Override
    public SliderComponent setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");
        this.helperText = text;
        return this;
    }

    @Override public Optional<String> helperText() {
        return this.helperText;
    }

    private Optional<String> helperText = Optional.empty();

    @Override
    public SliderComponent hideMarginBottom() {
        return this;
    }

    @Override
    public SliderComponent removeBorders() {
        return this;
    }

    // FIXES
    //
    // java.lang.NoSuchMethodError: walkingkooka.spreadsheet.dominokit.value.TextBoxComponent.setCssText(Ljava/lang/String;)Lwalkingkooka/spreadsheet/dominokit/ui/textbox/TextBoxComponent;
    @Override
    public SliderComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SliderComponent setCssProperty(final String name,
                                          final String value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");
        return this;
    }

    @Override
    public boolean isEditing() {
        return false;
    }
}
