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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.spreadsheet.SpreadsheetErrorKind;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.util.FunctionTesting;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunctionTest implements FunctionTesting<SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction, String, SpreadsheetFormula> {

    private final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.BIG_DECIMAL;

    private final static SpreadsheetMetadata METADATA = SpreadsheetMetadata.EMPTY
            .set(SpreadsheetMetadataPropertyName.LOCALE, Locale.forLanguageTag("EN-AU"))
            .loadFromLocale()
            .set(SpreadsheetMetadataPropertyName.EXPRESSION_NUMBER_KIND, EXPRESSION_NUMBER_KIND)
            .set(SpreadsheetMetadataPropertyName.PRECISION, 10)
            .set(SpreadsheetMetadataPropertyName.ROUNDING_MODE, RoundingMode.HALF_UP)
            .set(SpreadsheetMetadataPropertyName.DEFAULT_YEAR, 50)
            .set(SpreadsheetMetadataPropertyName.TWO_DIGIT_YEAR, 50);

    private final static Supplier<LocalDateTime> NOW = LocalDateTime::now;

    @Test
    public void testUnknownLabelFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction.with(
                        this.appContext(
                                SpreadsheetSelection.labelName("Unknown"),
                                SpreadsheetMetadata.EMPTY
                        )
                ).apply("=1")
        );
    }

    @Test
    public void testInvalidExpression() {
        final String text = "=1+";

        this.applyAndCheck(
                SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction.with(
                        this.appContext(
                                SpreadsheetSelection.A1,
                                METADATA
                        )
                ),
                text,
                SpreadsheetFormula.EMPTY.setText(text)
                        .setValue(
                                Optional.of(
                                        SpreadsheetErrorKind.ERROR.setMessage("End of text at (4,1) \"=1+\" expected BINARY_SUB_EXPRESSION")
                                )
                        )
        );
    }

    @Test
    public void testCellMissingWithNumber() {
        this.applyAndCheck2(
                SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction.with(
                        this.appContext(
                                SpreadsheetSelection.A1,
                                METADATA
                        )
                ),
                "=1.5"
        );
    }

    @Test
    public void testCellMissingWithDate() {
        this.applyAndCheck2(
                SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction.with(
                        this.appContext(
                                SpreadsheetSelection.A1,
                                METADATA
                        )
                ),
                "31/12/2023"
        );
    }

    @Test
    public void testCellMissingWithDateTime() {
        this.applyAndCheck2(
                SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction.with(
                        this.appContext(
                                SpreadsheetSelection.A1,
                                METADATA
                        )
                ),
                "31/12/2023, 12:58:59 AM"
        );
    }

    @Test
    public void testCellMissingWithTime() {
        this.applyAndCheck2(
                SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction.with(
                        this.appContext(
                                SpreadsheetSelection.A1,
                                METADATA
                        )
                ),
                "12:58:59"
        );
    }

    @Test
    public void testCellMissingWithCellReference() {
        this.applyAndCheck2(
                SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction.with(
                        this.appContext(
                                SpreadsheetSelection.A1,
                                METADATA
                        )
                ),
                "=1+B2"
        );
    }

    @Test
    public void testCellMissingWithLabelReference() {
        this.applyAndCheck2(
                SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction.with(
                        this.appContext(
                                SpreadsheetSelection.A1,
                                METADATA
                        )
                ),
                "=1+Label123"
        );
    }

    @Test
    public void testCellMissingWithFunction() {
        this.applyAndCheck2(
                SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction.with(
                        this.appContext(
                                SpreadsheetSelection.A1,
                                METADATA
                        )
                ),
                "=1+test123()"
        );
    }

    @Test
    public void testCellPresentWithoutParsePattern() {
        final SpreadsheetCellReference cellReference = SpreadsheetSelection.parseCell("B2");
        final AppContext context = this.appContext(
                cellReference,
                METADATA
        );
        final SpreadsheetViewportCache viewportCache = context.viewportCache();

        viewportCache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY.setCells(
                        Sets.of(
                                cellReference.setFormula(
                                        SpreadsheetFormula.EMPTY
                                )
                        )
                ),
                new FakeAppContext()
        );

        final SpreadsheetMetadata metadata = METADATA.set(
                SpreadsheetMetadataPropertyName.NUMBER_PARSE_PATTERN,
                SpreadsheetPattern.parseNumberParsePattern("$0.00")
        );

        final String text = "$1.23";

        this.applyAndCheck(
                SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction.with(
                        this.appContext(
                                cellReference,
                                metadata
                        )
                ),
                text,
                SpreadsheetFormula.parse(
                        TextCursors.charSequence(text),
                        metadata.parser(),
                        metadata.parserContext(NOW)
                )
        );
    }

    @Test
    public void testCellPresentUsingParsePattern() {
        final SpreadsheetCellReference cellReference = SpreadsheetSelection.parseCell("B2");
        final AppContext context = this.appContext(
                cellReference,
                METADATA
        );
        final SpreadsheetViewportCache viewportCache = context.viewportCache();
        final SpreadsheetParsePattern pattern = SpreadsheetPattern.parseNumberParsePattern("$0.00");

        viewportCache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY.setCells(
                        Sets.of(
                                cellReference.setFormula(
                                        SpreadsheetFormula.EMPTY
                                ).setParsePattern(
                                        Optional.of(
                                                pattern
                                        )
                                )
                        )
                ),
                new FakeAppContext()
        );

        final String text = "$4.56";

        this.applyAndCheck(
                SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction.with(
                        context
                ),
                text,
                SpreadsheetFormula.parse(
                        TextCursors.charSequence(text),
                        pattern.parser(),
                        METADATA.parserContext(NOW)
                )
        );
    }

    @Test
    public void testLabelPresent() {
        final SpreadsheetCellReference cellReference = SpreadsheetSelection.parseCell("B2");
        final SpreadsheetLabelName label = SpreadsheetSelection.labelName("Label123");

        final AppContext context = this.appContext(
                label,
                METADATA
        );
        final SpreadsheetViewportCache viewportCache = context.viewportCache();
        final SpreadsheetParsePattern pattern = SpreadsheetPattern.parseNumberParsePattern("$0.00");

        viewportCache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY.setCells(
                        Sets.of(
                                cellReference.setFormula(
                                        SpreadsheetFormula.EMPTY
                                ).setParsePattern(
                                        Optional.of(pattern)
                                )
                        )
                ).setLabels(
                        Sets.of(
                                label.mapping(cellReference)
                        )
                ),
                new FakeAppContext()
        );
        final String text = "$7.89";

        this.applyAndCheck(
                SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction.with(
                        context
                ),
                text,
                SpreadsheetFormula.parse(
                        TextCursors.charSequence(text),
                        pattern.parser(),
                        METADATA.parserContext(NOW)
                )
        );
    }

    private void applyAndCheck2(final SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction function,
                                final String text) {
        this.applyAndCheck(
                function,
                text,
                SpreadsheetFormula.parse(
                        TextCursors.charSequence(text),
                        METADATA.parser(),
                        METADATA.parserContext(NOW)
                )
        );
    }

    @Override
    public SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction createFunction() {
        return SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction.with(
                new FakeAppContext()
        );
    }

    private AppContext appContext(final SpreadsheetSelection selection,
                                  final SpreadsheetMetadata metadata) {
        return new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetDeltaWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetMetadataWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return HistoryToken.selection(
                        SpreadsheetId.with(1),
                        SpreadsheetName.with("Spreadsheet123"),
                        selection.setDefaultAnchor()
                );
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return metadata;
            }

            @Override
            public SpreadsheetViewportCache viewportCache() {
                return viewportCache;
            }

            private final SpreadsheetViewportCache viewportCache = SpreadsheetViewportCache.empty(this);
        };
    }

    @Override
    public Class<SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction> type() {
        return SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction.class;
    }
}
