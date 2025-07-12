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

package walkingkooka.spreadsheet.dominokit.anchor;

import java.util.Optional;

public interface ValueHistoryTokenAnchorComponentDelegator<C extends ValueHistoryTokenAnchorComponentDelegator<C, T>, T> extends AnchorComponentDelegator<C, T> {

    @Override
    default Optional<T> value() {
        return this.valueHistoryTokenAnchorComponent()
            .value();
    }

    @Override
    default C setValue(final Optional<T> value) {
        this.valueHistoryTokenAnchorComponent()
            .setValue(value);
        return (C) this;
    }

    ValueHistoryTokenAnchorComponent<T> valueHistoryTokenAnchorComponent();

    @Override
    default AnchorComponent<?, ?> anchorComponent() {
        return this.valueHistoryTokenAnchorComponent();
    }
}
