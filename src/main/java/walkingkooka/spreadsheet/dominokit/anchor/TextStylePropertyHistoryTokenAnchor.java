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

import elemental2.dom.HTMLAnchorElement;
import walkingkooka.naming.HasName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * An anchor that when clicked supports saving a value for the given constant {@link TextStylePropertyName}. The link
 * will automatically refresh as it adds itself as a {@link walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher}.
 */
public final class TextStylePropertyHistoryTokenAnchor<V> implements ValueComponent<HTMLAnchorElement, V, TextStylePropertyHistoryTokenAnchor<V>>,
    AnchorComponentDelegator<TextStylePropertyHistoryTokenAnchor<V>>,
    HasName<TextStylePropertyName<V>> {

    public static <V> TextStylePropertyHistoryTokenAnchor<V> with(final TextStylePropertyName<V> textStylePropertyName,
                                                                  final TextStylePropertyHistoryTokenAnchorContext context) {
        return new TextStylePropertyHistoryTokenAnchor<>(
            Objects.requireNonNull(textStylePropertyName, "textStylePropertyName"),
            Objects.requireNonNull(context, "context")
        );
    }

    private TextStylePropertyHistoryTokenAnchor(final TextStylePropertyName<V> textStylePropertyName,
                                                final TextStylePropertyHistoryTokenAnchorContext context) {
        super();
        this.textStylePropertyName = textStylePropertyName;
        this.anchor = HistoryTokenAnchorComponent.empty();
        this.value = Optional.empty();

        this.context = context;
        context.addHistoryTokenWatcher(
            (final HistoryToken previous, final AppContext appContext) -> this.setValue(this.value)
        );
    }

    @Override
    public TextStylePropertyName<V> name() {
        return this.textStylePropertyName;
    }

    private final TextStylePropertyName<V> textStylePropertyName;

    @Override
    public Optional<V> value() {
        return this.value;
    }

    @Override
    public TextStylePropertyHistoryTokenAnchor<V> setValue(final Optional<V> value) {
        Objects.requireNonNull(value, "value");

        this.value = value;
        this.anchor.setValue(
            value.map(
                v -> context.historyToken()
                    .setStylePropertyName(this.textStylePropertyName)
                    .setSaveValue(
                        Optional.of(v)
                    )
            ).filter(HistoryToken::isSave)
        );
        return this;
    }

    private Optional<V> value;

    private final HistoryContext context;

    @Override
    public Runnable addValueWatcher(final ValueWatcher<V> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        throw new UnsupportedOperationException();
    }

    // AnchorComponentDelegator.........................................................................................

    @Override
    public HistoryTokenAnchorComponent anchorComponent() {
        return this.anchor;
    }

    private final HistoryTokenAnchorComponent anchor;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.anchor.toString();
    }
}
