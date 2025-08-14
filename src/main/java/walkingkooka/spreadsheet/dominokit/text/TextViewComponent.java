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

package walkingkooka.spreadsheet.dominokit.text;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.dom.DivComponent;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.text.CharSequences;

import java.util.Optional;

/**
 * A barebones component that may be used to display text. Many methods such as support for listeners etc all throw {@link UnsupportedOperationException}.
 */
public final class TextViewComponent extends TextViewComponentLike
    implements HtmlComponentDelegator<HTMLDivElement, TextViewComponent> {

    public static TextViewComponent empty() {
        return new TextViewComponent();
    }

    private TextViewComponent() {
        super();
        this.element = HtmlElementComponent.div();

        // https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/4338
        // honour(render) "file" line endings
        this.setCssProperty(
            "white-space",
            "pre;"
        );
    }

    @Override
    public TextViewComponent setId(final String id) {
        this.element.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.element.id();
    }

    @Override
    public TextViewComponent setValue(final Optional<String> value) {
        this.element.setText(
            value.orElse("")
        );
        return this;
    }

    @Override
    public Optional<String> value() {
        final String value = this.element.text();

        return Optional.ofNullable(
            CharSequences.isNullOrEmpty(value) ?
                null :
                value
        );
    }

    // HtmlComponentDelegator...........................................................................................


    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.element;
    }

    private final DivComponent element;
}
