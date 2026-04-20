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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontsize;

import org.dominokit.domino.ui.menu.MenuItem;
import walkingkooka.spreadsheet.dominokit.suggestbox.FakeSuggestBoxComponentSuggestionsProvider;
import walkingkooka.tree.text.FontSize;

import java.util.Optional;

public class FakeFontSizeComponentContext extends FakeSuggestBoxComponentSuggestionsProvider<FontSize>
    implements FontSizeComponentContext {

    public FakeFontSizeComponentContext() {
        super();
    }

    @Override
    public MenuItem<FontSize> createMenuItem(final FontSize value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<FontSize> toValue(final FontSize fontSize) {
        throw new UnsupportedOperationException();
    }
}
