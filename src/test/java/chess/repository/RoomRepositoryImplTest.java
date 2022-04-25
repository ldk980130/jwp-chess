package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;

import chess.web.dto.RoomDto;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@JdbcTest
public class RoomRepositoryImplTest {

    private static final String testName = "summer";
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private RoomRepository roomRepository;

    @BeforeEach
    void init() {
        roomRepository = new RoomRepositoryImpl(dataSource, jdbcTemplate);
    }

    @Test
    @DisplayName("뱡 insert")
    void insert() {
        int id = roomRepository.save(testName);
        assertThat(id).isGreaterThan(0);
    }

    @Test
    @DisplayName("방 find")
    void find() {
        roomRepository.save(testName);
        RoomDto room = roomRepository.find(testName).orElseThrow();
        assertThat(room.getName()).isEqualTo(testName);
    }

    @Test
    @DisplayName("이름으로 생성된 방을 삭제한다.")
    void removeByName() {
        roomRepository.save(testName);
        roomRepository.removeByName(testName);

        assertThat(roomRepository.find(testName)).isEmpty();
    }
}
