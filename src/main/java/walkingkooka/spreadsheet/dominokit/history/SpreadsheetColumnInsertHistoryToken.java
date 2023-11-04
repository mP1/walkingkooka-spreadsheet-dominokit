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

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

abstract class SpreadsheetColumnInsertHistoryToken extends SpreadsheetColumnHistoryToken {

    SpreadsheetColumnInsertHistoryToken(final SpreadsheetId id,
                                        final SpreadsheetName name,
                                        final AnchoredSpreadsheetSelection selection,
                                        final int count) {
        super(
                id,
                name,
                selection
        );

        if (count <= 0) {
            throw new IllegalArgumentException("Invalid count " + count + " <= 0");
        }
        this.count = count;
    }

    public final int count() {
        return this.count;
    }

    private final int count;

    final UrlFragment countUrlFragment() {
        return UrlFragment.SLASH.append(
                UrlFragment.with(
                        String.valueOf(this.count)
                )
        );
    }
}
