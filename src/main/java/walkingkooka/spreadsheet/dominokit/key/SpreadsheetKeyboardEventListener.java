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

package walkingkooka.spreadsheet.dominokit.key;

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.KeyboardEvent;
import walkingkooka.collect.map.Maps;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.Logging;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextDecorationLine;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.TextTransform;
import walkingkooka.tree.text.VerticalAlign;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;


/**
 * Maps well known Excel keyboard shortcuts
 * <pre>https://support.microsoft.com/en-au/office/keyboard-shortcuts-in-excel-1798d9d5-842a-42b8-9c99-9b7213f0040f</pre>
 */
public class SpreadsheetKeyboardEventListener implements EventListener,
    Logging {

    public static SpreadsheetKeyboardEventListener with(final SpreadsheetKeyBinding bindings,
                                                        final SpreadsheetKeyboardContext context) {
        return new SpreadsheetKeyboardEventListener(
            Objects.requireNonNull(bindings, "bindings"),
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetKeyboardEventListener(final SpreadsheetKeyBinding bindings,
                                             final SpreadsheetKeyboardContext context) {
        this.bindingToKeyboardEventHandler = Maps.sorted();
        this.context = context;

        this.registerBindings(
            bindings.bold(),
            this::bold
        );

        this.registerBindings(
            bindings.bottomVerticalAlign(),
            this::bottomVerticalAlign
        );

        this.registerBindings(
            bindings.capitalize(),
            this::capitalize
        );

        this.registerBindings(
            bindings.centerTextAlign(),
            this::centerTextAlign
        );

        this.registerBindings(
            bindings.currencyFormat(),
            this::currencyFormat
        );

        this.registerBindings(
            bindings.dateFormat(),
            this::dateFormat
        );

        this.registerBindings(
            bindings.generalFormat(),
            this::generalFormat
        );

        this.registerBindings(
            bindings.italics(),
            this::italics
        );

        this.registerBindings(
            bindings.justifyTextAlign(),
            this::justifyTextAlign
        );

        this.registerBindings(
            bindings.leftTextAlign(),
            this::leftTextAlign
        );

        this.registerBindings(
            bindings.lowerCase(),
            this::lowerCase
        );

        this.registerBindings(
            bindings.middleVerticalAlign(),
            this::middleVerticalAlign
        );

        this.registerBindings(
            bindings.normalText(),
            this::normalText
        );

        this.registerBindings(
            bindings.numberFormat(),
            this::numberFormat
        );

        this.registerBindings(
            bindings.percentFormat(),
            this::percentFormat
        );

        this.registerBindings(
            bindings.rightTextAlign(),
            this::rightTextAlign
        );

        this.registerBindings(
            bindings.scientificFormat(),
            this::scientificFormat
        );

        this.registerBindings(
            bindings.selectAll(),
            this::selectAll
        );

        this.registerBindings(
            bindings.strikethru(),
            this::strikethru
        );

        this.registerBindings(
            bindings.textFormat(),
            this::textFormat
        );

        this.registerBindings(
            bindings.timeFormat(),
            this::timeFormat
        );

        this.registerBindings(
            bindings.topVerticalAlign(),
            this::topVerticalAlign
        );

        this.registerBindings(
            bindings.underline(),
            this::underline
        );

        this.registerBindings(
            bindings.upperCase(),
            this::upperCase
        );
    }

    private void registerBindings(final Collection<KeyBinding> bindings,
                                  final Consumer<KeyboardEvent> handler) {
        for(KeyBinding binding : bindings) {
            this.bindingToKeyboardEventHandler.put(
                binding,
                handler
            );
        }
    }

    @Override
    public void handleEvent(final Event event) {
        this.handleKeyEvent(
            (KeyboardEvent) event
        );
    }

    private void handleKeyEvent(final KeyboardEvent event) {
        KeyBinding binding = KeyBinding.with(event.key);

        if(event.altKey) {
            binding = binding.setAlt();
        }
        if(event.ctrlKey) {
            binding = binding.setControl();
        }
        if(event.metaKey) {
            binding = binding.setMeta();
        }
        if(event.shiftKey) {
            binding = binding.setShift();
        }

        if (SPREADSHEET_KEYBOARD_EVENT_LISTENER) {
            this.context.debug(this.getClass().getSimpleName() + " handleKeyEvent " + binding);
        }

        final Consumer<KeyboardEvent> handler = this.bindingToKeyboardEventHandler.get(binding);
        if(null != handler) {
            handler.accept(event);
        }
    }

    private final Map<KeyBinding, Consumer<KeyboardEvent>> bindingToKeyboardEventHandler;

    private void bold(final KeyboardEvent event) {
        this.flipCellStyle(
            TextStylePropertyName.FONT_WEIGHT,
            FontWeight.BOLD
        );
        event.preventDefault();
    }

    private void bottomVerticalAlign(final KeyboardEvent event) {
        this.setCellStyle(
            TextStylePropertyName.VERTICAL_ALIGN,
            VerticalAlign.BOTTOM
        );
        event.preventDefault();
    }

    private void capitalize(final KeyboardEvent event) {
        this.setCellStyle(
            TextStylePropertyName.TEXT_TRANSFORM,
            TextTransform.CAPITALIZE
        );
        event.preventDefault();
    }

    private void centerTextAlign(final KeyboardEvent event) {
        this.setCellStyle(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        );
        event.preventDefault();
    }

    private void currencyFormat(final KeyboardEvent event) {
        this.setCellFormatter(
            Optional.of(
                SpreadsheetPattern.parseNumberFormatPattern("$0.00")
                    .spreadsheetFormatterSelector()
            )
        );
        event.preventDefault();
    }

    private void dateFormat(final KeyboardEvent event) {
        this.setCellFormatter(
            Optional.of(
                SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
                    .spreadsheetFormatterSelector()
            )
        );
        event.preventDefault();
    }

    private void generalFormat(final KeyboardEvent event) {
        this.setCellFormatter(
            Optional.of(
                SpreadsheetPattern.parseNumberFormatPattern("general")
                    .spreadsheetFormatterSelector()
            )
        );
        event.preventDefault();
    }

    private void italics(final KeyboardEvent event) {
        this.flipCellStyle(
            TextStylePropertyName.FONT_STYLE,
            FontStyle.ITALIC
        );
        event.preventDefault();
    }

    private void justifyTextAlign(final KeyboardEvent event) {
        this.setCellStyle(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.JUSTIFY
        );
        event.preventDefault();
    }

    private void leftTextAlign(final KeyboardEvent event) {
        this.setCellStyle(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT
        );
        event.preventDefault();
    }

    private void lowerCase(final KeyboardEvent event) {
        this.setCellStyle(
            TextStylePropertyName.TEXT_TRANSFORM,
            TextTransform.LOWERCASE
        );
        event.preventDefault();
    }

    private void middleVerticalAlign(final KeyboardEvent event) {
        this.setCellStyle(
            TextStylePropertyName.VERTICAL_ALIGN,
            VerticalAlign.MIDDLE
        );
        event.preventDefault();
    }

    private void normalText(final KeyboardEvent event) {
        this.setCellStyle(
            TextStylePropertyName.TEXT_TRANSFORM,
            TextTransform.NONE
        );
        event.preventDefault();
    }

    private void numberFormat(final KeyboardEvent event) {
        this.setCellFormatter(
            Optional.of(
                SpreadsheetPattern.parseNumberFormatPattern("0.00")
                    .spreadsheetFormatterSelector()
            )
        );
        event.preventDefault();
    }

    private void percentFormat(final KeyboardEvent event) {
        this.setCellFormatter(
            Optional.of(
                SpreadsheetPattern.parseNumberFormatPattern("0%")
                    .spreadsheetFormatterSelector()
            )
        );
        event.preventDefault();
    }

    private void rightTextAlign(final KeyboardEvent event) {
        this.setCellStyle(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.RIGHT
        );
        event.preventDefault();
    }

    private void scientificFormat(final KeyboardEvent event) {
        this.setCellFormatter(
            Optional.of(
                SpreadsheetPattern.parseNumberFormatPattern("0.00E+00")
                    .spreadsheetFormatterSelector()
            )
        );
        event.preventDefault();
    }

    private void selectAll(final KeyboardEvent event) {
        final SpreadsheetKeyboardContext context = this.context;

        final HistoryToken historyToken = context.historyToken()
            .setSelection(
                Optional.of(SpreadsheetSelection.ALL_CELLS)
            ).clearAction();
        if (historyToken instanceof SpreadsheetCellHistoryToken) {
            context.pushHistoryToken(historyToken);
        }

        event.preventDefault();
    }

    private void strikethru(final KeyboardEvent event) {
        this.flipCellStyle(
            TextStylePropertyName.TEXT_DECORATION_LINE,
            TextDecorationLine.LINE_THROUGH
        );
        event.preventDefault();
    }

    private void textFormat(final KeyboardEvent event) {
        this.setCellFormatter(
            Optional.of(
                SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN.spreadsheetFormatterSelector()
            )
        );
        event.preventDefault();
    }

    private void timeFormat(final KeyboardEvent event) {
        this.setCellFormatter(
            Optional.of(
                SpreadsheetPattern.parseTimeFormatPattern("hh:mm AM/PM")
                    .spreadsheetFormatterSelector()
            )
        );
        event.preventDefault();
    }

    private void topVerticalAlign(final KeyboardEvent event) {
        this.setCellStyle(
            TextStylePropertyName.VERTICAL_ALIGN,
            VerticalAlign.TOP
        );
        event.preventDefault();
    }

    private void underline(final KeyboardEvent event) {
        this.flipCellStyle(
            TextStylePropertyName.TEXT_DECORATION_LINE,
            TextDecorationLine.UNDERLINE
        );
        event.preventDefault();
    }

    private void upperCase(final KeyboardEvent event) {
        this.setCellStyle(
            TextStylePropertyName.TEXT_TRANSFORM,
            TextTransform.UPPERCASE
        );
        event.preventDefault();
    }

    /**
     * Unconditionally sets a {@link SpreadsheetFormatterSelector} belonging to a {@link SpreadsheetCell}.
     */
    private <T> void setCellFormatter(final Optional<SpreadsheetFormatterSelector> formatter) {
        final SpreadsheetKeyboardContext context = this.context;

        context.pushHistoryToken(
            context.historyToken()
                .formatter()
                .setSaveValue(formatter)
        );
    }

    /**
     * Unconditionally sets a {@link TextStyle} property.
     */
    private <T> void setCellStyle(final TextStylePropertyName<T> name,
                                  final T value) {
        final SpreadsheetKeyboardContext context = this.context;

        if (context.historyToken() instanceof SpreadsheetCellHistoryToken) {
            context.pushHistoryToken(
                context.historyToken()
                    .setStylePropertyName(
                        name
                    ).setSaveValue(
                        Optional.of(value)
                    )
            );
        }
    }

    /**
     * Helper that may be used to FLIP a {@link SpreadsheetCell} {@link TextStyle}, such as BOLD.
     */
    private <T> void flipCellStyle(final TextStylePropertyName<T> name,
                                   final T value) {
        final SpreadsheetKeyboardContext context = this.context;

        if(context.historyToken() instanceof SpreadsheetCellHistoryToken) {
            final SpreadsheetCell cell = context.historyTokenCell()
                .orElse(null);

            T previous = null;

            if (null != cell) {
                final TextStyle style = cell.textStyle();
                previous = style.get(name)
                    .orElse(null);
            }

            context.pushHistoryToken(
                context.historyToken()
                    .setStylePropertyName(
                        name
                    ).setSaveValue(
                        Optional.ofNullable(
                            Objects.equals(
                                previous,
                                value
                            ) ?
                                null :
                                value
                        )
                    )
            );
        }
    }

    private final SpreadsheetKeyboardContext context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
