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

import elemental2.dom.HTMLTableElement;
import org.jboss.elemento.Elements;
import org.jboss.elemento.HtmlContentBuilder;

public final class SpreadsheetViewportWidget {

    static SpreadsheetViewportWidget empty() {
        return new SpreadsheetViewportWidget();
    }

    private SpreadsheetViewportWidget() {
        this.tableElement = Elements.table();
        this.initTable();
    }

    private void initTable() {
        final HtmlContentBuilder<HTMLTableElement> tableElement = this.tableElement;
        tableElement.style("width: 100%; height: 100%;");
        tableElement.add(
                Elements.tbody()
                        .add(
                                Elements.tr()
                                        .add(
                                                Elements.td()
                                                        .add(
                                                                "spreadsheet here"
                                                        ).element()
                                        ).element()
                        ).element()
        );
    }

    public void setHeight(final int height) {
        this.tableElement.element()
                .style.set("height", height + "px");
    }

    /**
     * The root table element.
     */
    HTMLTableElement tableElement() {
        return this.tableElement.element();
    }

    private HtmlContentBuilder<HTMLTableElement> tableElement;
}
