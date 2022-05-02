package chess.domain.chessgame;

import chess.exception.UserInputException;

public class ChessGameInfo {

	private static final int NAME_MIN_SIZE = 1;
	private static final int NAME_MAX_SIZE = 16;

	private int id;
	private final String name;
	private final String password;

	public ChessGameInfo(int id, String name, String password) {
		validateNameSize(name);
		validatePassword(password);
		this.id = id;
		this.name = name;
		this.password = password;
	}

	public ChessGameInfo(String name, String password) {
		validateNameSize(name);
		validatePassword(password);
		this.name = name;
		this.password = password;
	}

	private void validateNameSize(String name) {
		if (name.length() < NAME_MIN_SIZE || name.length() > NAME_MAX_SIZE) {
			throw new UserInputException("방 이름은 1자 이상, 16자 이하입니다.");
		}
	}

	private void validatePassword(String password) {
		if (password.isBlank()) {
			throw new UserInputException("비밀번호를 입력하세요");
		}
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public boolean isSamePassword(String password) {
		return this.password.equals(password);
	}
}
