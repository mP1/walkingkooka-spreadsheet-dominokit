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

import java.util.Set;

public interface SpreadsheetKeyBindings {

    Set<KeyBinding> bold();

    Set<KeyBinding> bottomVerticalAlign();

    Set<KeyBinding> capitalize();

    Set<KeyBinding> centerTextAlign();

    Set<KeyBinding> currencyFormat();

    Set<KeyBinding> dateFormat();

    Set<KeyBinding> delete();

    Set<KeyBinding> exit();

    Set<KeyBinding> extendSelectionDown();

    Set<KeyBinding> extendSelectionLeft();

    Set<KeyBinding> extendSelectionRight();

    Set<KeyBinding> extendSelectionUp();

    Set<KeyBinding> generalFormat();

    Set<KeyBinding> italics();

    Set<KeyBinding> justifyTextAlign();

    Set<KeyBinding> leftTextAlign();

    Set<KeyBinding> lowerCase();

    Set<KeyBinding> middleVerticalAlign();

    Set<KeyBinding> normalText();

    Set<KeyBinding> numberFormat();

    Set<KeyBinding> percentFormat();

    Set<KeyBinding> rightTextAlign();

    Set<KeyBinding> scientificFormat();

    Set<KeyBinding> screenLeft();

    Set<KeyBinding> screenRight();

    Set<KeyBinding> screenUp();

    Set<KeyBinding> select();

    Set<KeyBinding> selectAll();

    Set<KeyBinding> selectionDown();

    Set<KeyBinding> selectionLeft();

    Set<KeyBinding> selectionRight();

    Set<KeyBinding> selectionUp();

    Set<KeyBinding> strikethru();

    Set<KeyBinding> textFormat();

    Set<KeyBinding> timeFormat();

    Set<KeyBinding> topVerticalAlign();

    Set<KeyBinding> underline();

    Set<KeyBinding> upperCase();
}
