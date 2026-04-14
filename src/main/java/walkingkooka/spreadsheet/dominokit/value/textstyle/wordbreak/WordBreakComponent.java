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

package walkingkooka.spreadsheet.dominokit.value.textstyle.wordbreak;

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.WordBreak;

public final class WordBreakComponent implements TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator<WordBreak, WordBreakComponent> {

    public static WordBreakComponent with(final String idPrefix,
                                          final WordBreakComponentContext context) {
        return new WordBreakComponent(
            idPrefix,
            context
        );
    }

    private WordBreakComponent(final String idPrefix,
                               final WordBreakComponentContext context) {
        super();
        this.component = TextStylePropertyNameEnumHistoryTokenAnchorListComponent.with(
            idPrefix,
            TextStylePropertyName.WORD_BREAK,
            Lists.of(
                null,
                WordBreak.NORMAL,
                WordBreak.BREAK_ALL,
                WordBreak.KEEP_ALL,
                WordBreak.BREAK_WORD
            ),
            TextStylePropertyNameEnumHistoryTokenAnchorListComponent.valueToText(),
            TextStylePropertyNameEnumHistoryTokenAnchorListComponent.noIcons(),
            context // TextStylePropertyNameEnumHistoryTokenAnchorListComponentContext
        );
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<WordBreak> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<WordBreak> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
