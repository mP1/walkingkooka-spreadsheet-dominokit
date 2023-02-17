package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Objects;

public final class SpreadsheetCellFormulaSaveHistoryHashToken extends SpreadsheetCellFormulaHistoryHashToken {

    static SpreadsheetCellFormulaSaveHistoryHashToken with(final SpreadsheetViewportSelection viewportSelection,
                                                           final SpreadsheetFormula formula) {
        return new SpreadsheetCellFormulaSaveHistoryHashToken(
                viewportSelection,
                formula
        );
    }

    private SpreadsheetCellFormulaSaveHistoryHashToken(final SpreadsheetViewportSelection viewportSelection,
                                                       final SpreadsheetFormula formula) {
        super(viewportSelection);

        this.formula = Objects.requireNonNull(formula, "formula");
    }

    public SpreadsheetFormula formula() {
        return this.formula;
    }

    private final SpreadsheetFormula formula;

    @Override
    UrlFragment formulaUrlFragment() {
        return SAVE.append(this.formula.urlFragment());
    }
}
