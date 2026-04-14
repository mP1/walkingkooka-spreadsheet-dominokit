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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontvariant;

import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator;
import walkingkooka.tree.text.FontVariant;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class FontVariantComponent implements TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator<FontVariant, FontVariantComponent> {

    public static FontVariantComponent with(final String idPrefix,
                                            final FontVariantComponentContext context) {
        return new FontVariantComponent(
            idPrefix,
            context
        );
    }

    private FontVariantComponent(final String idPrefix,
                                 final FontVariantComponentContext context) {
        super();
        this.component = TextStylePropertyNameEnumHistoryTokenAnchorListComponent.with(
            idPrefix,
            TextStylePropertyName.FONT_VARIANT,
            Lists.of(
                null,
                FontVariant.INITIAL,
                FontVariant.NORMAL,
                FontVariant.SMALL_CAPS
            ),
            TextStylePropertyNameEnumHistoryTokenAnchorListComponent.valueToText(),
            (Optional<FontVariant> valueToIcon) -> Optional.ofNullable(
                valueToIcon.map(
                    (FontVariant fontVariant) -> {
                        final Icon<?> icon;

                        switch (fontVariant) {
                            case INITIAL:
                                icon = null;
                                break;
                            case NORMAL:
                                icon = null;
                                break;
                            case SMALL_CAPS:
                                icon = null;
                                break;
                            default:
                                icon = NeverError.unhandledEnum(
                                    fontVariant,
                                    FontVariant.values()
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
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<FontVariant> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<FontVariant> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
