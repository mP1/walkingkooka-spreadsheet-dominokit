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

package walkingkooka.spreadsheet.dominokit.flex;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.IsElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;

import java.util.List;
import java.util.Objects;

public final class SpreadsheetFlexLayout extends SpreadsheetFlexLayoutLike
    implements TestHtmlElementComponent<HTMLDivElement, SpreadsheetFlexLayout> {

    public static SpreadsheetFlexLayout column() {
        return new SpreadsheetFlexLayout(true);
    }

    public static SpreadsheetFlexLayout row() {
        return new SpreadsheetFlexLayout(false);
    }

    private SpreadsheetFlexLayout(final boolean column) {
        super();
        this.column = column;
    }

    @Override
    public boolean isColumn() {
        return this.column;
    }

    private final boolean column;

    @Override
    public SpreadsheetFlexLayout displayBlock() {
        return this;
    }

    @Override
    public SpreadsheetFlexLayout setId(final String id) {
        this.id = id;
        return this;
    }

    @Override
    public String id() {
        return this.id;
    }

    private String id;

    @Override
    public SpreadsheetFlexLayout appendChild(final IsElement<?> child) {
        Objects.requireNonNull(child, "child");

        this.children.add(child);
        return this;
    }

    @Override
    public SpreadsheetFlexLayout removeChild(final int index) {
        this.children.remove(index);
        return this;
    }

    @Override
    public List<IsElement<?>> children() {
        return Lists.immutable(
            this.children
        );
    }

    private final List<IsElement<?>> children = Lists.array();

    @Override
    public SpreadsheetFlexLayout setCssProperty(final String property, final String value) {
        return this;
    }
}
