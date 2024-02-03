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

package walkingkooka.spreadsheet.dominokit.history.util;

import walkingkooka.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A {@link walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher} that captures and keeps at most the last
 * {@link walkingkooka.spreadsheet.dominokit.history.HistoryToken} instances. The {@link java.util.function.Predicate}
 * and max are all configurable at create time.
 * <br>
 * This is useful for tracking the last pattern saves and then create menu items in a menu.
 */
public final class HistoryTokenRecorder implements HistoryTokenWatcher {

    public static HistoryTokenRecorder with(final Predicate<HistoryToken> predicate,
                                            final int max) {
        Objects.requireNonNull(predicate, "predicate");
        if (max <= 0) {
            throw new IllegalArgumentException("Invalid max " + max + " <= 0");
        }

        return new HistoryTokenRecorder(
                predicate,
                max
        );
    }

    private HistoryTokenRecorder(final Predicate<HistoryToken> predicate,
                                 final int max) {
        this.predicate = predicate;
        this.max = max;
    }

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {

        final List<HistoryToken> tokens = this.tokens;

        final HistoryToken token = context.historyToken();
        if (this.predicate.test(token)) {
            tokens.remove(token);
            tokens.add(
                    0,
                    token
            );
            final int size = tokens.size();
            if (size > this.max) {
                tokens.remove(
                        size - 1
                ); // remove oldest.
            }
        }
    }

    private final Predicate<HistoryToken> predicate;

    /**
     * Clears the cache of tokens.
     */
    public void clear() {
        this.tokens.clear();
    }

    /**
     * Return the last {#max} tokens, with the first element holding the most recent {@link HistoryToken}.
     */
    public List<HistoryToken> tokens() {
        return Lists.readOnly(this.tokens);
    }

    private final List<HistoryToken> tokens = Lists.linkedList();

    private final int max;

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .label("max")
                .value(this.max)
                .value(this.tokens)
                .build();
    }
}
