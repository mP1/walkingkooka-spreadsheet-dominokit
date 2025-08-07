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
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.stream.Collectors;

abstract class SliderComponentLike implements FormValueComponent<HTMLDivElement, Double, SliderComponent>,
    TreePrintable {

    SliderComponentLike() {
        super();
    }

    final SliderComponent setVertical() {
        return this.setCssProperty(
            "writing-mode",
            "vertical"
        );
    }

    abstract boolean isVertical();

    // MinValue.........................................................................................................

    public abstract SliderComponent setMinValue(final double minValue);

    public abstract double minValue();

    // MaxValue.........................................................................................................

    public abstract SliderComponent setMaxValue(final double maxValue);

    public abstract double maxValue();

    // Step.............................................................................................................

    public abstract SliderComponent setStep(final double step);

    public abstract double step();

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            printer.println(
                this.isVertical() ?
                    "Vertical" :
                    "Horizontal"
            );
            printer.indent();
            {

                final List<String> components = Lists.array();

                final String label = this.label();
                if (null != label) {
                    components.add(label);
                }

                components.add(
                    "[" +
                        this.value()
                            .map(Object::toString)
                            .orElse("") +
                        "]"
                );

                components.add(
                    "min=" + this.minValue()
                );

                components.add(
                    "max=" + this.maxValue()
                );

                final String id = this.id();
                if (null != id) {
                    components.add("id=" + id);
                }

                if (this.isDisabled()) {
                    components.add("DISABLED");
                }

                if (this.isRequired()) {
                    components.add("REQUIRED");
                }

                printer.println(
                    components.stream()
                        .collect(Collectors.joining(" "))
                );
            }
            printer.outdent();
        }
        printer.outdent();
    }
}
