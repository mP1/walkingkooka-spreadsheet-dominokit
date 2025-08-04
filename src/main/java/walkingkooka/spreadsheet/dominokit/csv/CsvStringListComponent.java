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

package walkingkooka.spreadsheet.dominokit.csv;

import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.collect.list.CsvStringList;
import walkingkooka.spreadsheet.dominokit.validator.SpreadsheetValidators;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;

import java.util.Objects;

/**
 * A component that will be used to support entry text entry of lists of days, months etc as a CSV text.
 */
public final class CsvStringListComponent implements ValueSpreadsheetTextBoxWrapper<CsvStringListComponent, CsvStringList> {

    public final static boolean INCLUSIVE = true;

    public final static boolean EXCLUSIVE = false;

    public static CsvStringListComponent empty(final int min,
                                               final int max,
                                               final boolean inclusive) {
        return new CsvStringListComponent(
            min,
            max,
            inclusive
        );
    }

    private CsvStringListComponent(final int min,
                                   final int max,
                                   final boolean inclusive) {
        this.textBox = ValueTextBoxComponent.with(
            CsvStringListComponentParserFunction.with(
                min,
                max,
                inclusive
            ),
            CsvStringList::text
        );
    }

    public Validator<CsvStringListComponent> validator() {
        return this.validator;
    }

    public CsvStringListComponent setValidator(final Validator<CsvStringListComponent> validator) {
        Objects.requireNonNull(validator, "validator");

        this.valueTextBoxComponent()
            .setValidator(
                SpreadsheetValidators.tryCatch(CsvStringList::parse)
            );
        this.validator = validator;

        return this;
    }

    private Validator<CsvStringListComponent> validator;

    // ValueSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ValueTextBoxComponent<CsvStringList> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<CsvStringList> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}