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

package walkingkooka.spreadsheet.dominokit.contextmenu;

import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

/**
 * A {@link TreePrintable} that prints the 5 dashes. Adding a string to the list would be quoted otherwise.
 */
final class SpreadsheetContextMenuNativeSeparator implements TreePrintable {

    final static SpreadsheetContextMenuNativeSeparator INSTANCE = new SpreadsheetContextMenuNativeSeparator();

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.print("-----");
    }
}
