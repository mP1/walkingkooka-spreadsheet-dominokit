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

import walkingkooka.collect.list.Lists;

import java.util.List;

/**
 * A component with errors and methods to get or update them.
 */
public interface ComponentWithErrors<T> {

    /**
     * Getter that returns any current errors.
     */
    List<String> errors();

    /**
     * Sets or replaces any existing error messages.
     */
    T setErrors(final List<String> errors);

    /**
     * Clears any error messages, leaving no errors.
     */
    default T clearErrors() {
        return this.setErrors(Lists.empty());
    }
}
