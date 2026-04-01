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

import elemental2.dom.HTMLElement;

import java.util.Optional;

public interface FormValueComponentDelegator2<E extends HTMLElement, V, C extends FormValueComponent<E, V, C>> extends FormValueComponent<E, V, C>,
    FormValueComponentLikeDelegator<E, C>{

    @Override
    default Optional<V> value() {
        return this.formValueComponent()
            .value();
    }

    @Override
    default C setValue(final Optional<V> value) {
        this.formValueComponent()
            .setValue(value);
        return (C) this;
    }

    @Override
    default Runnable addValueWatcher(final ValueWatcher<V> watcher) {
        return this.formValueComponent()
            .addValueWatcher(watcher);
    }

    FormValueComponent<E, V, ?> formValueComponent();

    @Override
    default FormValueComponentLike<E, ?> formValueComponentLike() {
        return this.formValueComponent();
    }
}
