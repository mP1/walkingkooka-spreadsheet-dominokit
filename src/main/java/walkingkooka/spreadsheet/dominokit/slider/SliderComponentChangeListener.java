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

package walkingkooka.spreadsheet.dominokit.slider;

import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.Cast;

import java.util.Objects;
import java.util.Optional;

final class SliderComponentChangeListener implements ChangeListener<Double> {

    static SliderComponentChangeListener with(final ChangeListener<Optional<Double>> listener) {
        return new SliderComponentChangeListener(
            Objects.requireNonNull(listener, "listener")
        );
    }

    private SliderComponentChangeListener(final ChangeListener<Optional<Double>> listener) {
        this.listener = listener;
    }

    // ChangeListener...................................................................................................

    @Override
    public void onValueChanged(final Double oldValue,
                               final Double newValue) {
        this.listener.onValueChanged(
            Optional.of(oldValue),
            Optional.of(newValue)
        );
    }

    final ChangeListener<Optional<Double>> listener;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.listener.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof SliderComponentChangeListener &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final SliderComponentChangeListener other) {
        return this.listener.equals(other.listener);
    }

    @Override
    public String toString() {
        return this.listener.toString();
    }
}
