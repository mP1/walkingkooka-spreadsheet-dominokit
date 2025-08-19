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

package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.Value;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.net.HasUrlFragmentTesting;
import walkingkooka.net.UrlFragment;
import walkingkooka.plugin.PluginName;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.MethodAttributes;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.engine.SpreadsheetCellFindQuery;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportHomeNavigationList;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class HistoryTokenTestCase<T extends HistoryToken> implements ClassTesting<T>,
    HashCodeEqualsDefinedTesting2<T>,
    HasUrlFragmentTesting,
    ToStringTesting<T> {

    final static SpreadsheetId ID = SpreadsheetId.with(0x123);

    final static SpreadsheetName NAME = SpreadsheetName.with("SpreadsheetName456");

    HistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testWithNotPublic() {
        this.checkEquals(
            Lists.empty(),
            Arrays.stream(
                    this.type().getMethods()
                ).filter(MethodAttributes.STATIC::is)
                .filter(m -> m.getName().startsWith("with"))
                .collect(Collectors.toList())
        );
    }

    // clearAction......................................................................................................

    final void clearActionAndCheck() {
        final HistoryToken token = this.createHistoryToken();

        assertSame(
            token,
            token.clearAction()
        );
    }

    final void clearActionAndCheck(final HistoryToken token,
                                   final HistoryToken expected) {
        this.checkEquals(
            expected,
            token.clearAction(),
            () -> token + " clearAction"
        );
    }

    // setAnchoredSelection............................................................................................

    @Test
    public final void setAnchoredSelectionWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHistoryToken()
                .setAnchoredSelection(
                    null
                )
        );
    }

    final void setAnchoredSelectionAndCheck(final T token,
                                            final HistoryToken expected) {
        this.setAnchoredSelectionAndCheck(
            token,
            Optional.empty(),
            expected
        );
    }

    final void setAnchoredSelectionAndCheck(final T token,
                                            final AnchoredSpreadsheetSelection anchoredSelection,
                                            final HistoryToken expected) {
        this.setAnchoredSelectionAndCheck(
            token,
            Optional.of(anchoredSelection),
            expected
        );
    }

    final void setAnchoredSelectionAndCheck(final T token,
                                            final Optional<AnchoredSpreadsheetSelection> anchoredSelection,
                                            final HistoryToken expected) {
        this.checkEquals(
            expected,
            token.setAnchoredSelection(
                anchoredSelection
            ),
            () -> token + " setAnchoredSelection " + anchoredSelection
        );
    }

    // close............................................................................................................

    final void closeAndCheck() {
        final HistoryToken token = this.createHistoryToken();

        assertSame(
            token,
            token.close()
        );
    }

    final void closeAndCheck(final HistoryToken expected) {
        this.closeAndCheck(
            this.createHistoryToken(),
            expected
        );
    }

    final void closeAndCheck(final HistoryToken token,
                             final HistoryToken expected) {
        this.checkEquals(
            expected,
            token.close(),
            () -> token + " close"
        );
    }

    // count............................................................................................................

    final void countAndCheck(final HistoryToken historyToken) {
        this.countAndCheck(
            historyToken,
            OptionalInt.empty()
        );
    }

    final void countAndCheck(final HistoryToken historyToken,
                             final int expected) {
        this.countAndCheck(
            historyToken,
            OptionalInt.of(expected)
        );
    }

    final void countAndCheck(final HistoryToken historyToken,
                             final OptionalInt expected) {
        this.checkEquals(
            expected,
            historyToken.count()
        );
    }

    // setCount.........................................................................................................

    @Test
    public final void testSetCountWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHistoryToken()
                .setCount(null)
        );
    }

    final void setCountAndCheck(final HistoryToken historyToken,
                                final OptionalInt count,
                                final HistoryToken expected) {
        final HistoryToken set = historyToken.setCount(count);
        assertNotSame(
            set,
            historyToken
        );

        this.checkEquals(
            expected,
            set
        );
    }

    // setDelete........................................................................................................

    final void deleteAndCheck(final HistoryToken token) {
        assertSame(
            token,
            token.delete(),
            () -> token + " delete"
        );
    }

    final void deleteAndCheck(final HistoryToken token,
                              final HistoryToken expected) {
        this.checkEquals(
            expected,
            token.delete(),
            () -> token + " delete"
        );
    }

    // setFind..........................................................................................................

    final void setFindAndCheck(final HistoryToken token,
                               final SpreadsheetCellFindQuery find,
                               final HistoryToken expected) {
        this.checkEquals(
            expected,
            token.setQuery(find),
            () -> token + " setFind " + find
        );
    }

    // setIdAndName.....................................................................................................

    @Test
    public final void testSetIdAndNameNullIdFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHistoryToken()
                .setIdAndName(
                    null,
                    NAME
                )
        );
    }

    @Test
    public final void testSetIdAndNameNullNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHistoryToken()
                .setIdAndName(
                    ID,
                    null
                )
        );
    }

    final void setIdAndNameAndCheck(final SpreadsheetId id,
                                    final SpreadsheetName name,
                                    final HistoryToken expected) {
        this.setIdAndNameAndCheck(
            this.createHistoryToken(),
            id,
            name,
            expected
        );
    }

    final void setIdAndNameAndCheck(final HistoryToken token,
                                    final SpreadsheetId id,
                                    final SpreadsheetName name,
                                    final HistoryToken expected) {
        this.checkEquals(
            expected,
            token.setIdAndName(
                id,
                name
            ),
            () -> token + " id=" + id + " name=" + name
        );
    }

    // formatter........................................................................................................

    final void formatterAndCheck(final HistoryToken token) {
        this.formatterAndCheck(
            token,
            token
        );
    }

    final void formatterAndCheck(final HistoryToken token,
                                 final HistoryToken expected) {
        this.checkEquals(
            expected,
            token.formatter(),
            () -> token + " formatter"
        );
    }

    // createLabel......................................................................................................

    final void createLabelAndCheck(final HistoryToken token) {
        assertSame(
            token,
            token.createLabel()
        );
    }

    final void createLabelAndCheck(final HistoryToken token,
                                   final HistoryToken expected) {
        this.checkEquals(
            expected,
            token.createLabel(),
            token::toString
        );
    }

    // labelMappingReference...........................................................................................

    final void labelMappingReferenceAndCheck(final HistoryToken token) {
        this.labelMappingReferenceAndCheck(
            token,
            Optional.empty()
        );
    }

    final void labelMappingReferenceAndCheck(final HistoryToken token,
                                             final SpreadsheetExpressionReference expected) {
        this.labelMappingReferenceAndCheck(
            token,
            Optional.of(expected)
        );
    }

    final void labelMappingReferenceAndCheck(final HistoryToken token,
                                             final Optional<SpreadsheetExpressionReference> expected) {
        this.checkEquals(
            expected,
            token.labelMappingReference(),
            token::toString
        );
    }

    // setLabelMappingReference.........................................................................................

    @Test
    public final void testSetLabelMappingReferenceWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHistoryToken().setLabelMappingReference(null)
        );
    }

    final void setLabelMappingReferenceAndCheck() {
        final HistoryToken historyToken = this.createHistoryToken();

        assertSame(
            historyToken,
            historyToken.setLabelMappingReference(
                Optional.of(SpreadsheetSelection.A1)
            )
        );

        assertSame(
            historyToken,
            historyToken.setLabelMappingReference(
                Optional.empty()
            )
        );
    }

    final void setLabelMappingReferenceAndCheck(final HistoryToken historyToken,
                                                final HistoryToken expected) {
        this.setLabelMappingReferenceAndCheck(
            historyToken,
            Optional.empty(),
            expected
        );
    }

    final void setLabelMappingReferenceAndCheck(final HistoryToken historyToken,
                                                final SpreadsheetExpressionReference labelMappingReference) {
        this.setLabelMappingReferenceAndCheck(
            historyToken,
            Optional.of(labelMappingReference),
            historyToken
        );
    }

    final void setLabelMappingReferenceAndCheck(final HistoryToken historyToken,
                                                final SpreadsheetExpressionReference labelMappingReference,
                                                final HistoryToken expected) {
        this.setLabelMappingReferenceAndCheck(
            historyToken,
            Optional.of(labelMappingReference),
            expected
        );
    }

    final void setLabelMappingReferenceAndCheck(final HistoryToken historyToken,
                                                final Optional<SpreadsheetExpressionReference> labelMappingReference,
                                                final HistoryToken expected) {
        if (historyToken.equals(expected)) {
            assertSame(
                expected,
                historyToken.setLabelMappingReference(labelMappingReference),
                () -> historyToken + " setLabelMappingReference " + labelMappingReference + " returned different equal instance"
            );
        } else {
            this.checkEquals(
                expected,
                historyToken.setLabelMappingReference(labelMappingReference),
                historyToken::toString
            );
        }
    }

    // setLabel.........................................................................................................

    final static SpreadsheetLabelName LABEL = SpreadsheetSelection.labelName("Label123");

    final void labelNameAndCheck(final HistoryToken token) {
        this.labelNameAndCheck(
            token,
            Optional.empty()
        );
    }

    final void labelNameAndCheck(final HistoryToken token,
                                 final SpreadsheetLabelName labelName) {
        this.labelNameAndCheck(
            token,
            Optional.of(labelName)
        );
    }

    final void labelNameAndCheck(final HistoryToken token,
                                 final Optional<SpreadsheetLabelName> labelName) {
        this.checkEquals(
            labelName,
            token.labelName(),
            token::toString
        );
    }

    @Test
    public final void testSetLabelNameWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHistoryToken().setLabelName(null)
        );
    }

    final void setLabelNameAndCheck(final HistoryToken historyToken,
                                    final SpreadsheetLabelName labelName,
                                    final HistoryToken expected) {
        this.setLabelNameAndCheck(
            historyToken,
            Optional.of(labelName),
            expected
        );
    }

    final void setLabelNameAndCheck(final HistoryToken historyToken,
                                    final Optional<SpreadsheetLabelName> labelName,
                                    final HistoryToken expected) {
        this.checkEquals(
            expected,
            historyToken.setLabelName(labelName),
            historyToken::toString
        );
    }

    // labels...........................................................................................................

    final void labelsAndCheck(final HistoryToken token,
                              final HistoryTokenOffsetAndCount offsetAndCount) {
        assertSame(
            token,
            token.labels(offsetAndCount)
        );
    }

    final void labelsAndCheck(final HistoryToken token,
                              final HistoryTokenOffsetAndCount offsetAndCount,
                              final HistoryToken expected) {
        this.checkEquals(
            expected,
            token.labels(offsetAndCount),
            () -> token + " labels " + offsetAndCount
        );
    }

    // setList..........................................................................................................

    @Test
    public void testSetListWithNullOffsetAndCountFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject().setList(null)
        );
    }

    final void setListAndCheck(final HistoryToken token) {
        assertSame(
            token,
            token.setList(HistoryTokenOffsetAndCount.EMPTY)
        );
    }

    final void setListAndCheck(final HistoryToken token,
                               final HistoryTokenOffsetAndCount offsetAndCount,
                               final HistoryToken expected) {
        this.checkEquals(
            expected,
            token.setList(offsetAndCount),
            () -> token + " setList " + offsetAndCount
        );
    }

    // setMetadataPropertyName..........................................................................................

    @Test
    public final void testSetMetadataPropertyNameWithNullFails() {
        final T token = this.createHistoryToken();
        assertThrows(
            NullPointerException.class,
            () -> token.setMetadataPropertyName(null)
        );
    }

    final void setMetadataPropertyNameAndCheck(final SpreadsheetMetadataPropertyName<?> propertyName) {
        this.setMetadataPropertyNameAndCheck(
            this.createHistoryToken(),
            propertyName
        );
    }

    final void setMetadataPropertyNameAndCheck(final HistoryToken token,
                                               final SpreadsheetMetadataPropertyName<?> propertyName) {
        assertSame(
            token,
            token.setMetadataPropertyName(propertyName)
        );
    }

    final void setMetadataPropertyNameAndCheck(final SpreadsheetMetadataPropertyName<?> propertyName,
                                               final HistoryToken expected) {
        this.setMetadataPropertyNameAndCheck(
            this.createHistoryToken(),
            propertyName,
            expected
        );
    }

    final void setMetadataPropertyNameAndCheck(final HistoryToken token,
                                               final SpreadsheetMetadataPropertyName<?> propertyName,
                                               final HistoryToken expected) {
        this.checkEquals(
            expected,
            token.setMetadataPropertyName(propertyName),
            () -> token + " setMetadataPropertyName " + propertyName
        );
    }

    // setName.........................................................................................................

    @Test
    public final void testSetNameNullNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHistoryToken()
                .setName(
                    null
                )
        );
    }

    final void setNameAndCheck(final SpreadsheetName name,
                               final HistoryToken expected) {
        this.setNameAndCheck(
            this.createHistoryToken(),
            name,
            expected
        );
    }

    final void setNameAndCheck(final HistoryToken token,
                               final SpreadsheetName name,
                               final HistoryToken expected) {
        this.checkEquals(
            expected,
            token.setName(
                name
            ),
            () -> token + " name=" + name
        );
    }

    // navigation.......................................................................................................

    final void navigationAndCheck(final HistoryToken token) {
        this.navigationAndCheck(
            token,
            Optional.empty()
        );
    }

    final void navigationAndCheck(final HistoryToken token,
                                  final SpreadsheetViewportHomeNavigationList expected) {
        this.navigationAndCheck(
            token,
            Optional.of(expected)
        );
    }

    final void navigationAndCheck(final HistoryToken token,
                                  final Optional<SpreadsheetViewportHomeNavigationList> expected) {
        this.checkEquals(
            expected,
            token.navigation()
        );
    }

    // offset...........................................................................................................

    final void offsetAndCheck(final HistoryToken historyToken) {
        this.offsetAndCheck(
            historyToken,
            OptionalInt.empty()
        );
    }

    final void offsetAndCheck(final HistoryToken historyToken,
                              final int expected) {
        this.offsetAndCheck(
            historyToken,
            OptionalInt.of(expected)
        );
    }

    final void offsetAndCheck(final HistoryToken historyToken,
                              final OptionalInt expected) {
        this.checkEquals(
            expected,
            historyToken.offset()
        );
    }

    // setOffset.........................................................................................................

    @Test
    public final void testSetOffsetWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHistoryToken()
                .setOffset(null)
        );
    }

    final void setOffsetAndCheck(final HistoryToken historyToken,
                                 final OptionalInt offset,
                                 final HistoryToken expected) {
        final HistoryToken set = historyToken.setOffset(offset);
        assertNotSame(
            set,
            historyToken
        );

        this.checkEquals(
            expected,
            set
        );
    }

    // patternKind......................................................................................................

    final void patternKindAndCheck(final HistoryToken token) {
        this.patternKindAndCheck(
            token,
            Optional.empty()
        );
    }

    final void patternKindAndCheck(final HistoryToken token,
                                   final SpreadsheetPatternKind expected) {
        this.patternKindAndCheck(
            token,
            Optional.of(expected)
        );
    }

    final void patternKindAndCheck(final HistoryToken token,
                                   final Optional<SpreadsheetPatternKind> expected) {
        this.checkEquals(
            expected,
            token.patternKind(),
            token::toString
        );
    }

    // setPatternKind...................................................................................................

    @Test
    public final void testSetPatternKindNullFails() {
        final T token = this.createHistoryToken();
        assertThrows(
            NullPointerException.class,
            () -> token.setPatternKind(null)
        );
    }

    final void setPatternKindAndCheck(final HistoryToken token,
                                      final HistoryToken expected) {
        this.setPatternKindAndCheck(
            token,
            Optional.empty(),
            expected
        );
    }

    final void setPatternKindAndCheck(final SpreadsheetPatternKind patternKind) {
        this.setPatternKindAndCheck(
            this.createHistoryToken(),
            patternKind
        );
    }

    final void setPatternKindAndCheck(final HistoryToken token,
                                      final SpreadsheetPatternKind patternKind) {
        assertSame(
            token,
            token.setPatternKind(
                Optional.of(patternKind)
            ),
            () -> token + " setPatternKind " + patternKind
        );
    }

    final void setPatternKindAndCheck(final HistoryToken token,
                                      final SpreadsheetPatternKind patternKind,
                                      final HistoryToken expected) {
        this.setPatternKindAndCheck(
            token,
            Optional.of(patternKind),
            expected
        );
    }

    final void setPatternKindAndCheck(final HistoryToken token,
                                      final Optional<SpreadsheetPatternKind> patternKind,
                                      final HistoryToken expected) {
        this.checkEquals(
            expected,
            token.setPatternKind(patternKind),
            () -> token + " setPatternKind " + patternKind
        );
    }

    // pluginName.......................................................................................................

    final void pluginNameAndCheck(final HistoryToken token) {
        this.pluginNameAndCheck(
            token,
            Optional.empty()
        );
    }

    final void pluginNameAndCheck(final HistoryToken token,
                                  final PluginName expected) {
        this.pluginNameAndCheck(
            token,
            Optional.of(expected)
        );
    }

    final void pluginNameAndCheck(final HistoryToken token,
                                  final Optional<PluginName> expected) {
        this.checkEquals(
            expected,
            token.pluginName(),
            token::toString
        );
    }

    // reload...........................................................................................................

    final void reloadAndCheck(final HistoryToken token,
                              final HistoryToken expected) {
        this.checkEquals(
            expected,
            token.reload(),
            token::toString
        );
    }

    // value............................................................................................................

    @Test
    public final void testClassXXXSaveHistoryTokenImplementsValue() {
        final Class<T> type = this.type();
        if (type.getSimpleName().contains("Save")) {
            this.checkEquals(
                true,
                Value.class.isAssignableFrom(type),
                () -> type.getName() + " should implement " + Value.class.getName()
            );
        }
    }

    @Test
    public final void testClassImplementValueShouldIncludeSaveInClassName() {
        final Class<T> type = this.type();
        if (type.isAssignableFrom(Value.class)) {
            this.checkEquals(
                true,
                type.getSimpleName().contains("Save"),
                () -> "Missing save in class name " + type.getName()
            );
        }
    }

    // setSave..........................................................................................................

    @Test
    public final void testSaveValueEmptyWhenNotImplementingValue() {
        final T token = this.createHistoryToken();

        if (false == (token instanceof Value)) {
            this.checkEquals(
                Optional.empty(),
                token.saveValue()
            );
        }
    }

    @Test
    public final void testSetSaveStringValueNullStringFails() {
        final T token = this.createHistoryToken();
        assertThrows(
            NullPointerException.class,
            () -> token.setSaveStringValue(null)
        );
    }

    final void setSaveStringValueAndCheck(final String save) {
        this.setSaveStringValueAndCheck(
            this.createHistoryToken(),
            save
        );
    }

    final void setSaveStringValueAndCheck(final HistoryToken token,
                                          final String save) {
        this.setSaveStringValueAndCheck(
            token,
            save,
            token
        );
    }

    final void setSaveStringValueAndCheck(final HistoryToken token,
                                          final String save,
                                          final HistoryToken expected) {
        if (token.equals(expected)) {
            assertSame(
                expected,
                token.setSaveStringValue(save),
                () -> token + " setSaveStringValue " + save + " should have returned same not different instance"
            );
        } else {
            this.checkEquals(
                expected,
                token.setSaveStringValue(save),
                () -> token + " setSaveStringValue " + save
            );
        }
    }

    @Test
    public final void testSetSaveValueNullOptionalFails() {
        final T token = this.createHistoryToken();
        assertThrows(
            NullPointerException.class,
            () -> token.setSaveValue(null)
        );
    }

    final void setSaveValueFails(final Optional<?> value) {
        final T token = this.createHistoryToken();
        assertThrows(
            RuntimeException.class, // InvalidArgumentException | ClassCastException
            () -> token.setSaveValue(value)
        );
    }

    final void setSaveValueAndCheck(final Optional<?> save) {
        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            save
        );
    }

    final void setSaveValueAndCheck(final HistoryToken token,
                                    final Optional<?> save) {
        this.setSaveValueAndCheck(
            token,
            save,
            token
        );
    }

    final void setSaveValueAndCheck(final HistoryToken token,
                                    final Optional<?> save,
                                    final HistoryToken expected) {
        if (token.equals(expected)) {
            assertSame(
                token,
                token.setSaveValue(save),
                () -> token + " setSaveStringValue " + save + " should have returned same not different instance"
            );
        } else {
            this.checkEquals(
                expected,
                token.setSaveValue(save),
                () -> token + " setSaveStringValue " + save
            );

            assertSame(
                expected,
                expected.setSaveValue(save),
                () -> token + " setSaveStringValue " + save + " should have returned same not different instance"
            );
        }
    }

    // SELECTION........................................................................................................

    @Test
    public final void testSetSelectionWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHistoryToken()
                .setSelection(null)
        );
    }

    @Test
    public final void testSetSelectionWithSame() {
        final HistoryToken token = this.createHistoryToken();

        assertSame(
            token,
            token.setSelection(token.selection()),
            token::toString
        );
    }

    final void setSelectionAndCheck(final HistoryToken token) {
        this.setSelectionAndCheck(
            token,
            token.selection()
        );
    }

    final void setSelectionAndCheck(final HistoryToken token,
                                    final SpreadsheetSelection selection) {
        this.setSelectionAndCheck(
            token,
            selection,
            token
        );
    }

    final void setSelectionAndCheck(final HistoryToken token,
                                    final Optional<SpreadsheetSelection> selection) {
        assertSame(
            token,
            token.setSelection(selection),
            token::toString
        );
    }

    final void setSelectionAndCheck(final HistoryToken token,
                                    final HistoryToken expected) {
        this.setSelectionAndCheck(
            token,
            Optional.empty(),
            expected
        );
    }

    final void setSelectionAndCheck(final HistoryToken token,
                                    final SpreadsheetSelection selection,
                                    final HistoryToken expected) {
        this.setSelectionAndCheck(
            token,
            Optional.of(selection),
            expected
        );
    }

    final void setSelectionAndCheck(final HistoryToken token,
                                    final Optional<SpreadsheetSelection> selection,
                                    final HistoryToken expected) {
        this.checkEquals(
            expected,
            token.setSelection(selection),
            () -> token + " setSelection " + selection
        );
    }

    // factory..........................................................................................................

    abstract T createHistoryToken();

    static String urlEncode(final Object object) {
        return URLEncoder.encode(object.toString());
    }

    // equals...........................................................................................................

    @Test
    public final void testEqualsDifferentHistoryTokenType() {
        this.checkNotEquals(
            new FakeHistoryToken() {
                @Override
                public UrlFragment urlFragment() {
                    return UrlFragment.SLASH;
                }
            }
        );
    }

    @Test
    public final void testEqualsDifferentTypeSameUrlFragment() {
        final T token = this.createHistoryToken();

        this.checkNotEquals(
            token,
            new FakeHistoryToken() {
                @Override
                public UrlFragment urlFragment() {
                    return token.urlFragment();
                }
            }
        );
    }

    @Override
    public final T createObject() {
        return this.createHistoryToken();
    }

    // UrlFragment......................................................................................................

    final void urlFragmentAndCheck(final String expected) {
        this.urlFragmentAndCheck(
            this.createHistoryToken(),
            expected
        );
    }

    // Class............................................................................................................

    @Test
    public final void testAncestorClassesArePublic() {
        Set<String> nonPublic = SortedSets.tree();

        Class<?> type = this.type();
        while (type != HistoryToken.class) {
            if (false == JavaVisibility.PUBLIC.equals(JavaVisibility.of(type))) {
                nonPublic.add(type.getSimpleName());
            }

            type = type.getSuperclass();
        }

        this.checkEquals(
            Sets.empty(),
            nonPublic
        );
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
