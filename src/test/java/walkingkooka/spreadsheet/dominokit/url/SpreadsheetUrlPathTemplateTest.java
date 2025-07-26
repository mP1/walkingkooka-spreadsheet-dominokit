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

package walkingkooka.spreadsheet.dominokit.url;

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.UrlPath;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.engine.SpreadsheetEngineEvaluation;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReferenceOrRange;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.server.locale.LocaleTag;
import walkingkooka.template.TemplateContext;
import walkingkooka.template.TemplateContexts;
import walkingkooka.template.TemplateTesting2;
import walkingkooka.template.TemplateValueName;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetUrlPathTemplateTest implements TemplateTesting2<SpreadsheetUrlPathTemplate>,
    HashCodeEqualsDefinedTesting2<SpreadsheetUrlPathTemplate> {

    private final static SpreadsheetId ID = SpreadsheetId.with(0x123);
    private final static SpreadsheetName NAME = SpreadsheetName.with("SpreadsheetName456");
    private final LocaleTag LOCALE_TAG = LocaleTag.with(
        Locale.forLanguageTag("en-AU")
    );
    private final static SpreadsheetEngineEvaluation SPREADSHEET_ENGINE_EVALUATION = SpreadsheetEngineEvaluation.SKIP_EVALUATE;
    private final static SpreadsheetExpressionReference CELL = SpreadsheetSelection.A1;
    private final static SpreadsheetLabelName LABEL = SpreadsheetSelection.labelName("Label123");
    private final static SpreadsheetCellRangeReference CELL_RANGE = SpreadsheetSelection.parseCellRange("A2:A3");

    private final static SpreadsheetColumnReference COLUMN  = SpreadsheetSelection.parseColumn("B");
    private final static SpreadsheetColumnRangeReference COLUMN_RANGE = SpreadsheetSelection.parseColumnRange("C:D");

    // get..........................................................................................................

    @Test
    public void testGetWithNullPathFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createTemplate()
                .get(
                    null,
                    TemplateValueName.with("Hello")
                )
        );
    }

    @Test
    public void testGetWithNullNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createTemplate()
                .get(
                    UrlPath.EMPTY,
                    null
                )
        );
    }

    @Test
    public void testGetSpreadsheetIdExisting() {
        this.getAndCheck(
            "/api/spreadsheet/${SpreadsheetId}/name/${SpreadsheetName}/cell",
            "/api/spreadsheet/123/name/SpreadsheetName456/cell",
            SpreadsheetUrlPathTemplate.SPREADSHEET_ID,
           Optional.of(ID)
        );
    }

    @Test
    public void testGetSpreadsheetNameExisting() {
        this.getAndCheck(
            "/api/spreadsheet/${SpreadsheetId}/name/${SpreadsheetName}/cell",
            "/api/spreadsheet/123/name/SpreadsheetName456/cell",
            SpreadsheetUrlPathTemplate.SPREADSHEET_NAME,
            Optional.of(NAME)
        );
    }

    @Test
    public void testGetLocaleTagExisting() {
        this.getAndCheck(
            "/api/dateTimeSymbols/${LocaleTag}/*",
            "/api/dateTimeSymbols/en-AU/*",
            SpreadsheetUrlPathTemplate.LOCALE_TAG,
            Optional.of(LOCALE_TAG)
        );
    }

    @Test
    public void testGetMissing() {
        this.getAndCheck(
            "/api/spreadsheet/${SpreadsheetId}/name/${SpreadsheetName}/cell",
            "/api/spreadsheet/123/name/SpreadsheetName456/cell",
            TemplateValueName.with("Unknown"),
            Optional.empty()
        );
    }

    private void getAndCheck(final String template,
                             final String path,
                             final TemplateValueName name,
                             final Optional<Object> expected) {
        this.getAndCheck(
            SpreadsheetUrlPathTemplate.parse(template),
            UrlPath.parse(path),
            name,
            expected
        );
    }

    private void getAndCheck(final SpreadsheetUrlPathTemplate template,
                             final UrlPath path,
                             final TemplateValueName name,
                             final Optional<Object> expected) {
        this.checkEquals(
            expected,
            template.get(
                path,
                name
            )
        );
    }

    // localeTag..................................................................................................

    @Test
    public void testLocaleTag() {
        this.checkEquals(
            LOCALE_TAG,
            SpreadsheetUrlPathTemplate.parse("/api/locale/${LocaleTag}/")
                .localeTag(
                    UrlPath.parse("/api/locale/en-AU/"
                    )
                )
        );
    }

    // spreadsheetColumnReferenceOrRange................................................................................

    @Test
    public void testSpreadsheetColumnReferenceOrRangeWithCell() {
        this.spreadsheetColumnReferenceOrRangeAndCheck(
            "/api/spreadsheet/${SpreadsheetId}/column/${SpreadsheetColumnReferenceOrRange}",
            "/api/spreadsheet/123/column/B",
            COLUMN
        );
    }

    @Test
    public void testSpreadsheetColumnReferenceOrRangeWithCellRange() {
        this.spreadsheetColumnReferenceOrRangeAndCheck(
            "/api/spreadsheet/${SpreadsheetId}/column/${SpreadsheetColumnReferenceOrRange}",
            "/api/spreadsheet/123/column/C:D",
            COLUMN_RANGE
        );
    }

    private void spreadsheetColumnReferenceOrRangeAndCheck(final String template,
                                                           final String path,
                                                           final SpreadsheetColumnReferenceOrRange expected) {
        this.spreadsheetColumnReferenceOrRangeAndCheck(
            SpreadsheetUrlPathTemplate.parse(template),
            UrlPath.parse(path),
            expected
        );
    }

    private void spreadsheetColumnReferenceOrRangeAndCheck(final SpreadsheetUrlPathTemplate template,
                                                           final UrlPath path,
                                                           final SpreadsheetColumnReferenceOrRange expected) {
        this.checkEquals(
            expected,
            template.spreadsheetColumnReferenceOrRange(path)
        );
    }

    // spreadsheetEngineEvaluation......................................................................................

    @Test
    public void testSpreadsheetEngineEvaluation() {
        this.spreadsheetEngineEvaluationAndCheck(
            "/api/spreadsheet/${SpreadsheetId}/cell/A1/${SpreadsheetEngineEvaluation}",
            "/api/spreadsheet/123/cell/A1/skip-evaluate",
            SpreadsheetEngineEvaluation.SKIP_EVALUATE
        );
    }

    private void spreadsheetEngineEvaluationAndCheck(final String template,
                                                     final String path,
                                                     final SpreadsheetEngineEvaluation expected) {
        this.spreadsheetEngineEvaluationAndCheck(
            SpreadsheetUrlPathTemplate.parse(template),
            UrlPath.parse(path),
            expected
        );
    }

    private void spreadsheetEngineEvaluationAndCheck(final SpreadsheetUrlPathTemplate template,
                                                     final UrlPath path,
                                                     final SpreadsheetEngineEvaluation expected) {
        this.checkEquals(
            expected,
            template.spreadsheetEngineEvaluation(path)
        );
    }

    // spreadsheetExpressionReference...................................................................................

    @Test
    public void testSpreadsheetExpressionReferenceWithCell() {
        this.spreadsheetExpressionReferenceAndCheck(
            "/api/spreadsheet/${SpreadsheetId}/cell/${SpreadsheetExpressionReference}/${SpreadsheetExpressionReference}",
            "/api/spreadsheet/123/cell/A1/skip-evaluate",
            CELL
        );
    }

    @Test
    public void testSpreadsheetExpressionReferenceWithCellRange() {
        this.spreadsheetExpressionReferenceAndCheck(
            "/api/spreadsheet/${SpreadsheetId}/cell/${SpreadsheetExpressionReference}/${SpreadsheetExpressionReference}",
            "/api/spreadsheet/123/cell/A2:A3/skip-evaluate",
            CELL_RANGE
        );
    }

    @Test
    public void testSpreadsheetExpressionReferenceWithLabel() {
        this.spreadsheetExpressionReferenceAndCheck(
            "/api/spreadsheet/${SpreadsheetId}/cell/${SpreadsheetExpressionReference}/${SpreadsheetExpressionReference}",
            "/api/spreadsheet/123/cell/Label123/skip-evaluate",
            LABEL
        );
    }

    private void spreadsheetExpressionReferenceAndCheck(final String template,
                                                        final String path,
                                                        final SpreadsheetExpressionReference expected) {
        this.spreadsheetExpressionReferenceAndCheck(
            SpreadsheetUrlPathTemplate.parse(template),
            UrlPath.parse(path),
            expected
        );
    }

    private void spreadsheetExpressionReferenceAndCheck(final SpreadsheetUrlPathTemplate template,
                                                        final UrlPath path,
                                                        final SpreadsheetExpressionReference expected) {
        this.checkEquals(
            expected,
            template.spreadsheetExpressionReference(path)
        );
    }

    // spreadsheetId....................................................................................................

    @Test
    public void testSpreadsheetId() {
        this.spreadsheetIdAndCheck(
            "/api/spreadsheet/${SpreadsheetId}/",
            "/api/spreadsheet/123/different",
            Optional.of(ID)
        );
    }

    @Test
    public void testSpreadsheetIdMissing() {
        this.spreadsheetIdAndCheck(
            "/api/spreadsheet/${SpreadsheetId}/",
            "/api/different",
            Optional.empty()
        );
    }

    @Test
    public void testSpreadsheetIdDifferentPath() {
        this.spreadsheetIdAndCheck(
            "/api/spreadsheet/${SpreadsheetId}/different",
            "/api/spreadsheet/123",
            Optional.empty()
        );
    }

    private void spreadsheetIdAndCheck(final String template,
                                       final String path,
                                       final Optional<SpreadsheetId> expected) {
        this.spreadsheetIdAndCheck(
            SpreadsheetUrlPathTemplate.parse(template),
            UrlPath.parse(path),
            expected
        );
    }

    private void spreadsheetIdAndCheck(final SpreadsheetUrlPathTemplate template,
                                       final UrlPath path,
                                       final Optional<SpreadsheetId> expected) {
        this.checkEquals(
            expected,
            template.spreadsheetId(path)
        );
    }

    // spreadsheetName..................................................................................................

    @Test
    public void testSpreadsheetName() {
        this.checkEquals(
            NAME,
            SpreadsheetUrlPathTemplate.parse("/api/spreadsheetName/${SpreadsheetName}/")
                .spreadsheetName(
                    UrlPath.parse("/api/spreadsheetName/SpreadsheetName456/"
                    )
                )
        );
    }

    // extract..........................................................................................................

    @Test
    public void testExtractWithNullMapFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createTemplate().extract(null)
        );
    }

    @Test
    public void testExtract() {
        this.checkEquals(
            Maps.of(
                SpreadsheetUrlPathTemplate.SPREADSHEET_ID,
                ID,
                SpreadsheetUrlPathTemplate.SPREADSHEET_NAME,
                NAME,
                SpreadsheetUrlPathTemplate.SPREADSHEET_EXPRESSION_REFERENCE,
                CELL,
                SpreadsheetUrlPathTemplate.SPREADSHEET_ENGINE_EVALUATION,
                SPREADSHEET_ENGINE_EVALUATION,
                SpreadsheetUrlPathTemplate.SPREADSHEET_COLUMN_REFERENCE_OR_RANGE,
                COLUMN
            ),
            this.createTemplate()
                .extract(UrlPath.parse("/api/spreadsheet/123/name/SpreadsheetName456/cell/A1/skip-evaluate/column/B"))
        );
    }

    // render...........................................................................................................

    @Test
    public void testRender() {
        this.renderAndCheck(
            this.createTemplate(),
            TemplateContexts.renderOnly(
                (n) -> {
                    if (n.equals(SpreadsheetUrlPathTemplate.SPREADSHEET_ID)) {
                        return ID.toString();
                    }
                    if (n.equals(SpreadsheetUrlPathTemplate.SPREADSHEET_NAME)) {
                        return NAME.toString();
                    }
                    if (n.equals(SpreadsheetUrlPathTemplate.SPREADSHEET_EXPRESSION_REFERENCE)) {
                        return CELL.toString();
                    }
                    if (n.equals(SpreadsheetUrlPathTemplate.SPREADSHEET_ENGINE_EVALUATION)) {
                        return SPREADSHEET_ENGINE_EVALUATION.toLinkRelation()
                            .toUrlPathName()
                            .get()
                            .value();
                    }
                    if (n.equals(SpreadsheetUrlPathTemplate.SPREADSHEET_COLUMN_REFERENCE_OR_RANGE)) {
                        return COLUMN.toStringMaybeStar();
                    }

                    throw new IllegalArgumentException("Unknown value=" + n);
                }
            ),
            "/api/spreadsheet/123/name/SpreadsheetName456/cell/A1/skip-evaluate/column/B"
        );
    }

    // renderPath.......................................................................................................

    @Test
    public void testRenderPath() {
        this.checkEquals(
            UrlPath.parse("/api/spreadsheet/123/name/SpreadsheetName456/cell/A1/localeTag/en-AU/column/B"),
            SpreadsheetUrlPathTemplate.parse("/api/spreadsheet/${SpreadsheetId}/name/${SpreadsheetName}/cell/${SpreadsheetExpressionReference}/localeTag/${LocaleTag}/column/${SpreadsheetColumnReferenceOrRange}")
                .render(
                    Maps.of(
                        SpreadsheetUrlPathTemplate.SPREADSHEET_ID,
                        ID,
                        SpreadsheetUrlPathTemplate.SPREADSHEET_NAME,
                        NAME,
                        SpreadsheetUrlPathTemplate.LOCALE_TAG,
                        LOCALE_TAG,
                        SpreadsheetUrlPathTemplate.SPREADSHEET_EXPRESSION_REFERENCE,
                        CELL,
                        SpreadsheetUrlPathTemplate.SPREADSHEET_COLUMN_REFERENCE_OR_RANGE,
                        COLUMN
                    )
                )
        );
    }

    @Override
    public SpreadsheetUrlPathTemplate createTemplate() {
        return SpreadsheetUrlPathTemplate.parse("/api/spreadsheet/${SpreadsheetId}/name/${SpreadsheetName}/cell/${SpreadsheetExpressionReference}/${SpreadsheetEngineEvaluation}/column/${SpreadsheetColumnReferenceOrRange}");
    }

    @Override
    public TemplateContext createContext() {
        return TemplateContexts.fake();
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferentTemplate(){
        this.checkNotEquals(
            this.createContext(),
            SpreadsheetUrlPathTemplate.parse("/different")
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            this.createTemplate(),
            "SpreadsheetUrlPathTemplate\n" +
                "  UrlPathTemplate\n" +
                "    TemplateCollection\n" +
                "      StringTemplate\n" +
                "        \"/\"\n" +
                "      StringTemplate\n" +
                "        \"api\"\n" +
                "      StringTemplate\n" +
                "        \"/\"\n" +
                "      StringTemplate\n" +
                "        \"spreadsheet\"\n" +
                "      StringTemplate\n" +
                "        \"/\"\n" +
                "      TemplateValueNameTemplate\n" +
                "        ${SpreadsheetId}\n" +
                "      StringTemplate\n" +
                "        \"/\"\n" +
                "      StringTemplate\n" +
                "        \"name\"\n" +
                "      StringTemplate\n" +
                "        \"/\"\n" +
                "      TemplateValueNameTemplate\n" +
                "        ${SpreadsheetName}\n" +
                "      StringTemplate\n" +
                "        \"/\"\n" +
                "      StringTemplate\n" +
                "        \"cell\"\n" +
                "      StringTemplate\n" +
                "        \"/\"\n" +
                "      TemplateValueNameTemplate\n" +
                "        ${SpreadsheetExpressionReference}\n" +
                "      StringTemplate\n" +
                "        \"/\"\n" +
                "      TemplateValueNameTemplate\n" +
                "        ${SpreadsheetEngineEvaluation}\n" +
                "      StringTemplate\n" +
                "        \"/\"\n" +
                "      StringTemplate\n" +
                "        \"column\"\n" +
                "      StringTemplate\n" +
                "        \"/\"\n" +
                "      TemplateValueNameTemplate\n" +
                "        ${SpreadsheetColumnReferenceOrRange}\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetUrlPathTemplate> type() {
        return SpreadsheetUrlPathTemplate.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
