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

package walkingkooka.spreadsheet.dominokit.file;

import org.gwtproject.core.shared.GwtIncompatible;
import walkingkooka.Binary;
import walkingkooka.net.HasUrlFragment;
import walkingkooka.spreadsheet.dominokit.fetcher.Fetcher;

import java.util.Optional;

/**
 * Holds a native file blob, such as a dropped file or a file selected using a file picker.
 */
public abstract class File extends FileGwt implements HasUrlFragment {

    @GwtIncompatible
    public static File parse(final String value) {
        return FileBinary.parseFileBinary(value);
    }

    /**
     * {@see FileBinary}.
     */
    public static File withBinary(final Binary binary) {
        return FileBinary.withBinary0(binary);
    }

    /**
     * {@see FileElemental}.
     */
    public static File withFile(final elemental2.dom.File file) {
        return FileElemental.withFile0(file);
    }

    File() {
        super();
    }

    /**
     * Getter only intended to be called by {@link Fetcher}.
     * <br>
     * The file may be absent if {@link #parse(String)} was used to create this instance.
     */
    public abstract Optional<elemental2.dom.File> file();

    // Object...........................................................................................................

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(final Object other);

    @Override
    public abstract String toString();
}
