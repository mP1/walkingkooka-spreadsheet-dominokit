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

package walkingkooka.spreadsheet.dominokit.format;

import org.junit.jupiter.api.Test;
import walkingkooka.Context;
import walkingkooka.ContextTesting;
import walkingkooka.Either;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.format.FakeSpreadsheetFormatter;
import walkingkooka.spreadsheet.format.FakeSpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatter;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatters;
import walkingkooka.spreadsheet.format.SpreadsheetText;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatTextParserToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.tree.text.TextNode;

import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetPatternComponentTableRowProviderContextTest implements ContextTesting<SpreadsheetPatternComponentTableRowProviderContext> {

    @Test
    public void testDefaultFormat() {
        final String text = "Abc123";

        this.checkEquals(
                SpreadsheetText.with(text)
                        .toTextNode(),
                this.createContext().defaultFormat(text)
        );
    }

    private final static Color RED = Color.parse("#f01");

    @Test
    public void testDefaultFormat2() {
        final String text = "Abc123";

        this.checkEquals(
                SpreadsheetText.with(
                        text + text + text
                ).setColor(
                        Optional.of(RED)
                ).toTextNode(),
                new FakeSpreadsheetPatternComponentTableRowProviderContext() {
                    @Override
                    public SpreadsheetFormatter defaultSpreadsheetFormatter() {
                        return SpreadsheetFormatters.text(
                                SpreadsheetPattern.parseTextFormatPattern("[RED]@@@")
                                        .value()
                                        .children()
                                        .get(0)
                                        .cast(SpreadsheetFormatTextParserToken.class)
                        );
                    }

                    @Override
                    public SpreadsheetFormatterContext spreadsheetFormatterContext() {
                        return new TestSpreadsheetFormatterContext();
                    }
                }.defaultFormat(text)
        );
    }

    @Test
    public void testFormat() {
        final String text = "Abc123";
        final TextNode expected = SpreadsheetText.with(
                text + text + text
        ).setColor(
                Optional.of(RED)
        ).toTextNode();

        this.checkEquals(
                expected,
                new FakeSpreadsheetPatternComponentTableRowProviderContext() {

                    @Override
                    public SpreadsheetFormatterContext spreadsheetFormatterContext() {
                        return new TestSpreadsheetFormatterContext();
                    }
                }.format(
                        new FakeSpreadsheetFormatter() {

                            @Override
                            public Optional<TextNode> format(final Object value,
                                                             final SpreadsheetFormatterContext context) {
                                checkEquals(text, value);
                                return Optional.of(expected);
                            }
                        },
                        text
                )
        );
    }

    @Override
    public void testCheckToStringOverridden() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetPatternComponentTableRowProviderContext createContext() {
        return new FakeSpreadsheetPatternComponentTableRowProviderContext() {

            @Override
            public SpreadsheetFormatter defaultSpreadsheetFormatter() {
                return SpreadsheetPatternKind.TEXT_FORMAT_PATTERN.formatter(
                        Locale.forLanguageTag("EN-AU")
                );
            }

            @Override
            public SpreadsheetFormatterContext spreadsheetFormatterContext() {
                return new TestSpreadsheetFormatterContext();
            }
        };
    }

    static class TestSpreadsheetFormatterContext extends FakeSpreadsheetFormatterContext {

        @Override
        public boolean canConvert(final Object value,
                                  final Class<?> type) {
            return value instanceof String && String.class == type;
        }

        @Override
        public <T> Either<T, String> convert(final Object value,
                                             final Class<T> target) {
            this.canConvertOrFail(value, target);
            return this.successfulConversion(
                    value,
                    target
            );
        }

        @Override
        public Optional<Color> colorName(final SpreadsheetColorName name) {
            return Optional.of(RED);
        }
    }

    @Override
    public Class<SpreadsheetPatternComponentTableRowProviderContext> type() {
        return SpreadsheetPatternComponentTableRowProviderContext.class;
    }

    @Override public String typeNameSuffix() {
        return Context.class.getSimpleName();
    }
}
