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

package walkingkooka.spreadsheet.dominokit.value.textstyle.hyphens;

import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.Hyphens;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class HyphensComponentTest implements TextStylePropertyEnumComponentTesting<Hyphens, HyphensComponent> {

    @Test
    public void testSetValue() {
        final HyphensComponent component = this.createComponent();

        component.setValue(
            Optional.of(
                Hyphens.AUTO
            )
        );

        this.treePrintAndCheck(
            component,
            "HyphensComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/hyphens/save/] id=Test123-hyphens-Link\n" +
                "          \"None\" [#/1/SpreadsheetName111/cell/A1/style/hyphens/save/NONE] id=Test123-hyphens-NONE-Link\n" +
                "          \"Manual\" [#/1/SpreadsheetName111/cell/A1/style/hyphens/save/MANUAL] id=Test123-hyphens-MANUAL-Link\n" +
                "          \"Auto\" [#/1/SpreadsheetName111/cell/A1/style/hyphens/save/AUTO] CHECKED id=Test123-hyphens-AUTO-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final HyphensComponent component = this.createComponent();

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.HYPHENS,
                        Hyphens.MANUAL
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "HyphensComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/hyphens/save/] id=Test123-hyphens-Link\n" +
                "          \"None\" [#/1/SpreadsheetName111/cell/A1/style/hyphens/save/NONE] id=Test123-hyphens-NONE-Link\n" +
                "          \"Manual\" [#/1/SpreadsheetName111/cell/A1/style/hyphens/save/MANUAL] CHECKED id=Test123-hyphens-MANUAL-Link\n" +
                "          \"Auto\" [#/1/SpreadsheetName111/cell/A1/style/hyphens/save/AUTO] id=Test123-hyphens-AUTO-Link\n"
        );
    }

    @Override
    public HyphensComponent createComponent() {
        return HyphensComponent.with(
            "Test123-",
            new FakeHyphensComponentContext() {
                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return () -> {
                    };
                }

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.cellStyle(
                        SpreadsheetId.with(1),
                        SpreadsheetName.with("SpreadsheetName111"),
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        Optional.of(
                            TextStylePropertyName.HYPHENS
                        )
                    );
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<HyphensComponent> type() {
        return HyphensComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
