
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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.util.List;

/**
 * A {@link SpreadsheetPatternComponentTableComponentRowProvider} that always returns an empty {@link List} or no rows.
 */
final class SpreadsheetPatternComponentTableComponentRowProviderEmpty extends SpreadsheetPatternComponentTableComponentRowProvider {

    /**
     * Singleton
     */
    final static SpreadsheetPatternComponentTableComponentRowProviderEmpty INSTANCE = new SpreadsheetPatternComponentTableComponentRowProviderEmpty();

    private SpreadsheetPatternComponentTableComponentRowProviderEmpty() {
        super();
    }

    @Override
    public List<SpreadsheetPatternComponentTableComponentRow> apply(final String patternText,
                                                                    final SpreadsheetPatternComponentTableComponentRowProviderContext context) {
        return Lists.empty();
    }

    @Override
    public String toString() {
        return SpreadsheetPatternKind.TEXT_FORMAT_PATTERN.toString();
    }
}
