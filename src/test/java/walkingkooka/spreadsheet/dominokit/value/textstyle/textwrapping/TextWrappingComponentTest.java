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

package walkingkooka.spreadsheet.dominokit.value.textstyle.textwrapping;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.TextWrapping;

import java.util.List;
import java.util.Optional;

public final class TextWrappingComponentTest implements TextStylePropertyEnumComponentTesting<TextWrapping, TextWrappingComponent> {

    @Test
    public void testSetValue() {
        final TextWrappingComponent component = this.createComponent();

        component.setValue(
            Optional.of(
                TextWrapping.WRAP
            )
        );

        this.treePrintAndCheck(
            component,
            "TextWrappingComponent\n" +
                "  TextStylePropertyEnumComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/text-wrapping/save/] id=Test123-textWrapping-Link\n" +
                "          \"Clip\" [#/1/SpreadsheetName111/cell/A1/style/text-wrapping/save/CLIP] id=Test123-textWrapping-CLIP-Link\n" +
                "          \"Overflow\" [#/1/SpreadsheetName111/cell/A1/style/text-wrapping/save/OVERFLOW] id=Test123-textWrapping-OVERFLOW-Link\n" +
                "          \"Wrap\" [#/1/SpreadsheetName111/cell/A1/style/text-wrapping/save/WRAP] CHECKED id=Test123-textWrapping-WRAP-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final TextWrappingComponent component = this.createComponent();

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_WRAPPING,
                        TextWrapping.OVERFLOW
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "TextWrappingComponent\n" +
                "  TextStylePropertyEnumComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/text-wrapping/save/] id=Test123-textWrapping-Link\n" +
                "          \"Clip\" [#/1/SpreadsheetName111/cell/A1/style/text-wrapping/save/CLIP] id=Test123-textWrapping-CLIP-Link\n" +
                "          \"Overflow\" [#/1/SpreadsheetName111/cell/A1/style/text-wrapping/save/OVERFLOW] CHECKED id=Test123-textWrapping-OVERFLOW-Link\n" +
                "          \"Wrap\" [#/1/SpreadsheetName111/cell/A1/style/text-wrapping/save/WRAP] id=Test123-textWrapping-WRAP-Link\n"
        );
    }

    @Override
    public TextWrappingComponent createComponent() {
        return TextWrappingComponent.with(
            "Test123-",
            new FakeTextWrappingComponentContext() {
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
                            TextStylePropertyName.TEXT_WRAPPING
                        )
                    );
                }
            }
        );
    }

    @Override
    public List<TextWrapping> enumValues() {
        return Lists.of(
            TextWrapping.values()
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextWrappingComponent> type() {
        return TextWrappingComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
