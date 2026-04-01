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

package org.dominokit.domino.ui.forms.suggest;

import org.dominokit.domino.ui.IsElement;

public class Option<V, C extends IsElement<?>, O extends Option<V, C, O>> {

    Option() {
        super();
    }

    /**
     * Functional interface for supplying an instance of a type {@code T} based on a key and a value.
     *
     * @param <T> The type of object to be created.
     * @param <V> The type of the value used for creating the object.
     */
    public interface OptionSupplier<T, V> {

        /**
         * Creates and returns an instance of type {@code T} based on the provided key and value.
         *
         * @param key The key used for creating the object.
         * @param value The value used for creating the object.
         * @return An instance of type {@code T}.
         */
        T get(String key, V value);
    }
}
