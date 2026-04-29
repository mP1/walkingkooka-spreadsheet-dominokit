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

package walkingkooka.spreadsheet.dominokit.value.textstyle.lineheight;

import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyLengthComponentLike;
import walkingkooka.spreadsheet.dominokit.value.textstyle.length.LengthComponent;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStylePropertyName;

public final class LineHeightComponent implements TextStylePropertyLengthComponentLike<LineHeightComponent> {

    public static LineHeightComponent empty(final String idPrefix) {
        return new LineHeightComponent(idPrefix);
    }

    private LineHeightComponent(final String idPrefix) {
        this.lengthComponent = LengthComponent.with(
            this.name()
        );

        this.setIdPrefix(idPrefix);
    }

    @Override
    public TextStylePropertyName<Length<?>> name() {
        return TextStylePropertyName.LINE_HEIGHT;
    }

    // LengthComponentDelegator.........................................................................................

    @Override
    public LengthComponent lengthComponent() {
        return this.lengthComponent;
    }

    private final LengthComponent lengthComponent;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.lengthComponent.toString();
    }
}
