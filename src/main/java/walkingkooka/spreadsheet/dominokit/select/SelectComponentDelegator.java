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

package walkingkooka.spreadsheet.dominokit.select;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentDelegator;

import java.util.Optional;

public interface SelectComponentDelegator<V, C extends FormValueComponent<HTMLFieldSetElement, V, C>> extends FormValueComponentDelegator<HTMLFieldSetElement, V, C> {

    SelectComponent<V> selectComponent();

    @Override
    default FormValueComponent<HTMLFieldSetElement, V, ?> formValueComponentLike() {
        return this.selectComponent();
    }

    @Override
    default FormValueComponent<HTMLFieldSetElement, V, ?> formValueComponent() {
        return this.selectComponent();
    }

    default SelectComponent<V> appendOption(final Optional<V> value) {
        return this.selectComponent()
            .appendOption(value);
    }

    default SelectComponent<V> clearOptions() {
        return this.selectComponent()
            .clearOptions();
    }
}
