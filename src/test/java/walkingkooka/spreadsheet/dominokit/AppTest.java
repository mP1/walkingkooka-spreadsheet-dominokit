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

import com.google.gwt.junit.client.GWTTestCase;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

public final class AppTest extends GWTTestCase {

    @Override
    public String getModuleName() {                                         // (2)
        return "walkingkooka.spreadsheet.dominokit.App";
    }

    public void testFireSpreadsheetMetadata() {
        this.fireSpreadsheetMetadataCounter = 0;

        final App app = new App();
        app.addSpreadsheetMetadataWatcher((d, c) -> this.fireSpreadsheetMetadataCounter++);
        app.addSpreadsheetMetadataWatcher((d, c) -> this.fireSpreadsheetMetadataCounter++);

        app.fireSpreadsheetMetadata(
                SpreadsheetMetadata.EMPTY
                        .set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, SpreadsheetId.with(1))
                        .set(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME, SpreadsheetName.with("Untitled123"))
        );
    }

    private int fireSpreadsheetMetadataCounter = 0;
}
