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

package walkingkooka.spreadsheet.dominokit;

import org.junit.jupiter.api.Test;
import walkingkooka.CanBeEmptyTesting;
import walkingkooka.spreadsheet.dominokit.card.CardComponent;
import walkingkooka.spreadsheet.dominokit.text.TextBoxComponent;
import walkingkooka.text.printer.TreePrintableTesting;

public final class ComponentWithChildrenTest implements TreePrintableTesting,
    CanBeEmptyTesting {

    @Test
    public void testCanBeEmptyWithNoChildren() {
        this.isEmptyAndCheck(
            CardComponent.empty(),
            true
        );
    }

    @Test
    public void testCanBeEmptyWithOnlyEmptyChildren() {
        this.isEmptyAndCheck(
            CardComponent.empty()
                .appendChild(
                    CardComponent.empty()
                ).appendChild(
                    CardComponent.empty()
                ),
            true
        );
    }

    @Test
    public void testCanBeEmptyWithMixture() {
        this.isEmptyAndCheck(
            CardComponent.empty()
                .appendChild(
                    CardComponent.empty()
                ).appendChild(
                    CardComponent.empty()
                ).appendChild(
                    TextBoxComponent.empty()
                ),
            false
        );
    }

    @Test
    public void testCanBeEmptyWithNonEmpty() {
        this.isEmptyAndCheck(
            CardComponent.empty()
                .appendChild(
                    CardComponent.empty()
                        .appendChild(
                            TextBoxComponent.empty()
                        )
                ),
            false
        );
    }

    @Test
    public void testCanBeEmptyWithNonEmpty2() {
        this.isEmptyAndCheck(
            CardComponent.empty()
                .appendChild(
                    TextBoxComponent.empty()
                ).appendChild(
                    TextBoxComponent.empty()
                ).appendChild(
                    TextBoxComponent.empty()
                ),
            false
        );
    }
}
