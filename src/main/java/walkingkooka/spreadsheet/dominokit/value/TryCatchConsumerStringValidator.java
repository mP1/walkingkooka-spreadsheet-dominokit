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

import org.dominokit.domino.ui.forms.validations.ValidationResult;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.InvalidCharacterException;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A {@link Validator} that invokes {@link Consumer#accept(Object)}, catching any exceptions and using that as the fail message.
 */
final class TryCatchConsumerStringValidator implements Validator<Optional<String>> {

    /**
     * Factory
     */
    static TryCatchConsumerStringValidator with(final Consumer<String> consumer) {
        Objects.requireNonNull(consumer, "consumer");

        return new TryCatchConsumerStringValidator(consumer);
    }

    private TryCatchConsumerStringValidator(final Consumer<String> consumer) {
        super();
        this.consumer = consumer;
    }

    @Override
    public ValidationResult isValid(final Optional<String> value) {
        ValidationResult result;

        try {
            this.consumer.accept(
                    value.orElse("")
            );
            result = ValidationResult.valid();
        } catch (final InvalidCharacterException fail) {
            result = ValidationResult.invalid(fail.getShortMessage());
        } catch (final Exception fail) {
            result = ValidationResult.invalid(fail.getMessage());
        }
        return result;
    }

    private final Consumer<String> consumer;

    @Override
    public String toString() {
        return this.consumer.toString();
    }
}
