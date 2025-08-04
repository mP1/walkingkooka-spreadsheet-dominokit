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
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCardLinkListComponentTest implements HtmlComponentTesting<SpreadsheetCardLinkListComponent, HTMLDivElement> {

    private final static String ID = "LinkList123-";

    private final static String TITLE = "Title123";

    private final static Function<String, String> LABEL_MAKER = (text) -> "Label-" + text;

    // with.............................................................................................................

    @Test
    public void testWithNullIdFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCardLinkListComponent.with(
                null,
                TITLE,
                LABEL_MAKER
            )
        );
    }

    @Test
    public void testWithEmptyIdFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetCardLinkListComponent.with(
                "",
                TITLE,
                LABEL_MAKER
            )
        );
    }

    @Test
    public void testWithNullTitleFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCardLinkListComponent.with(
                ID,
                null,
                LABEL_MAKER
            )
        );
    }

    @Test
    public void testWithNullLabelMakerFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCardLinkListComponent.with(
                ID,
                TITLE,
                null
            )
        );
    }

    // refresh..........................................................................................................

    @Test
    public void testRefreshWhenEmpty() {
        this.refreshAndCheck(
            Lists.empty(),
            Optional.empty(), // no selection
            "SpreadsheetCardLinkListComponent\n"
        );
    }

    @Test
    public void testRefreshWithSelectionAndWhenNotEmpty() {
        this.refreshAndCheck(
            Lists.of(
                "apple1",
                "banana2",
                "carrot3"
            ),
            Optional.of(
                SpreadsheetFormatterName.with("banana2")
            ),
            "SpreadsheetCardLinkListComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Title123\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"Label-apple1\" [#/1/Spreadsheet123/cell/A1/formatter/save/save%20apple1] id=LinkList123-0-Link\n" +
                "            \"Label-banana2\" DISABLED id=LinkList123-1-Link\n" +
                "            \"Label-carrot3\" [#/1/Spreadsheet123/cell/A1/formatter/save/save%20carrot3] id=LinkList123-2-Link\n"
        );
    }

    @Test
    public void testRefreshWhenNotEmpty() {
        this.refreshAndCheck(
            Lists.of(
                "apple1",
                "banana2",
                "carrot3"
            ),
            Optional.empty(),
            "SpreadsheetCardLinkListComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Title123\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"Label-apple1\" [#/1/Spreadsheet123/cell/A1/formatter/save/save%20apple1] id=LinkList123-0-Link\n" +
                "            \"Label-banana2\" [#/1/Spreadsheet123/cell/A1/formatter/save/save%20banana2] id=LinkList123-1-Link\n" +
                "            \"Label-carrot3\" [#/1/Spreadsheet123/cell/A1/formatter/save/save%20carrot3] id=LinkList123-2-Link\n"
        );
    }

    private void refreshAndCheck(final List<String> texts,
                                 final Optional<SpreadsheetFormatterName> selected,
                                 final String expected) {
        final SpreadsheetCardLinkListComponent list = SpreadsheetCardLinkListComponent.with(
            ID,
            TITLE,
            LABEL_MAKER
        );

        list.refresh(
            texts,
            new FakeSpreadsheetCardLinkListComponentContext() {

                @Override
                public boolean isDisabled(final String text) {
                    return selected.map(SpreadsheetFormatterName::value)
                        .equals(Optional.of(text));
                }

                @Override
                public String saveValueText(final String text) {
                    return "save " + text;
                }

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.cellFormatterSelect(
                        SpreadsheetId.with(1),
                        SpreadsheetName.with("Spreadsheet123"),
                        SpreadsheetSelection.A1.setDefaultAnchor()
                    );
                }
            }
        );

        this.treePrintAndCheck(
            list,
            expected
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetCardLinkListComponent> type() {
        return SpreadsheetCardLinkListComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
