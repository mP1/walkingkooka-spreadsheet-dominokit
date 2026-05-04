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

package walkingkooka.spreadsheet.dominokit.value.textstyle.verticalalign;

import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.Cast;
import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentDelegator;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.VerticalAlign;

import java.util.Optional;

public final class VerticalAlignComponent implements TextStylePropertyEnumComponentDelegator<VerticalAlign, VerticalAlignComponent> {

    public static VerticalAlignComponent with(final String idPrefix,
                                              final VerticalAlignComponentContext context) {
        return new VerticalAlignComponent(
            idPrefix,
            context
        );
    }

    private VerticalAlignComponent(final String idPrefix,
                                   final VerticalAlignComponentContext context) {
        super();
        this.component = TextStylePropertyEnumComponent.with(
            idPrefix,
            TextStylePropertyName.VERTICAL_ALIGN,
            Lists.of(
                null,
                VerticalAlign.TOP,
                VerticalAlign.MIDDLE,
                VerticalAlign.BOTTOM
            ),
            TextStylePropertyEnumComponent.valueToText(),
            (Optional<VerticalAlign> valueToIcon) -> Optional.of(
                valueToIcon.map(
                    (VerticalAlign verticalAlign) -> {
                        final Icon<?> icon;

                        switch (verticalAlign) {
                            case TOP:
                                icon = SpreadsheetIcons.verticalAlignTop();
                                break;
                            case MIDDLE:
                                icon = SpreadsheetIcons.verticalAlignMiddle();
                                break;
                            case BOTTOM:
                                icon = SpreadsheetIcons.verticalAlignBottom();
                                break;
                            default:
                                icon = NeverError.unhandledEnum(
                                    verticalAlign,
                                    VerticalAlign.values()
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
            TextStylePropertyComponent.TEXT_STYLE_PROPERTY_FILTER_KINDS_TEXT,
            context // TextStylePropertyEnumComponentContext
        );
    }

    @Override
    public TextStylePropertyEnumComponent<VerticalAlign> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyEnumComponent<VerticalAlign> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
