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

package walkingkooka.spreadsheet.dominokit.ui.formula;

import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.forms.validations.ValidationResult;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;
import walkingkooka.spreadsheet.parser.SpreadsheetParsers;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;

import java.util.Optional;

/**
 * Parse the text from the {@link SpreadsheetFormulaComponent}. Any caught {@link Exception#getMessage()} becomes the validation failure message.
 */
final class SpreadsheetFormulaComponentValidator implements Validator<TextBox> {

    /**
     * Factory
     */
    static SpreadsheetFormulaComponentValidator with(final AppContext context) {
        return new SpreadsheetFormulaComponentValidator(
                context
        );
    }

    private SpreadsheetFormulaComponentValidator(final AppContext context) {
        super();
        this.context = context;
    }

    @Override
    public ValidationResult isValid(final TextBox component) {
        final AppContext context = this.context;
        final SpreadsheetMetadata metadata = context.spreadsheetMetadata();

        String errorMessage = null;
        Parser<SpreadsheetParserContext> parser = null;

        final SpreadsheetViewportCache viewportCache = context.viewportCache();
        final SpreadsheetSelection nonLabelSelection = context.historyToken()
                .nonLabelSelection(viewportCache)
                .orElse(null);
        if (null != nonLabelSelection) {
            final Optional<SpreadsheetCell> maybeCell = viewportCache.cell(nonLabelSelection.toCell());
            if (maybeCell.isPresent()) {
                final SpreadsheetCell cell = maybeCell.get();
                errorMessage = cell.formula()
                        .error()
                        .map(SpreadsheetError::message)
                        .orElse(null);
                parser = cell.parsePattern()
                        .map(p -> p.parser())
                        .orElseGet(() -> SpreadsheetParsers.valueOrExpression(
                                        metadata.parser()
                                )
                        );
            }
        }
        if (null == parser) {
            parser = SpreadsheetParsers.valueOrExpression(
                    metadata.parser()
            );
        }

        try {
            final SpreadsheetFormula formula = SpreadsheetFormula.parse(
                    TextCursors.charSequence(
                            component.getInputElement()
                                    .element()
                                    .value
                    ),
                    parser,
                    metadata.parserContext(
                            () -> context.now()
                    )
            );

            errorMessage = formula.error()
                    .map(SpreadsheetError::message)
                    .orElse(errorMessage);

        } catch (final Exception fail) {
            errorMessage = fail.getMessage();

            if (CharSequences.isNullOrEmpty(errorMessage)) {
                errorMessage = "Failed to parse formula";
            }
        }
        return null == errorMessage ?
                ValidationResult.valid() :
                ValidationResult.invalid(errorMessage);
    }

    private final AppContext context;

    @Override
    public String toString() {
        return this.context.spreadsheetMetadata()
                .toString();
    }
}
