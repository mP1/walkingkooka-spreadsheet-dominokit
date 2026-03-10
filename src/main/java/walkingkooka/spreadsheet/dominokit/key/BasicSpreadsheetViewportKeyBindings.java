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

package walkingkooka.spreadsheet.dominokit.key;

import walkingkooka.collect.set.Sets;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextTransform;
import walkingkooka.tree.text.VerticalAlign;

import java.util.Set;

final class BasicSpreadsheetViewportKeyBindings implements SpreadsheetViewportKeyBindings {

    /**
     * Singleton
     */
    final static BasicSpreadsheetViewportKeyBindings INSTANCE = new BasicSpreadsheetViewportKeyBindings();

    private BasicSpreadsheetViewportKeyBindings() {
        super();
    }

    @Override
    public Set<KeyBinding> bold() {
        return Sets.of(
            control(
                "Bold",
                "b"
            ),
            control(
                "Bold",
                "2"
            )
        );
    }

    @Override
    public Set<KeyBinding> bottomVerticalAlign() {
        return Sets.of(
            controlShift(
                verticalAlign(VerticalAlign.BOTTOM),
                "B"
            )
        );
    }

    @Override
    public Set<KeyBinding> capitalize() {
        return Sets.of(
            controlShift(
                textTransform(TextTransform.CAPITALIZE),
                "C"
            )
        );
    }

    @Override
    public Set<KeyBinding> centerTextAlign() {
        return Sets.of(
            control(
                textAlign(TextAlign.CENTER),
                "c"
            )
        );
    }

    @Override
    public Set<KeyBinding> currencyFormat() {
        return Sets.of(
            formatter(
                "Currency",
                4
            )
        );
    }

    @Override
    public Set<KeyBinding> dateFormat() {
        return Sets.of(
            formatter(
                "Date",
                3
            )
        );
    }

    @Override
    public Set<KeyBinding> delete() {
        return Sets.of(
            key(
                "Delete",
                Key.Backspace
            )
        );
    }

    @Override
    public Set<KeyBinding> exit() {
        return Sets.of(
            key(
                "Exit",
                Key.Escape
            )
        );
    }

    @Override
    public Set<KeyBinding> extendSelectionDown() {
        return Sets.of(
            shiftedKey(
                extendSelection(DOWN),
                Key.ArrowDown
            )
        );
    }

    @Override
    public Set<KeyBinding> extendSelectionLeft() {
        return Sets.of(
            shiftedKey(
                extendSelection(LEFT),
                Key.ArrowLeft
            )
        );
    }

    @Override
    public Set<KeyBinding> extendSelectionRight() {
        return Sets.of(
            shiftedKey(
                extendSelection(RIGHT),
                Key.ArrowRight
            )
        );
    }

    @Override
    public Set<KeyBinding> extendSelectionUp() {
        return Sets.of(
            shiftedKey(
                extendSelection(UP),
                Key.ArrowUp
            )
        );
    }

    @Override
    public Set<KeyBinding> extendScreenDown() {
        return Sets.of(
            shiftedKey(
                extendScreen(DOWN),
                Key.PageDown
            )
        );
    }

    @Override
    public Set<KeyBinding> extendScreenLeft() {
        return Sets.of(
            shiftedKey(
                extendScreen(LEFT),
                Key.Home
            )
        );
    }

    @Override
    public Set<KeyBinding> extendScreenRight() {
        return Sets.of(
            shiftedKey(
                extendScreen(RIGHT),
                Key.End
            )
        );
    }

    @Override
    public Set<KeyBinding> extendScreenUp() {
        return Sets.of(
            shiftedKey(
                extendScreen(UP),
                Key.PageUp
            )
        );
    }

    @Override
    public Set<KeyBinding> generalFormat() {
        return Sets.of(
            formatter(
                "General",
                7
            )
        );
    }

    @Override
    public Set<KeyBinding> italics() {
        return Sets.of(
            control(
                "Italics",
                "i"
            ),
            control(
                "Italics",
                "3"
            )
        );
    }

    @Override
    public Set<KeyBinding> justifyTextAlign() {
        return Sets.of(
            control(
                textAlign(TextAlign.JUSTIFY),
                "j"
            )
        );
    }

    @Override
    public Set<KeyBinding> leftTextAlign() {
        return Sets.of(
            control(
                textAlign(TextAlign.LEFT),
                "l"
            )
        );
    }

    @Override
    public Set<KeyBinding> lowerCase() {
        return Sets.of(
            controlShift(
                textTransform(TextTransform.LOWERCASE),
                "L"
            )
        );
    }

    @Override
    public Set<KeyBinding> middleVerticalAlign() {
        return Sets.of(
            controlShift(
                verticalAlign(VerticalAlign.MIDDLE),
                "M"
            )
        );
    }

    @Override
    public Set<KeyBinding> normalText() {
        return Sets.of(
            controlShift(
                "Normal text",
                "N"
            )
        );
    }

    @Override
    public Set<KeyBinding> numberFormat() {
        return Sets.of(
            formatter(
                "Number",
                1
            )
        );
    }

