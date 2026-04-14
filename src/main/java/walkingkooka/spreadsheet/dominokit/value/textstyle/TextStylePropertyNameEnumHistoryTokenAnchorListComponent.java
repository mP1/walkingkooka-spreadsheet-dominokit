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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.text.CaseKind;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T extends Enum<T>> extends TextStylePropertyNameEnumHistoryTokenAnchorListComponentLike<T> {

    /**
     * A function that converts {@link Enum#name()} to Title Case.
     * <pre>
     * UPPER_CASE -> Upper Case
     * </pre>
     */
    public static <T extends Enum<T>> Function<Optional<T>, String> valueToText() {
        return (value) -> value.map(
            (T v) -> CaseKind.SNAKE.change(
                v.name(),
                CaseKind.TITLE
            )
        ).orElse("Clear");
    }

    public static <T extends Enum<T>> TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> with(final String idPrefix,
                                                                                                       final TextStylePropertyName<T> propertyName,
                                                                                                       final List<T> values,
                                                                                                       final Function<Optional<T>, String> valueToText,
                                                                                                       final Function<Optional<T>, Optional<Icon<?>>> valueToIcon,
                                                                                                       final TextStylePropertyNameEnumHistoryTokenAnchorListComponentContext context) {
        return new TextStylePropertyNameEnumHistoryTokenAnchorListComponent<>(
            idPrefix,
            propertyName,
            values,
            valueToText,
            valueToIcon,
            context
        );
    }

    private TextStylePropertyNameEnumHistoryTokenAnchorListComponent(final String idPrefix,
                                                                     final TextStylePropertyName<T> propertyName,
                                                                     final List<T> values,
                                                                     final Function<Optional<T>, String> valueToText,
                                                                     final Function<Optional<T>, Optional<Icon<?>>> valueToIcon,
                                                                     final TextStylePropertyNameEnumHistoryTokenAnchorListComponentContext context) {
        super(
            idPrefix,
            propertyName,
            values,
            valueToText,
            valueToIcon,
            context
        );
    }
}
