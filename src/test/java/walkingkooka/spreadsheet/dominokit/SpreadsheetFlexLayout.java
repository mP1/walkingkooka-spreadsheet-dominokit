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

package walkingkooka.spreadsheet.dominokit;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.IsElement;
import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Objects;

public final class SpreadsheetFlexLayout implements SpreadsheetFlexLayoutLike,
        TestHtmlElementComponent<HTMLDivElement, walkingkooka.spreadsheet.dominokit.SpreadsheetFlexLayout> {

    public static walkingkooka.spreadsheet.dominokit.SpreadsheetFlexLayout column() {
        return new walkingkooka.spreadsheet.dominokit.SpreadsheetFlexLayout(true);
    }

    public static walkingkooka.spreadsheet.dominokit.SpreadsheetFlexLayout row() {
        return new walkingkooka.spreadsheet.dominokit.SpreadsheetFlexLayout(false);
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
    public walkingkooka.spreadsheet.dominokit.SpreadsheetFlexLayout displayBlock() {
        return this;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.SpreadsheetFlexLayout setId(final String id) {
        this.id = id;
        return this;
    }

    @Override
    public String id() {
        return this.id;
    }

    private String id;

    @Override
    public walkingkooka.spreadsheet.dominokit.SpreadsheetFlexLayout appendChild(final IsElement<?> child) {
        Objects.requireNonNull(child, "child");

        this.children.add(child);
        return this;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.SpreadsheetFlexLayout removeChild(final int index) {
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
}