    @Override
    public Set<KeyBinding> percentFormat() {
        return Sets.of(
            formatter(
                "Percent",
                5
            )
        );
    }

    @Override
    public Set<KeyBinding> rightTextAlign() {
        return Sets.of(
            control(
                textAlign(TextAlign.RIGHT),
                "r"
            )
        );
    }

    @Override
    public Set<KeyBinding> scientificFormat() {
        return Sets.of(
            formatter(
                "Scientific",
                6
            )
        );
    }

    @Override
    public Set<KeyBinding> screenDown() {
        return Sets.of(
            key(
                screen(DOWN),
                Key.PageDown
            )
        );
    }

    @Override
    public Set<KeyBinding> screenLeft() {
        return Sets.of(
            key(
                screen(LEFT),
                Key.Home
            )
        );
    }

    @Override
    public Set<KeyBinding> screenRight() {
        return Sets.of(
            key(
                screen(RIGHT),
                Key.End
            )
        );
    }

    @Override
    public Set<KeyBinding> screenUp() {
        return Sets.of(
            key(
                screen(UP),
                Key.PageUp
            )
        );
    }

    @Override
    public Set<KeyBinding> select() {
        return Sets.of(
            key(
                "Select",
                Key.Enter
            )
        );
    }

    @Override
    public Set<KeyBinding> selectAll() {
        return Sets.of(
            control(
                "Select all",
                "a"
            )
        );
    }

    @Override
    public Set<KeyBinding> selectionDown() {
        return Sets.of(
            key(
                selection(DOWN),
                Key.ArrowDown
            )
        );
    }

    @Override
    public Set<KeyBinding> selectionLeft() {
        return Sets.of(
            key(
                selection(LEFT),
                Key.ArrowLeft
            )
        );
    }

    @Override
    public Set<KeyBinding> selectionRight() {
        return Sets.of(
            key(
                selection(RIGHT),
                Key.ArrowRight
            )
        );
    }

    @Override
    public Set<KeyBinding> selectionUp() {
        return Sets.of(
            key(
                selection(UP),
                Key.ArrowUp
            )
        );
    }

    @Override
    public Set<KeyBinding> strikethru() {
        return Sets.of(
            control(
                "Strikethru",
                "5"
            )
        );
    }

    @Override
    public Set<KeyBinding> textFormat() {
        return Sets.of(
            formatter(
                "Text",
                8
            )
        );
    }

    @Override
    public Set<KeyBinding> timeFormat() {
        return Sets.of(
            formatter(
                "Time",
                2
            )
        );
    }

    @Override
    public Set<KeyBinding> topVerticalAlign() {
        return Sets.of(
            controlShift(
                verticalAlign(VerticalAlign.TOP),
                "T"
            )
        );
    }

    @Override
    public Set<KeyBinding> underline() {
        return Sets.of(
            control("Underline", "u"),
            control("Underline", "4")
        );
    }

    @Override
    public Set<KeyBinding> upperCase() {
        return Sets.of(
            controlShift(
                textTransform(TextTransform.UPPERCASE),
                "U"
            )
        );
    }

    private static String selection(final Direction direction) {
        return direction.toString();
    }

    private static String extendSelection(final Direction direction) {
        return "Extend " + direction;
    }

    private static String screen(final Direction direction) {
        return "Screen " + direction;
    }

    private static String extendScreen(final Direction direction) {
        return "Extend screen " + direction;
    }

    private static String textAlign(final TextAlign textAlign) {
        return "Text Align: " + textAlign.name()
            .toLowerCase();
    }

    private static String textTransform(final TextTransform textTransform) {
        return textTransform.name()
            .toLowerCase();
    }

    private static String verticalAlign(final VerticalAlign verticalAlign) {
        return "Vertical Align: " + verticalAlign.name()
            .toLowerCase();
    }

    enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN;

        @Override
        public String toString() {
            return this.name()
                .toLowerCase();
        }
    }

    private final static Direction LEFT = Direction.LEFT;

    private final static Direction RIGHT = Direction.RIGHT;

    private final static Direction UP = Direction.UP;

    private final static Direction DOWN = Direction.DOWN;

    private static KeyBinding key(final String label,
                                  final Key key) {
        return KeyBinding.down(
            key.toString()
        ).setLabel(label);
    }

    private static KeyBinding shiftedKey(final String label,
                                         final Key key) {
        return key(
            label,
            key
        ).setShift();
    }

    private static KeyBinding control(final String label,
                                      final String key) {
        return KeyBinding.down(key)
            .setLabel(label)
            .setControl();
    }

    private static KeyBinding controlShift(final String label,
                                           final String key) {
        return control(
            label,
            key
        ).setShift();
    }

    private static KeyBinding formatter(final String label,
                                        final int number) {
        if (number < 1 || number > 8) {
            throw new IllegalArgumentException("Invalid number " + number);
        }

        return controlShift(
            label + " format",
            String.valueOf(number)
        );
    }
}
