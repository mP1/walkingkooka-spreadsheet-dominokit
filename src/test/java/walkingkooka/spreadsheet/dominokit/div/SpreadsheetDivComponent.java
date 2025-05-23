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

package walkingkooka.spreadsheet.dominokit.div;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;

import java.util.Objects;

/**
 * A {@link walkingkooka.spreadsheet.dominokit.Component} that uses surrounds any given child components with a DIV.
 */
public final class SpreadsheetDivComponent extends SpreadsheetDivComponentLike implements TestHtmlElementComponent<HTMLDivElement, SpreadsheetDivComponent> {

    public static SpreadsheetDivComponent empty() {
        return new SpreadsheetDivComponent();
    }

    private SpreadsheetDivComponent() {
    }

    @Override
    public SpreadsheetDivComponent appendChild(final HtmlElementComponent<?, ?> child) {
        Objects.requireNonNull(child, "child");

        this.children.add(child);
        return this;
    }
}
