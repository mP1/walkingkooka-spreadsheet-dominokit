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

package walkingkooka.spreadsheet.dominokit.value.textstyle.direction;

import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilterKind;
import walkingkooka.tree.text.Direction;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.EnumSet;
import java.util.Optional;

public final class DirectionComponent implements TextStylePropertyEnumComponentDelegator<Direction, DirectionComponent> {

    public static DirectionComponent with(final String idPrefix,
                                          final DirectionComponentContext context) {
        return new DirectionComponent(
            idPrefix,
            context
        );
    }

    private DirectionComponent(final String idPrefix,
                               final DirectionComponentContext context) {
        super();
        this.component = TextStylePropertyEnumComponent.with(
            idPrefix,
            TextStylePropertyName.DIRECTION,
            Lists.of(
                null,
                Direction.LTR,
                Direction.RTL
            ),
            (Optional<Direction> d) -> d.map(
                (Direction direction) -> {
                    final String text;

                    switch (direction) {
                        case LTR:
                            text = "Left to Right";
                            break;
                        case RTL:
                            text = "Right to Left";
                            break;
                        default:
                            text = NeverError.unhandledEnum(
                                direction,
                                Direction.values()
                            );
                    }

                    return text;
                }
            ).orElse("Clear"),
            TextStylePropertyEnumComponent.noIcons(),
            EnumSet.noneOf(TextStylePropertyFilterKind.class),
            context // TextStylePropertyEnumComponentContext
        );
    }

    @Override
    public TextStylePropertyEnumComponent<Direction> textStylePropertyEnumComponent() {
        return this.component;
    }

    private final TextStylePropertyEnumComponent<Direction> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
