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

package walkingkooka.spreadsheet.dominokit.value.textstyle.direction;

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
import walkingkooka.tree.text.Direction;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class DirectionComponentTest implements HtmlComponentTesting<DirectionComponent, HTMLFieldSetElement> {

    @Test
    public void testSetValue() {
        final DirectionComponent component = DirectionComponent.with(
            "Test123-",
            new FakeDirectionComponentContext() {
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
                            TextStylePropertyName.DIRECTION
                        )
                    );
                }
            }
        );

        component.setValue(
            Optional.of(
                Direction.RTL
            )
        );

        this.treePrintAndCheck(
            component,
            "DirectionComponent\n" +
                "  TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "    Direction\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/direction/save/] id=Test123-direction-Link\n" +
                "            \"Left to Right\" [#/1/SpreadsheetName111/cell/A1/style/direction/save/LTR] id=Test123-direction-LTR-Link\n" +
                "            \"Right to Left\" [#/1/SpreadsheetName111/cell/A1/style/direction/save/RTL] CHECKED id=Test123-direction-RTL-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final DirectionComponent component = DirectionComponent.with(
            "Test123-",
            new FakeDirectionComponentContext() {
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
                            TextStylePropertyName.DIRECTION
                        )
                    );
                }
            }
        );

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.DIRECTION,
                        Direction.RTL
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "DirectionComponent\n" +
                "  TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "    Direction\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/direction/save/] id=Test123-direction-Link\n" +
                "            \"Left to Right\" [#/1/SpreadsheetName111/cell/A1/style/direction/save/LTR] id=Test123-direction-LTR-Link\n" +
                "            \"Right to Left\" [#/1/SpreadsheetName111/cell/A1/style/direction/save/RTL] CHECKED id=Test123-direction-RTL-Link\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<DirectionComponent> type() {
        return DirectionComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
