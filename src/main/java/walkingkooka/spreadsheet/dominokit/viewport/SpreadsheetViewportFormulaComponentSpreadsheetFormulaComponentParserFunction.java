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

package walkingkooka.spreadsheet.dominokit.viewport;

import walkingkooka.locale.LocaleContexts;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.formula.SpreadsheetFormulaParsers;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;
import walkingkooka.spreadsheet.parser.SpreadsheetParserProvider;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link Function} that parsers any given text into a {@link SpreadsheetFormula}. The parser will be taken from an existing
 * {@link SpreadsheetCell#parser()} or falling back to {@link SpreadsheetMetadata#spreadsheetParser(SpreadsheetParserProvider, ProviderContext)} for the current {@link SpreadsheetCellReference}.
 */
final class SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction implements Function<String, SpreadsheetFormula> {

    public static SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction with(final SpreadsheetViewportFormulaComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction(context);
    }

    private SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction(final SpreadsheetViewportFormulaComponentContext context) {
        this.context = context;
    }

    @Override
    public SpreadsheetFormula apply(final String text) {
        Parser<SpreadsheetParserContext> parser = null;

        final SpreadsheetViewportFormulaComponentContext context = this.context;
        final SpreadsheetViewportCache viewportCache = context.spreadsheetViewportCache();

        final Optional<SpreadsheetCell> maybeCell = viewportCache.cell(
            context.historyToken()
                .cast(SpreadsheetAnchoredSelectionHistoryToken.class)
                .anchoredSelection()
                .selection()

        );
        if (maybeCell.isPresent()) {
            final SpreadsheetCell cell = maybeCell.get();
            parser = cell.parser()
                .map(p -> context.spreadsheetParser(
                        p,
                        context
                    )
                ).orElse(null);
        }

        final SpreadsheetMetadata metadata = context.spreadsheetMetadata();
        if (null == parser) {
            parser = SpreadsheetFormulaParsers.valueOrExpression(
                metadata.spreadsheetParser(
                    context, // SpreadsheetParserProvider
                    context // ProviderContext
                )
            );
        }
        final SpreadsheetParserContext parserContext = metadata.spreadsheetParserContext(
            maybeCell,
            LocaleContexts.fake(),
            context
        );

        return SpreadsheetFormula.parse(
            TextCursors.charSequence(text),
            parser,
            parserContext
        );
    }

    private final SpreadsheetViewportFormulaComponentContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
