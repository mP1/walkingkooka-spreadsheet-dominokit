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

import java.util.function.BiConsumer;

public interface Browser {

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
}
