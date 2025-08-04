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

package walkingkooka.spreadsheet.dominokit.link;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AnchorListComponentTest implements HtmlComponentTesting<AnchorListComponent, HTMLDivElement> {

    private final static String ID = "LinkList123-";

    // appendChild......................................................................................................


    @Test
    public void testAppendChildWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> AnchorListComponent.empty()
                .appendChild(null)
        );
    }

    @Test
    public void testAppendChild() {
        this.treePrintAndCheck(
            AnchorListComponent.empty()
                .appendChild(
                    HistoryTokenAnchorComponent.empty()
                        .setTextContent("Edit spreadsheet name")
                        .setHistoryToken(
                            Optional.of(
                                HistoryToken.spreadsheetSelect(
                                    SpreadsheetId.with(1),
                                    SpreadsheetName.with("SpreadsheetName222")
                                )
                            )
                        )
                ),
            "AnchorListComponent\n" +
                "  FlexLayoutComponent\n" +
                "    ROW\n" +
                "      \"Edit spreadsheet name\" [#/1/SpreadsheetName222]\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<AnchorListComponent> type() {
        return AnchorListComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
