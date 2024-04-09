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

package walkingkooka.spreadsheet.dominokit.ui.find;

import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.SpreadsheetParsers;
import walkingkooka.text.cursor.TextCursors;

import java.util.Objects;
import java.util.function.Function;

/**
 * A {@link Function} that parsers any given text into a {@link SpreadsheetFormula}. The parser used will be a combination of those in {@link SpreadsheetMetadata}.
 */
final class SpreadsheetFindDialogComponentSpreadsheetFormulaComponentParserFunction implements Function<String, SpreadsheetFormula> {

    public static SpreadsheetFindDialogComponentSpreadsheetFormulaComponentParserFunction with(final SpreadsheetFindDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetFindDialogComponentSpreadsheetFormulaComponentParserFunction(context);
    }

    private SpreadsheetFindDialogComponentSpreadsheetFormulaComponentParserFunction(final SpreadsheetFindDialogComponentContext context) {
        this.context = context;
    }

    @Override
    public SpreadsheetFormula apply(final String text) {
        final SpreadsheetFindDialogComponentContext context = this.context;
        final SpreadsheetMetadata metadata = context.spreadsheetMetadata();

        return SpreadsheetFormula.parse(
                TextCursors.charSequence(text),
                SpreadsheetParsers.valueOrExpression(
                        metadata.parser()
                ),
                metadata.parserContext(
                        context::now
                )
        );
    }

    private final SpreadsheetFindDialogComponentContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
