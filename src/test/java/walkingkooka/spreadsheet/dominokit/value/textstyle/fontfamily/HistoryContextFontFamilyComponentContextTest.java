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

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryContexts;
import walkingkooka.tree.text.FontFamily;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HistoryContextFontFamilyComponentContextTest implements HistoryContextTesting<HistoryContextFontFamilyComponentContext>,
    ToStringTesting<HistoryContextFontFamilyComponentContext> {

    private final static List<FontFamily> FONT_FAMILIES = Lists.of(
        FontFamily.with("Times New Roman")
    );

    private final static HistoryContext HISTORY_CONTEXT = HistoryContexts.fake();

    @Test
    public void testWithNullFontFamiliesFails() {
        assertThrows(
            NullPointerException.class,
            () -> HistoryContextFontFamilyComponentContext.with(
                null,
                HISTORY_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullHistoryContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> HistoryContextFontFamilyComponentContext.with(
                FONT_FAMILIES,
                null
            )
        );
    }

    @Override
    public void testAddHistoryTokenWatcherWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testAddHistoryTokenWatcherOnceWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testPushHistoryTokenWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public HistoryContextFontFamilyComponentContext createContext() {
        return HistoryContextFontFamilyComponentContext.with(
            FONT_FAMILIES,
            HISTORY_CONTEXT
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createContext(),
            FONT_FAMILIES + " " + HISTORY_CONTEXT
        );
    }

    @Override
    public Class<HistoryContextFontFamilyComponentContext> type() {
        return HistoryContextFontFamilyComponentContext.class;
    }

    @Override
    public String typeNameSuffix() {
        return FontFamilyComponentContext.class.getSimpleName();
    }
}
