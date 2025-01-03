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

package walkingkooka.spreadsheet.dominokit.upload;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Optional;

/**
 * Base class that captures the common methods between the real and test implementation/sub-classes.
 */
abstract class SpreadsheetUploadFileComponentLike implements ValueComponent<HTMLFieldSetElement, BrowserFile, SpreadsheetUploadFileComponent> {

    SpreadsheetUploadFileComponentLike() {
        super();
    }

    @Override
    public final SpreadsheetUploadFileComponent setLabel(final String label) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String label() {
        return "";
    }

    @Override
    public final Optional<String> helperText() {
        return Optional.empty();
    }

    @Override
    public final SpreadsheetUploadFileComponent setHelperText(final Optional<String> text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetUploadFileComponent optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetUploadFileComponent required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean isRequired() {
        return false;
    }

    @Override
    public final SpreadsheetUploadFileComponent validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final List<String> errors() {
        return List.of();
    }

    @Override
    public final SpreadsheetUploadFileComponent setErrors(final List<String> errors) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetUploadFileComponent alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetUploadFileComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetUploadFileComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetUploadFileComponent addKeydownListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetUploadFileComponent addKeyupListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            printer.println("id=" + this.id());
            if(this.isDisabled()) {
                printer.println("disabled");
            }

            this.value()
                    .ifPresent(value -> value.printTree(printer));
        }
        printer.outdent();
    }
}
