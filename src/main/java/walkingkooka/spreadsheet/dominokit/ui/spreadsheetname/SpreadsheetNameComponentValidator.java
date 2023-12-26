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

package walkingkooka.spreadsheet.dominokit.ui.spreadsheetname;

import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.forms.validations.ValidationResult;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.spreadsheet.SpreadsheetName;

/**
 * A {@link Validator} that uses {@link SpreadsheetName#with(String)} to validate entry.
 * Any {@link Exception#getMessage()} becomes the validation error text.
 */
final class SpreadsheetNameComponentValidator implements Validator<TextBox> {

    /**
     * Singleton
     */
    final static SpreadsheetNameComponentValidator INSTANCE = new SpreadsheetNameComponentValidator();

    private SpreadsheetNameComponentValidator() {
        super();
    }

    @Override
    public ValidationResult isValid(final TextBox component) {
        ValidationResult result;

        try {
            SpreadsheetName.with(
                    component.getValue()
            );
            result = ValidationResult.valid();
        } catch (final Exception fail) {
            result = ValidationResult.invalid(fail.getMessage());
        }
        return result;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
