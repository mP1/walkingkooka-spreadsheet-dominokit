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

import walkingkooka.collect.set.Sets;

import java.util.Set;

final class BasicSpreadsheetKeyBinding implements SpreadsheetKeyBinding{

    /**
     * Singleton
     */
    final static BasicSpreadsheetKeyBinding INSTANCE = new BasicSpreadsheetKeyBinding();

    private BasicSpreadsheetKeyBinding() {
        super();
    }

    @Override
    public Set<KeyBinding> bold() {
        return Sets.of(
            control("b"),
            control("2")
        );
    }

    @Override
    public Set<KeyBinding> italics() {
        return Sets.of(
            control("i"),
            control("3")
        );
    }

    @Override
    public Set<KeyBinding> selectAll() {
        return Sets.of(
            control("a")
        );
    }

    @Override
    public Set<KeyBinding> strikethru() {
        return Sets.of(
            control("5")
        );
    }

    @Override
    public Set<KeyBinding> underline() {
        return Sets.of(
            control("u"),
            control("4")
        );
    }
    
    private static KeyBinding control(final String key) {
        return KeyBinding.with(key)
            .setControl();
    }
}
