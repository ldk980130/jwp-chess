package chess.web.dto;

import chess.domain.GameState;
import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardDto {

    private final int boardId;
    private final GameStateDto state;
    private final List<PieceDto> pieces;

    public BoardDto(int boardId, Board board) {
        this.boardId = boardId;

        GameState state = GameState.from(board);
        this.state = new GameStateDto(state.name().toLowerCase(), state.getTurn());

        Map<Position, Piece> pieces = board.getPieces();
        this.pieces = pieces.keySet().stream()
                .map(position -> PieceDto.from(position, pieces.get(position)))
                .collect(Collectors.toList());;
    }

    public int getBoardId() {
        return boardId;
    }

    public GameStateDto getState() {
        return state;
    }

    public List<PieceDto> getPieces() {
        return pieces;
    }
}
