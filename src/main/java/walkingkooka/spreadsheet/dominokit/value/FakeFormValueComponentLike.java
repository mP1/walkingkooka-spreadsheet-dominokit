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

package walkingkooka.spreadsheet.dominokit.value;

import elemental2.dom.HTMLElement;

import java.util.List;
import java.util.Optional;

public class FakeFormValueComponentLike<E extends HTMLElement, C extends FormValueComponentLike<E, C>> extends FakeValueComponentLike<E, C>
    implements FormValueComponentLike<E, C> {

    public FakeFormValueComponentLike() {
        super();
    }

    // FormValueComponent...............................................................................................

    @Override
    public String label() {
        throw new UnsupportedOperationException();
    }

    @Override
    public C setLabel(String label) {
        throw new UnsupportedOperationException();
    }

    @Override 
    public C validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public C optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public C required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public C alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Optional<String> helperText() {
        throw new UnsupportedOperationException();
    }
    
    @Override 
    public C setHelperText(Optional<String> text) {
        throw new UnsupportedOperationException();
    }

    @Override 
    public List<String> errors() {
        throw new UnsupportedOperationException();
    }

    @Override
    public C setErrors(List<String> errors) {
        throw new UnsupportedOperationException();
    }
}
