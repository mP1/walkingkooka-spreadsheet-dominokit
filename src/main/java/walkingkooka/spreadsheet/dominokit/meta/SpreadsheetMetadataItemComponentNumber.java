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

import elemental2.dom.Event;
import elemental2.dom.HTMLFieldSetElement;
import elemental2.dom.KeyboardEvent;
import jsinterop.base.Js;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.forms.IntegerBox;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CharSequences;

import java.util.Optional;

/**
 * A {@link SpreadsheetMetadataItemComponent} that displays a number text box.
 */
final class SpreadsheetMetadataItemComponentNumber extends SpreadsheetMetadataItemComponent<Integer> {

    static SpreadsheetMetadataItemComponentNumber with(final SpreadsheetMetadataPropertyName<Integer> propertyName,
                                                       final int min,
                                                       final int max,
                                                       final SpreadsheetMetadataPanelComponentContext context) {
        checkPropertyName(propertyName);
        checkContext(context);

        return new SpreadsheetMetadataItemComponentNumber(
                propertyName,
                min,
                max,
                context
        );
    }

    private SpreadsheetMetadataItemComponentNumber(final SpreadsheetMetadataPropertyName<Integer> propertyName,
                                                   final int min,
                                                   final int max,
                                                   final SpreadsheetMetadataPanelComponentContext context) {
        super(
                propertyName,
                context
        );

        // https://github.com/DominoKit/domino-ui/issues/825
        final IntegerBox integerBox = new IntegerBox() {
            @Override
            public String getType() {
                return "number";
            }
        }.setMinValue(min)
                .setMaxValue(max)
                .setStep(1)
                .addEventListener(
                        EventType.change,
                        this::onChange
                ).addEventListener(
                        EventType.keydown,
                        (event) -> onKeyDownEvent(
                                Js.cast(event)
                        )
                );

        // clear the margin-bottom: 16px
        integerBox.element()
                .style
                .setProperty("margin-bottom", "0");
        this.integerBox = integerBox;
    }

    private void onChange(final Event event) {
        this.save();
    }

    private void onKeyDownEvent(final KeyboardEvent event) {
        final SpreadsheetMetadataPanelComponentContext context = this.context;

        switch (Key.fromEvent(event)) {
            case Enter:
                context.debug(this.getClass().getSimpleName() + ".onKeyDownEvent ENTER");

                this.save();
                break;
            default:
                // ignore other keys
                break;
        }
    }

    private void save() {
        final SpreadsheetMetadataPanelComponentContext context = this.context;
        final IntegerBox integerBox = this.integerBox;
        final String saveText;

        if (integerBox.isEmpty()) {
            saveText = "";
        } else {
            saveText = String.valueOf(integerBox.getValue());
        }
        context.debug(this.getClass().getSimpleName() + ".save " + CharSequences.quoteAndEscape(saveText));

        context.pushHistoryToken(
                context.historyToken()
                        .setMetadataPropertyName(this.propertyName)
                        .setSave(saveText)
        );
    }

    @Override
    Optional<Button> defaultButton() {
        return Optional.empty(); // default button later.
    }

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final AppContext context) {
        this.value = null;
        context.spreadsheetMetadata()
                .get(this.propertyName)
                .ifPresent(v -> {
                    this.value = v;
                    this.integerBox.setValue(v);
                });
    }

    private Integer value;

    // isElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.integerBox.element();
    }

    private final IntegerBox integerBox;
}
