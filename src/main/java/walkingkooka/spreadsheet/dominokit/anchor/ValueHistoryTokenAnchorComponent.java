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

package walkingkooka.spreadsheet.dominokit.anchor;

import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A ValueComponent that wraps a {@link HistoryTokenAnchorComponent}, adding additional support for setting a value via a function.
 * Decorations such as icon for the anchor must be set on the {@link HistoryTokenAnchorComponent} itself as no delegating methods are available.
 */
public final class ValueHistoryTokenAnchorComponent<T> implements AnchorComponentDelegator<ValueHistoryTokenAnchorComponent<T>, T> {

    public static <T> ValueHistoryTokenAnchorComponent<T> with(final HistoryTokenAnchorComponent anchor,
                                                               final Function<HistoryTokenAnchorComponent, Optional<T>> getter,
                                                               final BiConsumer<Optional<T>, HistoryTokenAnchorComponent> setter) {
        return new ValueHistoryTokenAnchorComponent<>(
            Objects.requireNonNull(anchor, "anchor"),
            Objects.requireNonNull(getter, "getter"),
            Objects.requireNonNull(setter, "setter")
        );
    }

    private ValueHistoryTokenAnchorComponent(final HistoryTokenAnchorComponent anchor,
                                             final Function<HistoryTokenAnchorComponent, Optional<T>> getter,
                                             final BiConsumer<Optional<T>, HistoryTokenAnchorComponent> setter) {
        this.anchor = anchor;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> setId(String id) {
        this.anchor.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.anchor.id();
    }

    @Override
    public Optional<T> value() {
        return this.getter.apply(this.anchor);
    }

    private final Function<HistoryTokenAnchorComponent, Optional<T>> getter;

    @Override
    public ValueHistoryTokenAnchorComponent<T> setValue(final Optional<T> value) {
        this.setter.accept(
            value,
            this.anchor
        );
        return this;
    }

    private final BiConsumer<Optional<T>, HistoryTokenAnchorComponent> setter;

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> setDisabled(final boolean disabled) {
        this.anchor.setDisabled(disabled);
        return this;
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> hideMarginBottom() {
        this.anchor.hideMarginBottom();
        return this;
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> removeBorders() {
        this.anchor.removeBorders();
        return this;
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> focus() {
        this.anchor.focus();
        return this;
    }

    // AnchorComponentDelegator......................................................................................

    @Override
    public AnchorComponent<?, ?> anchorComponent() {
        return this.anchor;
    }

    private final HistoryTokenAnchorComponent anchor;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.anchor.toString();
    }
}
