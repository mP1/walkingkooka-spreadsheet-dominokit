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
import org.dominokit.domino.ui.forms.TextBox;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CharSequences;

import java.util.Optional;

/**
 * A {@link SpreadsheetMetadataItemComponent} that displays {@link String text}
 */
final class SpreadsheetMetadataItemComponentText extends SpreadsheetMetadataItemComponent<String> {

    static SpreadsheetMetadataItemComponentText with(final SpreadsheetMetadataPropertyName<String> propertyName,
                                                     final SpreadsheetMetadataPanelComponentContext context) {
        checkPropertyName(propertyName);
        checkContext(context);

        return new SpreadsheetMetadataItemComponentText(
                propertyName,
                context
        );
    }

    private SpreadsheetMetadataItemComponentText(final SpreadsheetMetadataPropertyName<String> propertyName,
                                                 final SpreadsheetMetadataPanelComponentContext context) {
        super(
                propertyName,
                context
        );

        final TextBox textBox = new TextBox()
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
        textBox.element()
                .style
                .setProperty("margin-bottom", "0");
        this.textBox = textBox;
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
        final TextBox textBox = this.textBox;
        final String saveText;

        if (textBox.isEmpty()) {
            saveText = "";
        } else {
            saveText = String.valueOf(textBox.getValue());
        }

        final SpreadsheetMetadataPropertyName<?> propertyName = this.propertyName;
        final SpreadsheetMetadataPanelComponentContext context = this.context;
        context.debug(this.getClass().getSimpleName() + ".save " + propertyName + "=" + CharSequences.quoteAndEscape(saveText));

        context.save(
                propertyName,
                saveText
        );
    }

    @Override
    Optional<Button> defaultButton() {
        return Optional.empty(); // default button later.
    }

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final AppContext context) {
        this.textBox.setValue(
                context.spreadsheetMetadata()
                        .get(this.propertyName)
                        .orElse(null)
        );
    }

    // isElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.textBox.element();
    }

    private final TextBox textBox;
}
