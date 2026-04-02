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

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.TableComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.List;
import java.util.Optional;

public final class KeyBindingTableComponentTest implements TableComponentTesting<HTMLDivElement, List<KeyBinding>, KeyBindingTableComponent>,
    SpreadsheetMetadataTesting {

    @Test
    public void testRefresh() {
        final KeyBindingTableComponent component = KeyBindingTableComponent.empty(
            "id123-"
        );
        component.setValue(
            Optional.ofNullable(
                Lists.of(
                    KeyBinding.down("A")
                        .setLabel("Select ALL")
                )
            )
        );

        component.refresh(
            new FakeKeyBindingTableComponentContext() {

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.parseString("/2/Untitled/cell/D1/key");
                }

                @Override
                public List<KeyBinding> keyBindings() {
                    return Lists.of(
                        KeyBinding.down("A")
                            .setLabel("Select ALL"),
                        KeyBinding.down("B")
                            .setLabel("Bold")
                            .setShift(),
                        KeyBinding.down("Z")
                            .setLabel("Zap")
                            .setShift()
                    );
                }
            }
        );

        this.treePrintAndCheck(
            component,
            "KeyBindingTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      DataTableComponent\n" +
                "        id=id123-Table\n" +
                "        ROW(S)\n" +
                "          ROW 0\n" +
                "            TextNodeComponent\n" +
                "              Select ALL\n" +
                "            TextNodeComponent\n" +
                "              A \n" +
                "          ROW 1\n" +
                "            TextNodeComponent\n" +
                "              Bold\n" +
                "            TextNodeComponent\n" +
                "              B shift\n" +
                "          ROW 2\n" +
                "            TextNodeComponent\n" +
                "              Zap\n" +
                "            TextNodeComponent\n" +
                "              Z shift\n"
        );
    }

    @Test
    public void testRefreshMultipleBindings() {
        final KeyBindingTableComponent component = KeyBindingTableComponent.empty(
            "id123-"
        );

        component.refresh(
            new FakeKeyBindingTableComponentContext() {

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.parseString("/2/Untitled/cell/D1/key");
                }

                @Override
                public List<KeyBinding> keyBindings() {
                    return Lists.of(
                        KeyBinding.down("A")
                            .setLabel("Select ALL"),
                        KeyBinding.down("Z")
                            .setLabel("Zap")
                            .setShift(),
                        KeyBinding.down("B1")
                            .setLabel("Bold")
                            .setShift(),
                        KeyBinding.down("B2")
                            .setLabel("Bold")
                            .setShift()
                    );
                }
            }
        );

        this.treePrintAndCheck(
            component,
            "KeyBindingTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      DataTableComponent\n" +
                "        id=id123-Table\n" +
                "        ROW(S)\n" +
                "          ROW 0\n" +
                "            TextNodeComponent\n" +
                "              Select ALL\n" +
                "            TextNodeComponent\n" +
                "              A \n" +
                "          ROW 1\n" +
                "            TextNodeComponent\n" +
                "              Zap\n" +
                "            TextNodeComponent\n" +
                "              Z shift\n" +
                "          ROW 2\n" +
                "            TextNodeComponent\n" +
                "              Bold\n" +
                "            TextNodeComponent\n" +
                "              B1 shift\n" +
                "          ROW 3\n" +
                "            TextNodeComponent\n" +
                "              Bold\n" +
                "            TextNodeComponent\n" +
                "              B2 shift\n"
        );
    }

    @Override
    public KeyBindingTableComponent createComponent() {
        return KeyBindingTableComponent.empty(
            "TestId123-"
        );
    }

    // class............................................................................................................

    @Override
    public Class<KeyBindingTableComponent> type() {
        return KeyBindingTableComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
