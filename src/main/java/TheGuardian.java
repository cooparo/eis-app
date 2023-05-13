import java.util.ArrayList;

public class TheGuardian {
    public class Articlee implements Article {
        private String id;
        private String webTitle;
        private String body = null;

        @Override
        public String getTitle() {
            return webTitle;
        }

        @Override
        public String getBody() {
            return body;
        }


    }
    public class Response {
        private String status;
        private int total;
        private ArrayList<Article> results;

        public ArrayList<Article> getResults() {
            return results;
        }
    }

}
