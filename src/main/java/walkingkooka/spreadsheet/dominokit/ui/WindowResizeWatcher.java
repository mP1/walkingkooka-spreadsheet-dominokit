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

package walkingkooka.spreadsheet.dominokit.ui;

import elemental2.dom.DomGlobal;
import org.dominokit.domino.ui.events.EventType;
import org.gwtproject.core.client.Scheduler;
import org.gwtproject.core.client.Scheduler.ScheduledCommand;

import java.util.function.BiConsumer;

/**
 * Helper which contains method to assist with watching window resize events.
 */
public interface WindowResizeWatcher {

    /**
     * Registers a {@link BiConsumer} which will receive browser window resize events.
     */
    default void addWindowResizeListener(final BiConsumer<Integer, Integer> listener) {
        DomGlobal.window.addEventListener(
                EventType.resize.getName(),
                (e) -> listener.accept(
                        DomGlobal.window.innerWidth,
                        DomGlobal.window.innerHeight
                )
        );
    }

    /**
     * Fire the window size. This is eventually used to compute the spreadsheet viewport size.
     */
    default void fireWindowSizeLater(final BiConsumer<Integer, Integer> listener) {
        Scheduler.get()
                .scheduleDeferred(
                        new ScheduledCommand() {
                            @Override
                            public void execute() {
                                listener.accept(
                                        DomGlobal.window.innerWidth,
                                        DomGlobal.window.innerHeight
                                );
                            }
                        }
                );
    }
}
