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

import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Optional;

/**
 * Helps with validating values and turning the result into error messages.
 */
public interface ValidatorHelper {

    default <V> List<String> validateAndGetErrors(final Optional<V> value,
                                                  final Optional<Validator<Optional<V>>> validator) {
        String message = null;

        if (validator.isPresent()) {
            message = validator.get()
                    .isValid(value)
                    .getErrorMessage();
        }

        return CharSequences.isNullOrEmpty(message) ?
                Lists.empty() :
                Lists.of(message);
    }
}
