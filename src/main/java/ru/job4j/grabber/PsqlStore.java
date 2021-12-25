package ru.job4j.grabber;

import ru.job4j.grabber.utils.Post;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection cnn;

    public PsqlStore(Properties cfg) {
        String url;
        String login;
        String password;
                url = cfg.getProperty("jdbc.url");
                login = cfg.getProperty("jdbc.username");
                password = cfg.getProperty("jdbc.password");
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            cnn = DriverManager.getConnection(url, login, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement =
                     cnn.prepareStatement(
                             "insert into post(name, text, link, created) "
                                     + "values (?, ?, ?, ?)",  Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            statement.execute();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> list = new ArrayList<>();
        try (PreparedStatement statement =
                     cnn.prepareStatement("select * from post")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(postFromRs(resultSet));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement statement =
                     cnn.prepareStatement("select * from post where id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    post = postFromRs(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    public Post postFromRs(ResultSet rs) throws SQLException {
        return new Post(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("link"),
                rs.getString("text"),
                rs.getTimestamp("created").toLocalDateTime());
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) {
   Properties properties = new Properties();
        InputStream in = PsqlStore.class.getClassLoader()
                .getResourceAsStream("postbase.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PsqlStore store = new PsqlStore(properties);
          Post post1 = new Post("Java", "https://www.sql.ru/forum/job-offers/61",
                  "Something..", LocalDateTime.now());
        Post post2 = new Post("JavaDevelop", "https://www.sql.ru/forum/job-offers/62",
                "Something..", LocalDateTime.now());
        Post post3 = new Post("JavaTest", "https://www.sql.ru/forum/job-offers/63",
                "Something..", LocalDateTime.now());
          store.save(post1);
          store.save(post2);
          store.save(post3);

          Post newPost = store.findById(17);
        System.out.println(newPost.getLink());
        List<Post> list = store.getAll();
        list.forEach(s -> System.out.println(s));

    }
}
