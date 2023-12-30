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

package walkingkooka.spreadsheet.dominokit.ui.select;

import org.dominokit.domino.ui.forms.suggest.Select;
import org.dominokit.domino.ui.forms.validations.ValidationResult;
import org.dominokit.domino.ui.utils.HasValidation.Validator;

final class SpreadsheetSelectComponentValidator<T> implements Validator<Select<T>> {

    static <T> SpreadsheetSelectComponentValidator<T> with(final SpreadsheetSelectComponent<T> spreadsheetSelectComponent) {
        return new SpreadsheetSelectComponentValidator(spreadsheetSelectComponent);
    }

    private SpreadsheetSelectComponentValidator(final SpreadsheetSelectComponent<T> spreadsheetSelectComponent) {
        this.spreadsheetSelectComponent = spreadsheetSelectComponent;
    }

    @Override
    public ValidationResult isValid(final Select<T> component) {
        return null == component.getValue() && this.spreadsheetSelectComponent.required/**/ ?
                ValidationResult.invalid("Selection required") :
                ValidationResult.valid();
    }

    private final SpreadsheetSelectComponent<T> spreadsheetSelectComponent;

    @Override
    public String toString() {
        return this.spreadsheetSelectComponent.toString();
    }
}
