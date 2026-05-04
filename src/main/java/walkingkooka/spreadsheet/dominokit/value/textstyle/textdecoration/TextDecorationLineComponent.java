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
import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumHistoryTokenAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumHistoryTokenAnchorListComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilterKind;
import walkingkooka.tree.text.TextDecorationLine;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Function;

public final class TextDecorationLineComponent implements TextStylePropertyEnumHistoryTokenAnchorListComponentDelegator<TextDecorationLine, TextDecorationLineComponent> {

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

        final Function<Optional<TextDecorationLine>, String> valueToText = TextStylePropertyEnumHistoryTokenAnchorListComponent.valueToText();

        this.component = TextStylePropertyEnumHistoryTokenAnchorListComponent.with(
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
            (Optional<TextDecorationLine> valueToIcon) -> {
                final Icon<?> icon;

                final TextDecorationLine textDecorationLine = valueToIcon.orElse(null);

                if (null != textDecorationLine) {
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
                } else {
                    icon = null;
                }

                return Optional.ofNullable(icon);
            },
            EnumSet.of(
                TextStylePropertyFilterKind.TEXT
            ),
            context // TextStylePropertyEnumHistoryTokenAnchorListComponentContext
        );
    }

    @Override
    public TextStylePropertyEnumHistoryTokenAnchorListComponent<TextDecorationLine> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyEnumHistoryTokenAnchorListComponent<TextDecorationLine> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
