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

package walkingkooka.spreadsheet.dominokit.viewport;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.key.KeyBinding;
import walkingkooka.text.LineEnding;

import java.util.stream.Collectors;

public final class BasicSpreadsheetViewportComponentKeyBindingsTest implements SpreadsheetViewportComponentKeyBindingsTesting<BasicSpreadsheetViewportComponentKeyBindings> {

    @Test
    public void testAllToString() {
        this.checkEquals(
            "Select \"Enter\" DOWN\n" +
                "Select all control \"a\" DOWN\n" +
                "Up \"ArrowUp\" DOWN\n" +
                "Right \"ArrowRight\" DOWN\n" +
                "Down \"ArrowDown\" DOWN\n" +
                "Left \"ArrowLeft\" DOWN\n" +
                "Delete \"Backspace\" DOWN\n" +
                "Extend up shift \"ArrowUp\" DOWN\n" +
                "Extend right shift \"ArrowRight\" DOWN\n" +
                "Extend down shift \"ArrowDown\" DOWN\n" +
                "Extend left shift \"ArrowLeft\" DOWN\n" +
                "Extend screen up shift \"PageUp\" DOWN\n" +
                "Extend screen right shift \"End\" DOWN\n" +
                "Extend screen down shift \"PageDown\" DOWN\n" +
                "Extend screen left shift \"Home\" DOWN\n" +
                "Screen up \"PageUp\" DOWN\n" +
                "Screen right \"End\" DOWN\n" +
                "Screen down \"PageDown\" DOWN\n" +
                "Screen left \"Home\" DOWN\n" +
                "Exit \"Escape\" DOWN\n" +
                "Text Align: left control \"l\" DOWN\n" +
                "Text Align: center control \"c\" DOWN\n" +
                "Text Align: right control \"r\" DOWN\n" +
                "Text Align: justify control \"j\" DOWN\n" +
                "Vertical Align: top control+shift \"T\" DOWN\n" +
                "Vertical Align: middle control+shift \"M\" DOWN\n" +
                "Vertical Align: bottom control+shift \"B\" DOWN\n" +
                "Currency formatter control+shift \"4\" DOWN\n" +
                "General formatter control+shift \"7\" DOWN\n" +
                "Date formatter control+shift \"3\" DOWN\n" +
                "Number formatter control+shift \"1\" DOWN\n" +
                "Percent formatter control+shift \"5\" DOWN\n" +
                "Scientific formatter control+shift \"6\" DOWN\n" +
                "Text formatter control+shift \"8\" DOWN\n" +
                "Time formatter control+shift \"2\" DOWN\n" +
                "Bold control \"b\" DOWN\n" +
                "Bold control \"2\" DOWN\n" +
                "Italics control \"i\" DOWN\n" +
                "Italics control \"3\" DOWN\n" +
                "Normal text control+shift \"N\" DOWN\n" +
                "Strikethru control \"5\" DOWN\n" +
                "Underline control \"u\" DOWN\n" +
                "Underline control \"4\" DOWN\n" +
                "Capitalize control+shift \"C\" DOWN\n" +
                "Lowercase control+shift \"L\" DOWN\n" +
                "Uppercase control+shift \"U\" DOWN",
            BasicSpreadsheetViewportComponentKeyBindings.INSTANCE.all()
                .stream()
                .map(KeyBinding::toString)
                .collect(
                    Collectors.joining(
                        LineEnding.NL.toString()
                    )
                )
        );
    }

    @Override
    public BasicSpreadsheetViewportComponentKeyBindings createSpreadsheetKeyBinding() {
        return BasicSpreadsheetViewportComponentKeyBindings.INSTANCE;
    }

    // class............................................................................................................

    @Override
    public Class<BasicSpreadsheetViewportComponentKeyBindings> type() {
        return BasicSpreadsheetViewportComponentKeyBindings.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
