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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontsize;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryContexts;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponent;
import walkingkooka.tree.text.FontSize;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HistoryContextFontSizeComponentContextTest implements ClassTesting2<HistoryContextFontSizeComponentContext> {

    @Test
    public void testWithNullHistoryContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> HistoryContextFontSizeComponentContext.with(null)
        );
    }

    // class............................................................................................................

    @Test
    public void testFilterNonNumber() {
        this.filterAndCheck(
            "abc"
        );
    }

    @Test
    public void testFilterWithZero() {
        this.filterAndCheck(
            "0",
            1,
            2,
            3
        );
    }

    @Test
    public void testFilterWith10() {
        this.filterAndCheck(
            "10",
            7,
            8,
            9,
            10, //
            11,
            12,
            13
        );
    }

    private void filterAndCheck(final String startsWith) {
        this.filterAndCheck(
            startsWith,
            new int[0]
        );
    }

    private void filterAndCheck(final String startsWith,
                                final int... expected) {
        this.filterAndCheck(
            startsWith,
            Arrays.stream(expected)
                .mapToObj(FontSize::with)
                .toArray(FontSize[]::new)
        );
    }

    private void filterAndCheck(final String startsWith,
                                final FontSize... expected) {
        this.filterAndCheck(
            startsWith,
            Lists.of(expected)
        );
    }

    private void filterAndCheck(final String startsWith,
                                final List<FontSize> expected) {
        final HistoryContextFontSizeComponentContext context = HistoryContextFontSizeComponentContext.with(
            HistoryContexts.fake()
        );

        final SuggestBoxComponent<FontSize> suggestBox = FontSizeComponent.empty(context)
                .suggestBoxComponent();

        context.filter(
            startsWith,
            suggestBox
        );

        this.checkEquals(
            expected,
            suggestBox.options(),
            suggestBox::toString
        );
    }

    // class............................................................................................................

    @Override
    public Class<HistoryContextFontSizeComponentContext> type() {
        return HistoryContextFontSizeComponentContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
