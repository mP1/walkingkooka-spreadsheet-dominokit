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

package walkingkooka.spreadsheet.dominokit.history;

import org.junit.Test;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.format.pattern.HasSpreadsheetPatternKindTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.expression.ExpressionNumberKind;

public abstract class SpreadsheetMetadataPropertyHistoryTokenTestCase<T extends SpreadsheetMetadataPropertyHistoryToken<V>, V> extends SpreadsheetMetadataHistoryTokenTestCase<T>
        implements HasSpreadsheetPatternKindTesting {

    final static SpreadsheetMetadataPropertyName<ExpressionNumberKind> EXPRESSION_NUMBER_KIND = SpreadsheetMetadataPropertyName.EXPRESSION_NUMBER_KIND;

    SpreadsheetMetadataPropertyHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testSetDifferentMetadataPropertyName() {
        final SpreadsheetMetadataPropertyName<?> different = SpreadsheetMetadataPropertyName.DATE_PARSE_PATTERN;

        this.setMetadataPropertyNameAndCheck(
                different,
                HistoryToken.metadataPropertySelect(
                        ID,
                        NAME,
                        different
                )
        );
    }

    abstract T createHistoryToken(final SpreadsheetId id,
                                  final SpreadsheetName name,
                                  final SpreadsheetMetadataPropertyName<V> propertyName);
}
