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

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.format.SpreadsheetFormatters;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetTextFormatPattern;

import java.util.List;
import java.util.Optional;

/**
 * A {@link SpreadsheetPatternComponentTableRowProvider} for {@link SpreadsheetTextFormatPattern}.
 * Currently it provides two samples, one for the current pattern and the second the default text format pattern.
 */
final class SpreadsheetPatternComponentTableRowProviderTextFormat extends SpreadsheetPatternComponentTableRowProvider {

    /**
     * Singleton
     */
    final static SpreadsheetPatternComponentTableRowProviderTextFormat INSTANCE = new SpreadsheetPatternComponentTableRowProviderTextFormat();

    private SpreadsheetPatternComponentTableRowProviderTextFormat() {
        super();
    }

    @Override
    public List<SpreadsheetPatternComponentTableRow> apply(final String patternText,
                                                           final SpreadsheetPatternComponentTableRowProviderContext context) {
        final String value = "Abc123";

        return Lists.of(
                SpreadsheetPatternComponentTableRow.with(
                        LABEL,
                        tryParsePatternText(
                                patternText,
                                SpreadsheetPattern::parseTextFormatPattern
                        ),
                        Lists.of(
                                context.format(
                                        tryParsePatternText(
                                                patternText,
                                                SpreadsheetPattern::parseTextFormatPattern
                                        ).map(SpreadsheetPattern::formatter)
                                                .orElse(SpreadsheetFormatters.emptyText()),
                                        value
                                ).toTextNode()
                        )
                ),
                SpreadsheetPatternComponentTableRow.with(
                        "Default text format",
                        DEFAULT_TEXT_FORMAT_PATTERN,
                        Lists.of(
                                context.format(
                                        SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN.formatter(),
                                        value
                                ).toTextNode()
                        )
                )
        );
    }

    private final static Optional<SpreadsheetPattern> DEFAULT_TEXT_FORMAT_PATTERN = Optional.of(
            SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
    );

    @Override
    public String toString() {
        return SpreadsheetPatternKind.TEXT_FORMAT_PATTERN.toString();
    }
}
