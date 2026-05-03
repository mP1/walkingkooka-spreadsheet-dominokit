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

package walkingkooka.spreadsheet.dominokit.value.textstyle.texttransform;

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
import walkingkooka.tree.text.TextTransform;

import java.util.List;
import java.util.Optional;

public final class TextTransformComponentTest implements TextStylePropertyEnumComponentTesting<TextTransform, TextTransformComponent> {

    @Test
    public void testSetValue() {
        final TextTransformComponent component = this.createComponent();

        component.setValue(
            Optional.of(
                TextTransform.UPPERCASE
            )
        );

        this.treePrintAndCheck(
            component,
            "TextTransformComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/text-transform/save/] id=Test123-textTransform-Link\n" +
                "          \"None\" [#/1/SpreadsheetName111/cell/A1/style/text-transform/save/NONE] id=Test123-textTransform-NONE-Link\n" +
                "          mdi-format-letter-case \"Capitalize\" [#/1/SpreadsheetName111/cell/A1/style/text-transform/save/CAPITALIZE] id=Test123-textTransform-CAPITALIZE-Link\n" +
                "          mdi-format-letter-case-upper \"Uppercase\" [#/1/SpreadsheetName111/cell/A1/style/text-transform/save/UPPERCASE] CHECKED id=Test123-textTransform-UPPERCASE-Link\n" +
                "          mdi-format-letter-case-lower \"Lowercase\" [#/1/SpreadsheetName111/cell/A1/style/text-transform/save/LOWERCASE] id=Test123-textTransform-LOWERCASE-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final TextTransformComponent component = this.createComponent();

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_TRANSFORM,
                        TextTransform.LOWERCASE
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "TextTransformComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/text-transform/save/] id=Test123-textTransform-Link\n" +
                "          \"None\" [#/1/SpreadsheetName111/cell/A1/style/text-transform/save/NONE] id=Test123-textTransform-NONE-Link\n" +
                "          mdi-format-letter-case \"Capitalize\" [#/1/SpreadsheetName111/cell/A1/style/text-transform/save/CAPITALIZE] id=Test123-textTransform-CAPITALIZE-Link\n" +
                "          mdi-format-letter-case-upper \"Uppercase\" [#/1/SpreadsheetName111/cell/A1/style/text-transform/save/UPPERCASE] id=Test123-textTransform-UPPERCASE-Link\n" +
                "          mdi-format-letter-case-lower \"Lowercase\" [#/1/SpreadsheetName111/cell/A1/style/text-transform/save/LOWERCASE] CHECKED id=Test123-textTransform-LOWERCASE-Link\n"
        );
    }

    @Override
    public TextTransformComponent createComponent() {
        return TextTransformComponent.with(
            "Test123-",
            new FakeTextTransformComponentContext() {
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
                            TextStylePropertyName.TEXT_TRANSFORM
                        )
                    );
                }
            }
        );
    }

    @Override
    public List<TextTransform> enumValues() {
        return Lists.of(
            TextTransform.values()
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextTransformComponent> type() {
        return TextTransformComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
