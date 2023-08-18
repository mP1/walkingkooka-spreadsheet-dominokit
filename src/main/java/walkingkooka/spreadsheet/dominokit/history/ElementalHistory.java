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

package walkingkooka.spreadsheet.dominokit.history;

import elemental2.dom.DomGlobal;
import org.gwtproject.core.client.Scheduler;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * A wrapper around a {@link DomGlobal#location}.
 */
final class ElementalHistory implements History {

    static ElementalHistory with(final LoggingContext context) {
        Objects.requireNonNull(context, "context");
        return new ElementalHistory(context);
    }

    private ElementalHistory(final LoggingContext context) {
        this.context = context;
    }

    /**
     * Pushes the given {@link HistoryToken} to the browser location#hash.
     */
    @Override
    public void pushHistoryToken(final HistoryToken token) {
        Scheduler.get()
                .scheduleDeferred(
                        () -> {
                            final String newHash = "#" + token.urlFragment();
                            final String current = DomGlobal.location.hash;
                            if (false == current.equals(newHash)) {
                                this.context.debug("ElementalHistory.pushHistoryToken from " + CharSequences.quoteAndEscape(current) + " to " + CharSequences.quoteAndEscape(newHash));

                                DomGlobal.location.hash = newHash;
                            }
                        }
                );
    }

    @Override
    public HistoryToken historyToken() {
        // remove the leading hash if necessary.
        String hash = DomGlobal.location.hash;
        if (false == hash.equals(this.locationHash)) {
            if (hash.startsWith("#")) {
                hash = hash.substring(1);
            }

            final UrlFragment urlFragment = UrlFragment.parse(hash);
            final HistoryToken historyToken = HistoryToken.parse(urlFragment);
            final UrlFragment historyTokenUrlFragment = historyToken.urlFragment();

            // if different, the hash must have been invalid, update with the actual parsed result.
            if(false == historyTokenUrlFragment.equals(urlFragment)) {
                hash = historyTokenUrlFragment.value();
                DomGlobal.location.hash = hash;
            }

            this.historyToken = historyToken;
            this.locationHash = hash;
        }

        return this.historyToken;
    }

    /**
     * The original window.location.hash used to produce the {@link #historyToken}.
     */
    private String locationHash;

    /**
     * A cached {@link HistoryToken} for the current {@link DomGlobal#location#locationHash}.
     */
    private HistoryToken historyToken;

    private final LoggingContext context;

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
