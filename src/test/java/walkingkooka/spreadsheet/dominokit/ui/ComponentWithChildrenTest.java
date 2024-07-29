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

package walkingkooka.spreadsheet.dominokit.ui;

import org.junit.jupiter.api.Test;
import walkingkooka.CanBeEmptyTesting;
import walkingkooka.text.printer.TreePrintableTesting;

public final class ComponentWithChildrenTest implements TreePrintableTesting,
        CanBeEmptyTesting<SpreadsheetCard> {

    @Test
    public void testCanBeEmptyWithNoChildren() {
        this.isEmptyAndCheck(
                SpreadsheetCard.empty(),
                true
        );
    }

    @Test
    public void testCanBeEmptyWithOnlyEmptyChildren() {
        this.isEmptyAndCheck(
                SpreadsheetCard.empty()
                        .appendChild(
                                SpreadsheetCard.empty()
                        ).appendChild(
                                SpreadsheetCard.empty()
                        ),
                true
        );
    }

    @Test
    public void testCanBeEmptyWithMixture() {
        this.isEmptyAndCheck(
                SpreadsheetCard.empty()
                        .appendChild(
                                SpreadsheetCard.empty()
                        ).appendChild(
                                SpreadsheetCard.empty()
                        ).appendChild(
                                SpreadsheetTextBox.empty()
                        ),
                false
        );
    }

    @Test
    public void testCanBeEmptyWithNonEmpty() {
        this.isEmptyAndCheck(
                SpreadsheetCard.empty()
                        .appendChild(
                                SpreadsheetCard.empty()
                                        .appendChild(
                                                SpreadsheetTextBox.empty()
                                        )
                        ),
                false
        );
    }

    @Test
    public void testCanBeEmptyWithNonEmpty2() {
        this.isEmptyAndCheck(
                SpreadsheetCard.empty()
                        .appendChild(
                                SpreadsheetTextBox.empty()
                        ).appendChild(
                                SpreadsheetTextBox.empty()
                        ).appendChild(
                                SpreadsheetTextBox.empty()
                        ),
                false
        );
    }

    @Override
    public SpreadsheetCard createCanBeEmpty() {
        return SpreadsheetCard.empty();
    }
}
