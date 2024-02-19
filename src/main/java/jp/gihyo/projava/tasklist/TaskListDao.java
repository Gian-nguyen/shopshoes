package jp.gihyo.projava.tasklist;

import jp.gihyo.projava.tasklist.HomeController.TaskItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TaskListDao {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    TaskListDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(TaskItem taskItem) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(taskItem);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("tasklist");
        insert.execute(param);
    }

    public List<TaskItem> findAll() {
        String query = "SELECT * FROM tasklist";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        List<TaskItem> taskItems = result.stream()
                .map((Map<String, Object> row) -> {
                    String id = row.get("id").toString();
                    String task = row.get("task").toString();
                    String picture = row.get("picture") != null ? row.get("picture").toString() : "";
                    String number = row.get("number") != null ? row.get("number").toString() : "";
                    String cost = row.get("cost") != null ? row.get("cost").toString() : "";
                    String price = row.get("price") != null ? row.get("price").toString() : "";
                    boolean done = (Boolean) row.get("done");
                    return new TaskItem(id, task, done, number, cost, price, picture);
                }).toList();
        return taskItems;
    }


    public String getId(int index) {
        List<TaskItem> taskItems = findAll();
        if (taskItems.size() <= 0) { return "-1"; }
        return taskItems.get(index).id().toString();
    }

    public int delete(String id) {
        int number = jdbcTemplate.update("DELETE FROM tasklist WHERE id = ?", id);
        return number;
    }

    public int update(TaskItem taskItem) {
        int number = jdbcTemplate.update(
                "UPDATE tasklist SET task = ?, picture = ?, done = ?, number = ?, cost = ?, price = ? WHERE id = ?",
                taskItem.task(),
                taskItem.picture(),
                taskItem.done(),
                taskItem.number(),
                taskItem.cost(),
                taskItem.price(),
                taskItem.id()
        );
        return number;
    }

//    public int update(TaskItem taskItem) {
//        int number = jdbcTemplate.update(
//                "UPDATE tasklist SET id = ?, task = ?, deadline = ?, done = ?, quantity = ?, number = ?, cost = ?, price = ? WHERE id = ?",
//                taskItem.id(),
//                taskItem.task(),
//                taskItem.deadline(),
//                taskItem.done(),
//                taskItem.quantity(),
//                taskItem.number(),
//                taskItem.cost(),
//                taskItem.price()
//        );
//        return number;
//    }
}
