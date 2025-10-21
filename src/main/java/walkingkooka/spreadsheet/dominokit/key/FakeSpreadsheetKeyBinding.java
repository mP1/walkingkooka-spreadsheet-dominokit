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

import java.util.Set;

public class FakeSpreadsheetKeyBinding implements SpreadsheetKeyBinding{

    public FakeSpreadsheetKeyBinding() {
        super();
    }

    @Override
    public Set<KeyBinding> bold() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<KeyBinding> italics() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<KeyBinding> leftTextAlign() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<KeyBinding> rightTextAlign() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<KeyBinding> selectAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<KeyBinding> strikethru() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<KeyBinding> underline() {
        throw new UnsupportedOperationException();
    }
}
