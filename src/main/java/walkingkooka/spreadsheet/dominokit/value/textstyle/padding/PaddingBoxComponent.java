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

package walkingkooka.spreadsheet.dominokit.value.textstyle.padding;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.dom.DivComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.MarginOrPaddingBoxComponent;
import walkingkooka.tree.text.Padding;

import java.util.Optional;

public final class PaddingBoxComponent implements MarginOrPaddingBoxComponent<Padding, PaddingBoxComponent> {

    public static PaddingBoxComponent empty() {
        return new PaddingBoxComponent();
    }

    private PaddingBoxComponent() {
        this.component = this.component();
        this.value = Optional.empty();
    }

    @Override
    public Optional<Padding> value() {
        return this.value;
    }

    @Override
    public PaddingBoxComponent setValue(final Optional<Padding> value) {
        this.refresh(
            value,
            this.component
        );
        this.value = value;
        return this;
    }

    private Optional<Padding> value;

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.component;
    }

    final DivComponent component;
}
