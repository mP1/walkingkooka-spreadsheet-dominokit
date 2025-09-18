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

package walkingkooka.spreadsheet.dominokit.meta;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.List;

public final class SpreadsheetMetadataPropertyNameTabsComponentTest implements HtmlComponentTesting<SpreadsheetMetadataPropertyNameTabsComponent, HTMLDivElement> {

    private final static String ID = "id123-";

    @Test
    public void testFormatters() {
        this.treePrintAndCheck(
            tabs(
                Cast.to(
                    SpreadsheetMetadataPropertyName.formatters()
                )
            ),
            "SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "  SpreadsheetTabsComponent\n" +
                "    TAB 0\n" +
                "      \"Date\" DISABLED id=id123-dateFormatter\n" +
                "    TAB 1\n" +
                "      \"Date Time\" DISABLED id=id123-dateTimeFormatter\n" +
                "    TAB 2\n" +
                "      \"Error\" DISABLED id=id123-errorFormatter\n" +
                "    TAB 3\n" +
                "      \"Number\" DISABLED id=id123-numberFormatter\n" +
                "    TAB 4\n" +
                "      \"Text\" DISABLED id=id123-textFormatter\n" +
                "    TAB 5\n" +
                "      \"Time\" DISABLED id=id123-timeFormatter\n"
        );
    }

    @Test
    public void testParser() {
        this.treePrintAndCheck(
            tabs(
                Cast.to(
                    SpreadsheetMetadataPropertyName.parsers()
                )
            ),
            "SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "  SpreadsheetTabsComponent\n" +
                "    TAB 0\n" +
                "      \"Date\" DISABLED id=id123-dateParser\n" +
                "    TAB 1\n" +
                "      \"Date Time\" DISABLED id=id123-dateTimeParser\n" +
                "    TAB 2\n" +
                "      \"Number\" DISABLED id=id123-numberParser\n" +
                "    TAB 3\n" +
                "      \"Time\" DISABLED id=id123-timeParser\n"
        );
    }

    private SpreadsheetMetadataPropertyNameTabsComponent tabs(final List<SpreadsheetMetadataPropertyName<?>> propertyNames) {
        return SpreadsheetMetadataPropertyNameTabsComponent.empty(
            ID,
            propertyNames,
            new FakeSpreadsheetMetadataPropertyNameTabsComponentContext()
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetMetadataPropertyNameTabsComponent> type() {
        return SpreadsheetMetadataPropertyNameTabsComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
