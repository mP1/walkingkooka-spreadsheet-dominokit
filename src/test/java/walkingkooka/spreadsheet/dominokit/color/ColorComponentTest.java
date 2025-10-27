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

package walkingkooka.spreadsheet.dominokit.color;

import elemental2.dom.HTMLTableElement;
import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ColorComponentTest implements ValueComponentTesting<HTMLTableElement, Color, ColorComponent>,
    SpreadsheetMetadataTesting {

    private final static Function<HistoryToken, Optional<HistoryToken>> HISTORY_TOKEN_PREPARER = (h) -> Optional.of(
        h.setStylePropertyName(TextStylePropertyName.COLOR)
    );

    @Test
    public void testWithNullHistoryTokenPreparerFails() {
        assertThrows(
            NullPointerException.class,
            () -> ColorComponent.with(
                null,
                ColorComponentContexts.fake()
            )
        );
    }

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> ColorComponent.with(
                HISTORY_TOKEN_PREPARER,
                null
            )
        );
    }

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            this.createComponent(),
            "ColorComponent\n" +
                "  TABLE\n" +
                "    id=\"color-picker\" className=dui dui-menu-item\n" +
                "      TBODY\n" +
                "        TR\n" +
                "          TD\n" +
                "            style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              \"Black\" [#/1/SpreadsheetName1/cell/A1/style/color/save/black] id=color-picker-color-1-Link\n" +
                "          TD\n" +
                "            style=\"background-color: white; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              \"White\" [#/1/SpreadsheetName1/cell/A1/style/color/save/white] id=color-picker-color-2-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-3-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-4-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-5-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-6-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-7-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-8-Link\n" +
                "        TR\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-9-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-10-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-11-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-12-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-13-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-14-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-15-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-16-Link\n" +
                "        TR\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-17-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-18-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-19-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-20-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-21-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-22-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-23-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-24-Link\n" +
                "        TR\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-25-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-26-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-27-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-28-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-29-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-30-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-31-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-32-Link\n" +
                "        TR\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-33-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-34-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-35-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-36-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-37-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-38-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-39-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-40-Link\n" +
                "        TR\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-41-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-42-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-43-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-44-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-45-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-46-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-47-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-48-Link\n" +
                "        TR\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-49-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-50-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-51-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-52-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-53-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-54-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-55-Link\n" +
                "          TD\n" +
                "            style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "              DISABLED id=color-picker-color-56-Link\n" +
                "        TR\n" +
                "          TD\n" +
                "            colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "              \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/color/save/] id=color-picker-color-clear-Link\n"
        );
    }

    @Override
    public ColorComponent createComponent() {
        return ColorComponent.with(
            HISTORY_TOKEN_PREPARER,
            new FakeColorComponentContext() {
                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return null;
                }

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.cellStyle(
                        SpreadsheetId.with(1),
                        SpreadsheetName.with("SpreadsheetName1"),
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        TextStylePropertyName.COLOR
                    );
                }

                @Override
                public SpreadsheetMetadata spreadsheetMetadata() {
                    return METADATA_EN_AU;
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<ColorComponent> type() {
        return ColorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
