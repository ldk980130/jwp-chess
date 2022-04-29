package chess.repository;

import chess.domain.Room;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class RoomRepositoryImpl implements RoomRepository {

	private static final String TABLE_NAME = "room";
	private static final String KEY_NAME = "id";
	private static final int KEY_INDEX = 1;
	private static final int NAME_INDEX = 2;
	private static final int PASSWORD_INDEX = 3;

	private final SimpleJdbcInsert insertActor;
	private final NamedParameterJdbcTemplate jdbcTemplate;

	public RoomRepositoryImpl(DataSource dataSource,
		NamedParameterJdbcTemplate jdbcTemplate) {
		this.insertActor = new SimpleJdbcInsert(dataSource)
			.withTableName(TABLE_NAME)
			.usingGeneratedKeyColumns(KEY_NAME);
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int save(Room room) {
		return insertActor.executeAndReturnKey(Map.of(
				"name", room.getName(),
				"password", room.getPassword()))
			.intValue();
	}

	@Override
	public Optional<Room> findByName(String name) {
		String sql = "select * from room where name = :name limit 1";
		try {
			return Optional.ofNullable(
				jdbcTemplate.queryForObject(sql, Map.of("name", name), getRoomMapper()));
		} catch (EmptyResultDataAccessException exception) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<Room> findById(int roomId) {
		String sql = "select * from room where id = :roomId";
		try {
			return Optional.ofNullable(
				jdbcTemplate.queryForObject(sql, Map.of("roomId", roomId), getRoomMapper()));
		} catch (EmptyResultDataAccessException exception) {
			return Optional.empty();
		}
	}

	@Override
	public void deleteById(int id) {
		String sql = "delete from room where id = :id";
		jdbcTemplate.update(sql, Map.of("id", id));
	}

	@Override
	public List<Room> findAll() {
		String sql = "select * from room";
		return jdbcTemplate.query(sql, getRoomMapper());
	}

	private RowMapper<Room> getRoomMapper() {
		return (resultSet, rowNum) -> new Room(
			resultSet.getInt(KEY_INDEX),
			new Room(
				resultSet.getString(NAME_INDEX),
				resultSet.getString(PASSWORD_INDEX)
			));
	}
}
