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

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Optional;

public final class UploadFileComponentTest implements FormValueComponentTesting<HTMLDivElement, BrowserFile, UploadFileComponent> {

    @Test
    public void testClearValueAndTreePrint() {
        this.treePrintAndCheck(
            UploadFileComponent.empty("id123")
                .clearValue(),
            "UploadFileComponent\n" +
                "  id=id123\n"
        );
    }

    @Test
    public void testSetValueAndTreePrint() {
        this.treePrintAndCheck(
            UploadFileComponent.empty("id456")
                .setValue(
                    Optional.of(
                        BrowserFile.base64(
                            "FileName123",
                            "FileContent456"
                        )
                    )
                ),
            "UploadFileComponent\n" +
                "  id=id456\n" +
                "    BrowserFileBase64\n" +
                "      \"FileName123\"\n" +
                "        FileContent456\n"
        );
    }

    @Test
    public void testSetLabelSetHelperSetValueAndTreePrint() {
        this.treePrintAndCheck(
            UploadFileComponent.empty("id456")
                .setLabel("Label123")
                .setHelperText(
                    Optional.of("HelperText123")
                ).setValue(
                    Optional.of(
                        BrowserFile.base64(
                            "FileName123",
                            "FileContent456"
                        )
                    )
                ),
            "UploadFileComponent\n" +
                "  id=id456\n" +
                "  label=Label123\n" +
                "  helperText=HelperText123\n" +
                "    BrowserFileBase64\n" +
                "      \"FileName123\"\n" +
                "        FileContent456\n"
        );
    }

    @Override
    public UploadFileComponent createComponent() {
        return UploadFileComponent.empty("id999");
    }

    // class............................................................................................................

    @Override
    public Class<UploadFileComponent> type() {
        return UploadFileComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
