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

import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatter;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.util.Objects;

final class BasicSpreadsheetPatternEditorComponentSampleRowProviderContext implements SpreadsheetPatternEditorComponentSampleRowProviderContext {

    static BasicSpreadsheetPatternEditorComponentSampleRowProviderContext with(final SpreadsheetPatternKind kind,
                                                                               final SpreadsheetFormatterContext spreadsheetFormatterContext,
                                                                               final LoggingContext loggingContext) {
        Objects.requireNonNull(kind, "kind");
        Objects.requireNonNull(spreadsheetFormatterContext, "spreadsheetFormatterContext");
        Objects.requireNonNull(loggingContext, "loggingContext");

        return new BasicSpreadsheetPatternEditorComponentSampleRowProviderContext(
                kind,
                spreadsheetFormatterContext,
                loggingContext
        );
    }

    private BasicSpreadsheetPatternEditorComponentSampleRowProviderContext(final SpreadsheetPatternKind kind,
                                                                           final SpreadsheetFormatterContext spreadsheetFormatterContext,
                                                                           final LoggingContext loggingContext) {
        this.kind = kind;
        this.spreadsheetFormatterContext = spreadsheetFormatterContext;
        this.loggingContext = loggingContext;
    }

    @Override
    public SpreadsheetFormatter defaultSpreadsheetFormatter() {
        return this.kind()
                .formatter(
                        this.spreadsheetFormatterContext()
                                .locale()
                );
    }

    @Override
    public SpreadsheetPatternKind kind() {
        return this.kind;
    }

    private final SpreadsheetPatternKind kind;

    @Override
    public SpreadsheetFormatterContext spreadsheetFormatterContext() {
        return this.spreadsheetFormatterContext;
    }

    private final SpreadsheetFormatterContext spreadsheetFormatterContext;

    @Override
    public void debug(final Object... values) {
        this.loggingContext.debug(values);
    }

    @Override
    public void error(final Object... values) {
        this.loggingContext.error(values);
    }

    private final LoggingContext loggingContext;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.kind() + " " + this.spreadsheetFormatterContext() + " " + this.loggingContext;
    }
}
