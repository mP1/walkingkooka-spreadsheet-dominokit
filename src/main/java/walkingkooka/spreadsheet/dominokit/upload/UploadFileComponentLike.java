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
import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;

/**
 * Base class that captures the common methods between the real and test implementation/sub-classes.
 */
abstract class UploadFileComponentLike implements FormValueComponent<HTMLDivElement, BrowserFile, UploadFileComponent> {

    UploadFileComponentLike() {
        super();
    }

    @Override
    public final UploadFileComponent optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final UploadFileComponent required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean isRequired() {
        return false;
    }

    @Override
    public final UploadFileComponent validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final List<String> errors() {
        return List.of();
    }

    @Override
    public final UploadFileComponent setErrors(final List<String> errors) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final UploadFileComponent alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final UploadFileComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final UploadFileComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    // addXXXListener...................................................................................................

    @Override
    public final UploadFileComponent addClickListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final UploadFileComponent addKeyDownListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final UploadFileComponent addKeyUpListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            printer.println("id=" + this.id());
            if (this.isDisabled()) {
                printer.println("disabled");
            }

            final String label = this.label();
            if (false == label.isEmpty()) {
                printer.println("label=" + label);
            }

            final String helperText = this.helperText()
                .orElse("");
            if (false == helperText.isEmpty()) {
                printer.println("helperText=" + helperText);
            }

            printer.indent();
            {
                this.value()
                    .ifPresent(value -> value.printTree(printer));
            }
            printer.outdent();
        }
        printer.outdent();
    }
}
