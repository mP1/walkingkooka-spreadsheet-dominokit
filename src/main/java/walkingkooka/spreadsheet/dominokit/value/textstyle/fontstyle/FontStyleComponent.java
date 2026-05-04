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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontstyle;

import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumHistoryTokenAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumHistoryTokenAnchorListComponentDelegator;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class FontStyleComponent implements TextStylePropertyEnumHistoryTokenAnchorListComponentDelegator<FontStyle, FontStyleComponent> {

    public static FontStyleComponent with(final String idPrefix,
                                          final FontStyleComponentContext context) {
        return new FontStyleComponent(
            idPrefix,
            context
        );
    }

    private FontStyleComponent(final String idPrefix,
                               final FontStyleComponentContext context) {
        super();
        this.component = TextStylePropertyEnumHistoryTokenAnchorListComponent.with(
            idPrefix,
            TextStylePropertyName.FONT_STYLE,
            Lists.of(
                null,
                FontStyle.NORMAL,
                FontStyle.ITALIC,
                FontStyle.OBLIQUE
            ),
            TextStylePropertyEnumHistoryTokenAnchorListComponent.valueToText(),
            (Optional<FontStyle> valueToIcon) -> Optional.ofNullable(
                valueToIcon.map(
                    (FontStyle fontStyle) -> {
                        final Icon<?> icon;

                        switch (fontStyle) {
                            case NORMAL:
                                icon = null;
                                break;
                            case ITALIC:
                                icon = SpreadsheetIcons.italics();
                                break;
                            case OBLIQUE:
                                icon = null;
                                break;
                            default:
                                icon = NeverError.unhandledEnum(
                                    fontStyle,
                                    FontStyle.values()
                                );
                        }

                        return icon;
                    }
                ).orElse(null)
            ),
            TEXT_STYLE_PROPERTY_FILTER_KINDS_FONT,
            context // TextStylePropertyEnumHistoryTokenAnchorListComponentContext
        );
    }

    @Override
    public TextStylePropertyEnumHistoryTokenAnchorListComponent<FontStyle> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyEnumHistoryTokenAnchorListComponent<FontStyle> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
