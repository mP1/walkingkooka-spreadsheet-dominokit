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

package walkingkooka.spreadsheet.dominokit.dom;

import elemental2.dom.EventListener;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;

import java.util.Optional;

public interface HasEventListenersDelegator<V, C extends HasEventListeners<V, C>> extends HasEventListeners<V, C> {

    @Override
    default C addChangeListener(final ChangeListener<Optional<V>> listener) {
        this.hasEventListeners()
            .addChangeListener(listener);
        return (C) this;
    }

    @Override
    default C addClickListener(final EventListener listener) {
        this.hasEventListeners()
            .addClickListener(listener);
        return (C) this;
    }

    @Override
    default C addContextMenuListener(final EventListener listener) {
        this.hasEventListeners()
            .addContextMenuListener(listener);
        return (C) this;
    }

    @Override
    default C addFocusListener(final EventListener listener) {
        this.hasEventListeners()
            .addFocusListener(listener);
        return (C) this;
    }

    @Override
    default C addKeydownListener(final EventListener listener) {
        this.hasEventListeners()
            .addKeydownListener(listener);
        return (C) this;
    }

    @Override
    default C addKeyUpListener(final EventListener listener) {
        this.hasEventListeners()
            .addKeyUpListener(listener);
        return (C) this;
    }

    HasEventListeners<V, ?> hasEventListeners();
}
