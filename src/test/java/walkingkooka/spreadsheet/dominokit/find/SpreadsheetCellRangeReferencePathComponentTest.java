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
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReferencePath;

import java.util.Optional;

public final class SpreadsheetCellRangeReferencePathComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetCellRangeReferencePath, SpreadsheetCellRangeReferencePathComponent> {

    @Test
    public void testEmpty() {
        this.treePrintAndCheck(
            SpreadsheetCellRangeReferencePathComponent.empty(),
            "SpreadsheetCellRangeReferencePathComponent\n" +
                "  SpreadsheetSelectComponent\n" +
                "    []\n" +
                "      left-right top-down=LRTD\n" +
                "      right-left top-down=RLTD\n" +
                "      left-right bottom-up=LRBU\n" +
                "      right-left bottom-up=RLBU\n" +
                "      top-down left-right=TDLR\n" +
                "      top-down right-left=TDRL\n" +
                "      bottom-up left-right=BULR\n" +
                "      bottom-up right-left=BURL\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            SpreadsheetCellRangeReferencePathComponent.empty()
                .setValue(
                    Optional.of(SpreadsheetCellRangeReferencePath.BULR)
                ),
            "SpreadsheetCellRangeReferencePathComponent\n" +
                "  SpreadsheetSelectComponent\n" +
                "    [BULR]\n" +
                "      left-right top-down=LRTD\n" +
                "      right-left top-down=RLTD\n" +
                "      left-right bottom-up=LRBU\n" +
                "      right-left bottom-up=RLBU\n" +
                "      top-down left-right=TDLR\n" +
                "      top-down right-left=TDRL\n" +
                "      bottom-up left-right=BULR\n" +
                "      bottom-up right-left=BURL\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetCellRangeReferencePathComponent createComponent() {
        return SpreadsheetCellRangeReferencePathComponent.empty();
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
