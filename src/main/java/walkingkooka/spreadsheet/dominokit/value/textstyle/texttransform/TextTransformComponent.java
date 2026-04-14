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

package walkingkooka.spreadsheet.dominokit.value.textstyle.texttransform;

import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.TextTransform;

import java.util.Optional;

public final class TextTransformComponent implements TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator<TextTransform, TextTransformComponent> {

    public static TextTransformComponent with(final String idPrefix,
                                              final TextTransformComponentContext context) {
        return new TextTransformComponent(
            idPrefix,
            context
        );
    }

    private TextTransformComponent(final String idPrefix,
                                   final TextTransformComponentContext context) {
        super();
        this.component = TextStylePropertyNameEnumHistoryTokenAnchorListComponent.with(
            idPrefix,
            TextStylePropertyName.TEXT_TRANSFORM,
            Lists.of(
                null,
                TextTransform.NONE,
                TextTransform.CAPITALIZE,
                TextTransform.UPPERCASE,
                TextTransform.LOWERCASE
            ),
            TextStylePropertyNameEnumHistoryTokenAnchorListComponent.valueToText(),
            (Optional<TextTransform> valueToIcon) -> Optional.ofNullable(
                valueToIcon.map(
                    (TextTransform textTransform) -> {
                        final Icon<?> icon;

                        switch (textTransform) {
                            case NONE:
                                icon = null;
                                break;
                            case CAPITALIZE:
                                icon = SpreadsheetIcons.textCaseCapitalize();
                                break;
                            case UPPERCASE:
                                icon = SpreadsheetIcons.textCaseUpper();
                                break;
                            case LOWERCASE:
                                icon = SpreadsheetIcons.textCaseLower();
                                break;
                            default:
                                icon = NeverError.unhandledEnum(
                                    textTransform,
                                    TextTransform.values()
                                );
                        }

                        return icon;
                    }
                ).orElse(null)
            ),
            context // TextStylePropertyNameEnumHistoryTokenAnchorListComponentContext
        );
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<TextTransform> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<TextTransform> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
