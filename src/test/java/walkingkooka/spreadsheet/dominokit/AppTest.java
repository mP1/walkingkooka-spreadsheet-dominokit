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
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

public final class AppTest extends GWTTestCase {

    @Override
    public String getModuleName() {                                         // (2)
        return "walkingkooka.spreadsheet.dominokit.App";
    }

    public void testFireSpreadsheetDelta() {
        this.fireSpreadsheetDeltaCounter = 0;

        final App app = new App();
        app.addSpreadsheetDeltaWatcher((d, c) -> this.fireSpreadsheetDeltaCounter++);
        app.addSpreadsheetDeltaWatcher((d, c) -> this.fireSpreadsheetDeltaCounter++);

        app.fireSpreadsheetDelta(SpreadsheetDelta.EMPTY);
    }

    private int fireSpreadsheetDeltaCounter = 0;

    public void testFireSpreadsheetMetadata() {
        this.fireSpreadsheetMetadataCounter = 0;

        final App app = new App();
        app.addSpreadsheetMetadataWatcher((d) -> this.fireSpreadsheetMetadataCounter++);
        app.addSpreadsheetMetadataWatcher((d) -> this.fireSpreadsheetMetadataCounter++);

        app.fireSpreadsheetMetadata(SpreadsheetMetadata.EMPTY);
    }

    private int fireSpreadsheetMetadataCounter = 0;
}
