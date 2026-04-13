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
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator;
import walkingkooka.tree.text.TextDecorationStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class TextDecorationStyleComponent implements TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator<TextDecorationStyle, TextDecorationStyleComponent> {

    public static TextDecorationStyleComponent with(final String idPrefix,
                                                    final TextDecorationStyleComponentContext context) {
        return new TextDecorationStyleComponent(
            idPrefix,
            context
        );
    }

    private TextDecorationStyleComponent(final String idPrefix,
                                         final TextDecorationStyleComponentContext context) {
        super();

        this.component = TextStylePropertyNameEnumHistoryTokenAnchorListComponent.with(
            idPrefix,
            TextStylePropertyName.TEXT_DECORATION_STYLE,
            Lists.of(
                null,
                TextDecorationStyle.SOLID,
                TextDecorationStyle.DOUBLE,
                TextDecorationStyle.DASHED,
                TextDecorationStyle.DOTTED,
                TextDecorationStyle.WAVY
            ),
            TextStylePropertyNameEnumHistoryTokenAnchorListComponent.valueToText(),
            (Optional<TextDecorationStyle> valueToIcon) -> {
                final Icon<?> icon;

                final TextDecorationStyle textDecorationStyle = valueToIcon.orElse(null);

                if (null != textDecorationStyle) {
                    switch (textDecorationStyle) {
                        case SOLID:
                            icon = null;
                            break;
                        case DOUBLE:
                            icon = null;
                            break;
                        case DASHED:
                            icon = null;
                            break;
                        case DOTTED:
                            icon = null;
                            break;
                        case WAVY:
                            icon = null;
                            break;
                        default:
                            icon = NeverError.unhandledEnum(
                                textDecorationStyle,
                                TextDecorationStyle.values()
                            );
                    }
                } else {
                    icon = null;
                }

                return Optional.ofNullable(icon);
            },
            context // TextStylePropertyNameEnumHistoryTokenAnchorListComponentContext
        );
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<TextDecorationStyle> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<TextDecorationStyle> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
