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

import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;

import java.util.OptionalInt;

/**
 * A collection of factory methods to create links that appear in the app layout.
 */
final class AppHistoryTokenAnchorComponents implements PublicStaticHelper {

    /**
     * A link that when clicked pushes a history token which shows the File browser dialog.
     */
    static HistoryTokenAnchorComponent files() {
        // TODO need to *READ* from and count
        return HistoryToken.spreadsheetListSelect(
                        OptionalInt.empty(), // from
                        OptionalInt.empty() // count
                )
                .link("files")
                .setTextContent("Files");
    }

    /**
     * Stop creation
     */
    private AppHistoryTokenAnchorComponents() {
        throw new UnsupportedOperationException();
    }
}
