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

package walkingkooka.spreadsheet.dominokit.ui.viewport;

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;
import walkingkooka.spreadsheet.parser.SpreadsheetParsers;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link Function} that parsers any given text into a {@link SpreadsheetFormula}. The parser will be taken from an existing
 * {@link SpreadsheetCell#parsePattern()} or falling back to {@link SpreadsheetMetadata#parser()} for the current {@link SpreadsheetCellReference}.
 */
final class SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction implements Function<String, SpreadsheetFormula> {

    public static SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction with(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction(context);
    }

    private SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction(final AppContext context) {
        this.context = context;
    }

    @Override
    public SpreadsheetFormula apply(final String text) {
        Parser<SpreadsheetParserContext> parser = null;

        final AppContext context = this.context;
        final SpreadsheetViewportCache viewportCache = context.spreadsheetViewportCache();

        final Optional<SpreadsheetCell> maybeCell = viewportCache.cell(
                context.historyToken()
                        .cast(SpreadsheetCellHistoryToken.class)
                        .anchoredSelection()
                        .selection()
        );
        if (maybeCell.isPresent()) {
            final SpreadsheetCell cell = maybeCell.get();
            parser = cell.parsePattern()
                    .map(SpreadsheetParsePattern::parser)
                    .orElse(null);
        }

        final SpreadsheetMetadata metadata = context.spreadsheetMetadata();
        if (null == parser) {
            parser = SpreadsheetParsers.valueOrExpression(
                    metadata.parser()
            );
        }
        final SpreadsheetParserContext parserContext = metadata.parserContext(
                context::now
        );

        return SpreadsheetFormula.parse(
                TextCursors.charSequence(text),
                parser,
                parserContext
        );
    }

    private final AppContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
