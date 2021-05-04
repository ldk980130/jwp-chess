package chess.domain.team;

import chess.domain.game.Position;
import chess.domain.piece.Piece;

import java.util.Map;

public class WhiteTeam extends Team {
    public static final String DEFAULT_NAME = "White";
    public static final int WHITE_PAWN_COLUMN = 1;
    private static final int WHITE_PIECE_COLUMN = 0;
    private static final int WHITE_PAWN_DIRECTION = 1;

    public WhiteTeam() {
        super(DEFAULT_NAME);
        initializePawn(WHITE_PAWN_COLUMN, WHITE_PAWN_DIRECTION);
        initializePiece(WHITE_PIECE_COLUMN);
    }

    public WhiteTeam(boolean isCurrentTurn) {
        super(DEFAULT_NAME, isCurrentTurn);
    }

    public WhiteTeam(boolean isCurrentTurn, Map<Position, Piece> pieces) {
        super(DEFAULT_NAME, isCurrentTurn, pieces);
    }
}
