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

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContexts;
import walkingkooka.spreadsheet.format.FakeSpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContexts;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicSpreadsheetPatternComponentSampleRowProviderContextTest implements ClassTesting<BasicSpreadsheetPatternComponentSampleRowProviderContext>,
        ToStringTesting<BasicSpreadsheetPatternComponentSampleRowProviderContext> {

    @Test
    public void testWithNullKindFails() {
        this.withFails(
                null,
                SpreadsheetFormatterContexts.fake(),
                LoggingContexts.fake()
        );
    }

    @Test
    public void testWithNullSpreadsheetFormatterContextFails() {
        this.withFails(
                SpreadsheetPatternKind.TEXT_FORMAT_PATTERN,
                null,
                LoggingContexts.fake()
        );
    }

    @Test
    public void testWithNullLoggingContextFails() {
        this.withFails(
                SpreadsheetPatternKind.TEXT_FORMAT_PATTERN,
                SpreadsheetFormatterContexts.fake(),
                null
        );
    }

    private void withFails(final SpreadsheetPatternKind kind,
                           final SpreadsheetFormatterContext spreadsheetFormatterContext,
                           final LoggingContext loggingContext) {
        assertThrows(
                NullPointerException.class,
                () -> BasicSpreadsheetPatternComponentSampleRowProviderContext.with(
                        kind,
                        spreadsheetFormatterContext,
                        loggingContext
                )
        );
    }

    @Test
    public void testWith() {
        final SpreadsheetPatternKind kind = SpreadsheetPatternKind.TEXT_FORMAT_PATTERN;
        final SpreadsheetFormatterContext spreadsheetFormatterContext = new FakeSpreadsheetFormatterContext() {
            @Override
            public Locale locale() {
                return Locale.forLanguageTag("EN-AU");
            }
        };

        final BasicSpreadsheetPatternComponentSampleRowProviderContext context = BasicSpreadsheetPatternComponentSampleRowProviderContext.with(
                kind,
                spreadsheetFormatterContext,
                LoggingContexts.fake()
        );

        this.checkNotEquals(
                null,
                context.defaultSpreadsheetFormatter(),
                "defaultSpreadsheetFormatter"
        );

        this.checkEquals(
                kind,
                context.kind(),
                "kind"
        );
        this.checkEquals(
                spreadsheetFormatterContext,
                context.spreadsheetFormatterContext(),
                "spreadsheetFormatterContext"
        );
    }

    // ToString..........................................................................................................

    @Test
    public void testToString() {
        final SpreadsheetPatternKind kind = SpreadsheetPatternKind.DATE_FORMAT_PATTERN;
        final SpreadsheetFormatterContext context = SpreadsheetFormatterContexts.fake();
        final LoggingContext loggingContext = LoggingContexts.fake();

        this.toStringAndCheck(
                BasicSpreadsheetPatternComponentSampleRowProviderContext.with(
                        kind,
                        context,
                        loggingContext
                ),
                kind + " " + context + " " + loggingContext
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<BasicSpreadsheetPatternComponentSampleRowProviderContext> type() {
        return BasicSpreadsheetPatternComponentSampleRowProviderContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
