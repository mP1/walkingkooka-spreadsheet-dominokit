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

import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class SpreadsheetCellSaveMapHistoryTokenTestCase<T extends SpreadsheetCellSaveMapHistoryToken<?>> extends SpreadsheetCellSaveHistoryTokenTestCase<T> {

    SpreadsheetCellSaveMapHistoryTokenTestCase() {
        super();
    }

    static String marshallMap(final Map<SpreadsheetCellReference, ?> cellToValue) {
        return MARSHALL_CONTEXT.marshallMap(
                cellToValue.entrySet()
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        entry -> entry.getKey().toString(),
                                        entry -> entry.getValue()
                                )
                        )
        ).toString();
    }

    static String marshallMapWithTypedValues(final Map<SpreadsheetCellReference, ?> cellToValue) {
        return MARSHALL_CONTEXT.marshallMap(
                cellToValue.entrySet()
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        entry -> entry.getKey().toString(),
                                        entry -> MARSHALL_CONTEXT.marshallWithType(
                                                entry.getValue()
                                        )
                                )
                        )
        ).toString();
    }
}
