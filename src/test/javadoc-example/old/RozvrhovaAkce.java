package kolekce;

public class RozvrhovaAkce {

    private String katedra;

    private String predmet;

    /** Toto je javadoc comment u typu */
    private String typ;

    private String semestr;

    public RozvrhovaAkce(String katedra, String predmet, String typ, String semestr) {

        this.katedra = katedra;
        this.predmet = predmet;
        this.typ = typ;
        this.semestr = semestr;
    }

/**
 * Validates a chess move.
 *
 * Use {@link #doMove(int, int, int, int)} to move a piece.
 *
 * @param theFromFile file from which a piece is being moved
 * @param theFromRank rank from which a piece is being moved
 * @param theToFile   file to which a piece is being moved
 * @param theToRank   rank to which a piece is being moved
 * @return            true if the move is valid, otherwise false
 */
boolean isValidMove(int theFromFile, int theFromRank, int theToFile, int theToRank)
{
 
}

/**
 * Validates a chess move.
 *
 * Use {@link #doMove(int, int, int, int)} to move a piece.
 *
 * @param theFromFile file from which a piece is being moved
 * @param theFromRank rank from which a piece is being moved
 * @param theToFile   file to which a piece is being moved
 * @param theToRank   rank to which a piece is being moved
 * @return            true if the move is valid, otherwise false
 */
public boolean isValidMovePublic(int theFromFile, int theFromRank, int theToFile, int theToRank)
{
 
}

/**
 * Validates a chess move.
 *
 * Use {@link #doMove(int, int, int, int)} to move a piece.
 *
 * @param theFromFile file from which a piece is being moved
 * @param theFromRank rank from which a piece is being moved
 * @param theToFile   file to which a piece is being moved
 * @param theToRank   rank to which a piece is being moved
 * @return            true if the move is valid, otherwise false
 */
private static boolean isValidMovePrivateStatic(int theFromFile, int theFromRank, int theToFile, int theToRank)
{
 
}

    public String getKatedra() {

        return katedra;
    }

    public void setKatedra(String katedra) {

        this.katedra = katedra;
    }

    public String getPredmet() {

        return predmet;
    }

    public void setPredmet(String predmet) {

        this.predmet = predmet;
    }

    public String getTyp() {

        return typ;
    }

    public void setTyp(String typ) {

        this.typ = typ;
    }

    public String getSemestr() {

        return semestr;
    }

    public void setSemestr(String semestr) {

        this.semestr = semestr;
    }

    @Override
    public String toString() {

        return "[katedra=" + katedra + ", predmet=" + predmet +", semestr=" + semestr + ", typ=" + typ + "]";
    }
}
