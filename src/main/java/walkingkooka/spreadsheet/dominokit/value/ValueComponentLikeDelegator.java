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

package walkingkooka.spreadsheet.dominokit.value;

import elemental2.dom.HTMLElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;

/**
 * A delegator for {@link ValueComponentLike} that may be within a {@link walkingkooka.spreadsheet.dominokit.Component}.
 */
public interface ValueComponentLikeDelegator<E extends HTMLElement, C extends ValueComponentLike<E, C>> extends ValueComponentLike<E, C>,
    HtmlComponentDelegator<E, C> {

    @Override
    default boolean isDisabled() {
        return this.valueComponentLike()
            .isDisabled();
    }

    @Override
    default C setDisabled(final boolean disabled) {
        this.valueComponentLike()
            .setDisabled(disabled);
        return (C) this;
    }

    @Override
    default C hideMarginBottom() {
        this.valueComponentLike()
            .hideMarginBottom();
        return (C) this;
    }

    @Override
    default C removeBorders() {
        this.valueComponentLike()
            .removeBorders();
        return (C) this;
    }

    @Override
    default C removePadding() {
        this.valueComponentLike()
            .removePadding();
        return (C) this;
    }

    @Override
    default C focus() {
        this.valueComponentLike()
            .focus();
        return (C) this;
    }

    @Override
    default C blur() {
        this.valueComponentLike()
            .blur();
        return (C) this;
    }

    @Override
    default boolean isEditing() {
        return this.valueComponentLike()
            .isEditing();
    }

    ValueComponentLike<E, ?> valueComponentLike();

    // HtmlComponentDelegator...........................................................................................

    @Override
    default HtmlComponent<E, ?> htmlComponent() {
        return this.valueComponentLike();
    }
}
