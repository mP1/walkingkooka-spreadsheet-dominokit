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
import elemental2.dom.HTMLUListElement;
import elemental2.dom.KeyboardEvent;
import jsinterop.base.Js;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.dominokit.dom.UlComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.text.TextBoxComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Optional;

/**
 * A {@link SpreadsheetMetadataPanelComponentItem} that displays {@link String text}
 */
final class SpreadsheetMetadataPanelComponentItemText extends SpreadsheetMetadataPanelComponentItem<String, SpreadsheetMetadataPanelComponentItemText, HTMLUListElement> {

    static SpreadsheetMetadataPanelComponentItemText with(final SpreadsheetMetadataPropertyName<String> propertyName,
                                                          final SpreadsheetMetadataPanelComponentContext context) {
        return new SpreadsheetMetadataPanelComponentItemText(
            propertyName,
            context
        );
    }

    private SpreadsheetMetadataPanelComponentItemText(final SpreadsheetMetadataPropertyName<String> propertyName,
                                                      final SpreadsheetMetadataPanelComponentContext context) {
        super(
            propertyName,
            context
        );

        final UlComponent list = this.ul();
        this.list = list;

        this.textBox = this.textBox();
        list.appendChild(
            this.li()
                .appendChild(
                    this.textBox
                )
        );

        final HistoryTokenAnchorComponent defaultValueAnchor = this.defaultValueAnchor();
        list.appendChild(
            this.li()
                .appendChild(
                    defaultValueAnchor
                )
        );
        this.defaultValueAnchor = defaultValueAnchor;
    }

    private TextBoxComponent textBox() {
        final TextBoxComponent textBox = TextBoxComponent.empty();

        textBox.addChangeListener(
            (final Optional<String> oldValue, final Optional<String> newValue) -> this.saveText(textBox)
        ).addKeyDownListener(
            (final Event event) -> {
                final KeyboardEvent keyboardEvent = Js.cast(event);
                switch (Key.fromEvent(keyboardEvent)) {
                    case Enter:
                        event.preventDefault();
                        this.saveText(textBox);
                        break;
                    default:
                        // ignore other keys
                        break;
                }
            }
        ).clear();

        // clear the margin-bottom: 16px
        return textBox.setCssProperty("width", TEXT_BOX_WIDTH)
            .setCssProperty("margin-bottom", "0");
    }

    private void saveText(final TextBoxComponent textBox) {
        this.save(
            textBox.value()
                .orElse("")
        );
    }

    @Override
    void focus() {
        this.textBox.focus();
    }

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final RefreshContext context) {
        final SpreadsheetMetadata metadata = this.context.spreadsheetMetadata();

        this.textBox.setValue(
            metadata.getIgnoringDefaults(this.propertyName)
        );

        this.refreshDefaultValue(
            this.defaultValueAnchor,
            metadata.defaults()
                .get(this.propertyName)
                .map(Object::toString)
                .orElse("")
        );
    }

    private final TextBoxComponent textBox;

    private final HistoryTokenAnchorComponent defaultValueAnchor;

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLUListElement, ?> htmlComponent() {
        return this.list;
    }

    private final UlComponent list;
}
