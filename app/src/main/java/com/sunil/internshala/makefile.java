package com.sunil.internshala;

public class makefile {

        private String title;
        private String description;
        private String url;

        public makefile() {}

        public makefile(String title, String description, String url) {
            this.title = title;
            this.description = description;
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getUrl() {
            return url;
        }


}
