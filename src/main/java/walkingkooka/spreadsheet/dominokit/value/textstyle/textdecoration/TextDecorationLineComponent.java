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

package walkingkooka.spreadsheet.dominokit.value.textstyle.textdecoration;

import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.Cast;
import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator;
import walkingkooka.tree.text.TextDecorationLine;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;
import java.util.function.Function;

public final class TextDecorationLineComponent implements TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator<TextDecorationLine, TextDecorationLineComponent> {

    public static TextDecorationLineComponent with(final String idPrefix,
                                                   final TextDecorationLineComponentContext context) {
        return new TextDecorationLineComponent(
            idPrefix,
            context
        );
    }

    private TextDecorationLineComponent(final String idPrefix,
                                        final TextDecorationLineComponentContext context) {
        super();

        final Function<Optional<TextDecorationLine>, String> valueToText = TextStylePropertyNameEnumHistoryTokenAnchorListComponent.valueToText();

        this.component = TextStylePropertyNameEnumHistoryTokenAnchorListComponent.with(
            idPrefix,
            TextStylePropertyName.TEXT_DECORATION_LINE,
            Lists.of(
                null,
                TextDecorationLine.NONE,
                TextDecorationLine.LINE_THROUGH,
                TextDecorationLine.OVERLINE,
                TextDecorationLine.UNDERLINE
            ),
            (Optional<TextDecorationLine> textDecorationLine) -> valueToText.apply(textDecorationLine)
                .replace("Line Through", "Strikethrough"),
            (Optional<TextDecorationLine> valueToIcon) -> Optional.of(
                valueToIcon.map(
                    (TextDecorationLine textDecorationLine) -> {
                        final Icon<?> icon;

                        switch (textDecorationLine) {
                            case NONE:
                                icon = SpreadsheetIcons.textDecorationLineNone();
                                break;
                            case LINE_THROUGH:
                                icon = SpreadsheetIcons.strikethrough();
                                break;
                            case OVERLINE:
                                icon = null;
                                break;
                            case UNDERLINE:
                                icon = SpreadsheetIcons.underline();
                                break;
                            default:
                                icon = NeverError.unhandledEnum(
                                    textDecorationLine,
                                    TextDecorationLine.values()
                                );
                        }

                        return icon;
                    }
                ).orElse(
                    Cast.to(
                        SpreadsheetIcons.textDecorationLineClear()
                    )
                )
            ),
            context // TextStylePropertyNameEnumHistoryTokenAnchorListComponentContext
        );
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<TextDecorationLine> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<TextDecorationLine> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
