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

package walkingkooka.spreadsheet.dominokit.meta;

import elemental2.dom.Element;
import elemental2.dom.Event;
import elemental2.dom.KeyboardEvent;
import jsinterop.base.Js;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.elements.UListElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.forms.IntegerBox;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.spreadsheet.dominokit.ComponentRefreshable;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Base class for a item that may be displayed within a {@link SpreadsheetMetadataPanelComponent}. It only
 * implements {@link ComponentRefreshable}, it is assumed it will only be refreshed when the parent panel is open and refreshed.
 */
abstract class SpreadsheetMetadataItemComponent<T> implements ComponentRefreshable, IsElement<Element> {

    /**
     * {@see SpreadsheetMetadataItemComponentEnum}
     */
    static <T extends Enum<T>> SpreadsheetMetadataItemComponentEnum<T> enumValue(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                                 final List<T> values,
                                                                                 final SpreadsheetMetadataPanelComponentContext context) {
        return SpreadsheetMetadataItemComponentEnum.with(
                propertyName,
                values,
                context
        );
    }

    /**
     * {@see SpreadsheetMetadataItemComponentDateTimeOffset}
     */
    static SpreadsheetMetadataItemComponentDateTimeOffset dateTimeOffset(final SpreadsheetMetadataPanelComponentContext context) {
        checkContext(context);

        return SpreadsheetMetadataItemComponentDateTimeOffset.with(
                context
        );
    }

    /**
     * {@see SpreadsheetMetadataItemComponentNumber}
     */
    static SpreadsheetMetadataItemComponentNumber number(final SpreadsheetMetadataPropertyName<Integer> propertyName,
                                                         final int min,
                                                         final int max,
                                                         final SpreadsheetMetadataPanelComponentContext context) {
        return SpreadsheetMetadataItemComponentNumber.with(
                propertyName,
                min,
                max,
                context
        );
    }

    /**
     * {@see SpreadsheetMetadataItemComponentPrecision}
     */
    static SpreadsheetMetadataItemComponentPrecision precision(final SpreadsheetMetadataPanelComponentContext context) {
        return SpreadsheetMetadataItemComponentPrecision.with(
                context
        );
    }

    /**
     * {@see SpreadsheetMetadataItemComponentReadOnlyText}
     */
    static <T> SpreadsheetMetadataItemComponent<T> readOnlyText(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                final Function<T, String> formatter,
                                                                final SpreadsheetMetadataPanelComponentContext context) {
        return SpreadsheetMetadataItemComponentReadOnlyText.with(
                propertyName,
                formatter,
                context
        );
    }

    /**
     * {@see SpreadsheetMetadataItemComponentReadOnlyText}
     */
    static <T extends SpreadsheetPattern> SpreadsheetMetadataItemComponentSpreadsheetPattern<T> spreadsheetPattern(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                                                                   final SpreadsheetPatternKind patternKind,
                                                                                                                   final SpreadsheetMetadataPanelComponentContext context) {
        return SpreadsheetMetadataItemComponentSpreadsheetPattern.with(
                propertyName,
                patternKind,
                context
        );
    }

    /**
     * {@see SpreadsheetMetadataItemComponentText}
     */
    static SpreadsheetMetadataItemComponentText text(final SpreadsheetMetadataPropertyName<String> propertyName,
                                                     final SpreadsheetMetadataPanelComponentContext context) {
        return SpreadsheetMetadataItemComponentText.with(
                propertyName,
                context
        );
    }

    // factory helpers..................................................................................................

    static void checkPropertyName(final SpreadsheetMetadataPropertyName<?> propertyName) {
        Objects.requireNonNull(propertyName, "propertyName");
    }

    static void checkContext(final SpreadsheetMetadataPanelComponentContext context) {
        Objects.requireNonNull(context, "context");
    }

    /**
     * Package private ctor to limit sub-classing.
     */
    SpreadsheetMetadataItemComponent(final SpreadsheetMetadataPropertyName<T> propertyName,
                                     final SpreadsheetMetadataPanelComponentContext context) {
        this.propertyName = propertyName;
        this.context = context;
    }

    // DOM factory methods..............................................................................................

    /**
     * Factory that creates an {@link IntegerBox} and fires save when the value changes or ENTER is typed.
     */
    final IntegerBox integerBox() {
        final IntegerBox integerBox = new IntegerBox() {
            @Override
            public String getType() {
                return "number";
            }
        };

        integerBox.addEventListener(
                EventType.change.getName(),
                (final Event event) -> this.saveIntegerValue(integerBox)
        ).addEventListener(
                EventType.keydown.getName(),
                (final Event event) -> {
                    event.preventDefault();

                    final KeyboardEvent keyboardEvent = Js.cast(event);
                    switch (Key.fromEvent(keyboardEvent)) {
                        case Enter:
                            this.saveIntegerValue(integerBox);
                            break;
                        default:
                            // ignore other keys
                            break;
                    }
                }
        );

        // clear the margin-bottom: 16px
        integerBox.element()
                .style
                .setProperty("margin-bottom", "0");

        return integerBox;
    }

    private void saveIntegerValue(final IntegerBox integerBox) {
        final String saveText;

        if (integerBox.isEmpty()) {
            saveText = "";
        } else {
            saveText = String.valueOf(integerBox.getValue());
        }

        this.save(saveText);
    }

    final TextBox textBox() {
        final TextBox textBox = new TextBox();

        textBox.addEventListener(
                EventType.change,
                (final Event event) -> this.saveText(textBox)
        ).addEventListener(
                EventType.keydown,
                (final Event event) -> {
                    event.preventDefault();

                    final KeyboardEvent keyboardEvent = Js.cast(event);
                    switch (Key.fromEvent(keyboardEvent)) {
                        case Enter:
                            this.saveText(textBox);
                            break;
                        default:
                            // ignore other keys
                            break;
                    }
                }
        );

        // clear the margin-bottom: 16px
        textBox.element()
                .style
                .setProperty("margin-bottom", "0");
        return textBox;
    }

    private void saveText(final TextBox textBox) {
        final String saveText;

        if (textBox.isEmpty()) {
            saveText = "";
        } else {
            saveText = String.valueOf(textBox.getValue());
        }

        this.save(saveText);
    }

    final void save(final String saveText) {
        final SpreadsheetMetadataPropertyName<?> propertyName = this.propertyName;
        final SpreadsheetMetadataPanelComponentContext context = this.context;
        context.debug(this.getClass().getSimpleName() + ".save " + propertyName + "=" + CharSequences.quoteAndEscape(saveText));

        context.save(
                propertyName,
                saveText
        );
    }

    final UListElement uListElement() {
        final UListElement element = ElementsFactory.elements.ul();
        element.style().setPaddingLeft("0");
        return element;
    }

    // properties......................................................................................................

    final SpreadsheetMetadataPropertyName<T> propertyName;

    /**
     * The parent {@link SpreadsheetMetadataPanelComponentContext} this will be used primarily to save updated values.
     */
    final SpreadsheetMetadataPanelComponentContext context;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.propertyName.toString();
    }
}
