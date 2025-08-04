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

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SliderComponentTest implements FormValueComponentTesting<HTMLDivElement, Double, SliderComponent> {

    // setId............................................................................................................

    @Test
    public void testSetIdWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createComponent()
                .setId(null)
        );
    }

    @Test
    public void testSetIdWithEmptyFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createComponent()
                .setId("")
        );
    }

    // setDisabled......................................................................................................

    @Test
    public void testSetDisabledFalse() {
        final SliderComponent slider = this.createComponent();
        slider.setDisabled(false);
        this.isDisabledAndCheck(
            slider,
            false
        );
    }

    @Test
    public void testSetDisabledTrue() {
        final SliderComponent slider = this.createComponent();
        slider.setDisabled(true);
        this.isDisabledAndCheck(
            slider,
            true
        );
    }

    // clearValue.......................................................................................................

    @Test
    public void testClearValue() {
        final SliderComponent slider = this.createComponent()
            .setValue(
                Optional.of(123.0)
            ).clearValue();
        this.valueAndCheck(
            slider,
            Optional.of(0.0)
        );
    }

    // setValue.........................................................................................................

    @Test
    public void testSetValue() {
        final double value = 123;

        final SliderComponent slider = this.createComponent()
            .setValue(
                Optional.of(value)
            );
        this.valueAndCheck(
            slider,
            Optional.of(value)
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testSetMinValueSetMaxValueSetStepAndTreePrint() {
        final Double value = 123.0;

        final SliderComponent slider = this.createComponent()
            .setValue(
                Optional.of(value)
            ).setMinValue(100.0)
            .setMaxValue(200.0)
            .setStep(5.0);
        this.treePrintAndCheck(
            slider,
            "SliderComponent\n" +
                "  [123.0] min=100.0 max=200.0\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SliderComponent createComponent() {
        return SliderComponent.empty(
            100.0, // minValue
            200.0 // maxValue
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SliderComponent> type() {
        return SliderComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
