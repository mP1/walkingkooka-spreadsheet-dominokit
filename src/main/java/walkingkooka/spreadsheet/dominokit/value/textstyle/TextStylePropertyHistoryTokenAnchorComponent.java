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

import elemental2.dom.HTMLAnchorElement;
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.Cast;
import walkingkooka.naming.HasName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * An anchor that when clicked supports saving a value including {@link Optional#empty()} for the given constant
 * {@link TextStylePropertyName}. The link will automatically refresh as it adds itself as a {@link walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher}.
 * Note the value cannot be changed.
 */
public final class TextStylePropertyHistoryTokenAnchorComponent<V> implements ValueComponent<HTMLAnchorElement, V, TextStylePropertyHistoryTokenAnchorComponent<V>>,
    AnchorComponentDelegator<TextStylePropertyHistoryTokenAnchorComponent<V>>,
    HasName<TextStylePropertyName<V>> {

    public final static Optional<Icon<?>> NO_ICON = Optional.empty();

    public static <V> TextStylePropertyHistoryTokenAnchorComponent<V> with(final String idPrefix,
                                                                           final TextStylePropertyName<V> textStylePropertyName,
                                                                           final Optional<V> value,
                                                                           final Optional<Icon<?>> icon,
                                                                           final TextStylePropertyHistoryTokenAnchorComponentContext context) {
        return new TextStylePropertyHistoryTokenAnchorComponent<>(
            CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix"),
            Objects.requireNonNull(textStylePropertyName, "textStylePropertyName"),
            Objects.requireNonNull(value, "value"),
            Objects.requireNonNull(icon, "icon"),
            Objects.requireNonNull(context, "context")
        );
    }

    private TextStylePropertyHistoryTokenAnchorComponent(final String idPrefix,
                                                         final TextStylePropertyName<V> textStylePropertyName,
                                                         final Optional<V> value,
                                                         final Optional<Icon<?>> icon,
                                                         final TextStylePropertyHistoryTokenAnchorComponentContext context) {
        super();
        this.textStylePropertyName = textStylePropertyName;

        // Test123-textAlign-LEFT-Link
        this.anchor = HistoryTokenAnchorComponent.empty()
            .setId(
                idPrefix +
                    CaseKind.KEBAB.change(
                        textStylePropertyName.value(),
                        CaseKind.CAMEL
                    ) +
                    value.map(
                        v -> "-" + v
                    ).orElse("") +
                    SpreadsheetElementIds.LINK
            ).setIconBefore(icon);
        this.value = value;

        this.context = context;
        context.addHistoryTokenWatcher(
            (final HistoryToken previous, final AppContext appContext) -> this.refreshAnchor()
        );

        this.refreshAnchor();
    }

    private void refreshAnchor() {
        this.anchor.setValue(
            Optional.of(
                this.context.historyToken()
                    .setStyleProperty(
                        this.textStylePropertyName.setOrRemoveValue(this.value)
                    )
            ).filter(HistoryToken::isSave)
        );
    }

    private final TextStylePropertyHistoryTokenAnchorComponentContext context;

    @Override
    public TextStylePropertyName<V> name() {
        return this.textStylePropertyName;
    }

    private final TextStylePropertyName<V> textStylePropertyName;

    @Override
    public Optional<V> value() {
        return this.value;
    }

    private final Optional<V> value;

    @Override
    public TextStylePropertyHistoryTokenAnchorComponent<V> setValue(final Optional<V> value) {
        Objects.requireNonNull(value, "value");

        throw new UnsupportedOperationException();
    }

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
    public int hashCode() {
        return this.anchor.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof TextStylePropertyHistoryTokenAnchorComponent &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final TextStylePropertyHistoryTokenAnchorComponent<?> other) {
        return this.anchor.equals(other.anchor);
    }

    @Override
    public String toString() {
        return this.anchor.toString();
    }
}
