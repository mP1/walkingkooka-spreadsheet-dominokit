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

import java.util.Set;

final class BasicSpreadsheetKeyBindings implements SpreadsheetKeyBindings {

    /**
     * Singleton
     */
    final static BasicSpreadsheetKeyBindings INSTANCE = new BasicSpreadsheetKeyBindings();

    private BasicSpreadsheetKeyBindings() {
        super();
    }

    @Override
    public Set<KeyBinding> bold() {
        return Sets.of(
            control("b"),
            control("2")
        );
    }

    @Override
    public Set<KeyBinding> bottomVerticalAlign() {
        return Sets.of(
            controlShift("B")
        );
    }

    @Override
    public Set<KeyBinding> capitalize() {
        return Sets.of(
            controlShift("C")
        );
    }

    @Override
    public Set<KeyBinding> centerTextAlign() {
        return Sets.of(
            control("c")
        );
    }

    @Override
    public Set<KeyBinding> currencyFormat() {
        return Sets.of(
            formatter(4)
        );
    }

    @Override
    public Set<KeyBinding> dateFormat() {
        return Sets.of(
            formatter(3)
        );
    }

    @Override
    public Set<KeyBinding> delete() {
        return Sets.of(
            key(Key.Backspace)
        );
    }

    @Override
    public Set<KeyBinding> exit() {
        return Sets.of(
            key(Key.Escape)
        );
    }

    @Override
    public Set<KeyBinding> extendSelectionDown() {
        return Sets.of(
            shiftedKey(Key.ArrowDown)
        );
    }

    @Override
    public Set<KeyBinding> extendSelectionLeft() {
        return Sets.of(
            shiftedKey(Key.ArrowLeft)
        );
    }

    @Override
    public Set<KeyBinding> extendSelectionRight() {
        return Sets.of(
            shiftedKey(Key.ArrowRight)
        );
    }

    @Override
    public Set<KeyBinding> extendSelectionUp() {
        return Sets.of(
            shiftedKey(Key.ArrowUp)
        );
    }

    @Override
    public Set<KeyBinding> extendScreenDown() {
        return Sets.of(
            shiftedKey(Key.PageDown)
        );
    }

    @Override
    public Set<KeyBinding> extendScreenLeft() {
        return Sets.of(
            shiftedKey(Key.Home)
        );
    }

    @Override
    public Set<KeyBinding> extendScreenRight() {
        return Sets.of(
            shiftedKey(Key.End)
        );
    }

    @Override
    public Set<KeyBinding> extendScreenUp() {
        return Sets.of(
            shiftedKey(Key.PageUp)
        );
    }
    
    @Override
    public Set<KeyBinding> generalFormat() {
        return Sets.of(
            formatter(7)
        );
    }

    @Override
    public Set<KeyBinding> italics() {
        return Sets.of(
            control("i"),
            control("3")
        );
    }

    @Override
    public Set<KeyBinding> justifyTextAlign() {
        return Sets.of(
            control("j")
        );
    }

    @Override
    public Set<KeyBinding> leftTextAlign() {
        return Sets.of(
            control("l")
        );
    }

    @Override
    public Set<KeyBinding> lowerCase() {
        return Sets.of(
            controlShift("L")
        );
    }

    @Override
    public Set<KeyBinding> middleVerticalAlign() {
        return Sets.of(
            controlShift("M")
        );
    }

    @Override
    public Set<KeyBinding> normalText() {
        return Sets.of(
            controlShift("N")
        );
    }

    @Override
    public Set<KeyBinding> numberFormat() {
        return Sets.of(
            formatter(1)
        );
    }

    @Override
    public Set<KeyBinding> percentFormat() {
        return Sets.of(
            formatter(5)
        );
    }

    @Override
    public Set<KeyBinding> rightTextAlign() {
        return Sets.of(
            control("r")
        );
    }

    @Override
    public Set<KeyBinding> scientificFormat() {
        return Sets.of(
            formatter(6)
        );
    }

    @Override
    public Set<KeyBinding> screenDown() {
        return Sets.of(
            key(Key.PageDown)
        );
    }

    @Override
    public Set<KeyBinding> screenLeft() {
        return Sets.of(
            key(Key.Home)
        );
    }

    @Override
    public Set<KeyBinding> screenRight() {
        return Sets.of(
            key(Key.End)
        );
    }

    @Override
    public Set<KeyBinding> screenUp() {
        return Sets.of(
            key(Key.PageUp)
        );
    }

    @Override
    public Set<KeyBinding> select() {
        return Sets.of(
            key(Key.Enter)
        );
    }

    @Override
    public Set<KeyBinding> selectAll() {
        return Sets.of(
            control("a")
        );
    }

    @Override
    public Set<KeyBinding> selectionDown() {
        return Sets.of(
            key(Key.ArrowDown)
        );
    }

    @Override
    public Set<KeyBinding> selectionLeft() {
        return Sets.of(
            key(Key.ArrowLeft)
        );
    }

    @Override
    public Set<KeyBinding> selectionRight() {
        return Sets.of(
            key(Key.ArrowRight)
        );
    }

    @Override
    public Set<KeyBinding> selectionUp() {
        return Sets.of(
            key(Key.ArrowUp)
        );
    }

    @Override
    public Set<KeyBinding> strikethru() {
        return Sets.of(
            control("5")
        );
    }

    @Override
    public Set<KeyBinding> textFormat() {
        return Sets.of(
            formatter(8)
        );
    }

    @Override
    public Set<KeyBinding> timeFormat() {
        return Sets.of(
            formatter(2)
        );
    }

    @Override
    public Set<KeyBinding> topVerticalAlign() {
        return Sets.of(
            controlShift("T")
        );
    }

    @Override
    public Set<KeyBinding> underline() {
        return Sets.of(
            control("u"),
            control("4")
        );
    }

    @Override
    public Set<KeyBinding> upperCase() {
        return Sets.of(
            controlShift("U")
        );
    }

    private static KeyBinding key(final Key key) {
        return KeyBinding.with(key.toString());
    }

    private static KeyBinding shiftedKey(final Key key) {
        return key(key)
            .setShift();
    }
    
    private static KeyBinding control(final String key) {
        return KeyBinding.with(key)
            .setControl();
    }

    private static KeyBinding controlShift(final String key) {
        return control(key)
            .setShift();
    }

    private static KeyBinding formatter(final int number) {
        if(number < 1 || number > 8) {
            throw new IllegalArgumentException("Invalid number " + number);
        }

        return controlShift(
            String.valueOf(number)
        );
    }
}
