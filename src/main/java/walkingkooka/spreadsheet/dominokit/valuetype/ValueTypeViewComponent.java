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

package walkingkooka.spreadsheet.dominokit.valuetype;

import com.google.gwt.core.client.GWT;
import elemental2.dom.HTMLElement;
import walkingkooka.color.Color;
import walkingkooka.color.WebColorName;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.label.LabelComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.validation.ValueType;

import java.util.Objects;
import java.util.Optional;

public final class ValueTypeViewComponent implements ValueComponent<HTMLElement, ValueType, ValueTypeViewComponent>,
    HtmlComponentDelegator<HTMLElement, ValueTypeViewComponent> {

    public static ValueTypeViewComponent empty() {
        return new ValueTypeViewComponent();
    }

    private ValueTypeViewComponent() {
        super();
        this.label = LabelComponent.empty();
    }

    @Override
    public ValueTypeViewComponent setValue(final Optional<ValueType> value) {
        Objects.requireNonNull(value, "value");

        this.label.setValue(value.map(ValueType::value));

        final ValueType valueType = value.orElse(null);
        if (null != valueType) {
            final Color color;

            if (valueType.isDate() || valueType.isDateTime() || valueType.isTime()) {
                color = WebColorName.CHOCOLATE.color();
            } else {
                if (valueType.isNumber()) {
                    color = WebColorName.LIMEGREEN.color();
                } else {
                    if (valueType.isError()) {
                        color = WebColorName.DEEPPINK.color();
                    } else {
                        if (valueType.isText()) {
                            color = WebColorName.PURPLE.color();
                        } else {
                            if (SpreadsheetValueType.isColor(valueType)) {
                                color = WebColorName.OLIVE.color();
                            } else {
                                if (SpreadsheetValueType.isReference(valueType)) {
                                    color = WebColorName.ORANGE.color();
                                } else {
                                    if (valueType.isJson() ) {
                                        color = WebColorName.SANDYBROWN.color();
                                    } else {
                                        if (valueType.isEmail() || valueType.isUrl()) {
                                            color = WebColorName.AZURE.color();
                                        } else {
                                            if (valueType.isList()) {
                                                color = WebColorName.DARKSALMON.color();
                                            } else {
                                                color = null;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (null != color) {
                if(GWT.isScript()) {
                    this.label.setCssProperty(
                        TextStylePropertyName.BACKGROUND_COLOR.value(),
                        color.text()
                    );
                }
            }
        }

        return this;
    }

    @Override
    public Optional<ValueType> value() {
        return this.label.value()
            .map(ValueType::with);
    }

    @Override
    public boolean isEditing() {
        return false;
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<ValueType> watcher) {
        throw new UnsupportedOperationException();
    }

    // HtmlElementComponentDelegator....................................................................................

    @Override
    public HtmlComponent<HTMLElement, ?> htmlComponent() {
        return this.label;
    }

    private final LabelComponent label;

    @Override
    public boolean isDisabled() {
        return true;
    }

    @Override
    public ValueTypeViewComponent setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValueTypeViewComponent hideMarginBottom() {
        return this;
    }

    @Override
    public ValueTypeViewComponent removeBorders() {
        return this;
    }

    @Override
    public ValueTypeViewComponent removePadding() {
        return this;
    }

    @Override
    public ValueTypeViewComponent focus() {
        return this;
    }

    @Override
    public ValueTypeViewComponent blur() {
        return this;
    }

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.label.printTree(printer);
        }
        printer.outdent();
    }
}
