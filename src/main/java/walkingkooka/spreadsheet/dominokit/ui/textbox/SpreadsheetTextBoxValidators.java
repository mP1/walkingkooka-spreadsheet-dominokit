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

package walkingkooka.spreadsheet.dominokit.ui.textbox;

import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.reflect.PublicStaticHelper;

import java.util.function.Consumer;

/**
 * Provides factory methods for a few {@link SpreadsheetTextBox} validators.
 */
public final class SpreadsheetTextBoxValidators implements PublicStaticHelper {

    /**
     * {@see SpreadsheetTextBoxOptionalValidator}
     */
    public static Validator<String> optional(final Validator<String> validator) {
        return SpreadsheetTextBoxOptionalValidator.with(validator);
    }

    /**
     * {@see SpreadsheetTextBoxStringParserValidator}
     */
    public static Validator<String> parser(final Consumer<String> parser) {
        return SpreadsheetTextBoxStringParserValidator.with(parser);
    }

    /**
     * Stop creation
     */
    private SpreadsheetTextBoxValidators() {
        throw new UnsupportedOperationException();
    }
}
