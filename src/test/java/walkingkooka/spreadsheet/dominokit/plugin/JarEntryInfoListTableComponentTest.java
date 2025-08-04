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

package walkingkooka.spreadsheet.dominokit.plugin;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.plugin.PluginName;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.TableComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfo;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoList;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

public final class JarEntryInfoListTableComponentTest implements TableComponentTesting<HTMLDivElement, JarEntryInfoList, JarEntryInfoListTableComponent>,
    SpreadsheetMetadataTesting {

    private final static String ID_PREFIX = "Table123-";

    @Test
    public void testEmpty() {
        this.treePrintAndCheck(
            this.createComponent(),
            "JarEntryInfoListTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      SpreadsheetDataTableComponent\n" +
                "        id=Table123-Table\n" +
                "        COLUMN(S)\n" +
                "          Name\n" +
                "          Size(Compressed)\n" +
                "          Method\n" +
                "          CRC\n" +
                "          Created\n" +
                "          Last modified timestamp\n" +
                "          Links\n" +
                "        PLUGINS\n" +
                "          EmptyStatePlugin (mdi-gauge-empty) \"empty JAR file\"\n"
        );
    }

    @Test
    public void testOneRow() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        JarEntryInfoList.with(
                            Lists.of(
                                this.jarEntryInfo(
                                    "/META/MANIFEST.MF", // filename
                                    100, // size
                                    50, // compressedSize
                                    1, // method
                                    0x12345678, // crc
                                    "1999-12-31T12:58:59", // created
                                    "2000-01-02T12:58:59" // last mod
                                )
                            )
                        )
                    )
                ),
            "JarEntryInfoListTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      SpreadsheetDataTableComponent\n" +
                "        id=Table123-Table\n" +
                "        COLUMN(S)\n" +
                "          Name\n" +
                "          Size(Compressed)\n" +
                "          Method\n" +
                "          CRC\n" +
                "          Created\n" +
                "          Last modified timestamp\n" +
                "          Links\n" +
                "        ROW(S)\n" +
                "          ROW 0\n" +
                "            TextComponent\n" +
                "              \"/META/MANIFEST.MF\"\n" +
                "            TextComponent\n" +
                "              \"100 (50)\"\n" +
                "            TextComponent\n" +
                "              \"1\"\n" +
                "            TextComponent\n" +
                "              \"12345678\"\n" +
                "            TextComponent\n" +
                "              \"31/12/99, 12:58 pm\"\n" +
                "            TextComponent\n" +
                "              \"2/1/00, 12:58 pm\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Download\" [/api/plugin/TestPlugin123/download/META/MANIFEST.MF] id=Table123-download-Link\n" +
                "                \"View\" [#/plugin/TestPlugin123/file/META/MANIFEST.MF] id=Table123-view-Link\n" +
                "        PLUGINS\n" +
                "          EmptyStatePlugin (mdi-gauge-empty) \"empty JAR file\"\n"
        );
    }

    @Test
    public void testOneRowMissingOptionalValues() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        JarEntryInfoList.with(
                            Lists.of(
                                this.jarEntryInfo(
                                    "/dir1/file2.txt", // filename
                                    0, // size
                                    0, // compressedSize
                                    0, // method
                                    0, // crc
                                    "", // created
                                    "" // last mod
                                )
                            )
                        )
                    )
                ),
            "JarEntryInfoListTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      SpreadsheetDataTableComponent\n" +
                "        id=Table123-Table\n" +
                "        COLUMN(S)\n" +
                "          Name\n" +
                "          Size(Compressed)\n" +
                "          Method\n" +
                "          CRC\n" +
                "          Created\n" +
                "          Last modified timestamp\n" +
                "          Links\n" +
                "        ROW(S)\n" +
                "          ROW 0\n" +
                "            TextComponent\n" +
                "              \"/dir1/file2.txt\"\n" +
                "            TextComponent\n" +
                "              \"\"\n" +
                "            TextComponent\n" +
                "              \"\"\n" +
                "            TextComponent\n" +
                "              \"\"\n" +
                "            TextComponent\n" +
                "              \"\"\n" +
                "            TextComponent\n" +
                "              \"\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Download\" [/api/plugin/TestPlugin123/download/dir1/file2.txt] id=Table123-download-Link\n" +
                "                \"View\" [#/plugin/TestPlugin123/file/dir1/file2.txt] id=Table123-view-Link\n" +
                "        PLUGINS\n" +
                "          EmptyStatePlugin (mdi-gauge-empty) \"empty JAR file\"\n"
        );
    }

    @Test
    public void testTwoRowsSecondMissingOptionalValues() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        JarEntryInfoList.with(
                            Lists.of(
                                this.jarEntryInfo(
                                    "/META/MANIFEST.MF", // filename
                                    100, // size
                                    50, // compressedSize
                                    1, // method
                                    0x12345678, // crc
                                    "1999-12-31T12:58:59", // created
                                    "2000-01-02T12:58:59" // last mod
                                ),
                                this.jarEntryInfo(
                                    "/dir1/file2.txt", // filename
                                    0, // size
                                    0, // compressedSize
                                    0, // method
                                    0, // crc
                                    "", // created
                                    "" // last mod
                                )
                            )
                        )
                    )
                ),
            "JarEntryInfoListTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      SpreadsheetDataTableComponent\n" +
                "        id=Table123-Table\n" +
                "        COLUMN(S)\n" +
                "          Name\n" +
                "          Size(Compressed)\n" +
                "          Method\n" +
                "          CRC\n" +
                "          Created\n" +
                "          Last modified timestamp\n" +
                "          Links\n" +
                "        ROW(S)\n" +
                "          ROW 0\n" +
                "            TextComponent\n" +
                "              \"/META/MANIFEST.MF\"\n" +
                "            TextComponent\n" +
                "              \"100 (50)\"\n" +
                "            TextComponent\n" +
                "              \"1\"\n" +
                "            TextComponent\n" +
                "              \"12345678\"\n" +
                "            TextComponent\n" +
                "              \"31/12/99, 12:58 pm\"\n" +
                "            TextComponent\n" +
                "              \"2/1/00, 12:58 pm\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Download\" [/api/plugin/TestPlugin123/download/META/MANIFEST.MF] id=Table123-download-Link\n" +
                "                \"View\" [#/plugin/TestPlugin123/file/META/MANIFEST.MF] id=Table123-view-Link\n" +
                "          ROW 1\n" +
                "            TextComponent\n" +
                "              \"/dir1/file2.txt\"\n" +
                "            TextComponent\n" +
                "              \"\"\n" +
                "            TextComponent\n" +
                "              \"\"\n" +
                "            TextComponent\n" +
                "              \"\"\n" +
                "            TextComponent\n" +
                "              \"\"\n" +
                "            TextComponent\n" +
                "              \"\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Download\" [/api/plugin/TestPlugin123/download/dir1/file2.txt] id=Table123-download-Link\n" +
                "                \"View\" [#/plugin/TestPlugin123/file/dir1/file2.txt] id=Table123-view-Link\n" +
                "        PLUGINS\n" +
                "          EmptyStatePlugin (mdi-gauge-empty) \"empty JAR file\"\n"
        );
    }

    @Override
    public JarEntryInfoListTableComponent createComponent() {
        return JarEntryInfoListTableComponent.empty(
            ID_PREFIX,
            context()
        );
    }

    private static FakeJarEntryInfoListTableComponentContext context() {
        return new FakeJarEntryInfoListTableComponentContext() {
            @Override
            public HistoryToken historyToken() {
                return HistoryToken.parseString("/plugin/TestPlugin123");
            }

            @Override public PluginName pluginName() {
                return PluginName.with("TestPlugin123");
            }

            @Override
            public Locale locale() {
                return Locale.forLanguageTag("EN-AU");
            }
        };
    }

    private JarEntryInfo jarEntryInfo(final String filename,
                                      final long size,
                                      final long compressedSize,
                                      final int method,
                                      final long crc,
                                      final String created,
                                      final String lastModified) {
        return JarEntryInfo.with(
            JarEntryInfoName.with(filename),
            this.optionalLong(size),
            this.optionalLong(compressedSize),
            this.optionalInt(method),
            this.optionalLong(crc),
            this.optionalDateTime(created),
            this.optionalDateTime(lastModified)
        );
    }

    private Optional<LocalDateTime> optionalDateTime(final String dateTime) {
        return Optional.ofNullable(
            dateTime.isEmpty() ?
                null :
                LocalDateTime.parse(dateTime)
        );
    }

    private OptionalInt optionalInt(final int value) {
        return value <= 0 ?
            OptionalInt.empty() :
            OptionalInt.of(value);
    }

    private OptionalLong optionalLong(final long value) {
        return value <= 0 ?
            OptionalLong.empty() :
            OptionalLong.of(value);
    }

    // class............................................................................................................

    @Override
    public Class<JarEntryInfoListTableComponent> type() {
        return JarEntryInfoListTableComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
