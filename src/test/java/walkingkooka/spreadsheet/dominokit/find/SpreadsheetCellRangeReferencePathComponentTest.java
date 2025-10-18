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

package walkingkooka.spreadsheet.dominokit.find;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReferencePath;

import java.util.Optional;

public final class SpreadsheetCellRangeReferencePathComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetCellRangeReferencePath, SpreadsheetCellRangeReferencePathComponent> {

    @Test
    public void testEmpty() {
        this.treePrintAndCheck(
            this.createComponent(),
            "SpreadsheetCellRangeReferencePathComponent\n" +
                "  SelectComponent\n" +
                "    [] id=Path123-\n" +
                "      LRTD\n" +
                "      RLTD\n" +
                "      LRBU\n" +
                "      RLBU\n" +
                "      TDLR\n" +
                "      TDRL\n" +
                "      BULR\n" +
                "      BURL\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(SpreadsheetCellRangeReferencePath.BULR)
                ),
            "SpreadsheetCellRangeReferencePathComponent\n" +
                "  SelectComponent\n" +
                "    [BULR] id=Path123-\n" +
                "      LRTD\n" +
                "      RLTD\n" +
                "      LRBU\n" +
                "      RLBU\n" +
                "      TDLR\n" +
                "      TDRL\n" +
                "      BULR\n" +
                "      BURL\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetCellRangeReferencePathComponent createComponent() {
        return SpreadsheetCellRangeReferencePathComponent.empty(
            "Path123-",
            new FakeSpreadsheetCellRangeReferencePathComponentContext() {
                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.parseString("/1/SpreadsheetName1/cell/A1");
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetCellRangeReferencePathComponent> type() {
        return SpreadsheetCellRangeReferencePathComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
