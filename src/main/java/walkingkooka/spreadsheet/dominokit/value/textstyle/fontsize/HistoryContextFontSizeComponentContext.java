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

import org.dominokit.domino.ui.menu.MenuItem;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponent;
import walkingkooka.tree.text.FontSize;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

final class HistoryContextFontSizeComponentContext implements FontSizeComponentContext,
    HistoryContextDelegator {

    static HistoryContextFontSizeComponentContext with(final HistoryContext historyContext) {
        return new HistoryContextFontSizeComponentContext(
            Objects.requireNonNull(historyContext, "historyContext")
        );
    }

    private HistoryContextFontSizeComponentContext(final HistoryContext historyContext) {
        super();

        this.historyContext = historyContext;
    }

    // FontSizeComponentContext.........................................................................................

    @Override
    public MenuItem<FontSize> createMenuItem(final FontSize fontSize) {
        Objects.requireNonNull(fontSize, "fontSize");

        return this.historyTokenMenuItem(
            fontSize.text(),
            fontSize,
            this.historyContext
        );
    }

    @Override
    public Optional<FontSize> toValue(final FontSize fontSize) {
        return Optional.of(fontSize);
    }

    @Override
    public void filter(final String startsWith,
                       final SuggestBoxComponent<FontSize> suggestBox) {
        final List<FontSize> fontSizes = Lists.array();

        try {
            final int number = Integer.parseInt(startsWith);

            for(int i = 0 ; i < 7; i++) {
                final int fontSize = number + i - 3;

                // cant add negative size FontSizes.
                if(fontSize > 0) {
                    fontSizes.add(
                        FontSize.with(fontSize)
                    );
                }
            }
        } catch (final NumberFormatException ignore) {
            // nop
        }

        suggestBox.setOptions(fontSizes);
    }

    @Override
    public void verifyOption(final FontSize fontSize,
                             final SuggestBoxComponent<FontSize> suggestBox) {
        if(null != fontSize) {
            suggestBox.setVerifiedOption(fontSize);
        }
    }

    // HistoryContextDelegator..........................................................................................

    @Override
    public HistoryContext historyContext() {
        return this.historyContext;
    }

    private final HistoryContext historyContext;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.historyContext.toString();
    }
}
