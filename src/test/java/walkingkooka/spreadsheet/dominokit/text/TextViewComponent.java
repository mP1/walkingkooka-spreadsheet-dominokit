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

import java.util.Objects;
import java.util.Optional;

/**
 * A barebones component that may be used to display text. Many methods such as support for listeners etc all throw {@link UnsupportedOperationException}.
 */
public final class TextViewComponent extends TextViewComponentLike {

    public static TextViewComponent empty() {
        return new TextViewComponent();
    }

    private TextViewComponent() {
        super();
        this.clearValue();
    }

    @Override
    public TextViewComponent setId(final String id) {
        this.id = id;
        return this;
    }

    @Override
    public String id() {
        return this.id;
    }

    private String id;

    @Override
    public TextViewComponent setValue(final Optional<String> value) {
        this.value = Objects.requireNonNull(value, "value");
        return this;
    }

    @Override
    public Optional<String> value() {
        return this.value;
    }

    private Optional<String> value;

    @Override
    public TextViewComponent setCssText(final String css) {
        return this;
    }

    @Override
    public TextViewComponent setCssProperty(final String name,
                                            final String value) {
        return this;
    }

    @Override
    public HTMLDivElement element() {
        throw new UnsupportedOperationException();
    }
}
