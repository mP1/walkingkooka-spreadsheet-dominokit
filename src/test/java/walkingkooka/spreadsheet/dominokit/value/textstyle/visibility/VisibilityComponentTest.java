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

package walkingkooka.spreadsheet.dominokit.value.textstyle.visibility;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.Visibility;

import java.util.Optional;

public final class VisibilityComponentTest implements HtmlComponentTesting<VisibilityComponent, HTMLFieldSetElement> {

    @Test
    public void testSetValue() {
        final VisibilityComponent component = VisibilityComponent.with(
            "Test123-",
            new FakeVisibilityComponentContext() {
                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return () -> {};
                }

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.cellStyle(
                        SpreadsheetId.with(1),
                        SpreadsheetName.with("SpreadsheetName111"),
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        Optional.of(
                            TextStylePropertyName.VISIBILITY
                        )
                    );
                }
            }
        );

        component.setValue(
            Optional.of(
                Visibility.VISIBLE
            )
        );

        this.treePrintAndCheck(
            component,
            "VisibilityComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    Visibility\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/visibility/save/] id=Test123-visibility-Link\n" +
                "            \"Visible\" [#/1/SpreadsheetName111/cell/A1/style/visibility/save/VISIBLE] CHECKED id=Test123-visibility-VISIBLE-Link\n" +
                "            \"Hidden\" [#/1/SpreadsheetName111/cell/A1/style/visibility/save/HIDDEN] id=Test123-visibility-HIDDEN-Link\n" +
                "            \"Collapse\" [#/1/SpreadsheetName111/cell/A1/style/visibility/save/COLLAPSE] id=Test123-visibility-COLLAPSE-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final VisibilityComponent component = VisibilityComponent.with(
            "Test123-",
            new FakeVisibilityComponentContext() {
                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return () -> {};
                }

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.cellStyle(
                        SpreadsheetId.with(1),
                        SpreadsheetName.with("SpreadsheetName111"),
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        Optional.of(
                            TextStylePropertyName.VISIBILITY
                        )
                    );
                }
            }
        );

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.VISIBILITY,
                        Visibility.COLLAPSE
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "VisibilityComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    Visibility\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/visibility/save/] id=Test123-visibility-Link\n" +
                "            \"Visible\" [#/1/SpreadsheetName111/cell/A1/style/visibility/save/VISIBLE] id=Test123-visibility-VISIBLE-Link\n" +
                "            \"Hidden\" [#/1/SpreadsheetName111/cell/A1/style/visibility/save/HIDDEN] id=Test123-visibility-HIDDEN-Link\n" +
                "            \"Collapse\" [#/1/SpreadsheetName111/cell/A1/style/visibility/save/COLLAPSE] CHECKED id=Test123-visibility-COLLAPSE-Link\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<VisibilityComponent> type() {
        return VisibilityComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
