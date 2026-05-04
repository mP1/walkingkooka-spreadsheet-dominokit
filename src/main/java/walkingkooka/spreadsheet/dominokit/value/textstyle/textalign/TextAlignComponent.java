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

package walkingkooka.spreadsheet.dominokit.value.textstyle.textalign;

import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.Cast;
import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentDelegator;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class TextAlignComponent implements TextStylePropertyEnumComponentDelegator<TextAlign, TextAlignComponent> {

    public static TextAlignComponent with(final String idPrefix,
                                          final TextAlignComponentContext context) {
        return new TextAlignComponent(
            idPrefix,
            context
        );
    }

    private TextAlignComponent(final String idPrefix,
                               final TextAlignComponentContext context) {
        super();
        this.component = TextStylePropertyEnumComponent.with(
            idPrefix,
            TextStylePropertyName.TEXT_ALIGN,
            Lists.of(
                null,
                TextAlign.LEFT,
                TextAlign.CENTER,
                TextAlign.RIGHT,
                TextAlign.JUSTIFY
            ),
            TextStylePropertyEnumComponent.valueToText(),
            (Optional<TextAlign> valueToIcon) -> Optional.of(
                valueToIcon.map(
                    (TextAlign textAlign) -> {
                        final Icon<?> icon;

                        switch (textAlign) {
                            case LEFT:
                                icon = SpreadsheetIcons.alignLeft();
                                break;
                            case CENTER:
                                icon = SpreadsheetIcons.alignCenter();
                                break;
                            case RIGHT:
                                icon = SpreadsheetIcons.alignRight();
                                break;
                            case JUSTIFY:
                                icon = SpreadsheetIcons.alignJustify();
                                break;
                            default:
                                icon = NeverError.unhandledEnum(
                                    textAlign,
                                    TextAlign.values()
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
            TEXT_STYLE_PROPERTY_FILTER_KINDS_TEXT,
            context // TextStylePropertyEnumComponentContext
        );
    }

    @Override
    public TextStylePropertyEnumComponent<TextAlign> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyEnumComponent<TextAlign> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
