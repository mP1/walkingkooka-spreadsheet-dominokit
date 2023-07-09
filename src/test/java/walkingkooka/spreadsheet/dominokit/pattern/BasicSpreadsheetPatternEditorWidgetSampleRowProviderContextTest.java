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

package walkingkooka.spreadsheet.dominokit.pattern;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.format.FakeSpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContexts;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicSpreadsheetPatternEditorWidgetSampleRowProviderContextTest implements ClassTesting<BasicSpreadsheetPatternEditorWidgetSampleRowProviderContext>,
        ToStringTesting<BasicSpreadsheetPatternEditorWidgetSampleRowProviderContext> {

    @Test
    public void testWithNullKindFails() {
        this.withFails(
                null,
                "",
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testWithNullPatternTextFails() {
        this.withFails(
                SpreadsheetPatternKind.TEXT_FORMAT_PATTERN,
                null,
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testWithNullSpreadsheetFormatterContextFails() {
        this.withFails(
                SpreadsheetPatternKind.TEXT_FORMAT_PATTERN,
                "",
                null
        );
    }

    private void withFails(final SpreadsheetPatternKind kind,
                           final String patternText,
                           final SpreadsheetFormatterContext spreadsheetFormatterContext) {
        assertThrows(
                NullPointerException.class,
                () -> BasicSpreadsheetPatternEditorWidgetSampleRowProviderContext.with(
                        kind,
                        patternText,
                        spreadsheetFormatterContext
                )
        );
    }

    @Test
    public void testWithInvalidPattern() {
        final SpreadsheetPatternKind kind = SpreadsheetPatternKind.TEXT_FORMAT_PATTERN;
        final String patternText = "Pattern123\"";
        final SpreadsheetFormatterContext spreadsheetFormatterContext = new FakeSpreadsheetFormatterContext() {
            @Override
            public Locale locale() {
                return Locale.forLanguageTag("EN-AU");
            }
        };

        final BasicSpreadsheetPatternEditorWidgetSampleRowProviderContext context = BasicSpreadsheetPatternEditorWidgetSampleRowProviderContext.with(
                kind,
                patternText,
                spreadsheetFormatterContext
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
                patternText,
                context.patternText(),
                "patternText"
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
        final String patternText = "Pattern123";

        this.toStringAndCheck(
                BasicSpreadsheetPatternEditorWidgetSampleRowProviderContext.with(
                        SpreadsheetPatternKind.DATE_FORMAT_PATTERN,
                        patternText,
                        SpreadsheetFormatterContexts.fake()
                ),
                patternText
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<BasicSpreadsheetPatternEditorWidgetSampleRowProviderContext> type() {
        return BasicSpreadsheetPatternEditorWidgetSampleRowProviderContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
