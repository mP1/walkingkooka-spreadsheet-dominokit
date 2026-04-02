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

package walkingkooka.spreadsheet.dominokit.key;

import walkingkooka.spreadsheet.dominokit.Component;
import walkingkooka.spreadsheet.dominokit.datatable.DataTableComponentCellRenderer;
import walkingkooka.spreadsheet.dominokit.text.TextNodeComponent;
import walkingkooka.tree.text.TextNode;

import java.util.Optional;

final class KeyBindingTableComponentDataTableComponentCellRenderer implements DataTableComponentCellRenderer<KeyBinding> {

    /**
     * Singleton
     */
    final static KeyBindingTableComponentDataTableComponentCellRenderer INSTANCE = new KeyBindingTableComponentDataTableComponentCellRenderer();

    private KeyBindingTableComponentDataTableComponentCellRenderer() {
        super();
    }

    @Override
    public Component render(final int column,
                            final KeyBinding binding) {
        final Component rendered;

        switch (column) {
            case 0:
                rendered = this.text(
                    binding.label()
                );
                break;
            case 1:
                // Shift A
                rendered = this.text(
                    binding.toStringModifiers() +
                        " " +
                        binding.key()
                );
                break;
            default:
                throw new IllegalArgumentException("Invalid column " + column);
        }

        return rendered;
    }

    private Component text(final String label) {
        return TextNodeComponent.with(
            Optional.of(
                TextNode.text(label)
            )
        );
    }
}
