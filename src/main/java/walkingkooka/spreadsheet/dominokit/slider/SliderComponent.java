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

import elemental2.dom.Element;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.sliders.Slider;
import org.dominokit.domino.ui.utils.DominoElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.dominokit.domino.ui.utils.ElementsFactory.elements;

/**
 * A slider which accept min/max double values.
 */
public final class SliderComponent extends SliderComponentLike {

    public static SliderComponent empty(final double min,
                                        final double max) {
        return new SliderComponent(min, max);
    }

    private SliderComponent(final double min,
                            final double max) {
        this.slider = Slider.create(
            min,
            max
        ).show();
    }

    @Override
    public SliderComponent setId(final String id) {
        CharSequences.failIfNullOrEmpty(id, "id");

        this.slider.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.slider.getId();
    }

    @Override
    public SliderComponent setLabel(final String label) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String label() {
        return "";
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.slider.element();
    }

    // Value............................................................................................................

    @Override
    public SliderComponent setValue(final Optional<Double> value) {
        Objects.requireNonNull(value, "value");

        this.slider.setValue(
            value.orElse(Double.valueOf(0)),
            true // silent dont fire change listeners.
        );
        return this;
    }

    @Override //
    public Optional<Double> value() {
        return Optional.of(
            this.slider.getValue()
        );
    }

    // MinValue.........................................................................................................

    @Override
    public SliderComponent setMinValue(final double minValue) {
        Objects.requireNonNull(minValue, "minValue");

        this.slider.setMinValue(minValue);
        return this;
    }

    @Override
    public double minValue() {
        return this.slider.getMin();
    }

    // MaxValue.........................................................................................................

    @Override
    public SliderComponent setMaxValue(final double maxValue) {
        Objects.requireNonNull(maxValue, "maxValue");

        this.slider.setMaxValue(maxValue);
        return this;
    }

    @Override
    public double maxValue() {
        return this.slider.getMin();
    }

    // Step.............................................................................................................

    @Override
    public SliderComponent setStep(final double step) {
        Objects.requireNonNull(step, "step");

        this.slider.setStep(step);
        return this;
    }

    @Override
    public double step() {
        return this.slider.getMin();
    }
    
    // ValueComponent...................................................................................................

    @Override
    public boolean isDisabled() {
        return this.slider.isDisabled();
    }

    @Override
    public SliderComponent setDisabled(final boolean disabled) {
        this.slider.setDisabled(disabled);
        return this;
    }

    @Override
    public SliderComponent required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SliderComponent optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public SliderComponent validate() {
        return this;
    }

    @Override
    public List<String> errors() {
        return Lists.empty();
    }

    @Override
    public SliderComponent setErrors(final List<String> errors) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SliderComponent addChangeListener(final ChangeListener<Optional<Double>> listener) {
        Objects.requireNonNull(listener, "listener");

        this.slider.addChangeListener(
            (Double oldValue, Double newValue) -> listener.onValueChanged(
                Optional.of(oldValue),
                Optional.of(newValue)
            )
        );
        return this;
    }

    @Override
    public SliderComponent addClickListener(final EventListener listener) {
        return this.addEventListener(
            EventType.click,
            listener
        );
    }

    @Override
    public SliderComponent addContextMenuListener(final EventListener listener) {
        return this.addEventListener(
            EventType.contextmenu,
            listener
        );
    }

    @Override
    public SliderComponent addFocusListener(final EventListener listener) {
        return this.addEventListener(
            EventType.focus,
            listener
        );
    }

    @Override
    public SliderComponent addKeydownListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keydown,
            listener
        );
    }

    @Override
    public SliderComponent addKeyUpListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keyup,
            listener
        );
    }

    private SliderComponent addEventListener(final EventType eventType,
                                             final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        this.slider.addEventListener(
            eventType,
            listener
        );
        return this;
    }

    @Override
    public SliderComponent focus() {
        return this;
    }

    @Override
    public SliderComponent alwaysShowHelperText() {
        final DominoElement<Element> element = elements.elementOf(
            this.slider.element()
                .firstElementChild
        );
        element.setHeight(HELPER_TEXT_HEIGHT);
        return this;
    }

    @Override
    public SliderComponent setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");

        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> helperText() {
        return Optional.empty();
    }

    @Override
    public SliderComponent hideMarginBottom() {
        this.slider.setMarginBottom("0");
        return this;
    }

    @Override
    public SliderComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    // setCssText.......................................................................................................

    @Override
    public SliderComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.slider.cssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SliderComponent setCssProperty(final String name,
                                          final String value) {
        this.slider.setCssProperty(
            name,
            value
        );
        return this;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return HtmlComponent.hasFocus(this.slider.element());
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.slider.toString();
    }

    private final Slider slider;
}
