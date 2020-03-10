package pavelnazimok.uitestingfeatures.testrail;

class APIClientFactory {

    static APIClient create() {
        final APIClient client = new APIClient("https://your.testrail.url/");
        client.setUser("your@user.email");
        client.setPassword("yourpassword");
        return client;
    }
}
