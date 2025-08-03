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

package walkingkooka.spreadsheet.dominokit.dom;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.net.Url;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;

public final class SpreadsheetDivComponentTest implements HtmlElementComponentTesting<walkingkooka.spreadsheet.dominokit.dom.SpreadsheetDivComponent, HTMLDivElement> {

    @Test
    public void testEmpty() {
        this.treePrintAndCheck(
            walkingkooka.spreadsheet.dominokit.dom.SpreadsheetDivComponent.empty(),
            "SpreadsheetDivComponent\n"
        );
    }

    @Test
    public void testAppendChild() {
        this.treePrintAndCheck(
            walkingkooka.spreadsheet.dominokit.dom.SpreadsheetDivComponent.empty()
                .appendChild(
                    HistoryTokenAnchorComponent.empty()
                        .setHref(Url.parseAbsolute("https://example.com/123"))
                ),
            "SpreadsheetDivComponent\n" +
                "  [https://example.com/123]"
        );
    }
    // class............................................................................................................

    @Override
    public Class<walkingkooka.spreadsheet.dominokit.dom.SpreadsheetDivComponent> type() {
        return SpreadsheetDivComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
