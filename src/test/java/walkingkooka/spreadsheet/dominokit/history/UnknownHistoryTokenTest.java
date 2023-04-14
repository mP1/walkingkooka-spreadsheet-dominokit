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
import walkingkooka.net.UrlFragment;

import java.util.Optional;

public final class UnknownHistoryTokenTest extends HistoryTokenTestCase<UnknownHistoryToken> {

    @Test
    public void testIdName() {
        this.idNameAndCheck(
                ID,
                NAME,
                HistoryToken.spreadsheetSelect(ID, NAME)
        );
    }

    @Test
    public void testViewportSelectionOrEmpty() {
        this.checkEquals(
                Optional.empty(),
                this.createHistoryToken().viewportSelectionOrEmpty()
        );
    }

    @Override
    UnknownHistoryToken createHistoryToken() {
        return UnknownHistoryToken.with(UrlFragment.with("hello"));
    }

    @Override
    public Class<UnknownHistoryToken> type() {
        return UnknownHistoryToken.class;
    }
}
