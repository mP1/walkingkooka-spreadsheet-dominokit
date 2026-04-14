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

package walkingkooka.spreadsheet.dominokit.value.textstyle.hangingpunctuation;

import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.Cast;
import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator;
import walkingkooka.tree.text.HangingPunctuation;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class HangingPunctuationComponent implements TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator<HangingPunctuation, HangingPunctuationComponent> {

    public static HangingPunctuationComponent with(final String idPrefix,
                                                   final HangingPunctuationComponentContext context) {
        return new HangingPunctuationComponent(
            idPrefix,
            context
        );
    }
    
    private HangingPunctuationComponent(final String idPrefix,
                                        final HangingPunctuationComponentContext context) {
        super();
        this.component = TextStylePropertyNameEnumHistoryTokenAnchorListComponent.with(
            idPrefix,
            TextStylePropertyName.HANGING_PUNCTUATION,
            Lists.of(
                null,
                HangingPunctuation.NONE,
                HangingPunctuation.FIRST,
                HangingPunctuation.LAST,
                HangingPunctuation.ALLOW_END,
                HangingPunctuation.FORCE_END
            ),
            TextStylePropertyNameEnumHistoryTokenAnchorListComponent.valueToText(),
            (Optional<HangingPunctuation> valueToIcon) -> Optional.of(
                valueToIcon.map(
                    (HangingPunctuation hangingPunctuation) -> {
                        final Icon<?> icon;

                        switch (hangingPunctuation) {
                            case NONE:
                                icon = null;
                                break;
                            case FIRST:
                                icon = null;
                                break;
                            case LAST:
                                icon = null;
                                break;
                            case ALLOW_END:
                                icon = null;
                                break;
                            case FORCE_END:
                                icon = null;
                                break;
                            default:
                                icon = NeverError.unhandledEnum(
                                    hangingPunctuation,
                                    HangingPunctuation.values()
                                );
                        }

                        return icon;
                    }
                ).orElse(
                    Cast.to(
                        SpreadsheetIcons.alignClear()
                    )
                )
            ),
            context // TextStylePropertyNameEnumHistoryTokenAnchorListComponentContext
        );
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<HangingPunctuation> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<HangingPunctuation> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
