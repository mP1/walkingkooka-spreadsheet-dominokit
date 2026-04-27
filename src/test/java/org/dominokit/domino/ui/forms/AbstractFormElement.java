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

package org.dominokit.domino.ui.forms;

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.dom.DivComponent;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;

import java.util.List;

public abstract class AbstractFormElement<T extends AbstractFormElement<T, V>, V> {

    protected AbstractFormElement() {
        super();
    }

    protected final DivComponent wrapperElement = HtmlElementComponent.div();

    public String getId() {
        return this.id;
    }

    public T setId(final String id) {
        this.id = id;
        return (T) this;
    }

    private String id;

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private String label;

    public String getHelperText() {
        return this.helperText;
    }

    public void setHelperText(final String helperText) {
        this.helperText = helperText;
    }

    private String helperText;

    public List<String> getErrors() {
        return this.errors;
    }

    public void setErrors(final List<String> errors) {
        this.errors = errors;
    }

    private List<String> errors = Lists.empty();
}
