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

package walkingkooka.spreadsheet.dominokit.ui;

import elemental2.dom.Element;
import org.dominokit.domino.ui.IsElement;
import walkingkooka.CanBeEmpty;

import java.util.List;

/**
 * Defines methods for a {@link Component} that has children.
 */
public interface ComponentWithChildren<C extends Component<E>, E extends Element> extends CanBeEmpty {

    /**
     * Appends a new child.
     */
    C appendChild(final IsElement<?> child);

    /**
     * Getter that returns the child at index.
     */
    default IsElement<?> child(final int index) {
        return this.children()
                .get(index);
    }

    /**
     * Removes an existing child.
     */
    C removeChild(final int index);

    /**
     * Removes all children.
     */
    default C removeAllChildren() {
        final int count = this.children()
                .size();
        for (int i = 0; i < count; i++) {
            this.removeChild(0);
        }

        return (C) this;
    }

    /**
     * Getter that returns all children.
     */
    List<IsElement<?>> children();

    /**
     * Returns true if this component is empty (has no children).
     */
    default boolean isEmpty() {
        boolean hide = true;
        
        for (final IsElement<?> child : this.children()) {
            if (child instanceof CanBeEmpty) {
                final CanBeEmpty canBeEmpty = (CanBeEmpty) child;
                if (canBeEmpty.isEmpty()) {
                    continue;
                }
            }

            hide = false;
            break;
        }

        return hide;
    }
}
