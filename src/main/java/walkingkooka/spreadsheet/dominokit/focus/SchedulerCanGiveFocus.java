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

package walkingkooka.spreadsheet.dominokit.focus;

import elemental2.dom.Element;
import org.gwtproject.core.client.Scheduler;
import walkingkooka.spreadsheet.dominokit.log.Logging;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;

import java.util.Objects;

/**
 * Schedules giving focus by calling the {@link Runnable} provided.
 * If multiple attempts are made to give focus in a short period of time an {@link IllegalStateException} will be thrown.
 */
final class SchedulerCanGiveFocus implements CanGiveFocus,
    Logging {

    static SchedulerCanGiveFocus with(final LoggingContext loggingContext) {
        return new SchedulerCanGiveFocus(
            Objects.requireNonNull(loggingContext, "loggingContext")
        );
    }

    private SchedulerCanGiveFocus(final LoggingContext loggingContext) {
        this.loggingContext = loggingContext;
    }

    /**
     * Schedules giving focus to the {@link Element} if it exists. If multiple attempts are made to give focus in a short
     * period of time an {@link IllegalStateException} will be thrown.
     */
    @Override
    public void giveFocus(final Runnable giveFocus) {
        if(SCHEDULER_CAN_GIVE_FOCUS) {
            this.loggingContext.debug(this.getClass().getSimpleName() + ".giveFocus " + giveFocus);
        }

        final Runnable existingGiveFocus = this.giveFocus;
        if (null != existingGiveFocus && false == giveFocus.equals(existingGiveFocus)) {
            this.giveFocus = null;
            throw new IllegalStateException("Second attempt to give focus " + existingGiveFocus + " AND " + giveFocus);
        }
        this.giveFocus = giveFocus;

        Scheduler.get()
            .scheduleDeferred(this::giveFocus0);
    }

    /**
     * If {@link Runnable #giveFocus} is available run it.
     */
    private void giveFocus0() {
        final Runnable giveFocus = this.giveFocus;
        this.giveFocus = null;

        if (null != giveFocus) {
            giveFocus.run();
        }
    }

    /**
     * A {@link Runnable} which will give focus to some element. This is used to track and prevent multiple give focus attempts
     */
    private Runnable giveFocus;

    private final LoggingContext loggingContext;
}
