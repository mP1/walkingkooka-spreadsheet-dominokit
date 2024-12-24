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

package walkingkooka.spreadsheet.dominokit.find;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import walkingkooka.spreadsheet.dominokit.Component;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;

import java.util.Collection;

abstract class SpreadsheetFindDialogComponentGridLayoutLike implements HtmlElementComponent<HTMLDivElement, SpreadsheetFindDialogComponentGridLayout> {

    abstract SpreadsheetFindDialogComponentGridLayout setLeft(final Collection<Component<?>> children);

    abstract SpreadsheetFindDialogComponentGridLayout setContent(final Collection<Component<?>> children);

    abstract SpreadsheetFindDialogComponentGridLayout setFooter(final Collection<Component<?>> children);

    @Override
    public final Node node() {
        return this.element();
    }
}
