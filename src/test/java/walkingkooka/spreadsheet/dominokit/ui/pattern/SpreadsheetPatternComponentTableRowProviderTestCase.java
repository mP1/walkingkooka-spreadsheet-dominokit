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

import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.util.BiFunctionTesting;

import java.util.List;
import java.util.stream.Collectors;

public abstract class SpreadsheetPatternComponentTableRowProviderTestCase<T extends SpreadsheetPatternComponentTableRowProvider> implements
        BiFunctionTesting<T, String, SpreadsheetPatternComponentTableRowProviderContext, List<SpreadsheetPatternComponentTableRow>>,
        ClassTesting<T>,
        ToStringTesting<T>,
        TreePrintableTesting {

    final static LoggingContext LOGGING_CONTEXT = new LoggingContext() {
        @Override
        public void debug(final Object... values) {
            // nop
        }

        @Override
        public void error(final Object... values) {
            // nop
        }
    };

    SpreadsheetPatternComponentTableRowProviderTestCase() {
        super();
    }

    final void applyAndCheckFirst(final String patternText,
                                  final SpreadsheetPatternComponentTableRowProviderContext context,
                                  final SpreadsheetPatternComponentTableRow expected) {
        this.applyAndCheck(
                patternText,
                context,
                Lists.of(expected)
        );
    }

    final void applyAndCheck(final String patternText,
                             final SpreadsheetPatternComponentTableRowProviderContext context,
                             final SpreadsheetPatternComponentTableRow... expected) {
        this.applyAndCheck(
                patternText,
                context,
                Lists.of(expected)
        );
    }

    final void applyAndCheck(final T provider,
                             final String patternText,
                             final SpreadsheetPatternComponentTableRowProviderContext context,
                             final SpreadsheetPatternComponentTableRow... expected) {
        this.applyAndCheck(
                provider,
                patternText,
                context,
                Lists.of(expected)
        );
    }

    final void applyAndCheck2(final String patternText,
                              final SpreadsheetPatternComponentTableRowProviderContext context,
                              final String rows) {
        this.applyAndCheck2(
                this.createProvider(),
                patternText,
                context,
                rows
        );
    }

    final void applyAndCheck2(final T provider,
                              final String patternText,
                              final SpreadsheetPatternComponentTableRowProviderContext context,
                              final String rows) {
        this.checkEquals(
                rows,
                provider.apply(
                                patternText,
                                context
                        ).stream()
                        .map(t -> t.toString().replace("\r\n", "\\r\\n"))
                        .collect(Collectors.joining(LineEnding.NL))
        );
    }

    abstract T createProvider();

    @Override
    public final T createBiFunction() {
        return this.createProvider();
    }

    // ClassTesting.....................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
