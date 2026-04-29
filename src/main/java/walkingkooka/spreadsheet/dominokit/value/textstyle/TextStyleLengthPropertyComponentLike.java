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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentLike;
import walkingkooka.spreadsheet.dominokit.value.textstyle.length.LengthComponentDelegator;
import walkingkooka.tree.text.Length;

import java.util.Optional;

public interface TextStyleLengthPropertyComponentLike<C extends TextStyleLengthPropertyComponentLike<C>>
    extends TextStylePropertyComponent<HTMLFieldSetElement, Length<?>, C>,
    ValueTextBoxComponentLike<C, Length<?>>,
    LengthComponentDelegator<C> {

    default C setIdPrefix(final String idPrefix) {
        return this.setIdPrefix(
            idPrefix,
            SpreadsheetElementIds.TEXT_BOX
        );
    }

    @Override
    default Optional<String> stringValue() {
        return this.lengthComponent()
            .stringValue();
    }

    @Override
    default C setStringValue(final Optional<String> stringValue) {
        this.lengthComponent()
            .setStringValue(stringValue);
        return (C) this;
    }

    @Override
    default C setInnerRight(final HtmlComponent<?, ?> innerRight) {
        this.lengthComponent()
            .setInnerRight(innerRight);
        return (C) this;
    }

    @Override
    default C clearIcon() {
        throw new UnsupportedOperationException();
    }

    @Override
    default C setIcon(final Icon<?> icon) {
        throw new UnsupportedOperationException();
    }

    @Override
    default C addBlurListener(final EventListener listener) {
        this.lengthComponent()
            .addBlurListener(listener);
        return (C) this;
    }

    @Override
    default C addClickListener(final EventListener listener) {
        this.lengthComponent()
            .addClickListener(listener);
        return (C) this;
    }

    @Override
    default C addChangeListener(final ChangeListener<Optional<Length<?>>> listener) {
        this.lengthComponent()
            .addChangeListener(listener);
        return (C) this;
    }

    @Override
    default C addContextMenuListener(final EventListener listener) {
        this.lengthComponent()
            .addContextMenuListener(listener);
        return (C) this;
    }

    @Override
    default C addFocusListener(final EventListener listener) {
        this.lengthComponent()
            .addFocusListener(listener);
        return (C) this;
    }

    @Override
    default C addInputListener(final EventListener listener) {
        this.lengthComponent()
            .addInputListener(listener);
        return (C) this;
    }

    @Override
    default C addKeyDownListener(final EventListener listener) {
        this.lengthComponent()
            .addKeyDownListener(listener);
        return (C) this;
    }

    @Override
    default C addKeyUpListener(final EventListener listener) {
        this.lengthComponent()
            .addKeyUpListener(listener);
        return (C) this;
    }
}
