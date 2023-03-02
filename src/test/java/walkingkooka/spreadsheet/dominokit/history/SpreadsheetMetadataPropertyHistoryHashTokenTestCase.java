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

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.expression.ExpressionNumberKind;

public abstract class SpreadsheetMetadataPropertyHistoryHashTokenTestCase<T extends SpreadsheetMetadataPropertyHistoryHashToken<V>, V> extends SpreadsheetMetadataHistoryHashTokenTestCase<T> {

    final static SpreadsheetMetadataPropertyName<ExpressionNumberKind> EXPRESSION_NUMBER_KIND = SpreadsheetMetadataPropertyName.EXPRESSION_NUMBER_KIND;

    SpreadsheetMetadataPropertyHistoryHashTokenTestCase() {
        super();
    }

    final T createHistoryHashToken(final SpreadsheetId id,
                                   final SpreadsheetName name) {
        throw new UnsupportedOperationException();
    }

    abstract T createHistoryHashToken(final SpreadsheetId id,
                                      final SpreadsheetName name,
                                      final SpreadsheetMetadataPropertyName<V> propertyName);
}
