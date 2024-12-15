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

import org.junit.jupiter.api.Test;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;

public abstract class SpreadsheetCellSaveHistoryTokenTestCase<T extends SpreadsheetCellSaveHistoryToken<?>> extends SpreadsheetCellHistoryTokenTestCase<T> {

    SpreadsheetCellSaveHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testMenuWithCell() {
        this.menuWithCellAndCheck();
    }

    final static JsonNodeMarshallContext MARSHALL_CONTEXT = JsonNodeMarshallContexts.basic();

    // patternKind......................................................................................................

    @Test
    public final void testPatternKind() {
        this.patternKindAndCheck(
                this.createHistoryToken()
        );
    }
}
