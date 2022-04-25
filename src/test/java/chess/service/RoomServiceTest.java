package chess.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import chess.configuration.RepositoryConfiguration;
import chess.web.dto.RoomDto;

@SpringBootTest
@Import(RepositoryConfiguration.class)
class RoomServiceTest {

	@Autowired
	private RoomService roomService;

	private String testName = "summer";

	@AfterEach
	void deleteCreated() {
		roomService.deleteByName(testName);
	}

	@Test
	@DisplayName("이름을 받아 체스 게임 방을 생성한다.")
	void create() {
		RoomDto room = roomService.create(testName);
		assertThat(room.getName()).isEqualTo(testName);
	}

	@Test
	@DisplayName("이미 있는 이름으 저장하면 예외가 발생한다.")
	void validateDuplicateName() {
		roomService.create(testName);

		assertThatThrownBy(() -> roomService.create(testName))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@ParameterizedTest
	@ValueSource(strings = {"", "16자를넘는방이름은안되니까돌아가"})
	@DisplayName("빈 이름이나 16자 초과 이름이 들어오면 예외가 발생한다.")
	void createException(String name) {
		assertThatThrownBy(() -> roomService.create(name))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("없는 id로 방을 조회하면 예외가 발생한다.")
	void validate() {
		assertThatThrownBy(() -> roomService.validateId(0))
			.isInstanceOf(IllegalArgumentException.class);
	}
}