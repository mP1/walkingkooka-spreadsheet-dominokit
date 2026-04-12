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

package walkingkooka.spreadsheet.dominokit.textstyle.textalign;

import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator;
import walkingkooka.text.CaseKind;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class TextAlignComponent implements TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator<TextAlign, TextAlignComponent> {

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
        this.component = TextStylePropertyNameEnumHistoryTokenAnchorListComponent.with(
            idPrefix,
            TextStylePropertyName.TEXT_ALIGN,
            Lists.of(
                TextAlign.values()
            ),
            (Optional<TextAlign> valueToText) ->
                valueToText.map(
                    (TextAlign textAlign) -> CaseKind.kebabEnumName(textAlign)
                ).orElse("Clear"), // valueToText
            (Optional<TextAlign> valueToIcon) -> valueToIcon.map(
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
                            icon = SpreadsheetIcons.alignRight();
                            break;
                        default:
                            icon = NeverError.unhandledEnum(
                                textAlign,
                                TextAlign.values()
                            );
                    }

                    return icon;
                }
            ),
            context // TextStylePropertyNameEnumHistoryTokenAnchorListComponentContext
        );
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<TextAlign> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<TextAlign> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
