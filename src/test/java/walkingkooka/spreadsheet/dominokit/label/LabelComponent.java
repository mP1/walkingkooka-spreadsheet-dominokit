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

package walkingkooka.spreadsheet.dominokit.label;

import elemental2.dom.HTMLElement;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;

import java.util.Objects;
import java.util.Optional;

public final class LabelComponent extends LabelComponentLike implements TestHtmlElementComponent<HTMLElement, LabelComponent> {

    public static LabelComponent empty() {
        return new LabelComponent();
    }

    private LabelComponent() {
        super();
    }

    @Override
    public LabelComponent setValue(final Optional<String> value) {
        Objects.requireNonNull(value, "value");

        this.value = value;
        return this;
    }

    @Override
    public Optional<String> value() {
        return this.value;
    }

    private Optional<String> value;
}
