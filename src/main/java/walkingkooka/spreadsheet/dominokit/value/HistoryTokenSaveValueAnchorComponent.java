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

import walkingkooka.Cast;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Creates a {@link HistoryTokenSaveValueAnchorComponent}, that is a link that when clicked saves the current value.
 * Note setting a clear or empty value does NOT disable the link, this must be done by {@link #disabled()} and
 * enabled using {@link #enabled()}.
 */
public final class HistoryTokenSaveValueAnchorComponent<T> implements ValueHistoryTokenAnchorComponentDelegator<HistoryTokenSaveValueAnchorComponent<T>, T> {

    public static <T> HistoryTokenSaveValueAnchorComponent<T> with(final String id,
                                                                   final HistoryContext context) {
        return new HistoryTokenSaveValueAnchorComponent<T>(
            id,
            context
        );
    }

    private HistoryTokenSaveValueAnchorComponent(final String id,
                                                 final HistoryContext context) {
        this.historyTokenAnchorComponent = HistoryTokenAnchorComponent.empty();

        this.component = ValueHistoryTokenAnchorComponent.with(
            this.historyTokenAnchorComponent,
            this::getter,
            this::setter
        );
        this.setId(id);
        this.context = Objects.requireNonNull(context, "context");

        this.setTextContent("Save");

        this.autoDisableWhenMissingValue = false;
    }

    /**
     * Getter that returns the saved value if one is present.
     */
    private Optional<T> getter(final HistoryTokenAnchorComponent anchor) {
        return Cast.to(
            anchor.historyToken()
                .map(HistoryToken::saveValue)
        );
    }

    private void setter(final Optional<T> value,
                        final HistoryTokenAnchorComponent anchor) {
        HistoryToken historyToken = this.context.historyToken()
            .setSaveValue(value);
        if (false == (historyToken.getClass().getSimpleName().contains("Save"))) {
            historyToken = null;
        }
        if(this.autoDisableWhenMissingValue && false == value.isPresent()) {
            historyToken = null;
        }

        anchor.setHistoryToken(
            Optional.ofNullable(historyToken)
        );
    }

    public HistoryTokenSaveValueAnchorComponent<T> setStringValue(final String value) {
        this.historyTokenAnchorComponent.setHistoryToken(
            Optional.ofNullable(
                this.autoDisableWhenMissingValue && CharSequences.isNullOrEmpty(value) ?
                    null :
                    this.context.historyToken()
                        .setSaveValue(value)
            )
        );
        return this;
    }

    private final HistoryTokenAnchorComponent historyTokenAnchorComponent;

    /**
     * Will disable the link if setValue is given a {@link Optional#empty()}.
     */
    public HistoryTokenSaveValueAnchorComponent<T> autoDisableWhenMissingValue() {
        this.autoDisableWhenMissingValue = true;
        return this;
    }

    private boolean autoDisableWhenMissingValue;

    // AnchorComponentDelegator.........................................................................................

    @Override
    public ValueHistoryTokenAnchorComponent<T> valueHistoryTokenAnchorComponent() {
        return this.component;
    }

    // @VisibleForTesting
    final ValueHistoryTokenAnchorComponent<T> component;

    final HistoryContext context;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
