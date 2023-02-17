package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Objects;

public final class SpreadsheetCellPatternSelectHistoryHashToken extends SpreadsheetCellPatternHistoryHashToken {

    static SpreadsheetCellPatternSelectHistoryHashToken with(final SpreadsheetViewportSelection viewportSelection,
                                                             final SpreadsheetPatternKind patternKind) {
        return new SpreadsheetCellPatternSelectHistoryHashToken(
                viewportSelection,
                patternKind
        );
    }

    private SpreadsheetCellPatternSelectHistoryHashToken(final SpreadsheetViewportSelection viewportSelection,
                                                         final SpreadsheetPatternKind patternKind) {
        super(viewportSelection);

        this.patternKind = Objects.requireNonNull(patternKind, "patternKind");
    }

    public SpreadsheetPatternKind patternKind() {
        return this.patternKind;
    }

    private final SpreadsheetPatternKind patternKind;

    @Override
    UrlFragment patternUrlFragment() {
        return this.patternKind()
                .urlFragment()
                .append(SELECT);
    }
}
