package hu.nje.mozifxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MoziApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /*
        private static void dateImporter() {
            MovieService movieService = MovieService.instanceOf();
            EntityManager em = movieService.getEntityManager();

            em.getTransaction().begin();

            try {
                Files.readAllLines(Path.of("D:\\GAMF\\java\\beadandok\\eloadas\\2-06-Mozi-3 táblás\\eloadas.txt"))
                        .stream().skip(1)
                        .forEach(row -> {
                            String[] data = row.split("\t");
                            Performance performance = new Performance();
                            performance.setMovieId(Long.parseLong(data[0]));
                            performance.setCinemaId(Long.parseLong(data[1]));
                            performance.setDate(LocalDate.parse(data[2], DateTimeFormatter.ofPattern("yyyy.MM.dd")));
                            performance.setNumberOfViewers(Integer.parseInt(data[3]));
                            performance.setIncome(Double.parseDouble(data[4]));

                            em.persist(performance);
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            em.getTransaction().commit();
            em.close();
        }
    */

    @Override
    public void start(Stage stage) throws IOException {
        final Scene scene = new Scene(
                FXMLLoader.load(
                        Objects.requireNonNull(MoziApplication.class.getResource("main-view.fxml"))
                ), 600, 480);
        stage.setTitle("Mozi - Java előadás beadandó");
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.show();
    }
}