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

package walkingkooka.spreadsheet.dominokit.anchor;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.CanBeEmptyTesting;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.dom.HasFocusBlurEventListenerTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AnchorListComponentTest implements HtmlComponentTesting<AnchorListComponent, HTMLDivElement>,
    CanBeEmptyTesting,
    HasFocusBlurEventListenerTesting<AnchorListComponent>,
    ToStringTesting<AnchorListComponent> {

    private static final SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1);
    private static final SpreadsheetName SPREADSHEET_NAME = SpreadsheetName.with("SpreadsheetName111");

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
        final HistoryTokenAnchorComponent anchor = HistoryTokenAnchorComponent.empty()
            .setTextContent("Edit spreadsheet name")
            .setHistoryToken(
                Optional.of(
                    HistoryToken.spreadsheetSelect(
                        SPREADSHEET_ID,
                        SPREADSHEET_NAME
                    )
                )
            );

        this.treePrintAndCheck(
            AnchorListComponent.empty()
                .appendChild(anchor),
            "AnchorListComponent\n" +
                "  FlexLayoutComponent\n" +
                "    ROW\n" +
                "      \"Edit spreadsheet name\" [#/1/SpreadsheetName111]\n"
        );
    }

    // removeAllChildren................................................................................................

    @Test
    public void testRemoveAllChildren() {
        this.treePrintAndCheck(
            AnchorListComponent.empty()
                .appendChild(
                    HistoryTokenAnchorComponent.empty()
                        .setTextContent("Lost")
                        .setHistoryToken(
                            Optional.of(
                                HistoryToken.spreadsheetSelect(
                                    SPREADSHEET_ID,
                                    SPREADSHEET_NAME
                                )
                            )
                        )
                ).removeAllChildren()
                .appendChild(
                    HistoryTokenAnchorComponent.empty()
                        .setTextContent("Again")
                        .setHistoryToken(
                            Optional.of(
                                HistoryToken.spreadsheetSelect(
                                    SPREADSHEET_ID,
                                    SPREADSHEET_NAME
                                )
                            )
                        )
                ),
            "AnchorListComponent\n" +
                "  FlexLayoutComponent\n" +
                "    ROW\n" +
                "      \"Again\" [#/1/SpreadsheetName111]\n"
        );
    }

    // get..............................................................................................................

    @Test
    public void testGet() {
        final AnchorListComponent anchorListComponent = AnchorListComponent.empty();

        final AnchorComponent<?> child = HistoryTokenAnchorComponent.empty()
            .setTextContent("Child111");

        this.checkEquals(
            child,
            anchorListComponent.appendChild(child)
                .child(0)
        );
    }

    // children.........................................................................................................

    @Test
    public void testChildren() {
        final AnchorListComponent anchorListComponent = AnchorListComponent.empty();

        final AnchorComponent<?> child1 = HistoryTokenAnchorComponent.empty()
            .setTextContent("Child111");

        final AnchorComponent<?> child2 = HistoryTokenAnchorComponent.empty()
            .setTextContent("Child222");

        anchorListComponent.appendChild(child1)
            .appendChild(child2);

        this.checkEquals(
            Lists.of(
                child1,
                child2
            ),
            anchorListComponent.children()
        );
    }

    // HasFocusBlurEventListener........................................................................................

    @Override
    public AnchorListComponent createHasFocusBlurEventListener() {
        return AnchorListComponent.empty();
    }

    // CanBeEmpty.......................................................................................................

    @Test
    public void testIsEmptyWhenEmpty() {
        this.isEmptyAndCheck(
            AnchorListComponent.empty(),
            true
        );
    }

    @Test
    public void testIsEmptyWhenNotEmpty() {
        this.isEmptyAndCheck(
            AnchorListComponent.empty()
                .appendChild(
                    HistoryTokenAnchorComponent.empty()
                ),
            false
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            AnchorListComponent.empty()
                .appendChild(
                    HistoryTokenAnchorComponent.empty()
                        .setHistoryToken(
                            Optional.of(
                                HistoryToken.spreadsheetSelect(
                                    SPREADSHEET_ID,
                                    SPREADSHEET_NAME
                                )
                            )
                        )
                ),
            "ROW DIV [[#/1/SpreadsheetName111]]"
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
