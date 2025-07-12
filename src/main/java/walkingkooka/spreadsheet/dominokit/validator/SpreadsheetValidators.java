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

package walkingkooka.spreadsheet.dominokit.validator;

import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.reflect.PublicStaticHelper;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Provides factory methods for a few {@link String} validators.
 */
public final class SpreadsheetValidators implements PublicStaticHelper {


    /**
     * {@see SpreadsheetOptionalStringValidator}
     */
    public static <T> Validator<T> fake() {
        return new FakeValidator<>();
    }

    /**
     * {@see SpreadsheetOptionalStringValidator}
     */
    public static Validator<Optional<String>> optional(final Validator<Optional<String>> validator) {
        return SpreadsheetOptionalStringValidator.with(validator);
    }

    /**
     * {@see SpreadsheetRequiredStringValidator}
     */
    public static Validator<Optional<String>> required() {
        return SpreadsheetRequiredStringValidator.INSTANCE;
    }

    /**
     * {@see TryCatchConsumerStringValidator}
     */
    public static Validator<Optional<String>> tryCatch(final Consumer<String> consumer) {
        return TryCatchConsumerStringValidator.with(consumer);
    }

    /**
     * Stop creation
     */
    private SpreadsheetValidators() {
        throw new UnsupportedOperationException();
    }
}
