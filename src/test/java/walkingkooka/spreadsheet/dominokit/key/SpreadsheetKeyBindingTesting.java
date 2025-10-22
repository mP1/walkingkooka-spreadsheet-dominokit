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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.text.printer.TreePrintableTesting;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface SpreadsheetKeyBindingTesting<T extends SpreadsheetKeyBinding> extends ClassTesting<T>,
    TreePrintableTesting {

    @Test
    default void testUniqueKeyBindings() throws Exception {
        final Map<KeyBinding, Set<String>> duplicates = Maps.sorted();

        final SpreadsheetKeyBinding spreadsheetKeyBinding = this.createSpreadsheetKeyBinding();

        for(final Method method : SpreadsheetKeyBinding.class.getDeclaredMethods()) {
            for(KeyBinding key : (Set<KeyBinding>) method.invoke(spreadsheetKeyBinding)) {
                Set<String> getters = duplicates.get(key);
                if(null == getters) {
                    getters = SortedSets.tree();
                    duplicates.put(
                        key,
                        getters
                    );
                }

                getters.add(method.getName());
            }
        }


        this.checkEquals(
            Sets.empty(),
            duplicates.values()
                .stream()
                .filter(v -> v.size() > 1)
                .collect(Collectors.toCollection(SortedSets::tree))
        );
    }

    T createSpreadsheetKeyBinding();
}
