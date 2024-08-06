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

package walkingkooka.spreadsheet.dominokit.util;

import elemental2.dom.DomGlobal;
import elemental2.dom.DomGlobal.SetTimeoutCallbackFn;

import java.util.Objects;

/**
 * A throttler may be used to throttle or batch multiple server requests with a delay, so only the last {@link Runnable} is executed when the shared delay period expires.
 */
public final class Throttler {

    public final static long KEYBOARD_DELAY = 1500;

    /**
     * Creates a new {@link Throttler}.
     */
    public static Throttler empty(final long delay) {
        if (delay <= 0) {
            throw new IllegalArgumentException("Invalid delay " + delay + " < 0");
        }
        return new Throttler(delay);
    }

    private Throttler(final long delay) {
        this.delay = delay;
        this.next = null;
        this.timerId = -1;
    }

    public Throttler add(final Runnable later) {
        Objects.requireNonNull(later, "later");

        final double timerId = this.timerId;
        if (timerId >= 0) {
            // clear any pending timer.
            DomGlobal.clearTimeout(timerId);
            this.timerId = -1;
        }


        // schedule another
        this.next = later;
        this.timerId = DomGlobal.setTimeout(
                new SetTimeoutCallbackFn() {
                    @Override
                    public void onInvoke(final Object... ignored) {
                        Throttler.this.onTimerExpire();
                    }
                },
                this.delay
        );

        return this;
    }

    /**
     * Invoked when the last timer expires.
     */
    private void onTimerExpire() {
        final Runnable next = this.next;
        this.next = null;
        this.timerId = -1;
        next.run();
    }

    /**
     * The delay value for each given {@link Runnable}.
     */
    private final double delay;

    /**
     * When non-null the {@link Runnable} to execute when any outstanding timer expires.
     */
    private Runnable next;

    /**
     * When a positive number, may be used to clear any outstanding timer when a new {@link Runnable} is given.
     */
    private double timerId;

    @Override
    public String toString() {
        return "timerId=" + this.timerId + " next: " + next;
    }
}
