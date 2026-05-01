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

package walkingkooka.spreadsheet.dominokit.value.textstyle.margin;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.dom.DivComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.MarginOrPaddingBoxComponent;
import walkingkooka.tree.text.Margin;

import java.util.Optional;

public final class MarginBoxComponent implements MarginOrPaddingBoxComponent<Margin, MarginBoxComponent> {

    public static MarginBoxComponent empty() {
        return new MarginBoxComponent();
    }

    private MarginBoxComponent() {
        this.component = this.component();
        this.value = Optional.empty();
    }

    @Override
    public Optional<Margin> value() {
        return this.value;
    }

    @Override
    public MarginBoxComponent setValue(final Optional<Margin> value) {
        this.refresh(
            value,
            this.component
        );
        this.value = value;
        return this;
    }

    private Optional<Margin> value;

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.component;
    }

    final DivComponent component;
}
