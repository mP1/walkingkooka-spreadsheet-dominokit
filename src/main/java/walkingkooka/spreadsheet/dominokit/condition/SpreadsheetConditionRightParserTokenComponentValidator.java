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

package walkingkooka.spreadsheet.dominokit.condition;

import org.dominokit.domino.ui.forms.validations.ValidationResult;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.InvalidCharacterException;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Validator} that parsers given text into a condition.
 */
final class ConditionRightSpreadsheetParserTokenComponentValidator implements Validator<Optional<String>> {

    static ConditionRightSpreadsheetParserTokenComponentValidator with(final ConditionRightSpreadsheetParserTokenComponentConditionFunction parser) {
        return new ConditionRightSpreadsheetParserTokenComponentValidator(
            Objects.requireNonNull(parser, "parser")
        );
    }

    private ConditionRightSpreadsheetParserTokenComponentValidator(final ConditionRightSpreadsheetParserTokenComponentConditionFunction parser) {
        this.parser = parser;
    }

    @Override
    public ValidationResult isValid(final Optional<String> text) {
        String message;

        try {
            this.parser.apply(
                text.orElse("")
            );

            message = null;
        } catch (final InvalidCharacterException invalidCharacterException) {
            message = invalidCharacterException.getShortMessage();
        } catch (final Exception fail) {
            message = fail.getMessage();
        }
        return CharSequences.isNullOrEmpty(message) ?
            ValidationResult.valid() :
            ValidationResult.invalid(message);
    }

    private final ConditionRightSpreadsheetParserTokenComponentConditionFunction parser;

    @Override
    public String toString() {
        return this.parser.toString();
    }
}

