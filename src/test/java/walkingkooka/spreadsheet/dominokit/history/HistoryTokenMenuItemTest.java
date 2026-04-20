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
import walkingkooka.Cast;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;

import java.util.Optional;

public final class HistoryTokenMenuItemTest implements HashCodeEqualsDefinedTesting2<HistoryTokenMenuItem<HistoryToken>>,
    ToStringTesting<HistoryTokenMenuItem<HistoryToken>> {

    @Test
    public void testEqualsDifferentHistoryTokenAnchorComponent() {
        this.checkNotEquals(
            new HistoryTokenMenuItem<>(
                HistoryTokenAnchorComponent.empty()
                    .setHistoryToken(
                        Optional.of(
                            HistoryToken.parseString("/2/Different")
                        )
                    )
            )
        );
    }

    @Override
    public HistoryTokenMenuItem<HistoryToken> createObject() {
        return new HistoryTokenMenuItem<>(
            HistoryTokenAnchorComponent.empty()
                .setHistoryToken(
                    Optional.of(
                        HistoryToken.parseString("/1/SpreadsheetName111")
                    )
                )
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createObject(),
            "[#/1/SpreadsheetName111]"
        );
    }

    @Override
    public Class<HistoryTokenMenuItem<HistoryToken>> type() {
        return Cast.to(HistoryTokenMenuItem.class);
    }
}
