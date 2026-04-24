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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontfamily;

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.tree.text.FontFamily;

import java.util.List;
import java.util.Objects;

final class HistoryContextFontFamilyComponentContext implements FontFamilyComponentContext,
    HistoryContextDelegator {

    static HistoryContextFontFamilyComponentContext with(final List<FontFamily> fontFamilies,
                                                         final HistoryContext historyContext) {
        return new HistoryContextFontFamilyComponentContext(
            Objects.requireNonNull(fontFamilies, "fontFamilies"),
            Objects.requireNonNull(historyContext, "historyContext")
        );
    }


    private HistoryContextFontFamilyComponentContext(final List<FontFamily> fontFamilies,
                                                     final HistoryContext historyContext) {
        this.fontFamilies = Lists.immutable(fontFamilies);
        this.historyContext = historyContext;
    }

    @Override
    public List<FontFamily> fontFamilies() {
        return this.fontFamilies;
    }

    private final List<FontFamily> fontFamilies;

    // HistoryContextDelegator..........................................................................................

    @Override
    public HistoryContext historyContext() {
        return this.historyContext;
    }

    private final HistoryContext historyContext;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.fontFamilies + " " + this.historyContext;
    }
}
